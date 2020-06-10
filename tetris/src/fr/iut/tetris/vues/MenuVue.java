package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.*;
import fr.iut.tetris.models.MenuModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;




public class MenuVue extends JPanel  {
	MenuModel model;
	MenuController ctrl;

	public MenuVue(MenuModel model, MenuController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		Color bg = Color.BLACK;

		int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
		int ww = Config.getInstance().getInt("WINDOW_WIDTH");
		setPreferredSize(new Dimension( ww, wh ));
		setBackground(bg);


		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,1);
		GridLayout subLayout = new GridLayout(1,2);

		mainPanel.setPreferredSize(new Dimension((int) (ww*0.7), (int) (wh*0.7)));

		JPanel myLabel = new TetrisLogo(this, (int) (ww*0.7));
		JButton soloButton = new MenuButton("Solo",Color.YELLOW,Color.WHITE,ctrl);
		JButton coopButton = new MenuButton("Coop",Color.RED,Color.WHITE,ctrl);
		JButton versusButton = new MenuButton("Versus",Color.ORANGE,Color.WHITE,ctrl);
		JButton settingsButton = new MenuButton("Settings",Color.CYAN,Color.WHITE,ctrl);

		soloButton.addActionListener(ctrl);
		soloButton.setActionCommand("CLICK:MENU:SOLO");

		coopButton.addActionListener(ctrl);
		coopButton.setActionCommand("CLICK:MENU:COOP");

		versusButton.addActionListener(ctrl);
		versusButton.setActionCommand("CLICK:MENU:VERSUS");

		settingsButton.addActionListener(ctrl);
		settingsButton.setActionCommand("CLICK:MENU:SETTINGS");
		
		JButton quitButton = new MenuButton("Quit",Color.LIGHT_GRAY,Color.WHITE,ctrl);
		JButton creditButton = new MenuButton("Credits",Color.LIGHT_GRAY,Color.WHITE,ctrl);

		creditButton.addActionListener(ctrl);
		creditButton.setActionCommand("CLICK:MENU:CREDIT");
		quitButton.addActionListener(ctrl);
		quitButton.setActionCommand("CLICK:MENU:QUIT");

		myLabel.setFont(Config.getInstance().getFont("FONT_ULTRABIG"));
		soloButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		coopButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		versusButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		settingsButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		quitButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		creditButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainPanel.setBackground(bg);
		mainLayout.setVgap(5);
		subLayout.setHgap(10);

		JPanel soloCoopPanel = new JPanel();
		soloCoopPanel.setOpaque(false);
		soloCoopPanel.setLayout(subLayout);
		soloCoopPanel.add(coopButton);
		soloCoopPanel.add(versusButton);

		JPanel quitCreditsPanel = new JPanel();
		quitCreditsPanel.setOpaque(false);
		quitCreditsPanel.setLayout(subLayout);
		quitCreditsPanel.add(quitButton);
		quitCreditsPanel.add(creditButton);


		mainPanel.add(myLabel);
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add(soloButton);
		mainPanel.add(soloCoopPanel);
		mainPanel.add(settingsButton);
		mainPanel.add( new Spacer());
		mainPanel.add(quitCreditsPanel);
		mainPanel.add( new Spacer());


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
