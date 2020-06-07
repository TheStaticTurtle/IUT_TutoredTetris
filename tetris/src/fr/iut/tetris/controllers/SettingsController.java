package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.MainController;
import fr.iut.tetris.models.SettingsModel;
import fr.iut.tetris.vues.SettingsVue;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsController implements ActionListener, ChangeListener {
	MainController mainCtrl;
	SettingsModel model;
	SettingsVue vue;
	AudioController audio;

	public SettingsController(MainController mainCtrl, SettingsModel model, SettingsVue vue, AudioController audio) {
		this.model = model;
		this.mainCtrl = mainCtrl;
		this.vue = vue;
		this.audio = audio;
	}

	public void setVue(SettingsVue vue) {
		this.vue = vue;
	}

	public void enteredVue() {
		vue.soundMusicLevel.setValue( (int)audio.musicLineVolumeControl );
		vue.soundSFXMusicLevel.setValue( (int)audio.soundEffetLineVolumeControl );
	}

	void saveConfig() {
		Config.getInstance().putInt("VOLUME_SFX",(int)audio.soundEffetLineVolumeControl);
		Config.getInstance().putInt("VOLUME_MUSIC",(int)audio.musicLineVolumeControl);
		Config.getInstance().saveAsync();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "MOUSE:ENTER":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
				break;
			case "CLICK:SETTINGS:BACK":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
				saveConfig();
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		audio.musicLineVolumeControl = (float)vue.soundMusicLevel.getValue();
		audio.soundEffetLineVolumeControl = (float)vue.soundSFXMusicLevel.getValue();
	}
}
