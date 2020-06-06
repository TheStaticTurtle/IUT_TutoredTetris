package fr.iut.tetris.models;

import fr.iut.tetris.Log;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.enums.LineCompleted;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class SoloModel {
	public int height = 20;
	public int witdh = 10;
	public int fallSpeed = 1000; //ms
	ArrayList<Object> pieceList = new ArrayList<>();
	public PieceModel fallingPiece = null;
	public PieceModel nextPiece;
	public GameState gameState = GameState.WAITING;
	SoloController ctrl;
	public int bestScore = 0;
	public int currentScore = 0;

	public SoloModel() {
		nextPiece = getRandomPiece();
	}

	public void setCtrl(SoloController ctrl) {
		this.ctrl = ctrl;
	}

	static Object getRandomElement(Object[] list)  {
		Random rand = new Random();
		return list[rand.nextInt(list.length)];
	}

	public PieceModel getRandomPiece() {
		Log.info(this,"Spawned a new random piece");
		return ((PieceModel)getRandomElement(PieceModel.Pieces)).clone();
	}
	public void spawnPiece() {
		Log.info(this,"Spawned a new piece");

		PieceModel p = nextPiece.clone();
		pieceList.add(p);
		fallingPiece = p;
		nextPiece = getRandomPiece();

		try {
			computeMixedGrid();
		} catch (OverlappedPieceException | PieceOutOfBoardException e) {
			gameState = GameState.FINISHED;
			this.bestScore = this.ctrl.gameEnded();
		}
	}

	//the mega function who does everything
	public BlockModel[][] computeMixedGrid() throws OverlappedPieceException, PieceOutOfBoardException {
		BlockModel[][] table = new BlockModel[height][witdh];
		for (Object obj: pieceList) {

			if(obj instanceof BlockModel) {
				BlockModel block = (BlockModel)obj;
				table[block.standAlonePos.y][block.standAlonePos.x] = block;
			}

			if(obj instanceof PieceModel) {
				PieceModel piece = (PieceModel)obj;
				for (int y = piece.y; y < piece.y+4; y++) {
					for (int x = piece.x; x < piece.x+4; x++) {
						if(piece.childs[y-piece.y][x-piece.x] != null) {
							if(y> height-1 || y<0) throw new PieceOutOfBoardException();
							if(x> witdh-1 || x<0) throw new PieceOutOfBoardException();
							if(table[y][x] != null) throw new OverlappedPieceException();
							table[y][x] = piece.childs[y-piece.y][x-piece.x];
						}
					}
				}
			}

		}
		return table;
	}

	Object checkForFullLineAndRemoveIt(boolean firstCall){
		try {
			BlockModel[][] grid = computeMixedGrid();
			int firstLineY = 0;
			Integer lineCount = 0;

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
	void convertFullPiecesToBlocks(PieceModel piece) {
		Log.info(this,"Converting the fallling piece to individual blocks");
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

	public boolean moveCurrentX(Direction dir) {
		if(fallingPiece != null) {
			fallingPiece.x += dir.step;
			try {
				computeMixedGrid();
				return true;
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				fallingPiece.x -= dir.step;
				return false;
			}
		}
		return false;
	}
	public boolean rotateCurrent(Direction dir) {
		if(fallingPiece != null) {
			fallingPiece.rotateModel(dir.step);
			try {
				computeMixedGrid();
				return true;
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				fallingPiece.rotateModel(dir.step * -1);
				return false;
			}
		}
		return false;
	}
	public void fallCurrent() {
		if(fallingPiece != null) {
			fallingPiece.y++;
			try {
				computeMixedGrid();
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				fallingPiece.y--;
				convertFullPiecesToBlocks(fallingPiece);
				LineCompleted score = (LineCompleted)checkForFullLineAndRemoveIt(true);
				this.calculateScore(score);
				Log.info(this,"Got score: "+score.toString());

				fallingPiece = null; // The piece can not fall anymore
			}
		}
	}
	public void fallCurrentAtBottom() {
		while(fallingPiece != null) {
			fallCurrent();
		}
	}


	public void calculateScore(LineCompleted lc) {
		this.currentScore += 10 * lc.pointMultiplier;
	}
}
