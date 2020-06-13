package fr.iut.tetris.vues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.models.*;

public class SoloVue extends JPanel {
	SoloModel model;
	SoloController ctrl;

	GamePanelSolo gamePanel;
	SplashScreenPanel splashScreen;
	JLayeredPane testPane;
	PauseMenu pauseMenu;

	public SoloVue(SoloModel model, SoloController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.BLACK;
		int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
		int ww = Config.getInstance().getInt("WINDOW_WIDTH");
		setPreferredSize(new Dimension( ww, wh ));
		setBackground(bg);


		gamePanel = new GamePanelSolo(model,getPreferredSize());
		gamePanel.setLocation(0, 0);
		gamePanel.setVisible(true);

		splashScreen = new SplashScreenPanel(getPreferredSize(), ctrl, model);
		splashScreen.setVisible(true);

		pauseMenu = new PauseMenu(getPreferredSize(), ctrl,model);

		testPane = new JLayeredPane();
		testPane.add(new StaticStarAnimation(getPreferredSize(),new Color(0f,0f,0.1f),55),JLayeredPane.DEFAULT_LAYER);
		testPane.add(gamePanel,JLayeredPane.PALETTE_LAYER);
		testPane.add(splashScreen,JLayeredPane.MODAL_LAYER);
		testPane.add(pauseMenu,JLayeredPane.POPUP_LAYER);

		testPane.setPreferredSize(getPreferredSize());


		SpringLayout lyt = new SpringLayout();
		SpringLayout lyt2 = new SpringLayout();

		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, splashScreen, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, splashScreen, 0, SpringLayout.VERTICAL_CENTER, testPane);
		lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, splashScreen, 0, SpringLayout.HORIZONTAL_CENTER, this);
		lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, splashScreen, 0, SpringLayout.VERTICAL_CENTER, this);

		testPane.setLayout(lyt);
		setLayout(lyt2);
		add(testPane);

		JPanel t = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			t.repaint();
			t.revalidate();
		}}).start();
	}

	public void setModel(SoloModel model) {
		this.model = model;
	}

	public void recalculate() {
		splashScreen.recalculate(model.gameState == GameState.WAITING || model.gameState == GameState.FINISHED,model.gameState);
		pauseMenu.recalculate(model.gameState);
		gamePanel.recalculate();
	}
}


class GamePanelSolo extends JPanel {
	SoloModel model;
	int squareSize = 20;
	BufferedImage img = null;
	JPanel mainPanel;
	NextPiecePanel nextPiecePanel;
	BlockModel noBlockModel;
	JLabel scoreLabel;

	public GamePanelSolo(SoloModel model, Dimension dimension) {
		setLocation(0, 0);
		setPreferredSize(dimension);
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		noBlockModel = new BlockModel(Color.BLACK);
		noBlockModel.recalculate();

		this.model = model;
		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,model.width);//ROW = 0 IF Else bug

		mainPanel.setLayout(mainLayout);
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);

		nextPiecePanel = new NextPiecePanel(squareSize);
		nextPiecePanel.setVisible(true);
		nextPiecePanel.setOpaque(true);
		nextPiecePanel.setBackground(Color.MAGENTA);

		JLabel labelNextPiece = new JLabel("<html>Next:");
		labelNextPiece.setForeground(Color.white);
		labelNextPiece.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		scoreLabel = new JLabel("<html>Score: "+this.model.currentScore);
		scoreLabel.setForeground(Color.white);
		scoreLabel.setFont(Config.getInstance().getFont("FONT_BIG"));

		add(mainPanel);
		add(nextPiecePanel);
		add(labelNextPiece);
		add(scoreLabel);

		try {
			Object[][] grid = model.computeMixedGrid(false);
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

		layout.putConstraint(SpringLayout.NORTH, labelNextPiece, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.WEST, labelNextPiece, 10, SpringLayout.EAST, mainPanel);

		layout.putConstraint(SpringLayout.NORTH, nextPiecePanel, 10, SpringLayout.SOUTH, labelNextPiece);
		layout.putConstraint(SpringLayout.WEST, nextPiecePanel, 10, SpringLayout.EAST, mainPanel);
		setLayout(layout);


		Dimension t = mainPanel.getPreferredSize();
		//Manual calculation of layout is needed because the vue isn't rendered yet
		int temp = getHeight() - (10+Common.getStringHeight(scoreLabel.getText())+10+10);
		squareSize = temp/model.height;

		for(Component c : mainPanel.getComponents()) {
			TetrisBlock b = (TetrisBlock)c;
			b.setSize(squareSize);
		}

		t.height = model.height * squareSize;
		t.width = model.width * squareSize;
		mainPanel.setPreferredSize(t);

		nextPiecePanel.resetSize((int)(squareSize/2));
		recalculate();
	}



	public void recalculate() {
		setIgnoreRepaint(true);

		/*Dimension t = mainPanel.getSize();
		t.width = t.height * (model.height / model.witdh);
		mainPanel.setSize(t);*/

		scoreLabel.setText("<html>Score: "+this.model.currentScore);

		if(model.gameState==GameState.PLAYING) {
			try {
				nextPiecePanel.recalulate(model.nextPiece);
				BlockModel[][] grid = model.computeMixedGrid(true);

				for (int y = 0; y < grid.length; y++) {
					for (int x = 0; x < grid[y].length; x++) {
						TetrisBlock p = (TetrisBlock) mainPanel.getComponent(y*model.width + x);
						//p.setSize(mainPanel.getPreferredSize().height / model.height);
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
