package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;
import fr.iut.tetris.MainController;
import fr.iut.tetris.enums.Direction;
import fr.iut.tetris.enums.GameState;
import fr.iut.tetris.models.EffectModel;
import fr.iut.tetris.models.VersusModel;
import fr.iut.tetris.vues.VersusVue;

import javax.swing.*;
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
        this.mainCtrl = mainCtrl;
        this.audio = audio;
        
        model.setCtrl(this);

        VersusController me = this;
        new Timer(10, new ActionListener() { public void actionPerformed(ActionEvent e) {
            me.timerTicked();
        }}).start();

    }

    public void setVue(VersusVue vue) {
        this.vue = vue;
    }

    /*
    public int gameEnded() {
        int bestScore = Config.getInstance().getInt("SCORE_COOP_BEST");
        if(model.currentScore > bestScore) {
            bestScore = model.currentScore;
            Config.getInstance().putInt("SCORE_COOP_BEST",model.currentScore);
            Config.getInstance().saveAsync();
        }
        return bestScore;
    }*/

    /**
     * The main game timer ticks every 10 ms but to set adjustable times we increment the variable timerCounter by 10 and can then compare it with model.fallSpeed to seed if we should do something
     */
    private long timerCounterA;
    private long timerCounterB;
    void timerTicked() {
        timerCounterA += 10;
        timerCounterB += 10;

        int fallSpeedPlayerA = model.fallSpeedPlayerA;
        for (EffectModel m: model.effectListPlayerA) {
            fallSpeedPlayerA = m.speedFunction(fallSpeedPlayerA);
        }

        int fallSpeedPlayerB = model.fallSpeedPlayerA;
        for (EffectModel m: model.effectListPlayerB) {
            fallSpeedPlayerB = m.speedFunction(fallSpeedPlayerB);
        }

        if(timerCounterA > fallSpeedPlayerA) {
            timerCounterA -= fallSpeedPlayerA;

            if(model.gameState == GameState.PLAYING) {
                model.fallCurrentForPlayerA();
                if(model.fallingPiecePlayerA == null) {
                    model.spawnPlayerAPiece();
                }
                vue.recalculate();
            }
        }
        if(timerCounterB > fallSpeedPlayerB) {
            timerCounterB -= fallSpeedPlayerB;
            if(model.gameState == GameState.PLAYING) {
                model.fallCurrentForPlayerB();
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

        for (EffectModel m: model.effectListPlayerA) {
            e = m.keyDown(e);
        }
        for (EffectModel m: model.effectListPlayerB) {
            e = m.keyDown(e);
        }

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

        //Player 0 Controls
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_LEFT")) {
            model.moveCurrentX(0, Direction.LEFT);
            vue.recalculate();
        }
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_RIGHT")) {
            model.moveCurrentX(0, Direction.RIGHT);
            vue.recalculate();
        }
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_DOWN"))     { model.fallCurrentForPlayerA(); vue.recalculate();}
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_FASTDOWN")) { model.fallCurrentAtBottomForPlayerA(); vue.recalculate();}
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P2_ROTATE"))   { model.rotateCurrent(0,Direction.RIGHT); vue.recalculate();}

        //Player 1 Controls
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_LEFT")) {
            model.moveCurrentX(1, Direction.LEFT);
            vue.recalculate();
        }
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_RIGHT")) {
            model.moveCurrentX(1, Direction.RIGHT);
            vue.recalculate();
        }
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_DOWN"))     { model.fallCurrentForPlayerB(); vue.recalculate();}
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_FASTDOWN")) { model.fallCurrentAtBottomForPlayerB(); vue.recalculate();}
        if(model.gameState == GameState.PLAYING && e.getKeyCode()==Config.getInstance().getInt("KEYCODE_P1_ROTATE"))   { model.rotateCurrent(1,Direction.RIGHT); vue.recalculate();}

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
            case "GAME:GOT_BOUNS":
                this.audio.playSFX(getClass().getResource( "/res/sounds/bonus.wav"));
                break;
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
            case "GAME:PIECE_SPAWN_PLAYER_A":
            case "GAME:PIECE_SPAWN_PLAYER_B":
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
                mainCtrl.actionPerformed(new ActionEvent(this,0,"CLICK:MENU:VERSUS"));
                break;
            case "CLICK:BACK":
                this.audio.setMusicTrack(getClass().getResource("/res/sounds/music_calm.wav"));
                this.audio.playSFX(getClass().getResource( "/res/sounds/menu_select.wav"));
                mainCtrl.actionPerformed(e);
                break;
            default:
                break;
        }
    }
}
