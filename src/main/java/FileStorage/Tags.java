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
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.entities.User;

public class Tags {
	public static File tags = new File("../Data/tags.adam");
	public static String format = ": ";
	public static void setup() throws IOException {
		if (!tags.exists()) {
			tags.createNewFile();
		}		
	}
	public static boolean addTag(String tag, String value) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(tags.toURI()), Charset.defaultCharset());
		for (String str : lines) {
			String stremp = str.split(format)[0];
			if (stremp.equalsIgnoreCase(tag)) return false;
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(tags, true));
		bw.write(tag + ": " + value + "\n");
		bw.flush();
		bw.close();
		return true;	
	}

	public static void pmTagSyntax(User user) {
		user.getPrivateChannel().sendMessage("```Tag Help:\n Use /tag (tag) to display the tag's"
				+ " message\n Use /tag add (tag) (msg) to add a tag if you have permissions!\n Use /tag remove (tag) to remove a tag if you're"
				+ " an admin```");
	}
	public static boolean removeTag(String tagName) throws IOException {
		boolean found = false;
		File inputFile = tags;
		File tempFile = new File("../Data/tagsTemp.adam");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			// trim newline when comparing with lineToRemove
			String trimmedLine = currentLine.trim();
			String[] lry = trimmedLine.split(":");
			if (!tagName.equalsIgnoreCase(lry[0]) && !trimmedLine.isEmpty()) {
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
	public static String getTagContent(String tagName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(tags.toURI()), Charset.defaultCharset());
		for (String str : lines) {
			String stremp = str.split(format)[0];
			if (stremp.equalsIgnoreCase(tagName)) {
				String val = str.replace(tagName + format, "");
				return val;
			}
		}
		return null;
	}
	public static List<String> getTags() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(tags.toURI()), Charset.defaultCharset());
		List<String> tags = new ArrayList<String>();
		for (String s : lines) {
			String[] splitter = s.split(":");
			tags.add(splitter[0]);
		}
		return tags;

	}



}
