package fr.iut.tetris.controllers;

import fr.iut.tetris.MainController;
import fr.iut.tetris.models.MenuModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {
	MainController mainCtrl;
	MenuModel model;
	AudioController audio;

	public MenuController(MainController mainCtrl, MenuModel model, AudioController audio) {
		this.model = model;
		this.mainCtrl = mainCtrl;
		this.audio = audio;
	}

	/**
	 * Listen for incoming event and do some action accordingly
	 * @param e the event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "MOUSE:ENTER":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
				break;
			case "CLICK:MENU:CREDIT":
			case "CLICK:MENU:QUIT":
			case "CLICK:MENU:SOLO":
			case "CLICK:MENU:SETTINGS":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}
}
