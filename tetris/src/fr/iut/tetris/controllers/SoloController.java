package fr.iut.tetris.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.iut.tetris.MainController;
import fr.iut.tetris.models.SoloModel;

public class SoloController implements ActionListener {

	MainController mainCtrl;
	SoloModel model;

	public SoloController(MainController mainCtrl, SoloModel model) {
		this.model = model;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "CLICK:CREDIT:BACK":
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}
	
}
