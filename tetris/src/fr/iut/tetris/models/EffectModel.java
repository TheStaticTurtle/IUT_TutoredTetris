package fr.iut.tetris.models;

class BonusSpeed extends EffectModel {
    public BonusSpeed(String imagePath) {
        super(imagePath);
    }

    @Override
    public int speedFunction(int currentSpeed) {
        return currentSpeed + 500;
    }
}

class MalusSpeed extends EffectModel {
    public MalusSpeed(String imagePath) {
        super(imagePath);
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


    static BonusSpeed  bonusSpeed  = new BonusSpeed("/res/effects/bonus_speed.png");
    static EffectModel bonusDelete = new EffectModel("/res/effects/bonus_delete_line.png");
    static EffectModel malusRotate = new EffectModel("/res/effects/malus_rotate.png");
    static MalusSpeed  malusSpeed  = new MalusSpeed ("/res/effects/malus_speed.png");

    static EffectModel[] effects = new EffectModel[]{bonusSpeed,bonusDelete,malusRotate,malusSpeed};
}
