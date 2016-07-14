package Punishments;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class Banning {
	public static void passBan(MessageReceivedEvent event, Message m) {	
		try {
			if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
				event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
				return;
			}
			if (m.getMentionedUsers() != null && !m.mentionsEveryone()) {
				if (m.getMentionedUsers().size() > 1) event.getChannel().sendMessage("Multiple users were mentioned! Retry with only one");
				else {
					event.getGuild().getManager().ban(m.getMentionedUsers().get(0), 0);
					event.getChannel().sendMessage("Banned " + m.getMentionedUsers().get(0).getUsername());
				}
			}
			else event.getChannel().sendMessage("Invalid user :(");
		}
		catch (Exception ex) {
			event.getChannel().sendMessage("Unable to ban! Use logic to figure out where your error was.");
		}
	}

	public static void passUnban(MessageReceivedEvent event, Message m) {
		event.getChannel().sendMessage("This command doesn't work yet");
		return;
		/*
		if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
			event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
			return;
		}
		if (m.getMentionedUsers() != null && !m.mentionsEveryone()) {
			if (m.getMentionedUsers().size() > 1) event.getChannel().sendMessage("Multiple users were mentioned! Retry with only one");
			else {
				if (!event.getGuild().getManager().getBans().contains(m.getMentionedUsers().get(0))) event.getChannel().sendMessage("This person isn't banned...");
				else
					event.getGuild().getManager().unBan(m.getMentionedUsers().get(0));
				event.getChannel().sendMessage("Unbanned " + m.getMentionedUsers().get(0).getUsername());
			}
		}
		else event.getChannel().sendMessage("Invalid user :(");		*/
	}

}
