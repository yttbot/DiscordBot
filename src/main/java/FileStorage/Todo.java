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

public class Todo {	
	public static File todo = new File("Data/todo.adam");
	public static String format = ": ";
	public static void setup() throws IOException {
		if (!todo.exists()) {
			todo.createNewFile();
		}		
	}
	public static boolean addtodo(String value) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(todo, true));
		bw.write(value + "\n");
		bw.flush();
		bw.close();
		return true;	
	}

	public static void pmTodoSyntax(User user) {
		user.getPrivateChannel().sendMessage("```Todo Help:\n Use /todo list to display the todos"
				+ "\n Use /todo add (todo) to add a todo if you have permissions!\n Use /todo remove (number) to remove a todo if you're"
				+ " an admin```");
	}
	
	public static boolean removeTodo(int todoNumber) throws IOException {
		boolean found = false;
		File inputFile = todo;
		File tempFile = new File("Data/todosTemp.adam");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		List<String> lines = Files.readAllLines(Paths.get(todo.toURI()), Charset.defaultCharset());
		
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			String trimmedLine = currentLine.trim();
			if (!lines.get(todoNumber).equalsIgnoreCase(trimmedLine)) {
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
	public static String getTodo(int todoNumber) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(todo.toURI()), Charset.defaultCharset());
		return lines.get(todoNumber);
	}
	public static List<String> getTodos() throws IOException {
		return Files.readAllLines(Paths.get(todo.toURI()), Charset.defaultCharset());
	}



}
