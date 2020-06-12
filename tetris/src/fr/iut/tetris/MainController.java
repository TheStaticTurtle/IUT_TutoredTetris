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
	VersusModel versusModel;
	SettingsModel settingsModel;
	SettingsKeysModel settingsKeysModel;
	
	MenuController menuCtrl;
	CreditController creditCtrl;
	SoloController soloCtrl;
	CoopController coopController;
	VersusController versusController;
	SettingsController settingsCtrl;
	SettingsKeysController settingsKeysCtrl;
	
	MenuVue menuVue;
	CreditVue creditVue;
	SoloVue soloVue;
	CoopVue coopVue;
	VersusVue versusVue;
	SettingsVue settingsVue;
	SettingsKeysVue settingsKeysVue;

	AudioController audio;
	Config config;

	public MainController() {
		config = new Config(); //Need's to be instentiated first in order to be able to use Config.getInstance() on other methods

		audio = new AudioController();
		audio.setMusicTrack(getClass().getResource("/sounds/music_calm.wav"));

		mainVue = new MainVue(this);

		menuModel = new MenuModel();
		creditModel = new CreditModel();
		settingsModel = new SettingsModel();
		settingsKeysModel = new SettingsKeysModel();
		
		menuCtrl = new MenuController(this, menuModel, audio);
		creditCtrl = new CreditController(this, creditModel, audio);
		settingsCtrl = new SettingsController(this, settingsModel,settingsVue, audio);
		settingsKeysCtrl = new SettingsKeysController(this, settingsKeysModel,settingsKeysVue, audio);
		
		menuVue = new MenuVue(menuModel, menuCtrl);
		creditVue = new CreditVue(creditModel, creditCtrl);
		settingsVue = new SettingsVue(settingsModel, settingsCtrl);
		settingsKeysVue = new SettingsKeysVue(settingsKeysModel, settingsKeysCtrl);
		settingsCtrl.setVue(settingsVue);
		settingsKeysCtrl.setVue(settingsKeysVue);

		mainVue.setCurrentVueAndCenterWindows(menuVue);
		mainVue.display();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {

			case "RECENTERVUE":
				mainVue.recenter();
				break;

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

			case "CLICK:MENU:VERSUS":
				versusModel = new VersusModel();
				versusController = new VersusController(this, versusModel,audio);
				versusVue = new VersusVue(versusModel, versusController);
				versusController.setVue(versusVue);

				mainVue.setCurrentVue(versusVue);
				audio.setMusicTrack(getClass().getResource("/sounds/music_epic.wav"));
				break;

			case "CLICK:MENU:CREDIT":
				creditVue = new CreditVue(creditModel, creditCtrl);
				mainVue.setCurrentVue(creditVue);
				break;

			case "CLICK:MENU:SETTINGS":
				settingsKeysModel = null;
				settingsKeysCtrl = null;
				settingsKeysVue = null;
				settingsVue = new SettingsVue(settingsModel, settingsCtrl);
				mainVue.setCurrentVue(settingsVue);
				settingsCtrl.setVue(settingsVue);
				settingsCtrl.enteredVue();
				break;

			case "CLICK:MENU:SETTINGS:KEYS":
				settingsKeysModel = new SettingsKeysModel();
				settingsKeysCtrl = new SettingsKeysController(this, settingsKeysModel,settingsKeysVue,audio);
				settingsKeysVue = new SettingsKeysVue(settingsKeysModel, settingsKeysCtrl);
				mainVue.setCurrentVue(settingsKeysVue);
				settingsKeysCtrl.setVue(settingsKeysVue);
				//settingsKeysCtrl.enteredVue();
				break;

			case "CLICK:BACK":
				versusModel = null;
				versusController = null;
				versusVue = null;
				coopModel = null;
				coopController = null;
				coopVue = null;
				soloModel = null;
				soloCtrl = null;
				soloVue = null;
				settingsKeysModel = null;
				settingsKeysCtrl = null;
				settingsKeysVue = null;
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
		if(versusController != null) {
			versusController.keyTyped(e);
		}
		if(settingsKeysCtrl != null) {
			settingsKeysCtrl.keyTyped(e);
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
		if(versusController != null) {
			versusController.keyPressed(e);
		}
		if(settingsKeysCtrl != null) {
			settingsKeysCtrl.keyPressed(e);
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
		if(versusController != null) {
			versusController.keyReleased(e);
		}
		if(settingsKeysCtrl != null) {
			settingsKeysCtrl.keyReleased(e);
		}
	}
}
