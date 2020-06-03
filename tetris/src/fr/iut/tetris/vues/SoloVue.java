package fr.iut.tetris.vues;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import fr.iut.tetris.Main;
import fr.iut.tetris.component.Piece;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.models.SoloModel;

public class SoloVue extends JPanel  {

	private static final long serialVersionUID = 1L;
	
	SoloModel model;
	SoloController ctrl;

	public SoloVue(SoloModel model, SoloController ctrl) {
		this.model = model;
		this.ctrl = ctrl;

		Color bg = Color.BLACK;
		setPreferredSize(new Dimension( 640, 870 ));
		setBackground(bg);

		JPanel mainPanel = new JPanel();
		GridLayout mainLayout = new GridLayout(11,1);

		mainPanel.setPreferredSize(new Dimension(450,600));

		
		

		JButton backButton = new MenuButton("Retour", Color.ORANGE,Color.WHITE);

		backButton.addActionListener(ctrl);
		backButton.setActionCommand("CLICK:CREDIT:BACK");

		Font font = new JLabel().getFont();
		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			e.printStackTrace();
		}


		Font mormalFont = font.deriveFont(40f);
		backButton.setFont(mormalFont);

		
		
		
		
		mainPanel.setLayout(mainLayout);
		mainPanel.setOpaque(false);
		mainLayout.setVgap(5);

		mainPanel.add( new Spacer());
		mainPanel.add(backButton);

		mainPanel.setLocation(0, 0);
		mainPanel.setPreferredSize(mainPanel.getPreferredSize());

		mainPanel.setBounds(0, 0, (int) mainPanel.getPreferredSize().getWidth(), (int) mainPanel.getPreferredSize().getHeight());
		mainPanel.setVisible(true);

		
		
		
		JLayeredPane testPane = new JLayeredPane();
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


	
		
		
	
		PiecePanel panelPiece = new PiecePanel();
		panelPiece.addPiece(Piece.I);

		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			for (Piece piece : panelPiece.getListPiece()) {
				piece.moveDown();
			}
		}}).start();
		
		testPane.add( panelPiece );

	}

}


class PiecePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Piece> listPiece;
	
	public PiecePanel() {
		listPiece = new ArrayList<>();
	}
	
	public void addPiece(Piece piece){
		listPiece.add(piece);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		for(Piece piece : listPiece){
			Image img = piece.getImage();
			g2.drawImage(img, piece.position.x, piece.position.y, this);
		}	
		
	}
	
	public ArrayList<Piece> getListPiece(){
		return this.listPiece;
	}
	
}
