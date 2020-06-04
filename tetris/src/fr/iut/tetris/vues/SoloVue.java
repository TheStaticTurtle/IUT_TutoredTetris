package fr.iut.tetris.vues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import fr.iut.tetris.Main;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.models.BlockModel;
import fr.iut.tetris.models.SoloModel;

public class SoloVue extends JPanel {
	SoloModel model;
	SoloController ctrl;

	PiecePanel panelPiece;
	SplashScreenPanel splashScreen;

	public SoloVue(SoloModel model, SoloController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.BLACK;
		setPreferredSize(new Dimension( 640, 870 ));
		setBackground(bg);

		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(11,1);
		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainLayout.setVgap(5);

		mainPanel.setPreferredSize(new Dimension(450,600));


		/*JButton backButton = new MenuButton("Retour", Color.ORANGE,Color.WHITE);

		backButton.addActionListener(ctrl);
		backButton.setActionCommand("CLICK:CREDIT:BACK");

		Font font = new JLabel().getFont();
		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			e.printStackTrace();
		}


		Font mormalFont = font.deriveFont(40f);
		backButton.setFont(mormalFont);



		mainPanel.add( new Spacer());
		mainPanel.add(backButton);*/

		mainPanel.setLocation(0, 0);
		mainPanel.setPreferredSize(mainPanel.getPreferredSize());

		mainPanel.setBounds(0, 0, (int) mainPanel.getPreferredSize().getWidth(), (int) mainPanel.getPreferredSize().getHeight());
		mainPanel.setVisible(true);


		panelPiece = new PiecePanel(model);
		panelPiece.setLocation(0, 0);
		panelPiece.setVisible(true);

		splashScreen = new SplashScreenPanel(0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight());
		splashScreen.setVisible(true);

		//ICI Pour ajoutter des couches
		JLayeredPane testPane = new JLayeredPane();
		//testPane.add(mainPanel,JLayeredPane.DEFAULT_LAYER);
		testPane.add(panelPiece,JLayeredPane.PALETTE_LAYER);
		testPane.add(splashScreen,JLayeredPane.MODAL_LAYER);
		testPane.setPreferredSize(getPreferredSize());


		SpringLayout lyt = new SpringLayout();
		SpringLayout lyt2 = new SpringLayout();
		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, testPane);
		lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, this);


		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, splashScreen, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, splashScreen, 0, SpringLayout.VERTICAL_CENTER, testPane);
		lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, splashScreen, 0, SpringLayout.HORIZONTAL_CENTER, this);
		lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, splashScreen, 0, SpringLayout.VERTICAL_CENTER, this);
		testPane.setLayout(lyt);
		setLayout(lyt2);
		add(testPane);
	}
	public void redraw() {
		panelPiece.redraw();
		splashScreen.redraw(model.gameState != GameState.PLAYING);
		repaint();
	}
}


class SplashScreenPanel extends JPanel {
	JPanel mainPanel;
	JLabel pressSpace;
	public SplashScreenPanel(int x, int y, int width, int height) {
		setLocation(x, y);
		setPreferredSize(new Dimension(width,height));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		System.out.println(getPreferredSize());
		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,1,0,0);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLocation(0, 0);
		//mainPanel.setPreferredSize(getPreferredSize());
		//mainPanel.setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.BLUE);

		Font font = new JLabel().getFont();
		Font mormalFont = font.deriveFont(40f);
		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			mormalFont = font.deriveFont(40f);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(mormalFont);
		} catch (Exception e) {
			e.printStackTrace();
		}


		pressSpace = new JLabel("<html><div style='text-align: center;'>Press \"SPACE\" to start<br>a new game</div></html>");
		pressSpace.setFont(mormalFont);
		pressSpace.setForeground(Color.white);
		mainPanel.add(pressSpace);


		SpringLayout layout = new SpringLayout();

		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, this);
		setLayout(layout);
		setVisible(true);

		add(mainPanel);
	}

	public void redraw(boolean visible) {
		mainPanel.setVisible(visible);
		pressSpace.setVisible(visible);
		repaint();
	}
}

class TetrisPiece extends JPanel {

	int canvasWidth;
	int canvasHeight;
	boolean draw;

	Color color;
	BufferedImage img;

	TetrisPiece(boolean draw, int size,Color color,BufferedImage img) {
		this.canvasWidth = size;
		this.canvasHeight = size;
		this.color = color;
		this.draw = draw;
		this.img = img;
	}

	private static BufferedImage dye(BufferedImage image, Color color) {
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

	public void redraw(boolean draw,Color color) {
		this.color = color;
		this.draw = draw;
		invalidate();
		repaint();
	}

	@Override public int getHeight() { return canvasHeight; }
	@Override public int getWidth() { return canvasWidth; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(this.draw) {
			if (img != null) {
				Color c = new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue(),190);
				g2.drawImage(dye(img,c), 0, 0, this.canvasWidth, this.canvasHeight,this);
			} else {
				System.out.println("Error: Image couldn't be loaded displaying dummy instead");
				g2.setColor(this.color);
				g2.fillRect(0,0,canvasWidth,canvasHeight);
			}
		}
	}
}
class PiecePanel extends JPanel {
	SoloModel model;
	int squareSize = 45;

	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) { return (BufferedImage) img; }
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

	public PiecePanel(SoloModel model) {
		this.model = model;

		BufferedImage img = null;
		try {
			URL piece = getClass().getResource( "/res/piece_grayscale.png" );
			img = toBufferedImage(ImageIO.read(piece).getScaledInstance(model.witdh*squareSize,model.height*squareSize, Image.SCALE_REPLICATE));
		} catch (IOException e) {
		}

		setPreferredSize(new Dimension((model.witdh)*squareSize,(model.height)*squareSize));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);
		setLayout(new GridLayout(0,model.witdh)); //ROW = 0 IF Else bug

		try {
			BlockModel[][] grid = model.computeMixedGrid();
			for (int y = 0; y < grid.length; y++) {
				for (int x = 0; x < grid[y].length; x++) {
					add(new TetrisPiece(false,squareSize,null,img));
				}
			}
		} catch (PieceOutOfBoardException | OverlappedPieceException e) {
			e.printStackTrace();
		}
	}

	public void redraw() {
		setIgnoreRepaint(true);

		try {
			BlockModel[][] grid = model.computeMixedGrid();
			for (int y = 0; y < grid.length; y++) {
				for (int x = 0; x < grid[y].length; x++) {
					TetrisPiece p = (TetrisPiece) getComponent(y*model.witdh + x);
					if(grid[y][x] != null) {
						p.redraw(true,grid[y][x].color);
					} else {
						p.redraw(false,null);
					}
				}
			}
		} catch (PieceOutOfBoardException | OverlappedPieceException e) {
			e.printStackTrace();
		}
		setIgnoreRepaint(false);
		repaint();
	}

	@Override public void paintComponent(Graphics g) {
		Dimension d = getPreferredSize();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.gray);
		g2.fillRect(0,0,d.width,d.height);
	}
}
