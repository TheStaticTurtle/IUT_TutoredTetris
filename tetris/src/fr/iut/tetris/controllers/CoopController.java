package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.MainController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.models.CoopModel;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.CoopVue;
import fr.iut.tetris.vues.SoloVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CoopController implements ActionListener, KeyListener {
	MainController mainCtrl;
	CoopModel model;
	public CoopVue vue;
	AudioController audio;

	public CoopController(MainController mainCtrl, CoopModel model,AudioController audio) {
		this.model = model;
		model.setCtrl(this);
		this.mainCtrl = mainCtrl;
		this.audio = audio;

		CoopController me = this;
		new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
			me.timerTicked();
		}}).start();
	}

	public void setVue(CoopVue vue) {
		this.vue = vue;
	}

	/**
	 * Fonction executed by the model when the game ends and return the best scrore (and save it to the config)
	 * @return the best score
	 */
	public int gameEnded() {
		int bestScore = Config.getInstance().getInt("SCORE_COOP_BEST");
		if(model.currentScore > bestScore) {
			bestScore = model.currentScore;
			Config.getInstance().putInt("SCORE_COOP_BEST",model.currentScore);
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
				model.fallCurrentForPlayerA();
				model.fallCurrentForPlayerB();
				if(model.fallingPiecePlayerA == null) {
					model.spawnPlayerAPiece();
				}
				if(model.fallingPiecePlayerB == null) {
					model.spawnPlayerBPiece();
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
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_LEFT"))     { model.moveCurrentX(0,Direction.LEFT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_RIGHT"))    { model.moveCurrentX(0,Direction.RIGHT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_DOWN"))     { model.fallCurrentForPlayerA(); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_FASTDOWN")) { model.fallCurrentAtBottomForPlayerA(); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_ROTATE"))   { model.rotateCurrent(0,Direction.RIGHT); vue.recalculate();}

		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_LEFT"))     { model.moveCurrentX(1,Direction.LEFT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_RIGHT"))    { model.moveCurrentX(1,Direction.RIGHT); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_DOWN"))     { model.fallCurrentForPlayerB(); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_FASTDOWN")) { model.fallCurrentAtBottomForPlayerB(); vue.recalculate();}
		if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_ROTATE"))   { model.rotateCurrent(1,Direction.RIGHT); vue.recalculate();}

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
			case "CLICK:MENU:SOLO": //HACKY
			case "CLICK:SOLO:BACK":
				this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
				mainCtrl.actionPerformed(e);
				break;
			default:
				break;
		}
	}
}
