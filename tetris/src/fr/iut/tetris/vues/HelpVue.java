package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.HelpController;
import fr.iut.tetris.controllers.SettingsKeysController;
import fr.iut.tetris.models.HelpModel;
import fr.iut.tetris.models.SettingsKeysModel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HelpVue extends JPanel {
    HelpModel model;
    HelpController ctrl;

    public HelpVue(HelpModel model, HelpController ctrl) {
        this.model = model;
        this.ctrl = ctrl;

        Color bg = Color.BLACK;

        JPanel mainPanel = new JPanel();
        GridLayout mainLayout = new GridLayout(0, 1);
        //GridLayout subLayout = new GridLayout(1, 5);
        GridBagLayout subLayout = new GridBagLayout();
        //subLayout.columnWeights = new double[]{0.5,0.5};

        int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
        int ww = Config.getInstance().getInt("WINDOW_WIDTH");
        setPreferredSize(new Dimension(ww, wh));
        setBackground(bg);
        mainPanel.setPreferredSize(new Dimension((int) (ww * 0.90), (int) (wh * 0.95)));

        mainPanel.setLayout(subLayout);
        mainPanel.setOpaque(false);
        //mainLayout.setVgap(5);


		/*JPanel myLabel = new TetrisLogo(this, (int) (ww*0.7));
		myLabel.setFont(Config.getInstance().getFont("FONT_ULTRABIG"));*/

        ArrayList<JLabel> labels = new ArrayList<JLabel>();
        ArrayList<String> images = new ArrayList<String>();
        ArrayList<Integer> durations = new ArrayList<Integer>();

        JLabel helpLabel = new JLabel("Help:"); labels.add(helpLabel); images.add(""); durations.add(0);
        JLabel helpText = new JLabel("<html>Welcome to TutoredTetris, in this game you play with new pieces but you can disable them by activating the legacy option."); labels.add(helpText); images.add(""); durations.add(0);

        JLabel helpEffectLabel = new JLabel("Effects:"); labels.add(helpEffectLabel); images.add(""); durations.add(0);

        JLabel helpEffectgeneral = new JLabel("<html>In versus mode there are custom effects (if enabled in the settings). Purple effects are malus and yellow effects are bonuses you gain / send one every 80 points and they stay for a variable duration"); labels.add(helpEffectgeneral); images.add(""); durations.add(0);
        JLabel effectDeleteLine = new JLabel("<html>This will randomly delete a line"); labels.add(effectDeleteLine); images.add("/res/effects/bonus_delete_line.png"); durations.add(0);
        JLabel effectBSpeed = new JLabel("<html>This will slow your game down"); labels.add(effectBSpeed); images.add("/res/effects/bonus_speed.png"); durations.add(Config.getInstance().getInt("EFFECT_DURATION_BONUS_SPEED"));
        JLabel effectRemoveMalus = new JLabel("<html>This will remove a malus"); labels.add(effectRemoveMalus); images.add("/res/effects/malus_remove_malus.png"); durations.add(0);

        JLabel effectMSpeed = new JLabel("<html>This will speed up your game"); labels.add(effectMSpeed); images.add("/res/effects/malus_speed.png"); durations.add(Config.getInstance().getInt("EFFECT_DURATION_MALUS_SPEED"));
        JLabel effectBlind = new JLabel("<html>This will hides the next piece"); labels.add(effectBlind); images.add("/res/effects/malus_blind.png"); durations.add(Config.getInstance().getInt("EFFECT_DURATION_MALUS_BLIND"));
        JLabel effectReverse = new JLabel("<html>This will invert your controls"); labels.add(effectReverse); images.add("/res/effects/malus_reverse_cmds.png"); durations.add(Config.getInstance().getInt("EFFECT_DURATION_MALUS_REVERSE"));
        JLabel effectRotate = new JLabel("<html>This rotate you piece randomly"); labels.add(effectRotate); images.add("/res/effects/malus_rotate.png"); durations.add(Config.getInstance().getInt("EFFECT_DURATION_MALUS_ROTATE"));
        JLabel effectBlock = new JLabel("<html>This will place a random block"); labels.add(effectBlock); images.add("/res/effects/malus_random_block.png"); durations.add(0);


        JButton backButton = new MenuButton("Back", Color.GREEN, Color.WHITE, ctrl);

        for (JLabel l : labels) {
            l.setFont(Config.getInstance().getFont("FONT_VERYTINY"));
            l.setForeground(Color.white);
        }

        helpLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
        helpEffectLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));

        backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

        backButton.addActionListener(ctrl);
        backButton.setActionCommand("CLICK:BACK");

        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.BOTH;

        c1.weightx = 0.5;
        c1.weighty = 1.0;

        assert labels.size() == images.size();
        assert labels.size() == durations.size();
        for (int i = 0; i < labels.size(); i++) {
            /*JPanel pan = new JPanel();
            pan.setOpaque(false);
            pan.setLayout(subLayout);*/
            if (!images.get(i).equals("")) {
                c1.fill = GridBagConstraints.BOTH;
                c1.gridwidth = 1;
                c1.weightx = 0.12;

                c1.gridy = i;
                c1.gridx = 0;
                mainPanel.add(new EffectImage(images.get(i),true),c1);

                if(durations.get(i) != 0) {
                    c1.gridy = i;
                    c1.gridx = 2;
                    c1.weightx = 0.01;
                    JLabel label = new JLabel(durations.get(i)+" sec");
                    label.setFont(Config.getInstance().getFont("FONT_VERYTINY"));
                    label.setForeground(Color.white);
                    mainPanel.add(label,c1);
                } else {
                    c1.gridy = i;
                    c1.gridx = 2;
                    c1.weightx = 0.01;
                    JLabel label = new JLabel("auto");
                    label.setFont(Config.getInstance().getFont("FONT_VERYTINY"));
                    label.setForeground(Color.white);
                    mainPanel.add(label,c1);
                }

                c1.weightx = 0.87;
                c1.gridx = 1;
                mainPanel.add(labels.get(i),c1);
            } else {
                c1.fill = GridBagConstraints.BOTH;
                c1.weightx = 1;
                c1.gridx = 0;
                c1.gridy = i;
                c1.gridwidth = 3;
                mainPanel.add(labels.get(i),c1);
            }

            //mainPanel.add(pan);
        }

        /*c1.gridwidth = 2;
        c1.gridx = 0;
        c1.weightx = 1;
        c1.gridy = labels.size();
        mainPanel.add(backButton,c1);*/

        mainPanel.setLocation(0, 0);
        mainPanel.setPreferredSize(mainPanel.getPreferredSize());

        mainPanel.setBounds(0, 0, (int) mainPanel.getPreferredSize().getWidth(), (int) mainPanel.getPreferredSize().getHeight());
        mainPanel.setVisible(true);

        JLayeredPane testPane = new JLayeredPane();
        testPane.add(new MovingStarsAnimation(getPreferredSize()), JLayeredPane.DEFAULT_LAYER);
        testPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);
        testPane.add(backButton, JLayeredPane.PALETTE_LAYER);
        testPane.setPreferredSize(getPreferredSize());

        SpringLayout lyt = new SpringLayout();
        SpringLayout lyt2 = new SpringLayout();
        lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, backButton, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
        lyt.putConstraint(SpringLayout.SOUTH, backButton, -15, SpringLayout.SOUTH, testPane);
        lyt.putConstraint(SpringLayout.EAST, backButton, -35, SpringLayout.EAST, testPane);
        lyt.putConstraint(SpringLayout.WEST, backButton, 35, SpringLayout.WEST, testPane);

        lyt.putConstraint(SpringLayout.NORTH, mainPanel, 10, SpringLayout.NORTH, testPane);
        lyt.putConstraint(SpringLayout.SOUTH, mainPanel, -10, SpringLayout.NORTH, backButton);

        lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, testPane);


        lyt2.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        lyt2.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, this);
        testPane.setLayout(lyt);
        setLayout(lyt2);

        add(testPane);

        JPanel t = this;
        new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.repaint();
                t.revalidate();
            }
        }).start();
    }
}