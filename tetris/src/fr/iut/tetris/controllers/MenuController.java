package fr.iut.tetris.controllers;

import fr.iut.tetris.MainController;
import fr.iut.tetris.models.MenuModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {
	MainController mainCtrl;
	MenuModel model;

	public MenuController(MainController mainCtrl, MenuModel model) {
		this.model = model;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "CLICK:MENU:CREDIT":
			case "CLICK:MENU:QUIT":
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}
}
