package Messages;

import java.io.IOException;

import FileStorage.Mutes;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public abstract class MuteHandler {
	public static void passMute(MessageReceivedEvent event, String msg) throws IOException {
		if (msg.equalsIgnoreCase("/mute")) {
			Mutes.pmMutesSyntax(event.getAuthor());
		} 
		else if (msg.equalsIgnoreCase("/mute list")) {
			StringBuilder sb = new StringBuilder();
			sb.append("```Server-Wide Muted Players:\n");
			for (int i = 0; i < Mutes.getMutes().size(); i++) {
				sb.append("#" + (i + 1) + ": " + Mutes.getMutes(i) + "\n");
			}
			sb.append("```");
			event.getChannel().sendMessage(sb.toString());	
		}
		else if (msg.startsWith("/mute check")) {
			if (msg.equalsIgnoreCase("/mute check") || msg.equalsIgnoreCase("/mute check ")) event.getChannel().sendMessage("No player written to check!");
			else {
				String[] args = msg.split(" ");
				String mention = args[2];
				boolean isAUser = false;
				for (User u : event.getGuild().getUsers()) {
					if (("@" + u.getUsername()).equalsIgnoreCase(mention)) isAUser = true;
				}
				if (!isAUser) event.getChannel().sendMessage("The user you're trying to check isn't real! Try again :P");
				else {
					if (Mutes.isMuted(mention)) event.getChannel().sendMessage("This user is muted!");
					else event.getChannel().sendMessage("This user isn't muted!");
				}			
			}
		}
		else {
			if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
				event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
				return;
			}
			else {
				if (msg.startsWith("/mute add")) {
					if (msg.equalsIgnoreCase("/mute add") || msg.equalsIgnoreCase("/mute add ")) {
						event.getChannel().sendMessage("Invalid person to mute! Try again");
						return;
					}
					String mention = msg.replace("/mute add ", "");
					boolean isAUser = false;
					for (User u : event.getGuild().getUsers()) {
						if (event.getGuild().getNicknameForUser(u) != null) {
							if (("@" + event.getGuild().getNicknameForUser(u)).equalsIgnoreCase(mention)) isAUser = true;
						}
						else {
							if (("@" + u.getUsername()).equalsIgnoreCase(mention)) isAUser = true;
						}					}
					if (!isAUser || mention.equalsIgnoreCase("@YttBot")) event.getChannel().sendMessage("The user you're trying to mute isn't real! Try again :P");
					else {
						Mutes.addmutes(mention);
						Message m = event.getChannel().sendMessage("Muted " + mention);
						for (TextChannel tc : event.getGuild().getTextChannels()) {
							if (tc.getName().contains("staff")) {
								tc.pinMessageById(m.getId());
							}
						}

					}
				}
				else if (msg.startsWith("/mute remove")) {
					if (msg.equalsIgnoreCase("/mute remove") || msg.equalsIgnoreCase("/mute remove ")) {
						event.getChannel().sendMessage("Invalid person to unmute! Try again");
						return;
					}
					String mention = msg.replace("/mute remove ", "");
					boolean isAUser = false;
					for (User u : event.getGuild().getUsers()) {
						if (event.getGuild().getNicknameForUser(u) != null) {
							if (("@" + event.getGuild().getNicknameForUser(u)).equalsIgnoreCase(mention)) isAUser = true;
						}
						else {
							if (("@" + u.getUsername()).equalsIgnoreCase(mention)) isAUser = true;
						}
					}
					if (!isAUser) event.getChannel().sendMessage("The user you're trying to unmute isn't real! Try again :P");
					else {
						Mutes.removeMute(mention);
						Message m = event.getChannel().sendMessage("Unmuted " + mention);

						for (TextChannel tc : event.getGuild().getTextChannels()) {
							if (tc.getName().contains("staff")) {
								tc.pinMessageById(m.getId());
							}
						}

					}
				}
			}

		}
	}
}
