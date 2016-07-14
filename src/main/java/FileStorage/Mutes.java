package FileStorage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import net.dv8tion.jda.entities.User;

public class Mutes {	
	public static File mutes = new File("../Data/mutes.adam");
	public static String format = ": ";
	public static void setup() throws IOException {
		if (!mutes.exists()) {
			mutes.createNewFile();
		}		
	}
	public static boolean addmutes(String value) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(mutes, true));
		bw.write(value + "\n");
		bw.flush();
		bw.close();
		return true;	
	}

	public static void pmMutesSyntax(User user) {
		user.getPrivateChannel().sendMessage("```Mutes Help:\n Use /mutes list to display the mutes\n Check whether a player is muted with /mute check (Mention)"
				+ "\n Use /mute (Mention) to add a mute if you have permissions!\n Use /mute remove (Mention) to remove a mute if you're"
				+ " an admin```");
	}
	
	public static boolean removeMute(String muted) throws IOException {
		boolean found = false;
		File inputFile = mutes;
		File tempFile = new File("../Data/mutesTemp.adam");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			String trimmedLine = currentLine.trim();
			if (!muted.equalsIgnoreCase(trimmedLine)) {
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			else found = true;
		}
		writer.flush();
		writer.close(); 
		reader.close(); 

		if (!found) return false;
		
		Path from = tempFile.toPath(); //convert from File to Path
		Path to = Paths.get(inputFile.toURI()); //convert from String to Path
		Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);

		return true;
	}
	public static String getMutes(int mutesNumber) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(mutes.toURI()), Charset.defaultCharset());
		return lines.get(mutesNumber);
	}
	public static boolean isMuted(String muted) throws IOException {
		boolean isPersonMuted = false;
		List<String> lines = Files.readAllLines(Paths.get(mutes.toURI()), Charset.defaultCharset());
		for (String s : lines) {
			if (s.equalsIgnoreCase(muted)) {
				isPersonMuted = true;
				break;
			}
		}
		return isPersonMuted;
	}
	public static List<String> getMutes() throws IOException {
		return Files.readAllLines(Paths.get(mutes.toURI()), Charset.defaultCharset());
	}



}
