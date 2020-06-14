package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.HighScoresController;
import fr.iut.tetris.models.HighScoresModel;

import javax.swing.*;
import java.awt.*;

public class HighScoresVue extends JPanel {

    HighScoresModel model;
    HighScoresController ctrl;

    public HighScoresVue(HighScoresModel model, HighScoresController ctrl) {
        this.model = model;
        this.ctrl = ctrl;

        Color bg = Color.BLACK;

        int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
        int ww = Config.getInstance().getInt("WINDOW_WIDTH");
        setPreferredSize(new Dimension( ww, wh ));
        setBackground(bg);

        JPanel mainPanel = new JPanel();
        GridLayout mainLayout = new GridLayout(0,1);

        mainPanel.setPreferredSize(new Dimension((int) (ww*0.7), (int) (wh*0.85)));

        JPanel myLabel = new TetrisLogo((int) (ww*0.7));
        JButton backButton = new MenuButton("Back",Color.ORANGE,Color.WHITE,ctrl);

        backButton.addActionListener(ctrl);
        backButton.setActionCommand("CLICK:BACK");


        myLabel.setFont(Config.getInstance().getFont("FONT_BIG"));
        backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

        mainPanel.setLayout(mainLayout);
        mainPanel.setOpaque(false);
        mainLayout.setVgap(5);

        mainPanel.add(myLabel);
        mainPanel.add( new Spacer());
        mainPanel.add( new Spacer());

        JLabel normalLabel = new JLabel("Normal:");
        JLabel normalSoloBestLabel = new JLabel("  Solo: "+Config.getInstance().getInt("SCORE_SOLO_BEST")+" pts");
        JLabel normalCoopBestLabel = new JLabel("  Coop: "+Config.getInstance().getInt("SCORE_COOP_BEST")+" pts");

        JLabel legacyLabel = new JLabel("Legacy:");
        JLabel legacySoloBestLabel = new JLabel("  Solo: "+Config.getInstance().getInt("SCORE_SOLO_BEST_LEGACY")+" pts");
        JLabel legacyCoopBestLabel = new JLabel("  Coop: "+Config.getInstance().getInt("SCORE_COOP_BEST_LEGACY")+" pts");

        normalLabel.setFont(Config.getInstance().getFont("FONT_BIG"));
        normalSoloBestLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
        normalCoopBestLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
        normalLabel.setForeground(Color.white);
        normalSoloBestLabel.setForeground(Color.white);
        normalCoopBestLabel.setForeground(Color.white);

        legacyLabel.setFont(Config.getInstance().getFont("FONT_BIG"));
        legacySoloBestLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
        legacyCoopBestLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
        legacyLabel.setForeground(Color.white);
        legacySoloBestLabel.setForeground(Color.white);
        legacyCoopBestLabel.setForeground(Color.white);

        mainPanel.add(normalLabel);
        mainPanel.add(normalSoloBestLabel);
        mainPanel.add(normalCoopBestLabel);
        mainPanel.add(new Spacer());
        mainPanel.add(legacyLabel);
        mainPanel.add(legacySoloBestLabel);
        mainPanel.add(legacyCoopBestLabel);

        mainPanel.add( new Spacer());
        mainPanel.add(backButton);

        mainPanel.setLocation(0, 0);
        mainPanel.setPreferredSize(mainPanel.getPreferredSize());

        mainPanel.setBounds(0, 0, (int)mainPanel.getPreferredSize().getWidth(), (int)mainPanel.getPreferredSize().getHeight());
        mainPanel.setVisible(true);

        JLayeredPane testPane = new JLayeredPane();
        testPane.add(new MovingStarsAnimation(getPreferredSize()),JLayeredPane.DEFAULT_LAYER);
        testPane.add(mainPanel,JLayeredPane.PALETTE_LAYER);
        testPane.setPreferredSize(getPreferredSize());

        SpringLayout lyt = new SpringLayout();
        SpringLayout lyt2 = new SpringLayout();
        lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
        lyt.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, testPane);
        lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, this);
        testPane.setLayout(lyt);
        setLayout(lyt2);

        add(testPane);

        JPanel t = this;
        new Timer(1, e -> {
            t.repaint();
            t.revalidate();
        }).start();
    }
}
