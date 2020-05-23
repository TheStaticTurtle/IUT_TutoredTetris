package fr.iut.tetris.vues;

import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.models.MenuModel;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

public class MenuVue extends JPanel {
	MenuModel model;
	MenuController ctrl;

	public MenuVue(MenuModel model, MenuController ctrl) {
		this.model = model;
		this.ctrl = ctrl;
		setPreferredSize(new Dimension( 640, 870 ));
		add(new JLabel("ABCD"));
	}
}
