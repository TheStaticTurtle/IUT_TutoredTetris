package fr.iut.tetris.models;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BlockModel {
	public Color color;
	public BufferedImage image;
	public BufferedImage base_image;
	public Dimension size = new Dimension(32,32);
	public Point standAlonePos = new Point(0,0);
	public PieceModel parent;

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
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) { return (BufferedImage) img; }
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public void setParent(PieceModel parent) {
		this.parent = parent;
	}

	void setBaseImage() {
		try {
			URL piece = getClass().getResource( "/res/piece_grayscale.png" );
			this.base_image = toBufferedImage(ImageIO.read(piece).getScaledInstance(size.width,size.height, Image.SCALE_REPLICATE));
		} catch (IOException ignored) {
		}
	}

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