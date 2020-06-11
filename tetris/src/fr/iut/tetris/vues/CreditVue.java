package fr.iut.tetris.vues;

import fr.iut.tetris.Config;
import fr.iut.tetris.controllers.CreditController;
import fr.iut.tetris.models.CreditModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreditVue extends JPanel  {

	private static final long serialVersionUID = 1L;
	
	CreditModel model;
	CreditController ctrl;

	public CreditVue(CreditModel model, CreditController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.BLACK;

		int wh = Config.getInstance().getInt("WINDOW_HEIGHT");
		int ww = Config.getInstance().getInt("WINDOW_WIDTH");
		setPreferredSize(new Dimension( ww, wh ));
		setBackground(bg);

		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(11,1);

		mainPanel.setPreferredSize(new Dimension((int) (ww*0.7), (int) (wh*0.7)));

		JPanel myLabel = new TetrisLogo(this,(int) (ww*0.7));
		JButton backButton = new MenuButton("Back",Color.ORANGE,Color.WHITE,ctrl);

		backButton.addActionListener(ctrl);
		backButton.setActionCommand("CLICK:BACK");


		JLabel[] creditsLabel = new JLabel[6];

		myLabel.setFont(Config.getInstance().getFont("FONT_BIG"));
		backButton.setFont(Config.getInstance().getFont("FONT_NORMAL"));

		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainLayout.setVgap(5);

		creditsLabel[0] = new JLabel("Quentin COURDEROT");
		creditsLabel[1] = new JLabel("Jeremy EGREMY");
		creditsLabel[2] = new JLabel("Loic DEGRANGE");
		creditsLabel[3] = new JLabel("Samuel TUGLER");
		creditsLabel[4] = new JLabel("Lucas MATHIEU");
		creditsLabel[5] = new JLabel("Wassim CHALABI");

		mainPanel.add(myLabel);
		mainPanel.add( new Spacer());
		mainPanel.add( new Spacer());

		for(JLabel lbl : creditsLabel) {
			lbl.setFont(Config.getInstance().getFont("FONT_NORMAL"));
			lbl.setForeground(Color.white);
			mainPanel.add(lbl);
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
