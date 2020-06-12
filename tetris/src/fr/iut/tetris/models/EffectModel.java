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


public class EffectModel {

    public String imagePath;
    public EffectModel(String imagePath) {
        this.imagePath = imagePath;
    }

    public int speedFunction(int currentSpeed) {
        return currentSpeed;
    }
}
