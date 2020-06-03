package fr.iut.tetris.vues;

import fr.iut.tetris.Main;
import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.models.MenuModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

class Spacer extends Box {

	private static final long serialVersionUID = 1L;

	public Spacer() {
		super(0);
		setPreferredSize(new Dimension(1, 1));
		setSize(new Dimension(1, 1));
	}
}

class HoveredButtonIcon implements Icon {
	int height;
	int width;
	Color foreGroundColor;
	Color backGroundColor;
	String text;
	Font font;

	public HoveredButtonIcon(int height, int width,Font font, String text, Color foreGroundColor, Color backGroundColor) {
		this.height = height;
		this.width = width;
		this.foreGroundColor = foreGroundColor;
		this.backGroundColor = backGroundColor;
		this.text = text;
		this.font = font;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		x = getIconWidth() /2;
		y = getIconHeight()/2;

		int width = 12;
		int length = 35;

		g2.setColor(this.backGroundColor);
		g2.fillRect(0, 0, length, width);
		g2.fillRect(0, 0, width, length);
		g2.fillRect(getIconWidth()-length, getIconHeight()-width, length, width);
		g2.fillRect(getIconWidth()-width, getIconHeight()-length, 12, length);

		g2.setColor(foreGroundColor);
		g2.setFont(this.font);
		g2.drawString(this.text,x - g.getFontMetrics().stringWidth(this.text) /2 ,y + g.getFontMetrics().getHeight() /2 - 6);
	}

	public int getIconWidth() { return this.width; }
	public int getIconHeight() { return this.height; }
}
class MenuButton extends JButton implements MouseListener {

	private static final long serialVersionUID = 1L;
	
	String text;
	Font font;
	Color foreGroundColor;
	Color backGroundColor;

	public MenuButton(String text, Color foreGroundColor, Color backGroundColor) {
		this.text = text;
		this.foreGroundColor = foreGroundColor;
		this.backGroundColor = backGroundColor;
		super.setForeground(foreGroundColor);
		addMouseListener(this);
		setBorder(null);
		setText(text);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public MenuButton(String text) {
		this.text = text;
		this.foreGroundColor = Color.black;
		this.backGroundColor = Color.black;
		super.setForeground(foreGroundColor);
		addMouseListener(this);
		setBorder(null);
		setText(text);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override public void setForeground(Color foreGroundColor) {
		super.setForeground(foreGroundColor);
		this.foreGroundColor = foreGroundColor;
	}

	@Override public void setFont(Font font) {
		super.setFont(font);
		this.font = font;
	}

	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }

	@Override public void mouseEntered(MouseEvent e) {
		setText("");
		setIcon(new HoveredButtonIcon(getHeight(),getWidth(),this.font, this.text, this.foreGroundColor, this.backGroundColor));
	}
	@Override public void mouseExited(MouseEvent e) {
		setText(text);
		setIcon(null);
	}
}
class TetrisLogo extends JPanel {

	private static final long serialVersionUID = 1L;

	int offset = 10;

	int canvasWidth = 450;
	int canvasHeight = (int)(canvasWidth*0.333);
	int baseWidth = canvasWidth-offset;
	int baseHeight = canvasHeight-offset;

	int width = baseWidth;
	int height = baseHeight;

	int speed = 2;
	int direction = speed;
	Image img;

	TetrisLogo(JPanel parent, int width) {
		this.canvasWidth = width;
		this.canvasHeight = (int)(this.canvasWidth*0.333);
		this.baseWidth = this.canvasWidth-offset;
		this.baseHeight = this.canvasHeight-offset;

		try {

			URL logo = getClass().getResource( "/res/logo.png" );
			img = ImageIO.read(logo);
			
		} catch (IOException e) {
			System.out.println("Logo non trouvé");
		}
		

		final TetrisLogo p = this;
		new Timer(100, new ActionListener() { public void actionPerformed(ActionEvent e) {
			p.update_size();
		}}).start();
	}

	public void update_size() {
		width += direction;
		height += direction;
		if(width>(baseWidth+offset) || height>(baseHeight+offset)) { direction = -speed; }
		if(width<(baseWidth-offset) || height<(baseHeight-offset)) { direction = speed; }
	}

	@Override public int getHeight() { return canvasHeight; }
	@Override public int getWidth() { return canvasWidth; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		int spaceX = canvasWidth - width;
		int spaceY = canvasHeight - height;

		if (img != null) {
			Image tmp = img.getScaledInstance(width, height, Image.SCALE_REPLICATE);
			g2.setColor(Color.black);
			g2.drawImage(tmp, spaceX/2, spaceY/2, width, height,this);
		}
	}
}

class StarModel {
	Point position;
	Dimension parent;
	Random rng;
	int speed = 0;
	int size = 96;
	public StarModel(Random rng, Dimension parent, int speed) {
		this.parent = parent;
		this.speed = speed;
		this.rng = rng;
		this.position = new Point();
		this.position.y = rng.nextInt(this.parent.height);
		this.position.x = rng.nextInt(this.parent.width);
		this.size = 100;
	}

	public void move() {
		position.x += speed;
		if(position.x>parent.width+size) {
			position.x=0;
			this.position.y = this.rng.nextInt(this.parent.height);
		}
	}
}
class StarsAnimation extends JPanel {

	private static final long serialVersionUID = 1L;
	
	Random rn = new Random();
	StarModel[] stars = new StarModel[25];
	Dimension size;
	Image img;
	Image resize_img;

	public StarsAnimation(Dimension size) {
		this.size = size;

		for(int i=0; i<stars.length; i++) {
			int s = rn.nextInt(2)+1;
			stars[i] = new StarModel(rn,size,s+2);
		}

		
		try {

			URL star = getClass().getResource( "/res/star.png" );
			img = ImageIO.read(star);

			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-img.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter((BufferedImage) img, null);
			resize_img = img.getScaledInstance(8*8, 8, Image.SCALE_REPLICATE);
			
		} catch (IOException e) {
			System.out.println("Logo non trouvé");
		}
		

		StarsAnimation p = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			for (StarModel star : p.stars) {
				star.move();
			}
		}}).start();

	}

	@Override public int getHeight() { return size.height; }
	@Override public int getWidth() { return size.width; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (StarModel star : stars) {
			g2.drawImage(resize_img,star.position.x-star.size, star.position.y, this);
		}
	}
}

public class MenuVue extends JPanel  {

	private static final long serialVersionUID = 1L;
	
	MenuModel model;
	MenuController ctrl;

	public MenuVue(MenuModel model, MenuController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		Color bg = Color.BLACK;

		setPreferredSize(new Dimension( 640, 870 ));
		setBackground(bg);


		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(10,1);
		GridLayout subLayout = new GridLayout(1,2);

		mainPanel.setPreferredSize(new Dimension(450,600));

		JPanel myLabel = new TetrisLogo(this,450);
		JButton soloButton = new MenuButton("Solo",Color.YELLOW,Color.WHITE);
		JButton coopButton = new MenuButton("Coop",Color.RED,Color.WHITE);
		JButton versusButton = new MenuButton("Versus",Color.ORANGE,Color.WHITE);
		JButton settingsButton = new MenuButton("Parametres",Color.CYAN,Color.WHITE);

		JButton quitButton = new MenuButton("Quitter",Color.LIGHT_GRAY,Color.WHITE);
		JButton creditButton = new MenuButton("Credits",Color.LIGHT_GRAY,Color.WHITE);

		creditButton.addActionListener(ctrl);
		creditButton.setActionCommand("CLICK:MENU:CREDIT");
		quitButton.addActionListener(ctrl);
		quitButton.setActionCommand("CLICK:MENU:QUIT");

		Font font = new JLabel().getFont();
		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Font bigFont = font.deriveFont(96f);
		Font mormalFont = font.deriveFont(40f);
		myLabel.setFont(bigFont);
		soloButton.setFont(mormalFont);
		coopButton.setFont(mormalFont);
		versusButton.setFont(mormalFont);
		settingsButton.setFont(mormalFont);
		quitButton.setFont(mormalFont);
		creditButton.setFont(mormalFont);

		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		//mainPanel.setBackground(bg);
		mainLayout.setVgap(5);
		subLayout.setHgap(10);

		JPanel soloCoopPanel = new JPanel();
		//soloCoopPanel.setBackground(bg);
		soloCoopPanel.setOpaque(false);
		soloCoopPanel.setLayout(subLayout);
		soloCoopPanel.add(coopButton);
		soloCoopPanel.add(versusButton);

		JPanel quitCreditsPanel = new JPanel();
		//quitCreditsPanel.setBackground(bg);
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
