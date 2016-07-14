package GuildEvents;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.guild.member.GuildMemberBanEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.hooks.SubscribeEvent;

public class BansAndJoins {
	@SubscribeEvent
	public void onBan(GuildMemberBanEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("logs")) {
				StringBuilder sb = new StringBuilder();
				sb.append(event.getUser().getUsername() + "**Ban**: " + event.getUser().getUsername());
				ch.sendMessage(sb.toString());
				break;
			}
		}	
	}
	@SubscribeEvent
	public void onLeave(GuildMemberLeaveEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("logs")) {
				StringBuilder sb = new StringBuilder();
				sb.append("**Leave**: " + event.getUser().getUsername());
				ch.sendMessage(sb.toString());
				break;
			}
		}	
		try {
			event.getUser().getPrivateChannel().sendMessage("Sorry to see you go :(\nIf you want to join back, here's an invite:\n" + event.getGuild().getPublicChannel().getInvites().get(0));
		}
		catch (Exception ex) {}
	}

	@SubscribeEvent
	public void onJoin(GuildMemberJoinEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("logs")) {
				StringBuilder sb = new StringBuilder();
				sb.append("**Join**: " + event.getUser().getUsername());
				ch.sendMessage(sb.toString());
				break;
			}
		}	
		event.getUser().getPrivateChannel().sendMessage("Welcome to the server! Use /help for a list of commands! Enjoy your stay!");
		event.getGuild().getManager().addRoleToUser(event.getUser(), event.getGuild().getRoles().get(event.getGuild().getRoles().size() - 2));
	}
}
