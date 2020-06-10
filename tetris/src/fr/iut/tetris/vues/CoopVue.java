package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.CoopController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.models.BlockModel;
import fr.iut.tetris.models.CoopModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CoopVue extends JPanel {
	CoopModel model;
	CoopController ctrl;

	GamePanelCoop gamePanel;
	SplashScreenPanel splashScreen;
	PauseMenu pauseMenu;
	JLayeredPane testPane;

	public CoopVue(CoopModel model, CoopController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.RED;
		int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
		int ww = Config.getInstance().getInt("WINDOW_WIDTH");
		setPreferredSize(new Dimension( ww, wh ));
		setBackground(bg);


		gamePanel = new GamePanelCoop(model,0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight());
		gamePanel.setLocation(0, 0);
		gamePanel.setVisible(true);

		splashScreen = new SplashScreenPanel(getPreferredSize(), ctrl, model);
		splashScreen.setVisible(true);

		pauseMenu = new PauseMenu(0,0,(int) getPreferredSize().getWidth(),(int) getPreferredSize().getHeight(),ctrl,model);

		//ICI Pour ajoutter des couches
		testPane = new JLayeredPane();
		//testPane.add(mainPanel,JLayeredPane.DEFAULT_LAYER);
		testPane.add(gamePanel,JLayeredPane.PALETTE_LAYER);
		testPane.add(splashScreen,JLayeredPane.MODAL_LAYER);
		testPane.add(pauseMenu,JLayeredPane.POPUP_LAYER);
		testPane.setPreferredSize(getPreferredSize());


		SpringLayout lyt = new SpringLayout();
		SpringLayout lyt2 = new SpringLayout();
		/*lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, testPane);
		lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, this);*/


		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, splashScreen, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, splashScreen, 0, SpringLayout.VERTICAL_CENTER, testPane);
		lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, splashScreen, 0, SpringLayout.HORIZONTAL_CENTER, this);
		lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, splashScreen, 0, SpringLayout.VERTICAL_CENTER, this);
		testPane.setLayout(lyt);
		setLayout(lyt2);
		add(testPane);
	}

	public void setModel(CoopModel model) {
		this.model = model;
	}

	public void recalculate() {
		splashScreen.recalculate(model.gameState == GameState.WAITING || model.gameState == GameState.FINISHED,model.gameState);
		pauseMenu.recalculate(model.gameState);
		gamePanel.recalculate();
	}
}

class GamePanelCoop extends JPanel {
	CoopModel model;
	int squareSize = 40;
	BufferedImage img = null;
	JPanel mainPanel;
	NextPiecePanel nextPiecePanelPlayerA;
	NextPiecePanel nextPiecePanelPlayerB;
	BlockModel noBlockModel;
	JLabel scoreLabel;

	public GamePanelCoop(CoopModel model, int xp, int yp, int width, int height) {
		setLocation(xp, yp);
		setPreferredSize(new Dimension(width,height));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		noBlockModel = new BlockModel(Color.BLACK);
		noBlockModel.recalculate();

		this.model = model;
		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,model.witdh);//ROW = 0 IF Else bug

		mainPanel.setLayout(mainLayout);
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);

		nextPiecePanelPlayerA = new NextPiecePanel(squareSize);
		nextPiecePanelPlayerA.setVisible(true);
		nextPiecePanelPlayerA.setOpaque(true);
		nextPiecePanelPlayerA.setBackground(Color.MAGENTA);

		nextPiecePanelPlayerB = new NextPiecePanel(squareSize);
		nextPiecePanelPlayerB.setVisible(true);
		nextPiecePanelPlayerB.setOpaque(true);
		nextPiecePanelPlayerB.setBackground(Color.MAGENTA);

		JLabel labelNextPieceA = new JLabel("<html>Next A:");
		labelNextPieceA.setForeground(Color.white);
		labelNextPieceA.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		JLabel labelNextPieceB = new JLabel("<html>Next B:");
		labelNextPieceB.setForeground(Color.white);
		labelNextPieceB.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		scoreLabel = new JLabel("<html>Score: "+this.model.currentScore);
		scoreLabel.setForeground(Color.white);
		scoreLabel.setFont(Config.getInstance().getFont("FONT_BIG"));

		add(mainPanel);
		add(labelNextPieceA);
		add(nextPiecePanelPlayerA);
		add(labelNextPieceB);
		add(nextPiecePanelPlayerB);
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
		layout.putConstraint(SpringLayout.SOUTH, mainPanel, -10, SpringLayout.SOUTH, this);

		layout.putConstraint(SpringLayout.NORTH, labelNextPieceA, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.WEST, labelNextPieceA, 10, SpringLayout.EAST, mainPanel);

		layout.putConstraint(SpringLayout.NORTH, nextPiecePanelPlayerA, 10, SpringLayout.SOUTH, labelNextPieceA);
		layout.putConstraint(SpringLayout.WEST, nextPiecePanelPlayerA, 10, SpringLayout.EAST, mainPanel);

		layout.putConstraint(SpringLayout.NORTH, labelNextPieceB, 150, SpringLayout.SOUTH, nextPiecePanelPlayerA);
		layout.putConstraint(SpringLayout.WEST, labelNextPieceB, 10, SpringLayout.EAST, mainPanel);

		layout.putConstraint(SpringLayout.NORTH, nextPiecePanelPlayerB, 10, SpringLayout.SOUTH, labelNextPieceB);
		layout.putConstraint(SpringLayout.WEST, nextPiecePanelPlayerB, 10, SpringLayout.EAST, mainPanel);

		setLayout(layout);

		//Recalculate panel width
		Dimension t = mainPanel.getPreferredSize();
		squareSize = t.height / model.height;
		t.width = t.height * (model.height / model.witdh);
		mainPanel.setPreferredSize(t);
		nextPiecePanelPlayerA.resetSize((int)(squareSize*1.5));
		nextPiecePanelPlayerB.resetSize((int)(squareSize*1.5));
		recalculate();
	}

	public void recalculate() {
		setIgnoreRepaint(true);

		scoreLabel.setText("<html>Score: "+this.model.currentScore);

		if(model.gameState==GameState.PLAYING) {
			try {
				nextPiecePanelPlayerA.recalulate(model.nextPiecePlayerA);
				nextPiecePanelPlayerB.recalulate(model.nextPiecePlayerB);
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

	/*@Override public void paintComponent(Graphics g) {
		Dimension d = getPreferredSize();
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.gray);
		g2.fillRect(0,0,d.width,d.height);
	}*/
}