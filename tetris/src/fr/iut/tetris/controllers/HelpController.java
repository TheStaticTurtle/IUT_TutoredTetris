package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.MainController;
import fr.iut.tetris.models.HelpModel;
import fr.iut.tetris.vues.HelpVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpController implements ActionListener/*, ChangeListener*/ {

    public MainController mainCtrl;
    HelpModel model;
    HelpVue vue;
    AudioController audio;


    public HelpController(MainController mainCtrl, HelpModel model, HelpVue vue, AudioController audio) {
        this.model = model;
        this.mainCtrl = mainCtrl;
        this.vue = vue;
        this.audio = audio;
    }

    public void setVue(HelpVue vue) {
        this.vue = vue;
    }

	/*@Override
	public void stateChanged(ChangeEvent e) {

	}*/

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "MOUSE:ENTER":
                this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
                break;
            case "CLICK:BACK":
                this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
                mainCtrl.actionPerformed(e);
                break;
            default:
                break;
        }
    }

}