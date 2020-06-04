package fr.iut.tetris.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.iut.tetris.MainController;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.SoloVue;

import javax.swing.*;

public class SoloController implements ActionListener, KeyListener {

	MainController mainCtrl;
	SoloModel model;
	SoloVue vue;

	int fallSpeed = 1000; //ms

	public SoloController(MainController mainCtrl, SoloModel model) {
		this.model = model;
		this.mainCtrl = mainCtrl;

		SoloController me = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			me.timerTicked();
		}}).start();
	}

	public void setVue(SoloVue vue) {
		this.vue = vue;
	}

	private long timerCounter;
	void timerTicked() {
		timerCounter += 10;
		if(timerCounter > fallSpeed) {
			timerCounter -= fallSpeed;

			if(model.gameState == GameState.PLAYING) {
				model.fallCurrent();
				if(model.fallingPiece == null) {
					model.spawnRandomPiece();
					System.out.println("Spawn");
				}
				vue.redraw();
				vue.repaint();
			}
		}
	}

	@Override public void keyTyped(KeyEvent e) { }
	@Override public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(model.gameState == GameState.WAITING && e.getKeyCode()==32) {
			model.gameState = GameState.PLAYING;
		}
		/*
			SPACE = 32
			LEFT = 37
			RIGHT = 39
			UP = 38
			DOWN = 40
		*/
		/*if(model.gameState == GameState.WAITING && e.getKeyChar()==' ') {
			model.gameState = GameState.PLAYING;
		}*/
		System.out.println(e);
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
