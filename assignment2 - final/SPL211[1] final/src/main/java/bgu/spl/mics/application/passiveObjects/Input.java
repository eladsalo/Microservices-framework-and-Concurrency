package bgu.spl.mics.application.passiveObjects;

public class Input {
    private Attack[] attacks;
    private long R2D2;
    private long Lando;
    private int Ewoks;


    public Attack[] getAttacks() {
        return attacks;
    }

    public void setAttacks(Attack[] attacks) {
        this.attacks = attacks;
    }

    public int getEwoks() {
        return Ewoks;
    }

    public void setEwoks(int ewoks) {
        Ewoks = ewoks;
    }

    public long getLando() {
        return Lando;
    }

    public void setLando(long lando) {
        Lando = lando;
    }

    public long getR2D2() {
        return R2D2;
    }

    public void setR2D2(long r2D2) {
        R2D2 = r2D2;
    }


}