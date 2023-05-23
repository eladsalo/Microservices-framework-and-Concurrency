package bgu.spl.mics.application.services;

import java.util.ArrayList;         //
import java.util.List;          //
import bgu.spl.mics.Future;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.BroadcastDone;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;

// import java.util.Date;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
   // private Future<Boolean>[] futures;
    private final List<Future> listFuture;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        //this.futures = new Future[attacks.length];
        listFuture = new ArrayList<>();

    }

    @Override
    protected void initialize() {

        subscribeBroadcast(BroadcastDone.class, c -> {
            terminate();
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());

        });
        // we need to change
       // try {
            //System.out.println("Liea waits for attackers to finish initialize");
         //   Diary.getInstance().AttackersAwait();
        //} catch (InterruptedException ignored) {
        //}
        // until here

        for (Attack attack : attacks){
            Future future;
            do{
                future = sendEvent(new AttackEvent(attack));
            }while(future == null);
            listFuture.add(future);
        }
        for (Future future : listFuture){
            future.get();
        }

        // just after leia gets the results of all the attacks she sends the deactivation event to R2D2
       // Future<Boolean> R2D2Future = sendEvent(new DeactivationEvent());
        //R2D2Future.get();
        // just after R2D2 finishes the deactivation lando should start
        //Future<Boolean> LandoFuture = sendEvent(new BombDestroyerEvent());
        //LandoFuture.get();
        // after Lando finishes leia signals to everyone to terminate by sending them termination broadcast
        Future deactivationFuture = sendEvent(new DeactivationEvent());
        deactivationFuture.get();
        Future BombDestroyerFuture = sendEvent(new BombDestroyerEvent());
        BombDestroyerFuture.get();

    }
}