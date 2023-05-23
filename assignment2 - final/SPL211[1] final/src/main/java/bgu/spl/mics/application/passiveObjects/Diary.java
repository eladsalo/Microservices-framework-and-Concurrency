package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {


    private static class DiaryHolder{
        private static final Diary instance = new Diary();
    }

    private AtomicInteger totalAttacks;
    private long HanSoloFinish = 0;
    private long C3POFinish= 0;
    private long R2D2Deactivate = 0;
    private long LeiaTerminate = 0;
    private long HanSoloTerminate = 0;
    private long C3POTerminate = 0;
    private long R2D2Terminate = 0;
    private long LandoTerminate = 0;


    private Diary() {
        totalAttacks = new AtomicInteger(0);
    }

    public static Diary getInstance(){

        return DiaryHolder.instance;
    }

    public long getC3POFinish() {

        return C3POFinish;
    }

    public long getHanSoloFinish() {

        return HanSoloFinish;
    }

    public long getR2D2Deactivate() {

        return R2D2Deactivate;
    }

    public void setC3POFinish(long c3POFinish) {

        C3POFinish = c3POFinish;
    }

    public void setHanSoloFinish(long hanSoloFinish) {

        HanSoloFinish = hanSoloFinish;
    }

    public void setR2D2DeactivateFinish(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getC3POTerminate() {

        return C3POTerminate;
    }

    public long getHanSoloTerminate() {

        return HanSoloTerminate;
    }

    public long getR2D2Terminate() {

        return R2D2Terminate;
    }

    public long getLandoTerminate() {

        return LandoTerminate;
    }

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }


    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }

    public void addAnotherAttack() {
        int numberOfAttacks=totalAttacks.get();
        while (totalAttacks.compareAndSet(numberOfAttacks, numberOfAttacks + 1)==false)
        {
            numberOfAttacks=totalAttacks.get();
        }
    }


    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }

    public void resetNumberAttacks() {    // for the tester
        totalAttacks.set(0);
    }
}