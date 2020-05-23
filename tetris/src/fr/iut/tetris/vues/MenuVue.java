package fr.iut.tetris.vues;

import fr.iut.tetris.Main;
import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.models.MenuModel;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class MenuVue extends JPanel {
	MenuModel model;
	MenuController ctrl;

	public MenuVue(MenuModel model, MenuController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		setPreferredSize(new Dimension( 640, 870 ));

		JLabel myLabel = new JLabel("ABCD");


		try {
			InputStream is = Main.class.getResourceAsStream("/res/retro.ttf");
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			Font sizedFont = font.deriveFont(24f);
			myLabel.setFont(sizedFont);
		} catch (Exception e) {
			e.printStackTrace();
		}

		add(myLabel);
	}
}
