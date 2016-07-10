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
			if (ch.getName().equalsIgnoreCase("announcements")) {
				StringBuilder sb = new StringBuilder();
				sb.append(event.getUser().getUsername() + "**Ban** " + event.getUser().getUsername());
				ch.sendMessage(sb.toString());
				break;
			}
		}	
	}
	@SubscribeEvent
	public void onLeave(GuildMemberLeaveEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("announcements")) {
				StringBuilder sb = new StringBuilder();
				sb.append(event.getUser().getUsername() + "**Leave** " + event.getUser().getUsername());
				ch.sendMessage(sb.toString());
				break;
			}
		}	
	}
	
	@SubscribeEvent
	public void onJoin(GuildMemberJoinEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("announcements")) {
				StringBuilder sb = new StringBuilder();
				sb.append(event.getUser().getUsername() + "**Join** " + event.getUser().getUsername());
				ch.sendMessage(sb.toString());
				break;
			}
		}	
	}
}
