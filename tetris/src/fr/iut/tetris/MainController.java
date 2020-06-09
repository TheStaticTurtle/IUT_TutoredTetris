package fr.iut.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.iut.tetris.controllers.*;
import fr.iut.tetris.models.*;
import fr.iut.tetris.vues.*;


public class MainController implements ActionListener, KeyListener {
	MainVue mainVue;

	MenuModel menuModel;
	CreditModel creditModel;
	SoloModel soloModel;
	CoopModel coopModel;
	SettingsModel settingsModel;
	
	MenuController menuCtrl;
	CreditController creditCtrl;
	SoloController soloCtrl;
	CoopController coopController;
	SettingsController settingsCtrl;
	
	MenuVue menuVue;
	CreditVue creditVue;
	SoloVue soloVue;
	CoopVue coopVue;
	SettingsVue settingsVue;

	AudioController audio;
	Config config;

	public MainController() {
		config = new Config(); //Need's to be instentiated first in order to be able to use Config.getInstance() on other methods

		audio = new AudioController();
		audio.setMusicTrack(getClass().getResource( "/res/sounds/music2.wav"));

		mainVue = new MainVue(this);

		menuModel = new MenuModel();
		creditModel = new CreditModel();
		settingsModel = new SettingsModel();
		
		menuCtrl = new MenuController(this, menuModel, audio);
		creditCtrl = new CreditController(this, creditModel, audio);
		settingsCtrl = new SettingsController(this, settingsModel,settingsVue, audio);
		
		menuVue = new MenuVue(menuModel, menuCtrl);
		creditVue = new CreditVue(creditModel, creditCtrl);
		settingsVue = new SettingsVue(settingsModel, settingsCtrl);
		settingsCtrl.setVue(settingsVue);

		mainVue.setCurrentVueAndCenterWindows(menuVue);
		mainVue.display();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {

			case "CLICK:MENU:SOLO":
				soloModel = new SoloModel();
				soloCtrl = new SoloController(this, soloModel,audio);
				soloVue = new SoloVue(soloModel, soloCtrl);
				soloCtrl.setVue(soloVue);

				mainVue.setCurrentVue(soloVue);
				break;

			case "CLICK:MENU:COOP":
				coopModel = new CoopModel();
				coopController = new CoopController(this, coopModel,audio);
				coopVue = new CoopVue(coopModel, coopController);
				coopController.setVue(coopVue);

				mainVue.setCurrentVue(coopVue);
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
		if(coopController != null) {
			coopController.keyTyped(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(soloCtrl != null) {
			soloCtrl.keyPressed(e);
		}
		if(coopController != null) {
			coopController.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(soloCtrl != null) {
			soloCtrl.keyReleased(e);
		}
		if(coopController != null) {
			coopController.keyReleased(e);
		}
	}
}
