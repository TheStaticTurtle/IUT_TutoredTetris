package fr.iut.tetris.controllers;

import fr.iut.tetris.MainController;
import fr.iut.tetris.models.CreditModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreditController implements ActionListener {
	MainController mainCtrl;
	CreditModel model;
	AudioController audio;

	public CreditController(MainController mainCtrl, CreditModel model, AudioController audio) {
		this.model = model;
		this.mainCtrl = mainCtrl;
		this.audio = audio;
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
