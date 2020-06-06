package fr.iut.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import fr.iut.tetris.controllers.*;
import fr.iut.tetris.models.CreditModel;
import fr.iut.tetris.models.MenuModel;
import fr.iut.tetris.models.SettingsModel;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.CreditVue;
import fr.iut.tetris.vues.MenuVue;
import fr.iut.tetris.vues.SettingsVue;
import fr.iut.tetris.vues.SoloVue;

import javax.sound.sampled.LineUnavailableException;

public class MainController implements ActionListener, KeyListener {
	MainVue mainVue;

	MenuModel menuModel;
	CreditModel creditModel;
	SoloModel soloModel;
	SettingsModel settingsModel;
	
	MenuController menuCtrl;
	CreditController creditCtrl;
	SoloController soloCtrl;
	SettingsController settingsCtrl;
	
	MenuVue menuVue;
	CreditVue creditVue;
	SoloVue soloVue;
	SettingsVue settingsVue;

	AudioController audio;

	public MainController() {
		audio = new AudioController();
		audio.setMusicTrack(getClass().getResource( "/res/sounds/music2.wav"));


		mainVue = new MainVue(this);

		menuModel = new MenuModel();
		creditModel = new CreditModel();
		settingsModel = new SettingsModel();
		//soloModel = new SoloModel();
		
		menuCtrl = new MenuController(this, menuModel, audio);
		creditCtrl = new CreditController(this, creditModel, audio);
		settingsCtrl = new SettingsController(this, settingsModel,settingsVue, audio);
		//soloCtrl = new SoloController(this, soloModel);
		
		menuVue = new MenuVue(menuModel, menuCtrl);
		creditVue = new CreditVue(creditModel, creditCtrl);
		settingsVue = new SettingsVue(settingsModel, settingsCtrl);
		settingsCtrl.setVue(settingsVue);
		//soloVue = new SoloVue(soloModel, soloCtrl);
		//soloCtrl.setVue(soloVue);
		
		// gameVue = new GameVue();

		mainVue.setCurrentVue(menuVue);
		mainVue.display();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
		
			case "CLICK:MENU:SOLO":
				soloModel = new SoloModel();
				soloCtrl = new SoloController(this, soloModel);
				soloVue = new SoloVue(soloModel, soloCtrl);
				soloCtrl.setVue(soloVue);

				mainVue.setCurrentVue(soloVue);
				break;

			case "CLICK:MENU:CREDIT":
				creditVue = new CreditVue(creditModel, creditCtrl);
				mainVue.setCurrentVue(creditVue);
				break;

			case "CLICK:MENU:SETTINGS":
				settingsVue = new SettingsVue(settingsModel, settingsCtrl);
				mainVue.setCurrentVue(settingsVue);
				settingsCtrl.setVue(settingsVue);
				settingsCtrl.enteredVue();
				break;

			case "CLICK:CREDIT:BACK":
			case "CLICK:SOLO:BACK":
			case "CLICK:SETTINGS:BACK":
				menuVue = new MenuVue(menuModel, menuCtrl);
				mainVue.setCurrentVue(menuVue);
				break;

			case "CLICK:MENU:QUIT":
				System.exit(0);
				break;
			default:
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(soloCtrl != null) {
			soloCtrl.keyTyped(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(soloCtrl != null) {
			soloCtrl.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(soloCtrl != null) {
			soloCtrl.keyReleased(e);
		}
	}
}
