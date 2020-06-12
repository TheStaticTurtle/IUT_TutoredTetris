package fr.iut.tetris.models;

import fr.iut.tetris.Log;
import fr.iut.tetris.exceptions.OverlappedPieceException;
import fr.iut.tetris.exceptions.PieceOutOfBoardException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

class BonusSpeed extends EffectModel {
    public BonusSpeed() {
        super("/res/effects/bonus_speed.png");
    }

    @Override
    public int speedFunction(int currentSpeed) {
        return currentSpeed + 500;
    }
}

class MalusSpeed extends EffectModel {
    public MalusSpeed() {
        super("/res/effects/malus_speed.png");
    }

    @Override
    public int speedFunction(int currentSpeed) {
        return Math.max(currentSpeed - 500,10);
    }
}

class RandomLine extends EffectModel {
    VersusModel model;
    RandomLine tmp;
    int player;
    public RandomLine(VersusModel model, int player) {
        super("/res/effects/bonus_delete_line.png");
        this.model = model;
        this.player = player;
        this.tmp = this;

        Timer timer = new Timer(new Random().nextInt(2000)+2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doEffect();
            }
        });
        timer.setRepeats(false); // Only execute once
        timer.start(); // Go go go!
    }

    void doEffect() {
        BlockModel[][] models;
        if(player==0){
            this.model.effectListPlayerA.remove(tmp);
            try {
                models = this.model.computeMixedGrid(0);
            } catch (OverlappedPieceException | PieceOutOfBoardException e) {
                e.printStackTrace();
                models = null;
            }
        } else {
            this.model.effectListPlayerB.remove(tmp);
            try {
                models = this.model.computeMixedGrid(1);
            } catch (OverlappedPieceException | PieceOutOfBoardException e) {
                e.printStackTrace();
                models = null;
            }
        }

        if(models != null) {
            int maxY = models.length - 1;
            for (int y = models.length-1; y >= 0 ; y--) {
                boolean lineEmpty = true;
                for (BlockModel m: models[y]) {
                    if(m!=null) {
                        maxY = y;
                        lineEmpty = false;
                        break;
                    }
                }
                if(lineEmpty) break;
            }

            int y = new Random().nextInt(((model.height-1) - maxY) + 1) + maxY;


            for (BlockModel block: models[y]) {
                models[y] = null;
                if (player == 0) { model.pieceListPlayerA.remove(block); }
                else { model.pieceListPlayerB.remove(block); }
            }

            for (int fally = y-1; fally >= 0; fally--) {
                for (BlockModel block: models[fally]) {
                    if(block != null) {
                        Log.debug(this, "Falling line " + fally + " to "+(fally+1)+" (x:"+block.standAlonePos.x+")");
                        block.standAlonePos.y = Math.min( (fally+1) , model.height - 1);
                    }
                }
            }

            this.model.checkForFullLineAndRemoveIt(true,player);
            Log.debug(this,maxY);
        }
    }
}


public class EffectModel {

    public String imagePath;
    public EffectModel(String imagePath) {
        this.imagePath = imagePath;
    }

    public int speedFunction(int currentSpeed) {
        return currentSpeed;
    }
}
