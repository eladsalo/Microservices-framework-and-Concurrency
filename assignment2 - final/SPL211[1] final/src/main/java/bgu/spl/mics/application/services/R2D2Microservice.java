package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.BroadcastDone;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;

import java.util.Date;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private final long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
    }
    @Override
    protected void initialize() {
        subscribeBroadcast(BroadcastDone.class, c -> {
            terminate();
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
        });

        subscribeEvent(DeactivationEvent.class, c -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ignored) {

            }
            complete(c, true);
            Diary.getInstance().setR2D2DeactivateFinish(System.currentTimeMillis());

        });


    }
}