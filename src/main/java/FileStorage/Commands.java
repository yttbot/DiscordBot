package FileStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import Main.YttBot;
import net.dv8tion.jda.entities.User;

public class Commands {
	static File commands = new File("../Data/commands.adam");
	public static void setup() throws IOException {
		if (commands.exists()) {
			commands.delete();
		}
		commands.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(commands));
		bw.write("/tag: base for tags, use /tag for more info\n");
		bw.write("/music: base for music, use this to get music help!\n");
		bw.write("/helpop: sends a message to discord staff members (abuse will result in a ban)\n");
		bw.write("/online: counts the amount of online players currently\n");
		bw.write("/members: will list total amount of members (/members list will show all of them)\n");
		bw.write("/list: will list all currently online members in their roles\n");
		bw.write("/status: if online, bot will display a green check mark\n");
		bw.write("/clear: clears a certain amount of previous messages in the textchannel (10 default) (Must be administrator)\n");
		bw.write("/todo: base command for administrators to view server todos\n");
		bw.write("/ban: Ban a user by using /ban @mention\n");
		bw.write("/kick: Kick a user by using /kick @mention\n");
		bw.write("/realname @[user] (shows a user's real name if they have a nickname)\n");
		bw.write("/help: shows list of commands\n");
		bw.flush();
		bw.close();
	}
	public static String printCommands() throws IOException {
		String pLine = "";
		List<String> lines = Files.readAllLines(Paths.get(commands.toURI()), Charset.defaultCharset());
		for (String l : lines) {
			pLine = pLine + l + "\n";
		}
		return pLine;
		
	}
	
	public static void pmHelp(User user) throws IOException {
		if (user.getId() != YttBot.jda.getSelfInfo().getId()) {
			user.getPrivateChannel().sendMessage("```Hello! I'm YttBot and I'll help you with how to use me!\n"
					+ printCommands() + "```");
			
		}
	}
}
