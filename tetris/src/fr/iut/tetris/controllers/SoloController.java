package fr.iut.tetris.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.iut.tetris.Config;
import fr.iut.tetris.MainController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.enums.LineCompleted;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.SoloVue;

import javax.swing.*;

public class SoloController implements ActionListener, KeyListener {

	MainController mainCtrl;
	SoloModel model;
	public SoloVue vue;
	AudioController audio;
	Config config;


	public SoloController(MainController mainCtrl, Config config, SoloModel model,AudioController audio) {
		this.model = model;
		model.setCtrl(this);
		this.mainCtrl = mainCtrl;
		this.audio = audio;
		this.config = config;

		SoloController me = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			me.timerTicked();
		}}).start();
	}

	public void setVue(SoloVue vue) {
		this.vue = vue;
	}

	public void lineCompleted(LineCompleted score) {

	}

	private long timerCounter;
	void timerTicked() {
		timerCounter += 10;
		if(timerCounter > model.fallSpeed) {
			timerCounter -= model.fallSpeed;

			if(model.gameState == GameState.PLAYING) {
				model.fallCurrent();
				if(model.fallingPiece == null) {
					model.spawnPiece();
				}
				vue.recalculate();
			}
		}
	}

	@Override public void keyTyped(KeyEvent e) { }
	@Override public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(model.gameState == GameState.WAITING && e.getKeyCode()==32) {
			model.gameState = GameState.PLAYING;
			vue.recalculate();
		}
		if(model.gameState == GameState.FINISHED && e.getKeyCode()==32) { //HACKY
			this.actionPerformed(new ActionEvent(this,0,"CLICK:MENU:SOLO"));
		}
		if(model.gameState == GameState.FINISHED && e.getKeyCode()==27)  {
			this.actionPerformed(new ActionEvent(this,0,"CLICK:SOLO:BACK"));
		}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==37)  { model.moveCurrentX(Direction.LEFT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==39)  { model.moveCurrentX(Direction.RIGHT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==40)  { model.fallCurrent(); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==10)  { model.fallCurrentAtBottom(); vue.recalculate();}

		if(model.gameState == GameState.PLAYING && e.getKeyCode()==38)  { model.rotateCurrent(Direction.RIGHT); vue.recalculate();}
		//if(model.gameState == GameState.PLAYING && e.getKeyCode()==39)  { model.rotateCurrent(Direction.RIGHT); vue.recalculate();}

		/*
			SPACE = 32
			LEFT = 37
			RIGHT = 39
			UP = 38
			DOWN = 40
			ENTER = 10
		*/
		System.out.println(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "CLICK:MENU:SOLO": //HACKY
			case "CLICK:SOLO:BACK":
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}
}
