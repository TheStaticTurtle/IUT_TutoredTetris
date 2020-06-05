package fr.iut.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import fr.iut.tetris.controllers.AudioController;
import fr.iut.tetris.controllers.CreditController;
import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.models.CreditModel;
import fr.iut.tetris.models.MenuModel;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.CreditVue;
import fr.iut.tetris.vues.MenuVue;
import fr.iut.tetris.vues.SoloVue;

import javax.sound.sampled.LineUnavailableException;

public class MainController implements ActionListener, KeyListener {
	MainVue mainVue;

	MenuModel menuModel;
	CreditModel creditModel;
	SoloModel soloModel;
	
	MenuController menuCtrl;
	CreditController creditCtrl;
	SoloController soloCtrl;
	
	MenuVue menuVue;
	CreditVue creditVue;
	SoloVue soloVue;

	AudioController audio;

	public MainController() {
		audio = new AudioController();
		audio.setMusicTrack(getClass().getResource( "/res/sounds/music2.wav"));


		mainVue = new MainVue(this);

		menuModel = new MenuModel();
		creditModel = new CreditModel();
		//soloModel = new SoloModel();
		
		menuCtrl = new MenuController(this, menuModel, audio);
		creditCtrl = new CreditController(this, creditModel, audio);
		//soloCtrl = new SoloController(this, soloModel);
		
		menuVue = new MenuVue(menuModel, menuCtrl);
		creditVue = new CreditVue(creditModel, creditCtrl);
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
			case "CLICK:CREDIT:BACK":
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
		soloCtrl.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		soloCtrl.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(soloCtrl != null) {
			soloCtrl.keyReleased(e);
		}
	}
}
