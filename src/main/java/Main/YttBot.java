package Main;

import java.io.IOException;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import FileStorage.Commands;
import FileStorage.GameText;
import FileStorage.Mutes;
import FileStorage.Tags;
import FileStorage.Todo;
import GuildEvents.GuildMemberRoleUpdate;
import Messages.MessageEvent;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.hooks.AnnotatedEventManager;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.managers.AccountManager;

public class YttBot extends ListenerAdapter {
	public static JDA jda = null;
	public static AccountManager acc = null;
	public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		jda = new JDABuilder()
		.setBotToken("bottoken")
		.setEventManager(new AnnotatedEventManager())
		.buildBlocking();
		
		jda.addEventListener(new MessageEvent());
		jda.addEventListener(new GuildMemberRoleUpdate());	
		
		acc = jda.getAccountManager();

		GameText.setup();
		Tags.setup();
		Commands.setup();
		Todo.setup();
		Mutes.setup();
		

	}
}