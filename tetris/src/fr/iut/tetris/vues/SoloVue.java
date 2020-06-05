package fr.iut.tetris.vues;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.swing.*;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.iut.tetris.Main;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.models.BlockModel;
import fr.iut.tetris.models.PieceModel;
import fr.iut.tetris.models.SoloModel;

public class SoloVue extends JPanel {
	SoloModel model;
	SoloController ctrl;

	PiecePanel panelPiece;
	SplashScreenPanel splashScreen;
	JLayeredPane testPane;

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


		panelPiece = new PiecePanel(model,0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight());
		panelPiece.setLocation(0, 0);
		panelPiece.setVisible(true);

		splashScreen = new SplashScreenPanel(0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight());
		splashScreen.setVisible(true);

		//ICI Pour ajoutter des couches
		testPane = new JLayeredPane();
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

	public void setModel(SoloModel model) {
		this.model = model;
	}

	public void recalculate() {
		//panelPiece.recalculate();
		splashScreen.recalculate(model.gameState != GameState.PLAYING);
		panelPiece.recalculate();
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

	public void recalculate(boolean visible) {
		mainPanel.setVisible(visible);
		pressSpace.setVisible(visible);
		setVisible(visible);

	}
}

class TetrisBlock extends JPanel {

	int canvasWidth;
	int canvasHeight;

	boolean draw;
	BufferedImage img;

	TetrisBlock(int size) {
		this.canvasWidth = size;
		this.canvasHeight = size;
	}

	public void recalulate(@Nullable BlockModel model) {
		if(model != null) {
			if(model.size.width != this.canvasWidth || model.size.height != this.canvasHeight) {
				model.setSize(new Dimension(this.canvasWidth,this.canvasHeight));
				model.recalculate();
			}
			this.img = model.image;
			this.draw = true;
		} else {
			this.img = null;
			this.draw = false;
		}
	}

	@Override public int getHeight() { return canvasHeight; }
	@Override public int getWidth() { return canvasWidth; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(this.draw) {
			if (img != null) {
				g2.drawImage(img, 0, 0, this.canvasWidth, this.canvasHeight,this);
			} else {
				System.out.println("Error: Image couldn't be loaded displaying dummy instead");
				g2.setColor(Color.MAGENTA);
				g2.fillRect(0,0,canvasWidth,canvasHeight);
			}
		}
	}
}
class NextPiecePanel extends JPanel {
	int canvasWidth;
	int canvasHeight;
	int blockSize;
	BufferedImage img;
	PieceModel piece;

	NextPiecePanel(int blockSize) {
		this.blockSize = blockSize;
		this.canvasWidth = blockSize*4;
		this.canvasHeight = blockSize*4;
	}

	public void recalulate(@NotNull PieceModel model) {
		this.piece = model;
		invalidate();
		repaint();
	}

	@Override public int getHeight() { return canvasHeight; }
	@Override public int getWidth() { return canvasWidth; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.fillRect(0,0,canvasWidth,canvasHeight);
		if(this.piece != null) {
			for (int y = 0; y < this.piece.childs.length; y++) {
				for (int x = 0; x < this.piece.childs[y].length; x++) {
					if(this.piece.childs[y][x] != null) {
						g2.drawImage(this.piece.childs[y][x].image, x*blockSize, y*blockSize, this.blockSize, this.blockSize,this);
					}
				}
			}
		}
	}
}
class PiecePanel extends JPanel {
	SoloModel model;
	int squareSize = 40;
	BufferedImage img = null;
	JPanel mainPanel;
	NextPiecePanel nextPiecePanel;

	public PiecePanel(SoloModel model, int xp, int yp, int width, int height) {
		setLocation(xp, yp);
		setPreferredSize(new Dimension(width,height));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		this.model = model;

		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,model.witdh);
		mainPanel.setLocation(0, 0);
		mainPanel.setPreferredSize(new Dimension((model.witdh)*squareSize,(model.height)*squareSize));
		mainPanel.setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		//mainPanel.setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		mainPanel.setLayout(mainLayout); //ROW = 0 IF Else bug
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.BLUE);

		nextPiecePanel = new NextPiecePanel(squareSize/2);
		nextPiecePanel.setLocation(0, 0);
		nextPiecePanel.setPreferredSize(new Dimension(2*squareSize,2*squareSize));
		nextPiecePanel.setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		nextPiecePanel.setVisible(true);
		nextPiecePanel.setOpaque(true);
		nextPiecePanel.setBackground(Color.MAGENTA);

		/*setPreferredSize(new Dimension((model.witdh)*squareSize,(model.height)*squareSize));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);
		setLayout(new GridLayout(0,model.witdh)); //ROW = 0 IF Else bug*/

		try {
			BlockModel[][] grid = model.computeMixedGrid();
			for (int y = 0; y < grid.length; y++) {
				for (int x = 0; x < grid[y].length; x++) {
					mainPanel.add(new TetrisBlock(squareSize));
				}
			}
		} catch (PieceOutOfBoardException | OverlappedPieceException e) {
			e.printStackTrace();
		}

		add(mainPanel);
		add(nextPiecePanel);

		SpringLayout layout = new SpringLayout();

		layout.putConstraint(SpringLayout.WEST, mainPanel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mainPanel, 10, SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.EAST, nextPiecePanel, 10, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, nextPiecePanel, 10, SpringLayout.NORTH, this);
		setLayout(layout);

		recalculate();
	}

	public void recalculate() {
		setIgnoreRepaint(true);

		if(model.gameState==GameState.PLAYING) {
			try {
				nextPiecePanel.recalulate(model.nextPiece);
				BlockModel[][] grid = model.computeMixedGrid();
				for (int y = 0; y < grid.length; y++) {
					for (int x = 0; x < grid[y].length; x++) {
						TetrisBlock p = (TetrisBlock) mainPanel.getComponent(y*model.witdh + x);
						if(grid[y][x] != null) {
							p.recalulate(grid[y][x]);
						} else {
							p.recalulate(null);
						}
					}
				}
			} catch (PieceOutOfBoardException | OverlappedPieceException e) {
				e.printStackTrace();
			}
		}

		setIgnoreRepaint(false);
		repaint();
	}

	@Override public void paintComponent(Graphics g) {
		Dimension d = getPreferredSize();
		Graphics2D g2 = (Graphics2D) g;
		/*g2.setColor(Color.gray);
		g2.fillRect(0,0,d.width,d.height);*/
	}
}
