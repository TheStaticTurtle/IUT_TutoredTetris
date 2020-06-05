package fr.iut.tetris.models;

import fr.iut.tetris.enums.Direction;

import java.awt.*;
import java.util.Arrays;

public
class PieceModel {
	public BlockModel[][] childs= new BlockModel[4][4];

	//Position represent top-left corner of the 4x4 grid
	int x = 0;
	int y = 0;
	Point centerOfgravity;
	Point spawnPoint; //Just for the clone()

	public PieceModel(BlockModel[][] childs, Point spawnPoint, Point centerOfgravity) {
		this.childs = childs;
		this.x = spawnPoint.x;
		this.y = spawnPoint.y;
		this.spawnPoint = spawnPoint; //Just for the clone()
		this.centerOfgravity = centerOfgravity;
		for (int y = 0; y < childs.length; y++) {
			for (int x = 0; x < childs[y].length; x++) {
				if(childs[y][x] != null) {
					childs[y][x].setParent(this);
				}
			}
		}

	}

	void spawnPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	private static BlockModel[][] rotateClockWise(BlockModel[][] matrix) {
		int size = matrix.length;
		BlockModel[][] ret = new BlockModel[size][size];

		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				ret[i][j] = matrix[size - j - 1][i]; //***

		return ret;
	}
	private static BlockModel[][] rotateConterClockWise(BlockModel[][] matrix) {
		int size = matrix.length;
		BlockModel[][] ret = new BlockModel[size][size];

		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				ret[i][j] = matrix[j][size - i - 1]; //***

		return ret;
	}

	void rotateModel(int direction) { // -1LEFT / 1RIGHT
		if(direction == -1) {
			this.childs = rotateConterClockWise(this.childs);
		} else if (direction == 1){
			this.childs = rotateClockWise(this.childs);
		}
	}

	@Override
	protected PieceModel clone(){
		return new PieceModel(Arrays.copyOf(this.childs,this.childs.length), new Point(this.spawnPoint.x,this.spawnPoint.y), new Point(this.centerOfgravity.x,this.centerOfgravity.y));
	}
	static PieceModel PieceT = new PieceModel(
			new BlockModel[][] {
					{null                     , null                     , null                     , null},
					{null                     , null                     , null                     , null},
					{new BlockModel(Color.red), new BlockModel(Color.red), new BlockModel(Color.red), null},
					{null                     , new BlockModel(Color.red), null                     , null}
			},
			new Point(5,-2),
			new Point(2,2)
	);
	static PieceModel PieceL = new PieceModel(
			new BlockModel[][] {
					{null                       , null                       , null                       , null},
					{null                       , new BlockModel(Color.green), null                       , null},
					{null                       , new BlockModel(Color.green), null                       , null},
					{null                       , new BlockModel(Color.green), new BlockModel(Color.green), null}
			},
			new Point(5,-1),
			new Point(2,2)
	);
	static PieceModel Piece7 = new PieceModel(
			new BlockModel[][] {
					{null                       , null                       , null                       , null},
					{null                       , new BlockModel(Color.CYAN), null                       , null},
					{null                       , new BlockModel(Color.CYAN), null                       , null},
					{new BlockModel(Color.CYAN), new BlockModel(Color.CYAN), null                       , null}
			},
			new Point(5,-1),
			new Point(2,2)
	);
	static PieceModel PieceO = new PieceModel(
			new BlockModel[][] {
					{null                       , null                        , null                        , null},
					{null                       , null                        , null                        , null},
					{null                       , new BlockModel(Color.yellow), new BlockModel(Color.yellow), null},
					{null                       , new BlockModel(Color.yellow), new BlockModel(Color.yellow), null}
			},
			new Point(5,-2),
			new Point(1,2)
	);
	static PieceModel PieceS = new PieceModel(
			new BlockModel[][] {
					{null                      , null                      , null                      , null},
					{null                      , null                      , null                      , null},
					{null                      , new BlockModel(Color.BLUE), new BlockModel(Color.BLUE), null},
					{new BlockModel(Color.BLUE), new BlockModel(Color.BLUE), null                      , null}
			},
			new Point(5,-2),
			new Point(1,2)
	);
	static PieceModel PieceZ = new PieceModel(
			new BlockModel[][] {
					{null                         , null                         , null                         , null},
					{null                         , null                         , null                         , null},
					{new BlockModel(Color.MAGENTA), new BlockModel(Color.MAGENTA), null                         , null},
					{null                         , new BlockModel(Color.MAGENTA), new BlockModel(Color.MAGENTA), null}
			},
			new Point(5,-2),
			new Point(1,2)
	);
	static PieceModel Piece_ = new PieceModel(
			new BlockModel[][] {
					{null                         , null                         , null                         , null},
					{null                         , null                         , null                         , null},
					{new BlockModel(Color.WHITE), new BlockModel(Color.WHITE), new BlockModel(Color.WHITE), new BlockModel(Color.WHITE)},
					{null                         , null                         , null                         , null},
			},
			new Point(5,-2),
			new Point(1,2)
	);
	static PieceModel[] Pieces = new PieceModel[]{PieceModel.PieceL, PieceModel.PieceT, PieceModel.PieceO, PieceModel.PieceS,PieceModel.PieceZ,PieceModel.Piece_,PieceModel.Piece7};
}