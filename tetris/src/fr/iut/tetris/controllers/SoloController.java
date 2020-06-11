package fr.iut.tetris.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.MainController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.SoloVue;

import javax.swing.*;

public class SoloController implements ActionListener, KeyListener {

	MainController mainCtrl;
	SoloModel model;
	public SoloVue vue;
	AudioController audio;


	public SoloController(MainController mainCtrl, SoloModel model,AudioController audio) {
		this.model = model;
		model.setCtrl(this);
		this.mainCtrl = mainCtrl;
		this.audio = audio;

		SoloController me = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			me.timerTicked();
		}}).start();
	}

	public void setVue(SoloVue vue) {
		this.vue = vue;
	}

	/**
	 * Fonction executed by the model when the game ends and return the best scrore (and save it to the config)
	 * @return the best score
	 */
	public int gameEnded() {
		int bestScore = Config.getInstance().getInt("SCORE_SOLO_BEST");
		if(model.currentScore > bestScore) {
			bestScore = model.currentScore;
			Config.getInstance().putInt("SCORE_SOLO_BEST",model.currentScore);
			Config.getInstance().saveAsync();
		}
		return bestScore;
	}

	/**
	 * The main game timer ticks every 10 ms but to set adjustable times we increment the variable timerCounter by 10 and can then compare it with model.fallSpeed to seed if we should do something
	 */
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

	/**
	 * Listen for key presses and do action according to the keys in the config file
	 * @param e the event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(model.gameState == GameState.WAITING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_STARTGAME")) {
			model.gameState = GameState.PLAYING;
			vue.recalculate();
		}
		if(e.getKeyCode()==Config.getInstance().getInt("KEYCODE_GOBACK")) {
			if(model.gameState == GameState.PLAYING) {
				model.gameState = GameState.PAUSED;
				vue.recalculate();
				return;
			}
			if(model.gameState == GameState.PAUSED) {
				model.gameState = GameState.PLAYING;
				vue.recalculate();
				return;
			}
		}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_LEFT"))     { model.moveCurrentX(Direction.LEFT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_RIGHT"))    { model.moveCurrentX(Direction.RIGHT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_DOWN"))     { model.fallCurrent(); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_FASTDOWN")) { model.fallCurrentAtBottom(); vue.recalculate();}

		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_ROTATE"))  { model.rotateCurrent(Direction.RIGHT); vue.recalculate();}
		//if(model.gameState == GameState.PLAYING && e.getKeyCode()==39)  { model.rotateCurrent(Direction.RIGHT); vue.recalculate();}

		/*
			SPACE = 32
			LEFT = 37
			RIGHT = 39
			UP = 38
			DOWN = 40
			ENTER = 10
		*/
		Log.debug(this,e.toString());
	}

	/**
	 * Listen for incoming event and do some action accordingly
	 * @param e the event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "GAME:FOURLINES_COMPLETE":
				this.audio.playSFX(getClass().getResource( "/res/sounds/fourlines_completed.wav"));
				break;
			case "GAME:LINE_COMPLETE":
				this.audio.playSFX(getClass().getResource( "/res/sounds/line_completed.wav"));
				break;
			case "GAME:FAILED_ACTION":
			case "GAME:PIECE_PLACE":
				this.audio.playSFX(getClass().getResource( "/res/sounds/piece_place.wav"));
				break;
			case "GAME:PIECE_SPAWN":
				this.audio.playSFX(getClass().getResource( "/res/sounds/piece_spawn.wav"));
				break;
			case "MOUSE:ENTER":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_choose.wav"));
				break;


			case "CLICK:RESUME":
				if(model.gameState == GameState.PAUSED) {
					model.gameState = GameState.PLAYING;
					vue.recalculate();
				}
				break;
			case "CLICK:RESTART": //HACKY
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
				mainCtrl.actionPerformed(new ActionEvent(this,0,"CLICK:MENU:SOLO"));
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
