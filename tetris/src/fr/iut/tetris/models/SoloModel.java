package fr.iut.tetris.models;

import fr.iut.tetris.Log;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.enums.LineCompleted;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.vues.Common;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class SoloModel {
	public int height = 20;
	public int width = 10;
	public int fallSpeed = 1000; //ms
	final public int FALL_SPEED = 1000; //ms
	ArrayList<Object> pieceList = new ArrayList<>();
	public PieceModel fallingPiece = null;
	public PieceModel nextPiece;
	public GameState gameState = GameState.WAITING;
	SoloController ctrl;
	public int bestScore = 0;
	public int currentScore = 0;
	Random rand = new Random();

	public SoloModel() {
		nextPiece = getRandomPiece();
	}

	/**
	 * Sets the current controller
	 * @param ctrl an instance of SoloController
	 */
	public void setCtrl(SoloController ctrl) {
		this.ctrl = ctrl;
	}

	/**
	 * Fetch a random element from a list
	 * @param list the list of objects
	 * @param rand the Random class
	 * @return a random object
	 */
	static Object getRandomElement(Object[] list,Random rand)  {
		return list[rand.nextInt(list.length)];
	}

	/**
	 * Return a random pieces from PieceModel.Pieces
	 * @return a PieceModel
	 */
	public PieceModel getRandomPiece() {
		Log.info(this,"Spawned a new random piece");
		return ((PieceModel)getRandomElement(PieceModel.getPieces(),rand)).clone();
	}

	/**
	 * Spawn a new piece for the player. Transfer the `nextPiece` to `fallingPiece` and generate a new piece for `nextPiece`
	 */
	public void spawnPiece() {
		Log.info(this,"Spawned a new piece");

		PieceModel p = nextPiece.clone();
		pieceList.add(p);
		fallingPiece = p;
		nextPiece = getRandomPiece();
		this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:PIECE_SPAWN"));

		try {
			computeMixedGrid(false);
		} catch (OverlappedPieceException | PieceOutOfBoardException e) {
			gameState = GameState.FINISHED;
			this.bestScore = this.ctrl.gameEnded();
		}
	}

	/**
	 * This is the function that the game is based on throws an error if the board is impossible if else return the grid for the vue
	 * @return an arrays of the game size that contains BlockModels for the vue to display
	 * @throws OverlappedPieceException in case a piece collide with an other one
	 * @throws PieceOutOfBoardException if a piece has a position outside of the board
	 * @param render_dropped_piece should be false is the function isn't used by the ui
	 */
	public BlockModel[][] computeMixedGrid(boolean render_dropped_piece) throws OverlappedPieceException, PieceOutOfBoardException {
		BlockModel[][] table = new BlockModel[height][width];
		for (Object obj: pieceList) {

			if(obj instanceof BlockModel) {
				BlockModel block = (BlockModel)obj;
				if(table[block.standAlonePos.y][block.standAlonePos.x] != null) throw new OverlappedPieceException();
				table[block.standAlonePos.y][block.standAlonePos.x] = block;
			}

			if(obj instanceof PieceModel) {
				PieceModel piece = (PieceModel)obj;
				for (int y = piece.y; y < piece.y+4; y++) {
					for (int x = piece.x; x < piece.x+4; x++) {
						if(piece.childs[y-piece.y][x-piece.x] != null) {
							if(y> height-1 || y<0) throw new PieceOutOfBoardException();
							if(x> width -1 || x<0) throw new PieceOutOfBoardException();

							if(table[y][x] != null &&!(piece.ignoreCollisionWithFalling && table[y][x].parent==fallingPiece) ) throw new OverlappedPieceException();
							table[y][x] = piece.childs[y-piece.y][x-piece.x];
						}
					}
				}
			}

		}

		if(render_dropped_piece && fallingPiece != null) {
			PieceModel tmp = fallingPiece.clone();
			pieceList.add(tmp);
			//tmp.y = Math.min(fallingPiece.y+fallingPiece.getPieceHeight()-1,height-fallingPiece.getPieceHeight()+1);
			tmp.y = fallingPiece.y;
			tmp.x = fallingPiece.x;
			tmp.ignoreCollisionWithFalling =true;

			while (true) {
				tmp.y++;
				try {
					computeMixedGrid(false);
				} catch (PieceOutOfBoardException | OverlappedPieceException e) {
					tmp.y--;
					break;
				}
			}
			pieceList.remove(tmp);

			for (int y = tmp.y; y < tmp.y+4; y++) {
				for (int x = tmp.x; x < tmp.x+4; x++) {
					if(tmp.childs[y-tmp.y][x-tmp.x] != null) {
						if(y> height-1 || y<0) continue;
						if(x> width -1 || x<0) continue;
						if(table[y][x] != null) continue;
						tmp.childs[y-tmp.y][x-tmp.x].color = tmp.childs[y-tmp.y][x-tmp.x].color.darker().darker().darker();
						tmp.childs[y-tmp.y][x-tmp.x].recalculate();
						table[y][x] = tmp.childs[y-tmp.y][x-tmp.x];
					}
				}
			}

		}
		return table;
	}

	/**
	 * A recursive function that check for line that are completed and falls down every line over it then recursively calls itself with the parm `firstCall` at true which make the function return an integer instead of a LineCompleted
	 * @param firstCall In the program should be set to false only used by the function for the socre multiplier calculation
	 * @return When use in the program return LineCompleted if not it return an Integer of the umber of line removed
	 */
	Object checkForFullLineAndRemoveIt(boolean firstCall){
		try {
			BlockModel[][] grid = computeMixedGrid(false);
			int firstLineY = 0;
			Integer lineCount = 0;

			// Difficulty
			if(fallSpeed > 75) {
				fallSpeed = (int)(1000 - 0.3*currentScore);
				Log.debug(this, "FallSpeed = " + this.fallSpeed);
			}

			for (int y = grid.length-1; y >= 0; y--) {
				boolean isLineFull = true;
				for (BlockModel block: grid[y]) {
					if (block == null) {isLineFull = false; break;}
				}

				if(isLineFull) {

					lineCount += 1;
					firstLineY = y;
					Log.info(this,"Line "+y+" is full");

					for (BlockModel block: grid[y]) {
						grid[y] = null;
						pieceList.remove(block);
					}

					for (int fally = y-1; fally >= 0; fally--) {
						for (BlockModel block: grid[fally]) {
							if(block != null) {
								Log.debug(this, "Falling line " + fally + " to "+(fally+1)+" (x:"+block.standAlonePos.x+")");
								block.standAlonePos.y = Math.min( (fally+1) , this.height - 1);
							}
						}
					}
					// Line has fallen down recheck to see if there is more lines but tel the function to spit out an integer instead of the LineCompleted enum
					lineCount += (Integer)checkForFullLineAndRemoveIt(false);
					break;
				}
			}

			if(firstCall) {
				return LineCompleted.getScore(lineCount,firstLineY,this.height);
			} else {
				return lineCount;
			}
		} catch (PieceOutOfBoardException | OverlappedPieceException ignored) {}
		return LineCompleted.NO_LINE;
	}

	/**
	 * When a piece has fallen down to it's maximum it needs to be converted to individual blocks so that we can adjust individuals Y's of blocks instead of moving the whole piece (useful when a piece is cut in the middle for example)
	 * @param piece the piece to transform
	 */
	void convertFullPiecesToBlocks(PieceModel piece) {
		Log.info(this,"Converting the falling piece to individual blocks");
		pieceList.remove(piece);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if(piece.childs[y][x] != null) {
					BlockModel block = piece.childs[y][x];
					if(block != null) {
						block.standAlonePos = new Point(piece.x +x, piece.y+y);
						pieceList.add(block.clone());
					}
				}
			}
		}
	}

	/**
	 * Move the current piece in the X axis also send a "GAME:FAILED_ACTION" event to the controller if the movement is impossible
	 * @param dir the direction
	 * @return if the movment was successful
	 */
	public boolean moveCurrentX(Direction dir) {
		if(fallingPiece != null) {
			fallingPiece.x += dir.step;
			try {
				computeMixedGrid(false);
				return true;
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				fallingPiece.x -= dir.step;
				this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FAILED_ACTION"));
				return false;
			}
		}
		return false;
	}
	/**
	 * Rotate the current piece also send a "GAME:FAILED_ACTION" event to the controller if the rotation is impossible
	 * @param dir the direction of the rotation
	 * @return if the rotation was successful
	 */
	public boolean rotateCurrent(Direction dir) {
		if(fallingPiece != null) {
			fallingPiece.rotateModel(dir.step, fallingPiece.name);
			try {
				computeMixedGrid(false);
				return true;
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				fallingPiece.rotateModel(dir.step * -1, fallingPiece.name);
				this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FAILED_ACTION"));
				return false;
			}
		}
		return false;
	}

	/**
	 * Fall the current piece down by one. Also test if the fall is possible if else the piece get converted to blocks and we check for a full line, we send a event to the controller for sound effects and we calculate the score
	 */
	public void fallCurrent() {
		if(fallingPiece != null) {
			fallingPiece.y++;
			try {
				computeMixedGrid(false);
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				fallingPiece.y--;
				convertFullPiecesToBlocks(fallingPiece);
				currentScore += fallingPiece.getBlockCount();
				LineCompleted score = (LineCompleted)checkForFullLineAndRemoveIt(true);
				this.calculateScore(score);
				if(score == LineCompleted.QUAD_LINE || score == LineCompleted.BOTTOM_QUAD_LINE) {
					this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:FOURLINES_COMPLETE"));
				} else if(score == LineCompleted.NO_LINE ) {
					this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:PIECE_PLACE"));
				} else {
					this.ctrl.actionPerformed(new ActionEvent(this,0,"GAME:LINE_COMPLETE"));
				}
				Log.info(this,"Got score: "+score.toString());

				fallingPiece = null; // The piece can not fall anymore
			}
		}
	}

	/**
	 * Execute fallCurrent() until the `fallingPiece` is no more
	 */
	public void fallCurrentAtBottom() {
		while(fallingPiece != null) {
			fallCurrent();
		}
	}

	/**
	 * Calculate the current score based of the point multiplier of the LineCompleted parameter
	 * @param lc what multiplier should we use
	 */
	public void calculateScore(LineCompleted lc) {
		this.currentScore += 10 * lc.pointMultiplier;
	}
}
