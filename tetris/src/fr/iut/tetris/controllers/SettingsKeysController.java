package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.MainController;
import fr.iut.tetris.models.SettingsKeysModel;
import fr.iut.tetris.vues.SettingsKeysVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SettingsKeysController implements ActionListener, KeyListener/*, ChangeListener*/ {

	public MainController mainCtrl;
	SettingsKeysModel model;
	SettingsKeysVue vue;
	AudioController audio;

	boolean isChangingAKey = false;
	JButton keyChangeButton = null;
	String whichKeyToChange = null;

	public SettingsKeysController(MainController mainCtrl, SettingsKeysModel model, SettingsKeysVue vue, AudioController audio) {
		this.model = model;
		this.mainCtrl = mainCtrl;
		this.vue = vue;
		this.audio = audio;
	}

	public void setVue(SettingsKeysVue vue) {
		this.vue = vue;
	}

	void changeKey(JButton button, String whichOne) {
		if(!isChangingAKey) {
			isChangingAKey = true;
			whichKeyToChange = whichOne;
			keyChangeButton = button;
			button.setText("...");
			this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
		}
	}

	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyPressed(KeyEvent e) {
		Log.info(this,e);
		if(isChangingAKey) {
			isChangingAKey = false;
			keyChangeButton.setText(model.keycodes.get(e.getKeyCode()));
			Config.getInstance().putInt(whichKeyToChange,e.getKeyCode());

			keyChangeButton = null;
			whichKeyToChange = null;
			this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
		}
	}
	@Override public void keyReleased(KeyEvent e) {}

	/*@Override
	public void stateChanged(ChangeEvent e) {

	}*/

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().startsWith("CLICK:CHANGEKEY:")) {
			changeKey((JButton) e.getSource(),e.getActionCommand().split(":")[2]);
			return;
		}
		switch(e.getActionCommand()) {
			case "MOUSE:ENTER":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
				break;
			case "CLICK:BACK":
				mainCtrl.actionPerformed(new ActionEvent(e.getSource(),e.getID(),"CLICK:MENU:SETTINGS"));
				break;
			default:
				break;
		}
	}

}
