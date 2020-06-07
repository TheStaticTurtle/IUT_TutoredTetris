package fr.iut.tetris.models;

import fr.iut.tetris.enums.Direction;

import java.awt.*;
import java.util.Arrays;

public
class PieceModel {
	public static final Color COLOR_AQUA = Color.decode("#00FFFF");
	public static final Color COLOR_GREEN = Color.decode("#00F823");
	public static final Color COLOR_RED = Color.decode("#FF0000");
	public static final Color COLOR_PURPLE = Color.decode("#B200FF");
	public static final Color COLOR_ORANGE = Color.decode("#FFAF00");
	public static final Color COLOR_BLUE = Color.decode("#0026FF");
	public static final Color COLOR_YELLOW = Color.decode("#FFF500");

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
					{null                        , null                        , null                        , null},
					{null                        , null                        , null                        , null},
					{new BlockModel(COLOR_PURPLE), new BlockModel(COLOR_PURPLE), new BlockModel(COLOR_PURPLE), null},
					{null                        , new BlockModel(COLOR_PURPLE), null                        , null}
			},
			new Point(3,-2),
			new Point(2,2)
	);
	static PieceModel PieceL = new PieceModel(
			new BlockModel[][] {
					{null                       , null                        , null                        , null},
					{null                       , new BlockModel(COLOR_ORANGE), null                        , null},
					{null                       , new BlockModel(COLOR_ORANGE), null                        , null},
					{null                       , new BlockModel(COLOR_ORANGE), new BlockModel(COLOR_ORANGE), null}
			},
			new Point(3,-1),
			new Point(2,2)
	);
	static PieceModel PieceJ = new PieceModel(
			new BlockModel[][] {
					{null                       , null                      , null                       , null},
					{null                       , new BlockModel(COLOR_BLUE), null                       , null},
					{null                       , new BlockModel(COLOR_BLUE), null                       , null},
					{new BlockModel(COLOR_BLUE) , new BlockModel(COLOR_BLUE), null                       , null}
			},
			new Point(4,-1),
			new Point(2,2)
	);
	static PieceModel PieceO = new PieceModel(
			new BlockModel[][] {
					{null                       , null                        , null                        , null},
					{null                       , null                        , null                        , null},
					{null                       , new BlockModel(COLOR_YELLOW), new BlockModel(COLOR_YELLOW), null},
					{null                       , new BlockModel(COLOR_YELLOW), new BlockModel(COLOR_YELLOW), null}
			},
			new Point(3,-2),
			new Point(1,2)
	);
	static PieceModel PieceS = new PieceModel(
			new BlockModel[][] {
					{null                       , null                       , null                       , null},
					{null                       , null                       , null                       , null},
					{null                       , new BlockModel(COLOR_GREEN), new BlockModel(COLOR_GREEN), null},
					{new BlockModel(COLOR_GREEN), new BlockModel(COLOR_GREEN), null                       , null}
			},
			new Point(4,-2),
			new Point(1,2)
	);
	static PieceModel PieceZ = new PieceModel(
			new BlockModel[][] {
					{null                     , null                     , null                     , null},
					{null                     , null                     , null                     , null},
					{new BlockModel(COLOR_RED), new BlockModel(COLOR_RED), null                     , null},
					{null                     , new BlockModel(COLOR_RED), new BlockModel(COLOR_RED), null}
			},
			new Point(3,-2),
			new Point(1,2)
	);
	static PieceModel PieceI = new PieceModel(
			new BlockModel[][] {
					{null                      , null                      , null                      , null},
					{null                      , null                      , null                      , null},
					{new BlockModel(COLOR_AQUA), new BlockModel(COLOR_AQUA), new BlockModel(COLOR_AQUA), new BlockModel(COLOR_AQUA)},
					{null                      , null                      , null                      , null},
			},
			new Point(3,-2),
			new Point(1,2)
	);
	static PieceModel[] Pieces = new PieceModel[]{PieceModel.PieceL, PieceModel.PieceT, PieceModel.PieceO, PieceModel.PieceS,PieceModel.PieceZ,PieceModel.PieceI,PieceModel.PieceJ};
}