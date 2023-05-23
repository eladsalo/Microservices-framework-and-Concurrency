package bgu.spl.mics.application;



import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.services.*;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.Reader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		Gson gson = new Gson();
		try {

			Reader reader = Files.newBufferedReader(Paths.get(args[0]));
			//Reader reader = Files.newBufferedReader(Paths.get("input.json"));

			Input input = gson.fromJson(reader, Input.class);

			Ewoks.initialize(input.getEwoks());
			long startTime=System.currentTimeMillis();
			Thread leia = new Thread(new LeiaMicroservice(input.getAttacks()));
			Thread hanSolo = new Thread(new HanSoloMicroservice());
			Thread c3PO = new Thread(new C3POMicroservice());
			Thread r2D2 = new Thread(new R2D2Microservice(input.getR2D2()));
			Thread lando = new Thread(new LandoMicroservice(input.getLando()));

			leia.start();
			hanSolo.start();
			c3PO.start();
			r2D2.start();
			lando.start();


			leia.join();
			hanSolo.join();
			c3PO.join();
			r2D2.join();
			lando.join(); //the program will not continue until all the threads are finished and Lando destroyed the death star

			// we will take the starting time out from the diary
			Diary.getInstance().setHanSoloFinish(Diary.getInstance().getHanSoloFinish()-startTime);
			Diary.getInstance().setC3POFinish(Diary.getInstance().getC3POFinish()-startTime);
			Diary.getInstance().setR2D2DeactivateFinish(Diary.getInstance().getR2D2Deactivate()-startTime);
			Diary.getInstance().setLeiaTerminate(Diary.getInstance().getLeiaTerminate()-startTime);
			Diary.getInstance().setHanSoloTerminate(Diary.getInstance().getHanSoloTerminate()-startTime);
			Diary.getInstance().setC3POTerminate(Diary.getInstance().getC3POTerminate()-startTime);
			Diary.getInstance().setR2D2Terminate(Diary.getInstance().getR2D2Terminate()-startTime);
			Diary.getInstance().setLandoTerminate(Diary.getInstance().getLandoTerminate()-startTime);

			Gson outputGson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = new FileWriter(args[1]);

			//Writer writer = new FileWriter("output.json");
			outputGson.toJson(Diary.getInstance(), writer);
			writer.close();
		}
		catch (InterruptedException e) {
			}
		catch (IOException e) {
			}
	}
}