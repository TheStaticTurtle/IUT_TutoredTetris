package fr.iut.tetris.models;

import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SoloModel {
	public int height = 20;
	public int witdh = 10;
	public int fallSpeed = 1000; //ms
	ArrayList<PieceModel> pieceList = new ArrayList<PieceModel>();
	public PieceModel fallingPiece = null;
	public PieceModel nextPiece = null;
	public GameState gameState = GameState.WAITING;

	public SoloModel() {
		nextPiece = getRandomPiece();
	}

	static Object getRandomElement(Object[] list)
	{
		Random rand = new Random();
		return list[rand.nextInt(list.length)];
	}

	public PieceModel getRandomPiece() {
		System.out.println("Spawned a new random piece");
		PieceModel p = ((PieceModel)getRandomElement(PieceModel.Pieces)).clone();
		return p;
	}
	public void spawnPiece() {
		System.out.println("Spawned a new piece");

		PieceModel p = nextPiece.clone();
		pieceList.add(p);
		fallingPiece = p;
		nextPiece = getRandomPiece();

		try {
			computeMixedGrid();
		} catch (OverlappedPieceException | PieceOutOfBoardException e) {gameState = GameState.FINISHED;}
	}

	//the mega function who does everything
	public BlockModel[][] computeMixedGrid() throws OverlappedPieceException, PieceOutOfBoardException {
		BlockModel[][] table = new BlockModel[height][witdh];
		for (PieceModel piece: pieceList) {
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
		//System.out.println(Arrays.deepToString(table));
		return table;
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
				fallingPiece = null; // The piece can not fall anymore
			}
		}
	}
	public void fallCurrentAtBottom() {
		while(fallingPiece != null) {
			fallCurrent();
		}
	}

	/*private int score;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}*/
	
	
}