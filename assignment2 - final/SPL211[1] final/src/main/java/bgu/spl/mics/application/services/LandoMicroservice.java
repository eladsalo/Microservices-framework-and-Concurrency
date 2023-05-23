package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.BroadcastDone;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import java.util.Date;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice extends MicroService {
    private final long duration;
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }
    @Override
    protected void initialize() {
        subscribeBroadcast(BroadcastDone.class, c -> {
            terminate();
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
        });

        subscribeEvent(BombDestroyerEvent.class, c -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ignored) { }
            complete(c, true);
            sendBroadcast(new BroadcastDone());
        });

    }
}