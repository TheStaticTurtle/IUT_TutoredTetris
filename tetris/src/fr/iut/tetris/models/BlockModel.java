package fr.iut.tetris.models;

import fr.iut.tetris.Config;
import fr.iut.tetris.vues.Common;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockModel {
	public Color color;
	public BufferedImage image;
	public BufferedImage base_image;
	public Dimension size = new Dimension(32,32);
	public Point standAlonePos = new Point(0,0);
	public PieceModel parent;

	/**
	 * Change the size of the block (used for the image calculation)
	 * @param size the size
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}

	/**
	 * Set to which piece this perticular block is linked
	 * @param parent the piece
	 */
	public void setParent(PieceModel parent) {
		this.parent = parent;
	}

	/**
	 * Load the image of the block
	 */
	void setBaseImage() {
		this.base_image = (BufferedImage) Config.getInstance().getCachedObject("piece_grayscale_scaled");
		if(this.base_image == null) {
			this.base_image = Common.toBufferedImage(Config.getInstance().getRessourceImage("/res/piece_grayscale.png").getScaledInstance(size.width,size.height, Image.SCALE_REPLICATE));
			Config.getInstance().cacheObject("piece_grayscale_scaled",this.base_image);
		}
	}

	/**
	 * Recalculate the piece color based on the stored color variable
	 */
	public void recalculate() {
		Color c = new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue(),190);
		image = Common.dye(this.base_image,c);
	}

	public BlockModel clone() {
		return new BlockModel(color,base_image,size,standAlonePos,parent);
	}

	public BlockModel(Color color) {
		setBaseImage();
		this.color = color;
	}
	public BlockModel(Color color,BufferedImage base_image,Dimension size,Point standAlonePos, PieceModel parent) {
		this.color = color;
		this.base_image = base_image;
		this.size = size;
		this.standAlonePos = (Point) standAlonePos.clone();
		this.parent= parent;
		Color c = new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue(),190);
		this.image = Common.dye(this.base_image,c);
	}
}