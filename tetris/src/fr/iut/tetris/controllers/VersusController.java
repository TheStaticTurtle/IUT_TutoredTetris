package fr.iut.tetris.controllers;

import fr.iut.tetris.MainController;
import fr.iut.tetris.models.VersusModel;
import fr.iut.tetris.vues.VersusVue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class VersusController implements ActionListener, KeyListener {

    MainController mainCtrl;
    VersusModel model;
    public VersusVue vue;
    AudioController audio;

    public VersusController(MainController mainCtrl, VersusModel model,AudioController audio) {
        this.model = model;
        model.setCtrl(this);
        this.mainCtrl = mainCtrl;
        this.audio = audio;

    }

    public void setVue(VersusVue vue) {
        this.vue = vue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
