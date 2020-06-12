package fr.iut.tetris.models;

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
    }

    void doEffect() {
        if(player==0){
            this.model.effectListPlayerA.remove(tmp);
        }
        if(player==1){
            this.model.effectListPlayerB.remove(tmp);
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
