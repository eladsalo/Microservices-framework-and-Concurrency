package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BroadcastDone;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    public HanSoloMicroservice() {
        super("Han");
    }
    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, c-> {
            Ewoks.getInstance().acquireEwoks(c.getAttack().getSerials());
            try{
                Thread.sleep(c.getAttack().getDuration());
            }catch (InterruptedException e){}
            Ewoks.getInstance().releaseEwoks(c.getAttack().getSerials());
            complete(c,true);
            Diary.getInstance().addAnotherAttack();
            //Ewoks.getInstance().release(c.getAttack().getSerials());
            Diary.getInstance().setHanSoloFinish(System.currentTimeMillis());
        });

        subscribeBroadcast(BroadcastDone.class, c -> {
            Diary.getInstance().setHanSoloTerminate(System.currentTimeMillis());
            terminate();
        });

    }
}