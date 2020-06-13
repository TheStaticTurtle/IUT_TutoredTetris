package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.SettingsKeysController;
import fr.iut.tetris.enums.Resolution;
import fr.iut.tetris.models.SettingsKeysModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsKeysVue extends JPanel {

	SettingsKeysModel model;
	SettingsKeysController ctrl;

	public SettingsKeysVue(SettingsKeysModel model, SettingsKeysController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.BLACK;

		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,1);
		GridLayout subLayout = new GridLayout(1,2);

		int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
		int ww = Config.getInstance().getInt("WINDOW_WIDTH");
		setPreferredSize(new Dimension( ww, wh ));
		setBackground(bg);
		mainPanel.setPreferredSize(new Dimension((int) (ww*0.7), (int) (wh*0.85)));

		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		//mainLayout.setVgap(5);


		/*JPanel myLabel = new TetrisLogo(this, (int) (ww*0.7));
		myLabel.setFont(Config.getInstance().getFont("FONT_ULTRABIG"));*/

		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		ArrayList<String> controls = new ArrayList<String>();

		JLabel generalControlLabel = new JLabel("General:"); labels.add(generalControlLabel); controls.add("unused");
		JLabel backKeyLabel = new JLabel("<html>Back: "); labels.add(backKeyLabel);controls.add("KEYCODE_GOBACK");
		JLabel startKeyLabel = new JLabel("<html>Start: "); labels.add(startKeyLabel);controls.add("KEYCODE_STARTGAME");

		JLabel player1ControlLabel = new JLabel("Player A:"); labels.add(player1ControlLabel); controls.add("unused");
		JLabel p1LeftKeyLabel = new JLabel("<html>Left: "); labels.add(p1LeftKeyLabel);controls.add("KEYCODE_P1_LEFT");
		JLabel p1RightKeyLabel = new JLabel("<html>Right: "); labels.add(p1RightKeyLabel);controls.add("KEYCODE_P1_RIGHT");
		JLabel p1DownKeyLabel = new JLabel("<html>Down: "); labels.add(p1DownKeyLabel);controls.add("KEYCODE_P1_DOWN");
		JLabel p1FallKeyLabel = new JLabel("<html>Fall: "); labels.add(p1FallKeyLabel);controls.add("KEYCODE_P1_FASTDOWN");
		JLabel p1RotateKeyLabel = new JLabel("<html>Rotate: "); labels.add(p1RotateKeyLabel);controls.add("KEYCODE_P1_ROTATE");

		JLabel player2ControlLabel = new JLabel("Player B:");  labels.add(player2ControlLabel); controls.add("unused");
		JLabel p2LeftKeyLabel = new JLabel("<html>Left: "); labels.add(p2LeftKeyLabel);controls.add("KEYCODE_P2_LEFT");
		JLabel p2RightKeyLabel = new JLabel("<html>Right: "); labels.add(p2RightKeyLabel);controls.add("KEYCODE_P2_RIGHT");
		JLabel p2DownKeyLabel = new JLabel("<html>Down: "); labels.add(p2DownKeyLabel);controls.add("KEYCODE_P2_DOWN");
		JLabel p2FallKeyLabel = new JLabel("<html>Fall: "); labels.add(p2FallKeyLabel);controls.add("KEYCODE_P2_FASTDOWN");
		JLabel p2RotateKeyLabel = new JLabel("<html>Rotate: "); labels.add(p2RotateKeyLabel);controls.add("KEYCODE_P2_ROTATE");

		JButton backButton = new MenuButton("Back",Color.GREEN,Color.WHITE,ctrl);

		for (JLabel l :labels) {
			l.setFont(Config.getInstance().getFont("FONT_TINY"));
			l.setForeground(Color.white);
		}

		generalControlLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		player1ControlLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		player2ControlLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		backButton.addActionListener(ctrl);
		backButton.setActionCommand("CLICK:BACK");

		assert labels.size() == controls.size();
		for (int i = 0; i <labels.size(); i++) {
			JPanel pan = new JPanel();
			pan.setOpaque(false);
			pan.setLayout(subLayout);
			pan.add(labels.get(i));
			if(!controls.get(i).equals("unused")) {
				MenuButton btn = new MenuButton(model.keycodes.get(Config.getInstance().getInt(controls.get(i))),Color.orange,Color.WHITE,ctrl);
				btn.setActionCommand("CLICK:CHANGEKEY:"+controls.get(i));
				btn.addActionListener(ctrl);
				btn.addKeyListener(ctrl.mainCtrl);
				Font f = Config.getInstance().getFont("FONT_TINY");
				btn.setFont(f.deriveFont(f.getSize()+3f));
				btn.setFocusable(false);
				pan.add(btn);
			}
			//pan.add(labels.get(i));
			mainPanel.add(pan);
		}

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
		new Timer(1, new ActionListener() { public void actionPerformed(ActionEvent e) {
			t.repaint();
			t.revalidate();
		}}).start();
	}
}
