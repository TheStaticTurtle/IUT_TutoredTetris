package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.MainController;
import fr.iut.tetris.enums.Resolution;
import fr.iut.tetris.models.SettingsModel;
import fr.iut.tetris.vues.SettingsVue;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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

	/**
	 * set the vue audio level when loaded
	 */
	public void enteredVue() {
		vue.soundMusicLevel.setValue( (int)audio.musicLineVolumeControl );
		vue.soundSFXMusicLevel.setValue( (int)audio.soundEffetLineVolumeControl );
		vue.legacyCheckbox.setSelected(Config.getInstance().getBool("LEGACY_PIECES"));
		vue.versusEffectCheckBox.setSelected(Config.getInstance().getBool("VERSUS_EFFECTS"));
	}

	/**
	 * Save the config file when the button is pressed
	 */
	void saveConfig() {
		Config.getInstance().putInt("VOLUME_SFX",(int)audio.soundEffetLineVolumeControl);
		Config.getInstance().putInt("VOLUME_MUSIC",(int)audio.musicLineVolumeControl);
		Config.getInstance().putBool("LEGACY_PIECES",vue.legacyCheckbox.isSelected());
		Config.getInstance().putBool("VERSUS_EFFECTS",vue.versusEffectCheckBox.isSelected());
		Config.getInstance().saveAsync();
	}

	/**
	 * Listen for incoming event and do some action accordingly
	 * @param e the event
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "RESOLUTION_SELECT":
				JComboBox<Resolution> box = (JComboBox<Resolution>) e.getSource();
				Resolution r = (Resolution) box.getSelectedItem();
				assert r != null;
				Log.info(this,"Changing resolution to: "+box.getSelectedItem());
				Config.getInstance().putInt("WINDOW_HEIGHT",r.height);
				Config.getInstance().putInt("WINDOW_WIDTH" ,r.width);
				Config.getInstance().putInt("FONT_ULTRABIG",r.font_ultrabig);
				Config.getInstance().putInt("FONT_BIG"     ,r.font_big);
				Config.getInstance().putInt("FONT_NORMAL"  ,r.font_normal);
				Config.getInstance().putInt("FONT_TINY"    ,r.font_tiny);
				Config.getInstance().putInt("FONT_VERYTINY",r.font_verytiny);
				Config.getInstance().putInt("BORDER_SIZES" ,r.border_size);
				Config.getInstance().reloadFonts();
				Config.getInstance().saveAsync();
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
				break;
			case "MOUSE:ENTER":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
				break;
			case "CLICK:MENU:SETTINGS:KEYS":
			case "CLICK:BACK":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
				saveConfig();
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}

	/**
	 * Update our level when the sliders have moved
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		audio.musicLineVolumeControl = (float)vue.soundMusicLevel.getValue();
		audio.soundEffetLineVolumeControl = (float)vue.soundSFXMusicLevel.getValue();
	}
}
