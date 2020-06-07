package fr.iut.tetris.vues;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import fr.iut.tetris.Log;
import fr.iut.tetris.Main;
import fr.iut.tetris.controllers.CreditController;
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

	GamePanel gamePanel;
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
		mainPanel.setLocation(0, 0);
		mainPanel.setPreferredSize(mainPanel.getPreferredSize());
		mainPanel.setBounds(0, 0, (int) mainPanel.getPreferredSize().getWidth(), (int) mainPanel.getPreferredSize().getHeight());
		mainPanel.setVisible(true);


		gamePanel = new GamePanel(model,0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight());
		gamePanel.setLocation(0, 0);
		gamePanel.setVisible(true);

		splashScreen = new SplashScreenPanel(0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight(),ctrl,model);
		splashScreen.setVisible(true);

		//ICI Pour ajoutter des couches
		testPane = new JLayeredPane();
		//testPane.add(mainPanel,JLayeredPane.DEFAULT_LAYER);
		testPane.add(gamePanel,JLayeredPane.PALETTE_LAYER);
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
		splashScreen.recalculate(model.gameState != GameState.PLAYING,model.gameState);
		gamePanel.recalculate();
	}
}


class SplashScreenPanel extends JPanel {
	JPanel mainPanel;
	JLabel pressSpace;
	JLabel currentScoreLabel;
	JLabel bestScoreLabel;
	JPanel backReplayPanel;
	SoloController ctrl;
	SoloModel model;

	public SplashScreenPanel(int x, int y, int width, int height,SoloController ctrl,SoloModel model) {
		this.ctrl= ctrl;
		this.model = model;
		setLocation(x, y);
		setPreferredSize(new Dimension(width,height));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		System.out.println(getPreferredSize());
		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,1,0,0);
		GridLayout subLayout = new GridLayout(1,2);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLocation(0, 0);
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.BLACK);

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


		pressSpace = new JLabel("<html><div style='text-align: center;'>Press \"SPACE\" to start</div></html>");
		pressSpace.setFont(mormalFont);
		pressSpace.setForeground(Color.white);
		mainPanel.add(pressSpace);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white),new EmptyBorder(10, 10, 10, 10)));

		backReplayPanel = new JPanel();
		JButton backButton = new MenuButton("Back",Color.ORANGE,Color.WHITE, ctrl);
		JButton replayButton = new MenuButton("Replay",Color.RED,Color.WHITE, ctrl);
		backButton.setActionCommand("CLICK:SOLO:BACK");
		replayButton.setActionCommand("CLICK:MENU:SOLO"); //HACKY
		backButton.addActionListener(ctrl);
		replayButton.addActionListener(ctrl);

		backReplayPanel.setOpaque(false);
		backReplayPanel.setLayout(subLayout);
		backReplayPanel.add(backButton);
		backReplayPanel.add(replayButton);
		backButton.setFont(mormalFont);
		replayButton.setFont(mormalFont);

		currentScoreLabel = new JLabel();
		currentScoreLabel.setForeground(Color.white);
		currentScoreLabel.setFont(mormalFont);
		currentScoreLabel.setText("<html>Current Score: 0");
		bestScoreLabel = new JLabel();
		bestScoreLabel.setForeground(Color.white);
		bestScoreLabel.setFont(mormalFont);
		bestScoreLabel.setText("<html>Best Score: 0");


		SpringLayout layout = new SpringLayout();

		mainPanel.setOpaque(true);

		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, this);


		setLayout(layout);
		setVisible(true);

		add(mainPanel);
	}

	public void recalculate(boolean visible, GameState state) {
		mainPanel.setVisible(visible);
		setVisible(visible);
		if(state == GameState.FINISHED) {
			pressSpace.setText("<html><div style='text-align: center;'>Game Over</div></html>");
			currentScoreLabel.setText("<html>Current score: "+this.model.currentScore);
			bestScoreLabel.setText("<html>Best score: "+this.model.bestScore);
			mainPanel.removeAll();
			mainPanel.add(pressSpace);
			mainPanel.add(new Spacer());
			mainPanel.add(currentScoreLabel);
			mainPanel.add(bestScoreLabel);
			mainPanel.add(new Spacer());
			mainPanel.add(backReplayPanel);
		} else {
			mainPanel.removeAll();
			mainPanel.add(pressSpace);
		}
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

	public void recalulate(BlockModel model) {
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
				Log.critical(this,"Failed to load block image loading dummy");
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
	BlockModel noBlockModel;

	NextPiecePanel(int blockSize) {
		this.canvasWidth = blockSize*6;
		this.canvasHeight = blockSize*6;
		this.blockSize = this.canvasHeight/4;
		noBlockModel = new BlockModel(Color.DARK_GRAY);
		noBlockModel.recalculate();
	}

	public void recalulate(PieceModel model) {
		this.piece = model;
		invalidate();
		repaint();
	}

	@Override public int getHeight() { return canvasHeight; }
	@Override public int getWidth() { return canvasWidth; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.fillRect(0,0,canvasWidth,canvasHeight);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if(this.piece!= null && this.piece.childs[y][x] != null) {
					this.piece.childs[y][x].recalculate();
					g2.drawImage(this.piece.childs[y][x].image, x*blockSize, y*blockSize, this.blockSize, this.blockSize,this);
				} else {
					g2.drawImage(noBlockModel.image, x*blockSize, y*blockSize, this.blockSize, this.blockSize,this);
				}
			}
		}
	}
}
class GamePanel extends JPanel {
	SoloModel model;
	int squareSize = 40;
	BufferedImage img = null;
	JPanel mainPanel;
	NextPiecePanel nextPiecePanel;
	BlockModel noBlockModel;
	JLabel scoreLabel;

	public GamePanel(SoloModel model, int xp, int yp, int width, int height) {
		setLocation(xp, yp);
		setPreferredSize(new Dimension(width,height));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		noBlockModel = new BlockModel(Color.BLACK);
		noBlockModel.recalculate();

		Font font = new JLabel().getFont();
		Font bigFont = font.deriveFont(55f);
		Font mormalFont = font.deriveFont(40f);
		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			bigFont = font.deriveFont(55f);
			mormalFont = font.deriveFont(40f);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(mormalFont);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.model = model;
		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,model.witdh);//ROW = 0 IF Else bug
		//mainPanel.setLocation(0, 0);
		mainPanel.setPreferredSize(new Dimension((model.witdh)*squareSize,(model.height)*squareSize));
		//mainPanel.setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		mainPanel.setLayout(mainLayout);
		mainPanel.setVisible(true);
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Color.DARK_GRAY);

		nextPiecePanel = new NextPiecePanel(squareSize/2);
		//nextPiecePanel.setLocation(0, 0);
		//nextPiecePanel.setPreferredSize(new Dimension(2*squareSize,2*squareSize));
		//nextPiecePanel.setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		nextPiecePanel.setVisible(true);
		nextPiecePanel.setOpaque(true);
		nextPiecePanel.setBackground(Color.MAGENTA);

		JLabel labelNextPiece = new JLabel("<html>Next:");
		labelNextPiece.setForeground(Color.white);
		labelNextPiece.setFont(mormalFont);

		scoreLabel = new JLabel("<html>Score: "+this.model.currentScore);
		scoreLabel.setForeground(Color.white);
		scoreLabel.setFont(bigFont);

		add(mainPanel);
		add(nextPiecePanel);
		add(labelNextPiece);
		add(scoreLabel);

		try {
			Object[][] grid = model.computeMixedGrid();
			for (Object[] objects : grid) {
				for (int x = 0; x < objects.length; x++) {
					TetrisBlock b = new TetrisBlock(squareSize);
					mainPanel.add(b);
					b.recalulate(noBlockModel);
				}
			}
		} catch (PieceOutOfBoardException | OverlappedPieceException e) {
			e.printStackTrace();
		}


		SpringLayout layout = new SpringLayout();


		layout.putConstraint(SpringLayout.NORTH, scoreLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, scoreLabel, 10, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.WEST, mainPanel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mainPanel, 10, SpringLayout.SOUTH, scoreLabel);
		//layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);

		layout.putConstraint(SpringLayout.NORTH, labelNextPiece, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.WEST, labelNextPiece, 10, SpringLayout.EAST, mainPanel);

		layout.putConstraint(SpringLayout.NORTH, nextPiecePanel, 10, SpringLayout.SOUTH, labelNextPiece);
		layout.putConstraint(SpringLayout.WEST, nextPiecePanel, 10, SpringLayout.EAST, mainPanel);
		setLayout(layout);


		recalculate();
	}

	public void recalculate() {
		setIgnoreRepaint(true);

		scoreLabel.setText("<html>Score: "+this.model.currentScore);

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
							p.recalulate(noBlockModel);
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
