package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.controllers.CreditController;
import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.controllers.SettingsController;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
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

		int width = 12;//12
		int length = 35;//35

		g2.setColor(this.backGroundColor);
		g2.fillRect(0, 0, length, width);
		g2.fillRect(0, 0, width, length);
		g2.fillRect(getIconWidth()-length, getIconHeight()-width, length, width);
		g2.fillRect(getIconWidth()-width, getIconHeight()-length, width, length);

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
	ActionListener listener;

	public MenuButton(String text, CreditController ctrl) {
		this(text);
		listener = ctrl;
	}
	public MenuButton(String text, MenuController ctrl) {
		this(text);
		listener = ctrl;
	}
	public MenuButton(String text, Color foreGroundColor, Color backGroundColor, ActionListener ctrl) {
		this(text,foreGroundColor,backGroundColor);
		listener = ctrl;
	}
	public MenuButton(String text, Color foreGroundColor, Color backGroundColor, SoloController ctrl) {
		this(text,foreGroundColor,backGroundColor);
		listener = ctrl;
	}
	public MenuButton(String text, Color foreGroundColor, Color backGroundColor, SettingsController ctrl) {
		this(text,foreGroundColor,backGroundColor);
		listener = ctrl;
	}
	public MenuButton(String text, Color foreGroundColor, Color backGroundColor, CreditController ctrl) {
		this(text,foreGroundColor,backGroundColor);
		listener = ctrl;
	}
	public MenuButton(String text, Color foreGroundColor, Color backGroundColor,MenuController ctrl) {
		this(text,foreGroundColor,backGroundColor);
		super.setForeground(foreGroundColor);
		listener = ctrl;
	}
	public MenuButton(String text, Color foreGroundColor, Color backGroundColor) {
		this(text);
		super.setForeground(foreGroundColor);
		this.foreGroundColor = foreGroundColor;
		this.backGroundColor = backGroundColor;
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
		if(listener!=null)
			listener.actionPerformed(new ActionEvent(this,0,"MOUSE:ENTER"));
	}
	@Override public void mouseExited(MouseEvent e) {
		if(listener!=null)
			listener.actionPerformed(new ActionEvent(this,0,"MOUSE:EXIT"));
		setText(text);
		setIcon(null);
	}
}
class TetrisLogo extends JPanel {

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
		this.width = baseWidth;
		this.height = baseHeight;

		img = Config.getInstance().getRessourceImage("/res/logo.png");


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

	@Override public int getHeight() { return baseHeight +offset; }
	@Override public int getWidth() { return baseWidth+offset; }

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
class StarsAnimation extends JPanel {
	Random rn = new Random();
	StarModel[] stars = new StarModel[35];
	Dimension size;
	Image img;
	Image resize_img;
	Color bgColor = null;

	public static BufferedImage dye(BufferedImage image, Color color) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage dyed = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = dyed.createGraphics();
		g.drawImage(image, 0,0, null);
		g.setComposite(AlphaComposite.SrcAtop);
		g.setColor(color);
		g.fillRect(0,0,w,h);
		g.dispose();
		return dyed;
	}

	public StarsAnimation(Dimension size, Color bgColor, int count) {
		this(size);

		stars = new StarModel[count];
		for(int i=0; i<stars.length; i++) {
			int s = rn.nextInt(2)+1;
			stars[i] = new StarModel(rn,size,s+2);
		}

		this.bgColor = bgColor;
	}

	public StarsAnimation(Dimension size, Color bgColor, int count,Color colorize) {
		this(size,colorize);

		stars = new StarModel[count];
		for(int i=0; i<stars.length; i++) {
			int s = rn.nextInt(2)+1;
			stars[i] = new StarModel(rn,size,s+2);
		}

		this.bgColor = bgColor;
	}

	public StarsAnimation(Dimension size,Color colorize) {
		this.size = size;

		for(int i=0; i<stars.length; i++) {
			int s = rn.nextInt(2)+1;
			stars[i] = new StarModel(rn,size,s+2);
		}

		img = dye(Config.getInstance().getRessourceImage("/res/star.png"),colorize);

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter((BufferedImage) img, null);
		resize_img = img.getScaledInstance(8*8, 8, Image.SCALE_REPLICATE);

		StarsAnimation p = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			for (StarModel star : p.stars) {
				star.move();
			}
		}}).start();
	}

	public StarsAnimation(Dimension size) {
		this.size = size;

		for(int i=0; i<stars.length; i++) {
			int s = rn.nextInt(2)+1;
			stars[i] = new StarModel(rn,size,s+2);
		}


		img = Config.getInstance().getRessourceImage("/res/star.png");

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter((BufferedImage) img, null);
		resize_img = img.getScaledInstance(8*8, 8, Image.SCALE_REPLICATE);

		StarsAnimation p = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			for (StarModel star : p.stars) {
				star.move();
			}
		}}).start();
	}

	public void resetSize(Dimension size) {
		this.size = size;
		for(int i=0; i<stars.length; i++) {
			int s = rn.nextInt(2)+1;
			stars[i] = new StarModel(rn,size,s+2);
		}
	}

	@Override public Dimension getPreferredSize() {return size;}

	@Override public int getHeight() { return size.height; }
	@Override public int getWidth() { return size.width; }

	@Override public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(this.bgColor != null) {
			g2.setColor(this.bgColor);
			g2.fillRect(0,0,this.getWidth(),this.getHeight());
		}
		for (StarModel star : stars) {
			g2.drawImage(resize_img,star.position.x-star.offset, star.position.y, this);
		}
	}
}
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
		this.canvasWidth = blockSize*8;
		this.canvasHeight = blockSize*8;
		this.blockSize = this.canvasHeight/4;
		noBlockModel = new BlockModel(Color.DARK_GRAY);
		noBlockModel.recalculate();
	}
	public void resetSize(int blockSize) {
		this.canvasWidth = blockSize*8;
		this.canvasHeight = blockSize*8;
		this.blockSize = this.canvasHeight/4;
		recalulate(null);
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
class PauseMenu extends JPanel {
	JPanel mainPanel;
	Object ctrl;
	Object model;

	public PauseMenu(int x, int y, int width, int height,Object ctrl,Object model) {
		this.ctrl = ctrl;
		this.model = model;

		setLocation(x, y);
		setPreferredSize(new Dimension(width,height));
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,1,0,0);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLocation(0, 0);
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);

		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white),new EmptyBorder(10, 10, 10, 10)));

		GridLayout subLayout = new GridLayout(1,2);
		subLayout.setHgap(10);

		JLabel paused = new JLabel("<html><div style='text-align: center;'>PAUSED</div></html>");
		paused.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		paused.setForeground(Color.white);
		paused.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(paused);

		JPanel backResumePanel = new JPanel();
		backResumePanel.setOpaque(false);

		MenuButton resumeButton = new MenuButton("Resume",Color.GREEN,Color.WHITE, (ActionListener)ctrl);
		resumeButton.setActionCommand("CLICK:RESUME");
		resumeButton.addActionListener((ActionListener)ctrl);
		resumeButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		backResumePanel.add(resumeButton);

		MenuButton backButton = new MenuButton("Menu",Color.RED,Color.WHITE, (ActionListener)ctrl);
		backButton.setActionCommand("CLICK:BACK");
		backButton.addActionListener((ActionListener)ctrl);
		backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		backResumePanel.add(backButton);
		backResumePanel.setLayout(subLayout);
		mainPanel.add(backResumePanel);

		StarsAnimation animation = new StarsAnimation(mainPanel.getPreferredSize(),Color.black,10,Color.red);

		JLayeredPane testPane = new JLayeredPane();
		testPane.add(animation,JLayeredPane.DEFAULT_LAYER);
		testPane.add(mainPanel,JLayeredPane.PALETTE_LAYER);
		testPane.setPreferredSize(getPreferredSize());

		SpringLayout lyt = new SpringLayout();
		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, testPane);

		lyt.putConstraint(SpringLayout.EAST, animation, 0, SpringLayout.EAST, mainPanel);
		lyt.putConstraint(SpringLayout.NORTH, animation, 0, SpringLayout.NORTH, mainPanel);

		testPane.setLayout(lyt);
		add(testPane);
		setVisible(true);


		Dimension yt1 = resumeButton.getPreferredSize(); yt1.width +=35; yt1.height +=15;
		resumeButton.setPreferredSize(yt1);
		Dimension yt2 = backButton.getPreferredSize(); yt2.width +=35; yt2.height +=15;
		backButton.setPreferredSize(yt2);

		animation.size = mainPanel.getPreferredSize();

		recalculate(GameState.WAITING);
		JPanel t = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			t.repaint();
			t.revalidate();
		}}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(new Color(0f,0f,0f,.7f ));

		g2d.fillRect(0,0,getWidth(),getHeight());
		g2d.setColor(Color.BLACK);
		g2d.fillRect(mainPanel.getX(),mainPanel.getY()+5,mainPanel.getWidth(),mainPanel.getHeight());

		super.paintComponent(g2d);
	}

	public void recalculate(GameState state) {
		mainPanel.setVisible(state == GameState.PAUSED);
		setVisible(state == GameState.PAUSED);
		revalidate();
		repaint();
	}
}
class SplashScreenPanel extends JPanel {
	JPanel mainPanel;
	JLabel pressSpace;
	JLabel currentScoreLabel;
	JLabel bestScoreLabel;
	JPanel backReplayPanel;
	Object ctrl;
	Object model;
	StarsAnimation animation;

	JButton backButton;
	JButton replayButton;

	public SplashScreenPanel(Dimension dimension, Object ctrl, Object model) {
		this.ctrl = ctrl;
		this.model = model;

		setLocation(0,0);
		setPreferredSize(dimension);
		setBounds(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight());
		setOpaque(false);

		mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(0,1,0,0);
		GridLayout subLayout = new GridLayout(1,2);
		mainPanel.setLayout(mainLayout);
		mainPanel.setLocation(0, 0);
		mainPanel.setVisible(true);
		mainPanel.setOpaque(false);

		pressSpace = new JLabel("<html><div style='text-align: center;'>Press \"SPACE\" to start</div></html>");
		pressSpace.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		pressSpace.setForeground(Color.white);
		mainPanel.add(pressSpace);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white),new EmptyBorder(10, 10, 10, 10)));

		backReplayPanel = new JPanel();
		backButton = new MenuButton("Back",Color.ORANGE,Color.WHITE, (ActionListener)ctrl);
		replayButton = new MenuButton("Restart",Color.RED,Color.WHITE, (ActionListener)ctrl);
		backButton.setActionCommand("CLICK:BACK");
		replayButton.setActionCommand("CLICK:RESTART"); //HACKY
		backButton.addActionListener((ActionListener)ctrl);
		replayButton.addActionListener((ActionListener)ctrl);


		backReplayPanel.setOpaque(false);
		backReplayPanel.setLayout(subLayout);
		backReplayPanel.add(backButton);
		backReplayPanel.add(replayButton);
		backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		replayButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		currentScoreLabel = new JLabel();
		currentScoreLabel.setForeground(Color.white);
		currentScoreLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		currentScoreLabel.setText("<html>Current Score: 0");
		bestScoreLabel = new JLabel();
		bestScoreLabel.setForeground(Color.white);
		bestScoreLabel.setFont(Config.getInstance().getFont("FONT_NORMAL"));
		bestScoreLabel.setText("<html>Best Score: 0");


		animation = new StarsAnimation(mainPanel.getPreferredSize(),Color.black,20);

		JLayeredPane testPane = new JLayeredPane();
		testPane.add(animation,JLayeredPane.DEFAULT_LAYER);
		testPane.add(mainPanel,JLayeredPane.PALETTE_LAYER);
		testPane.setPreferredSize(getPreferredSize());

		SpringLayout lyt = new SpringLayout();
		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, mainPanel, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, mainPanel, 0, SpringLayout.VERTICAL_CENTER, testPane);

		lyt.putConstraint(SpringLayout.HORIZONTAL_CENTER, animation, 0, SpringLayout.HORIZONTAL_CENTER, testPane);
		lyt.putConstraint(SpringLayout.VERTICAL_CENTER, animation, 0, SpringLayout.VERTICAL_CENTER, testPane);

		testPane.setLayout(lyt);

		Dimension yt = replayButton.getPreferredSize(); yt.width +=35; yt.height +=15;
		replayButton.setPreferredSize(yt);

		add(testPane);

		setVisible(true);

		JPanel t = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			t.repaint();
			t.revalidate();
		}}).start();
	}

	public void recalculate(boolean visible, GameState state) {
		mainPanel.setVisible(visible);
		setVisible(visible);
		if(state == GameState.FINISHED) {

			if(this.model instanceof VersusModel) {
				pressSpace.setText("<html><div style='text-align: center;'>Congratulations :</div> <div style='text-align: center;'>player "+((VersusModel)model).winner+" wins the game</div></html>");
				mainPanel.removeAll();
				mainPanel.add(pressSpace);
				mainPanel.add(new Spacer());
				mainPanel.add(backReplayPanel);
				animation.resetSize(mainPanel.getPreferredSize());
				return;
			}
			if(this.model instanceof SoloModel) {
				pressSpace.setText("<html><div style='text-align: center;'>Game Over</div></html>");
				currentScoreLabel.setText("<html>Current score: "+((SoloModel)this.model).currentScore);
				bestScoreLabel.setText("<html>Best score: "+((SoloModel)this.model).bestScore);
			}
			if(this.model instanceof CoopModel) {
				pressSpace.setText("<html><div style='text-align: center;'>Game Over</div></html>");
				currentScoreLabel.setText("<html>Current score: "+((CoopModel)this.model).currentScore);
				bestScoreLabel.setText("<html>Best score: "+((CoopModel)this.model).bestScore);
				replayButton.setActionCommand("CLICK:MENU:COOP");
				replayButton.addActionListener((ActionListener)ctrl);
			}
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
		Log.debug(this,mainPanel.getPreferredSize());
		animation.resetSize(mainPanel.getPreferredSize());
		/*revalidate();
		repaint();*/
	}
}