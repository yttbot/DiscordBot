package Messages;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public abstract class Aids {
	public static void list(MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append("```");
		boolean anyoneOnline = false;

		for (Role r : event.getGuild().getRoles()) {
			boolean anyoneInRoleOnline = false;
			StringBuilder sbRole = new StringBuilder();
			sbRole.append(r.getName() + ": ");

			for (int i = 0; i < event.getGuild().getUsersWithRole(r).size(); i++) {
				if (event.getGuild().getUsersWithRole(r).get(i).getOnlineStatus() == OnlineStatus.ONLINE || event.getGuild().getUsersWithRole(r).get(i).getOnlineStatus() == OnlineStatus.AWAY) {
					if (anyoneOnline == false) anyoneOnline = true;
					if (anyoneInRoleOnline == false) anyoneInRoleOnline = true;
					sbRole.append(event.getGuild().getUsersWithRole(r).get(i).getUsername() + ", ");
				}
			}
			if (anyoneInRoleOnline) {
				sbRole.deleteCharAt(sbRole.length() - 2);
				sb.append("\n" + sbRole.toString());
			}				
		}
		if (!anyoneOnline) {
			sb.append("none online!");
		}
		sb.append("```");

		event.getTextChannel().sendMessage(sb.toString());
	}
}
