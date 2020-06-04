package fr.iut.tetris.models;

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
	}

	void spawnPoint(int x, int y) {
		this.x = x;
		this.y = y;
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
	static PieceModel[] Pieces = new PieceModel[]{PieceModel.PieceL, PieceModel.PieceT, PieceModel.PieceO, PieceModel.PieceS};
}