package bgu.spl.mics.application.passiveObjects;


import java.util.Collections;
import java.util.List;
/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static Ewoks instance = null;


    private Ewok[] ewoksArray;

    private Ewoks(int ewoksNumber)
    {
        ewoksArray=new Ewok[ewoksNumber+1];
        for (int i=1; i<=ewoksNumber; i++){
            ewoksArray[i]=new Ewok(i);
        }
    }

    public static void initialize(int ewoksNumber) {

        instance = new Ewoks(ewoksNumber);
    }

    public static Ewoks getInstance() {
        if (instance == null) {
            throw new RuntimeException("instance of Ewoks was not initialize in the program");
        }
        return instance;
    }

    public boolean acquireEwoks(List<Integer> ewoksForAttack) {
        //we need to sort the list because without it we might stuck the program (deadlock)
        Collections.sort(ewoksForAttack);
        for (int i = 0; i < ewoksForAttack.size(); i++) {
            ewoksArray[ewoksForAttack.get(i)].acquire();
        }
       return true;
    }


    public boolean releaseEwoks(List<Integer> ewoksFinishedAttack) {
    //we need to sort the list because without it we might stuck the program (deadlock)
        Collections.sort(ewoksFinishedAttack);
        for (int i = 0; i < ewoksFinishedAttack.size(); i++) {
            ewoksArray[ewoksFinishedAttack.get(i)].release();
        }
        return true;
    }

}