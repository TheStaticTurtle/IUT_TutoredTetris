package fr.iut.tetris.vues;


import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.SettingsController;
import fr.iut.tetris.models.SettingsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CustomSlider extends JSlider implements MouseListener {

	int borderSize = 10;
	int space = 5;
	int raw_thumb_x;
	int thumbSpaceing = borderSize + space;
	boolean isMouseDown = false;
	int min,max;
	Color borderColor;
	Color thumbColor;


	public CustomSlider(int min, int max, Color borderColor, Color thumbColor) {
		addMouseListener(this);
		raw_thumb_x = (borderSize+space);
		this.min = min;
		this.max= max;
		this.borderColor=borderColor;
		this.thumbColor=thumbColor;
	}

	static int map(int x, int in_min, int in_max, int out_min, int out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	@Override
	public int getValue() {
		try {
			return map(this.raw_thumb_x+25,thumbSpaceing*2,getWidth()-thumbSpaceing*2,this.min,this.max);
		}catch (ArithmeticException ignored) {
			return 0;
		}
	}

	@Override
	public void setValue(int n) {
		n = Math.min(Math.max(n,this.min),this.max);
		raw_thumb_x = map(n,this.min,this.max,thumbSpaceing*2,getWidth()-thumbSpaceing*2)-25;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		int thumbSize = getHeight() - borderSize*2 -space*2;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0 ,0,getWidth(),borderSize);
		g2d.fillRect(0 ,0,borderSize,getHeight());

		g2d.fillRect(0 ,getHeight()-borderSize,getWidth(),borderSize);
		g2d.fillRect(getWidth()-borderSize ,0,borderSize,getHeight());

		if(isOpaque()) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect( borderSize ,borderSize,getWidth()-borderSize*2,getHeight()-borderSize*2);
		}

		Point pos = getMousePosition();
		if(pos != null && isMouseDown) {
			raw_thumb_x = Math.min(Math.max(thumbSpaceing*2,pos.x),getWidth()-thumbSpaceing*2) -25;
			super.fireStateChanged();
		}

		g2d.setColor(this.thumbColor);
		g2d.fillRect( raw_thumb_x+thumbSpaceing ,borderSize+space,thumbSize,thumbSize );
	}


	@Override
	public void mousePressed(MouseEvent e) {
		isMouseDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isMouseDown = false;
	}

	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}

public class SettingsVue extends JPanel{

	SettingsModel model;
	SettingsController ctrl;

	public JSlider soundMusicLevel;
	public JSlider soundSFXMusicLevel;

	public SettingsVue(SettingsModel model, SettingsController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.BLACK;

		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(13,1);
		GridLayout subLayout = new GridLayout(1,2);

		int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
		int ww = Config.getInstance().getInt("WINDOW_WIDTH");
		setPreferredSize(new Dimension( ww, wh ));
		setBackground(bg);
		mainPanel.setPreferredSize(new Dimension((int) (ww*0.7), (int) (wh*0.85)));

		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainLayout.setVgap(5);


		JPanel myLabel = new TetrisLogo(this, (int) (ww*0.7));

		JLabel soundSettingsLabel = new JLabel("Sound");
		JLabel soundMusicLabel = new JLabel("<html>Music: ");
		soundMusicLevel = new CustomSlider(-50,5,Color.WHITE,Color.ORANGE);
		JLabel soundSFXMusicLabel = new JLabel("<html>SFX: ");
		soundSFXMusicLevel = new CustomSlider(-50,5,Color.WHITE,Color.GREEN);

		JButton backButton = new MenuButton("Save",Color.GREEN,Color.WHITE,ctrl);

		myLabel.setFont(Config.getInstance().getFont("FONT_ULTRABIG"));
		soundSettingsLabel.setFont(Config.getInstance().getFont("FONT_BIG"));
		soundSettingsLabel.setForeground(Color.white);

		soundMusicLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		soundMusicLabel.setForeground(Color.white);
		soundSFXMusicLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		soundSFXMusicLabel.setForeground(Color.white);

		backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		backButton.addActionListener(ctrl);
		backButton.setActionCommand("CLICK:SETTINGS:BACK");

		soundMusicLevel.addChangeListener(ctrl);
		soundSFXMusicLevel.addChangeListener(ctrl);

		mainPanel.add(myLabel);
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add(soundSettingsLabel);

		JPanel musicVolumeCtrlPanel = new JPanel();
		musicVolumeCtrlPanel.setOpaque(false);
		musicVolumeCtrlPanel.setLayout(subLayout);
		musicVolumeCtrlPanel.add(soundMusicLabel);
		musicVolumeCtrlPanel.add(soundMusicLevel);
		mainPanel.add(musicVolumeCtrlPanel);

		JPanel SFXVolumeCtrlPanel = new JPanel();
		SFXVolumeCtrlPanel.setOpaque(false);
		SFXVolumeCtrlPanel.setLayout(subLayout);
		SFXVolumeCtrlPanel.add(soundSFXMusicLabel);
		SFXVolumeCtrlPanel.add(soundSFXMusicLevel);
		mainPanel.add(SFXVolumeCtrlPanel);

		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());
		mainPanel.add(backButton);

		mainPanel.setLocation(0, 0);
		mainPanel.setPreferredSize(mainPanel.getPreferredSize());

		mainPanel.setBounds(0, 0, (int)mainPanel.getPreferredSize().getWidth(), (int)mainPanel.getPreferredSize().getHeight());
		mainPanel.setVisible(true);

		JLayeredPane testPane = new JLayeredPane();
		testPane.add(new StarsAnimation(getPreferredSize()),JLayeredPane.DEFAULT_LAYER);
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
