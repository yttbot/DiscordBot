package FileStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Main.YttBot;

public class GameText {
	static File games = new File("Data/games.adam");
	public static void setup() throws IOException {
		if (games.exists()) {
			games.delete();
		}
		games.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(games));
		bw.write("with Java\n");
		bw.write("sports\n");
		bw.write("in your favorite IDE\n");
		bw.write("in bed with Adam's girlfriend\n");
		bw.write("basketball\n");
		bw.write("with your emotions\n");
		bw.write("minecraft (Just kidding!)\n");
		bw.flush();
		bw.close();
		
		setupRepeatingThread();
	}
	public static void setupRepeatingThread() {
		Runnable gameChanger = new Runnable() {
			public void run() {
				try {
					List<String> lines = Files.readAllLines(Paths.get(games.toURI()), Charset.defaultCharset());
					Random random = new Random();
					int chosen = random.nextInt(lines.size());
					YttBot.acc.setGame(lines.get(chosen));
				
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(gameChanger, 0, 15, TimeUnit.SECONDS);
	}

}
