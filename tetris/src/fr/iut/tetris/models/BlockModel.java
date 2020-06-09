package fr.iut.tetris.models;

import fr.iut.tetris.Config;

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
	 * Colorize an image based on a specified color (Took from: https://stackoverflow.com/a/21385150/8165282)
	 * @param image the base image
	 * @param color the color you want to adjust to
	 * @return the colorized image
	 */
	public static BufferedImage dye(BufferedImage image, Color color) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage dyed = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dyed.createGraphics();
		g.drawImage(image, 0,0, null);
		g.setComposite(AlphaComposite.SrcAtop);
		g.setColor(color);
		g.fillRect(0,0,w,h);
		g.dispose();
		return dyed;
	}

	/**
	 * Convert a normal image of type Image to a BufferedImage to be used with graphics/dye
	 * @param img the raw image
	 * @return the buffer image
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) { return (BufferedImage) img; }
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

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
			this.base_image = toBufferedImage(Config.getInstance().getRessourceImage("/res/piece_grayscale.png").getScaledInstance(size.width,size.height, Image.SCALE_REPLICATE));
			Config.getInstance().cacheObject("piece_grayscale_scaled",this.base_image);
		}
	}

	/**
	 * Recalculate the piece color based on the stored color variable
	 */
	public void recalculate() {
		Color c = new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue(),190);
		image = dye(this.base_image,c);
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
		this.image = dye(this.base_image,c);
	}
}