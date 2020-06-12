package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.models.EffectModel;
import fr.iut.tetris.models.VersusModel;
import fr.iut.tetris.controllers.VersusController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;
import fr.iut.tetris.models.BlockModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class VersusVue extends JPanel {
	
    VersusModel model;
    VersusController ctrl;

    GamePanelVersus gamePanelPlayer1;
    GamePanelVersus gamePanelPlayer2;
    SplashScreenPanel splashScreen;
    PauseMenu pauseMenu;
    JLayeredPane testPane;

    public VersusVue(VersusModel model, VersusController ctrl) {
        this.model = model;
        this.ctrl = ctrl;

        Color bg = Color.BLUE;
        int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
        int ww = Config.getInstance().getInt("WINDOW_WIDTH")*2;
        setPreferredSize(new Dimension( ww, wh ));
        setBackground(bg);


        gamePanelPlayer1 = new GamePanelVersus(model,getPreferredSize() ,0);
        gamePanelPlayer1.setLocation(0, 0);
        gamePanelPlayer1.setVisible(true);

        gamePanelPlayer2 = new GamePanelVersus(model,getPreferredSize() ,1);
        gamePanelPlayer2.setLocation(0, 0);
        gamePanelPlayer2.setVisible(true);

        splashScreen = new SplashScreenPanel(getPreferredSize(), ctrl, model);
        splashScreen.setVisible(true);

        pauseMenu = new PauseMenu(getPreferredSize(), ctrl,model);

        testPane = new JLayeredPane();
        testPane.add(new StaticStarAnimation(getPreferredSize(),new Color(0f,0f,0.1f),55),JLayeredPane.DEFAULT_LAYER);
        testPane.add(gamePanelPlayer1,JLayeredPane.PALETTE_LAYER);
        testPane.add(gamePanelPlayer2,JLayeredPane.PALETTE_LAYER);
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

    public void setModel(VersusModel model) {
        this.model = model;
    }

    public void recalculate() {
        splashScreen.recalculate(model.gameState == GameState.WAITING || model.gameState == GameState.FINISHED,model.gameState);
        pauseMenu.recalculate(model.gameState);
        gamePanelPlayer1.recalculate();
        gamePanelPlayer2.recalculate();
    }
}

class GamePanelVersus extends JPanel {

	private static final long serialVersionUID = 1L;
	
	VersusModel model;
    int squareSize = 40;
    BufferedImage img = null;
    JPanel mainPanel;
    NextPiecePanel nextPiecePanel;
    BlockModel noBlockModel;
    JLabel scoreLabel;
    JPanel effectsPanel;
    int player;

    public GamePanelVersus(VersusModel model, Dimension dimension, int player) {
    	
        setLocation(0, 0);
        setPreferredSize(dimension);
        setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
        setOpaque(false);
        this.player = player;

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

        JLabel labelNextPiece;

        if (this.player == 1)
            labelNextPiece = new JLabel("<html>Next A:");
        else
            labelNextPiece = new JLabel("<html>Next B:");

        labelNextPiece.setForeground(Color.white);
        labelNextPiece.setFont(Config.getInstance().getFont("FONT_NORMAL"));

        if (this.player == 1) {
            scoreLabel = new JLabel("<html>Score: "+this.model.currentScorePlayerA);
        }
        else {
            scoreLabel = new JLabel("<html>Score: "+this.model.currentScorePlayerB);
        }
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(Config.getInstance().getFont("FONT_BIG"));

        effectsPanel = new JPanel();
        effectsPanel.setLayout(new GridLayout(0,2,10,10));
        effectsPanel.setOpaque(false);

        add(mainPanel);
        add(labelNextPiece);
        add(nextPiecePanel);
        add(scoreLabel);
        add(effectsPanel);

        if (this.player == 0) {
            try {
                Object[][] grid = model.computeMixedGrid(0);
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
        }
        else {
            try {
                Object[][] grid = model.computeMixedGrid(1);
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
        }

        SpringLayout layout = new SpringLayout();

        if (this.player == 0) {

            layout.putConstraint(SpringLayout.NORTH, scoreLabel, 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, scoreLabel, 0, SpringLayout.WEST, mainPanel);

            layout.putConstraint(SpringLayout.WEST, mainPanel, 10, SpringLayout.EAST, labelNextPiece);
            layout.putConstraint(SpringLayout.NORTH, mainPanel, 10, SpringLayout.SOUTH, scoreLabel);
            layout.putConstraint(SpringLayout.SOUTH, mainPanel, -10, SpringLayout.SOUTH, this);

            layout.putConstraint(SpringLayout.NORTH, labelNextPiece, 0, SpringLayout.NORTH, mainPanel);
            layout.putConstraint(SpringLayout.WEST, labelNextPiece, 10, SpringLayout.WEST, this);

            layout.putConstraint(SpringLayout.NORTH, nextPiecePanel, 10, SpringLayout.SOUTH, labelNextPiece);
            layout.putConstraint(SpringLayout.WEST, nextPiecePanel, 10, SpringLayout.WEST, this);

            layout.putConstraint(SpringLayout.NORTH, effectsPanel, 150, SpringLayout.SOUTH, nextPiecePanel);
            layout.putConstraint(SpringLayout.WEST, effectsPanel, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.EAST, effectsPanel, -10, SpringLayout.WEST, mainPanel);
        }
        else {

            layout.putConstraint(SpringLayout.NORTH, scoreLabel, 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.EAST, scoreLabel, 0, SpringLayout.EAST, mainPanel);

            layout.putConstraint(SpringLayout.EAST, mainPanel, -10, SpringLayout.WEST, labelNextPiece);
            layout.putConstraint(SpringLayout.NORTH, mainPanel, 10, SpringLayout.SOUTH, scoreLabel);
            layout.putConstraint(SpringLayout.SOUTH, mainPanel, -10, SpringLayout.SOUTH, this);

            layout.putConstraint(SpringLayout.NORTH, labelNextPiece, 0, SpringLayout.NORTH, mainPanel);
            layout.putConstraint(SpringLayout.EAST, labelNextPiece, -10, SpringLayout.EAST, this);

            layout.putConstraint(SpringLayout.NORTH, nextPiecePanel, 10, SpringLayout.SOUTH, labelNextPiece);
            layout.putConstraint(SpringLayout.WEST, nextPiecePanel, 10, SpringLayout.EAST, mainPanel);

            layout.putConstraint(SpringLayout.NORTH, effectsPanel, 150, SpringLayout.SOUTH, nextPiecePanel);
            layout.putConstraint(SpringLayout.WEST, effectsPanel, 10, SpringLayout.EAST, mainPanel);
            layout.putConstraint(SpringLayout.EAST, effectsPanel, -10, SpringLayout.EAST, this);
        }

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

        if (this.player == 0)
            scoreLabel.setText("<html>Score: "+this.model.currentScorePlayerA);
        else
            scoreLabel.setText("<html>Score: "+this.model.currentScorePlayerB);

        if(model.gameState==GameState.PLAYING) {
            if (this.player == 0) {
                try {
                    nextPiecePanel.recalulate(model.nextPiecePlayerA);
                    BlockModel[][] grid = model.computeMixedGrid(0);

                    for (int y = 0; y < grid.length; y++) {
                        for (int x = 0; x < grid[y].length; x++) {
                            TetrisBlock p = (TetrisBlock) mainPanel.getComponent(y*model.width + x);
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
            else {
                try {
                    nextPiecePanel.recalulate(model.nextPiecePlayerB);
                    BlockModel[][] grid = model.computeMixedGrid(1);

                    for (int y = 0; y < grid.length; y++) {
                        for (int x = 0; x < grid[y].length; x++) {
                            TetrisBlock p = (TetrisBlock) mainPanel.getComponent(y*model.width + x);
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
        }

        effectsPanel.removeAll();
        if(player == 0) {
            for (EffectModel m: model.effectListPlayerA) {
                effectsPanel.add(new EffectImage(m.imagePath));
            }
        } else {

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