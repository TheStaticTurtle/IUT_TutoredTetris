package fr.iut.tetris.component;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


import javax.imageio.ImageIO;

public enum Piece {

	I(),
	
	J(),
	
	L(),
	
	O(),
	
	S(),
	
	T(),
	
	Z();
	
	

	public Point position;
	private int speed;
	
	Piece() {
		this.position = new Point();
		this.speed = 1;
		this.position.x = 0;
		this.position.y = 0;
	}

	public void moveDown() {
		position.y += speed;
	}
	
	public Image getImage(){
		
		try {

			URL url = getClass().getResource( "/res/pieces/"+name()+".png" );
			Image img = ImageIO.read(url);

			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-img.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter((BufferedImage) img, null);
			
			return img;
		} catch (IOException e) {
			System.out.println("Image non trouvé");
		}
		
		return null;
	}
	
}
