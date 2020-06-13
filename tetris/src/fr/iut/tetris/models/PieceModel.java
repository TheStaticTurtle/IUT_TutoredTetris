package fr.iut.tetris.models;

import fr.iut.tetris.Config;

import java.awt.*;
import java.util.ArrayList;
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
	public static final Color COLOR_OLIVE = Color.decode("#D4FF00");
	public static final Color COLOR_LIGHTBLUE = Color.decode("#00BFFF");
	public static final Color COLOR_ORANGERED = Color.decode("#FF4300");
	public static final Color COLOR_CYAN = Color.decode("#00FFA5");
	public static final Color COLOR_DARKPURPLE = Color.decode("#7200FF");
	public static final Color COLOR_GOLD = Color.decode("#FFD200");
	public static final Color COLOR_DARKGOLD = Color.decode("#FF7700");
	public static final Color COLOR_DARKPINK = Color.decode("#FF0059");
	public static final Color COLOR_LIGHTPINK = Color.decode("#FF00B6");
	public static final Color COLOR_GRAY = Color.decode("#DBDBDB");
	public static final Color COLOR_DARKGRAY = Color.decode("#808080");
	public static final Color COLOR_PURPLE2 = Color.decode("#8E00DB");

	public BlockModel[][] childs = new BlockModel[4][4];

	//Position represent top-left corner of the 4x4 grid
	int x = 0;
	int y = 0;
	Point centerOfgravity;
	Point spawnPoint; //Just for the clone()
	String name;
	public boolean ignoreCollisionWithFalling = false;

	public PieceModel(BlockModel[][] childs, Point spawnPoint, Point centerOfgravity, String name) {
		this.childs = childs;
		this.x = spawnPoint.x;
		this.y = spawnPoint.y;
		this.spawnPoint = spawnPoint; //Just for the clone()
		this.centerOfgravity = centerOfgravity;
		this.name = name;
		for (int y = 0; y < childs.length; y++) {
			for (int x = 0; x < childs[y].length; x++) {
				if(childs[y][x] != null) {
					childs[y][x].setParent(this);
				}
			}
		}

	}

	/**
	 * Change the color of piece
	 * @param color the color to change to
	 */
	public void changeColor(Color color) {
		for (int y = 0; y < childs.length; y++) {
			for (int x = 0; x < childs[y].length; x++) {
				if(childs[y][x] != null) {
					childs[y][x].color = color;
					childs[y][x].recalculate();
				}
			}
		}
	}

	/**
	 * Rotate a given array of blocks
	 * @param matrix the arrays
	 * @param name
	 * @return the array rotated clockwise
	 */
	private static BlockModel[][] rotateClockWise(BlockModel[][] matrix, String name) {
		int size = matrix.length;
		BlockModel[][] ret = new BlockModel[size][size];

		if (name.equals("PieceO") || name.equals("PieceStar")) { return matrix; }

		if(name.equals("PieceI") || name.equals("PieceTLongA") || name.equals("PieceTLongB")) {
			for (int i = 0; i < size; ++i)
				for (int j = 0; j < size; ++j)
					ret[i][j] = matrix[size - j - 1][i]; //***
			return ret;
		}

		for (int i = 0; i < size-1; ++i)
			for (int j = 0; j < size-1; ++j)
				ret[i][j] = matrix[size - j - 2][i]; //***


		return ret;
	}

	/**
	 * Rotate a given array of blocks
	 * @param matrix the arrays
	 * @param name
	 * @return the array rotated counter clockwise
	 */
	private static BlockModel[][] rotateConterClockWise(BlockModel[][] matrix, String name) {
		int size = matrix.length;
		BlockModel[][] ret = new BlockModel[size][size];

		if (name.equals("PieceO") || name.equals("PieceStar")) { return matrix; }

		if(name.equals("PieceI") || name.equals("PieceTLongA") || name.equals("PieceTLongB")) {
			for (int i = 0; i < size; ++i)
				for (int j = 0; j < size; ++j)
					ret[i][j] = matrix[j][size - i -1]; //***

			return ret;
		}

		for (int i = 0; i < size-1; ++i)
			for (int j = 0; j < size-1; ++j)
				ret[i][j] = matrix[j][size - i - 2]; //***

		return ret;
	}

	/**
	 * Rotate the piece based on the given direction
	 * @param direction the direction
	 * @param name
	 */
	void rotateModel(int direction, String name) { // -1LEFT / 1RIGHT
		if(direction == -1) {
			this.childs = rotateConterClockWise(this.childs, name);
		} else if (direction == 1){
			this.childs = rotateClockWise(this.childs, name);
		}
	}

	@Override
	protected PieceModel clone() {
		BlockModel[][] c = new BlockModel[4][4];

		for (int y = 0; y < childs.length; y++) {
			for (int x = 0; x < childs[y].length; x++) {
				if(childs[y][x] != null) {
					c[y][x] = childs[y][x].clone();
				}
			}
		}

		return new PieceModel(
				c,
				new Point(this.spawnPoint.x,this.spawnPoint.y),
				new Point(this.centerOfgravity.x,this.centerOfgravity.y),
				this.name
		);
	}

	int getPieceHeight() {
		int total = 0;
		for (int y = 0; y < childs.length; y++) {
			boolean empty = true;
			for (int x = 0; x < childs[y].length && empty; x++) {
				if(childs[y][x] == null) empty=false;
			}

			if(!empty) total++;
		}
		return total;
	}

	static PieceModel PieceT = new PieceModel(
			new BlockModel[][] {

					{null						 , null						   , null						 , null},
					{new BlockModel(COLOR_PURPLE), new BlockModel(COLOR_PURPLE), new BlockModel(COLOR_PURPLE), null},
					{null                        , new BlockModel(COLOR_PURPLE), null                        , null},
					{null						 , null						   , null						 , null}
			},
			new Point(3,-1),
			new Point(3,1),
			"PieceT"
	);
	static PieceModel PieceL = new PieceModel(
			new BlockModel[][] {
					{null, new BlockModel(COLOR_ORANGE), null                        , null},
					{null, new BlockModel(COLOR_ORANGE), null                        , null},
					{null, new BlockModel(COLOR_ORANGE), new BlockModel(COLOR_ORANGE), null},
					{null, null                        , null                        , null}

			},
			new Point(3,0),
			new Point(1,2),
			"PieceL"
	);
	static PieceModel PieceJ = new PieceModel(
			new BlockModel[][] {
					{null                       , new BlockModel(COLOR_BLUE) , null, null},
					{null                       , new BlockModel(COLOR_BLUE) , null, null},
					{new BlockModel(COLOR_BLUE) , new BlockModel(COLOR_BLUE) , null, null},
					{null                       , null                       , null, null}

			},
			new Point(4,0),
			new Point(1,2),
			"PieceJ"
	);
	static PieceModel PieceO = new PieceModel(
			new BlockModel[][] {
					{null, null, null                        , null                        },
					{null, new BlockModel(COLOR_YELLOW), new BlockModel(COLOR_YELLOW), null},
					{null, new BlockModel(COLOR_YELLOW), new BlockModel(COLOR_YELLOW), null},
					{null, null, null                        , null                        }

			},
			new Point(3,-1),
			new Point(1,2),
			"PieceO"
	);
	static PieceModel PieceS = new PieceModel(
			new BlockModel[][] {
					{null                       , null                       , null                       , null},
					{null                       , new BlockModel(COLOR_GREEN), new BlockModel(COLOR_GREEN), null},
					{new BlockModel(COLOR_GREEN), new BlockModel(COLOR_GREEN), null                       , null},
					{null                       , null                       , null                       , null}

			},
			new Point(4,-1),
			new Point(1,2),
			"PieceS"
	);
	static PieceModel PieceZ = new PieceModel(
			new BlockModel[][] {
					{null                     , null                     , null                     , null},
					{new BlockModel(COLOR_RED), new BlockModel(COLOR_RED), null                     , null},
					{null                     , new BlockModel(COLOR_RED), new BlockModel(COLOR_RED), null},
					{null                     , null                     , null                     , null}

			},
			new Point(3,-1),
			new Point(1,2),
			"PieceZ"
	);
	static PieceModel PieceI = new PieceModel(
			new BlockModel[][] {
					{null                      , null                      , null                      , null},
					{null                      , null                      , null                      , null},
					{new BlockModel(COLOR_AQUA), new BlockModel(COLOR_AQUA), new BlockModel(COLOR_AQUA), new BlockModel(COLOR_AQUA)},
					{null                      , null                      , null                      , null},
			},
			new Point(3,-2),
			new Point(1,2),
			"PieceI"
	);

	static PieceModel PieceStar = new PieceModel(
			new BlockModel[][] {
					{null                       , new BlockModel(COLOR_OLIVE), null                       , null},
					{new BlockModel(COLOR_OLIVE), new BlockModel(COLOR_OLIVE), new BlockModel(COLOR_OLIVE), null},
					{null                       , new BlockModel(COLOR_OLIVE), null                       , null},
					{null                       , null                       , null                       , null},

			},
			new Point(3,0),
			new Point(1,2),
			"PieceStar"
	);

	static PieceModel PieceU = new PieceModel(
			new BlockModel[][] {
					{new BlockModel(COLOR_CYAN), new BlockModel(COLOR_CYAN), null                       , null},
					{null                      , new BlockModel(COLOR_CYAN), null                       , null},
					{new BlockModel(COLOR_CYAN), new BlockModel(COLOR_CYAN), null                       , null},
					{null                       , null                       , null                       , null},

			},
			new Point(3,0),
			new Point(1,2),
			"PieceU"
	);
	static PieceModel PieceBarre3 = new PieceModel(
			new BlockModel[][] {
					{ null                        , null                       , null                       , null},
					{ new BlockModel(COLOR_ORANGERED) , new BlockModel(COLOR_ORANGERED), new BlockModel(COLOR_ORANGERED), null},
					{ null                        , null                       , null                       , null},
					{ null                        , null                       , null                       , null},

			},
			new Point(3,-1),
			new Point(1,1),
			"PieceBarre3"
	);
	static PieceModel PieceMiniL = new PieceModel(
			new BlockModel[][] {
					{ null                        , new BlockModel(COLOR_AQUA), null                       , null},
					{ new BlockModel(COLOR_AQUA) , new BlockModel(COLOR_AQUA), null                       , null},
					{ null                        , null                       , null                       , null},
					{ null                        , null                       , null                       , null},

			},
			new Point(3,0),
			new Point(1,1),
			"PieceMiniL"
	);
	static PieceModel PieceTLongA = new PieceModel(
			new BlockModel[][] {

					{null					     	 , null				    		   , null					    	 , null},
					{new BlockModel(COLOR_DARKPURPLE), new BlockModel(COLOR_DARKPURPLE), new BlockModel(COLOR_DARKPURPLE), new BlockModel(COLOR_DARKPURPLE)},
					{null                            , new BlockModel(COLOR_DARKPURPLE), null                            , null},
					{null					         , null				     		   , null					    	 , null}
			},
			new Point(3,-1),
			new Point(3,1),
			"PieceTLongA"
	);
	static PieceModel PieceTLongB = new PieceModel(
			new BlockModel[][] {

					{null						 , null						   , null						 , null},
					{new BlockModel(COLOR_GOLD), new BlockModel(COLOR_GOLD), new BlockModel(COLOR_GOLD), new BlockModel(COLOR_GOLD)},
					{null                        , null                        , new BlockModel(COLOR_GOLD), null},
					{null						 , null						   , null						 , null}
			},
			new Point(3,-1),
			new Point(3,1),
			"PieceTLongB"
	);
	static PieceModel PieceBigL = new PieceModel(
			new BlockModel[][] {
					{ null                           , null                           , new BlockModel(COLOR_DARKGOLD) , null},
					{ null                           , null                           , new BlockModel(COLOR_DARKGOLD) , null},
					{ new BlockModel(COLOR_DARKGOLD) , new BlockModel(COLOR_DARKGOLD) , new BlockModel(COLOR_DARKGOLD) , null},
					{ null                           , null                           , null                           , null},

			},
			new Point(3,0),
			new Point(1,1),
			"PieceBigL"
	);

	static PieceModel PieceBigT = new PieceModel(
			new BlockModel[][] {
					{new BlockModel(COLOR_DARKPINK), new BlockModel(COLOR_DARKPINK), new BlockModel(COLOR_DARKPINK), null},
					{null                          , new BlockModel(COLOR_DARKPINK), null                          , null},
					{null                          , new BlockModel(COLOR_DARKPINK), null                          , null},
					{null					       , null						   , null		   			       , null}
			},
			new Point(3,0),
			new Point(3,1),
			"PieceBigT"
	);

	static PieceModel PieceBizzareA = new PieceModel(
			new BlockModel[][] {
					{null                           , null                           , new BlockModel(COLOR_LIGHTPINK), null},
					{new BlockModel(COLOR_LIGHTPINK), new BlockModel(COLOR_LIGHTPINK), new BlockModel(COLOR_LIGHTPINK), null},
					{null                           , new BlockModel(COLOR_LIGHTPINK), null                           , null},
					{null					        , null				   		     , null		   			          , null}
			},
			new Point(3,0),
			new Point(3,1),
			"PieceBizzareA"
	);

	static PieceModel PieceBizzareB = new PieceModel(
			new BlockModel[][] {
					{new BlockModel(COLOR_GRAY), null                      , null                      , null},
					{new BlockModel(COLOR_GRAY), new BlockModel(COLOR_GRAY), new BlockModel(COLOR_GRAY), null},
					{null                      , new BlockModel(COLOR_GRAY), null                      , null},
					{null					   , null				   	   , null		   			   , null}
			},
			new Point(3,0),
			new Point(3,1),
			"PieceBizzareB"
	);

	static PieceModel PieceBigZ = new PieceModel(
			new BlockModel[][] {
					{new BlockModel(COLOR_RED), new BlockModel(COLOR_RED), null                     , null},
					{null                     , new BlockModel(COLOR_RED), null                     , null},
					{null                     , new BlockModel(COLOR_RED), new BlockModel(COLOR_RED), null},
					{null                     , null                     , null                     , null}
			},
			new Point(3,0),
			new Point(3,1),
			"PieceBigZ"
	);
	static PieceModel PieceBigS = new PieceModel(
			new BlockModel[][]{
					{null                       , new BlockModel(COLOR_GREEN), new BlockModel(COLOR_GREEN),                        null},
					{null                       , new BlockModel(COLOR_GREEN),                        null,                        null},
					{new BlockModel(COLOR_GREEN), new BlockModel(COLOR_GREEN),                        null,                        null},
					{null                       ,                        null,                        null,                        null}
			},
			new Point(3,0),
			new Point(3,1),
			"PieceBigS"
	);


	static PieceModel[] PiecesLegacy = new PieceModel[]{PieceModel.PieceL, PieceModel.PieceT, PieceModel.PieceO, PieceModel.PieceS,PieceModel.PieceZ,PieceModel.PieceI,PieceModel.PieceJ};
	static PieceModel[] PiecesCustoms = new PieceModel[]{PieceStar,PieceU,PieceBarre3,PieceMiniL,PieceTLongA,PieceTLongB,PieceBigL,PieceBigT,PieceBizzareA,PieceBizzareB,PieceBigZ,PieceBigS};

	static public PieceModel[] getPieces() {
		if(Config.getInstance().getBool("LEGACY_PIECES")) {
			return PiecesLegacy;
		} else {
			ArrayList<PieceModel> baseArray = new ArrayList<>(Arrays.asList(PiecesLegacy));
			baseArray.addAll(Arrays.asList(PiecesCustoms));
			return baseArray.toArray(new PieceModel[baseArray.size()]);
		}
	}
}