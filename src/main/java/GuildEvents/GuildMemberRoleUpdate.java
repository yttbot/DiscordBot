package GuildEvents;

import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.hooks.SubscribeEvent;

public class GuildMemberRoleUpdate {
	@SubscribeEvent
	public void onAdd(GuildMemberRoleAddEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("changelog")) {
				StringBuilder sb = new StringBuilder();
				sb.append(event.getUser().getUsername() + " was given the following role: ");
				for (Role role : event.getRoles()) {
					sb.append(role.getName());
				}
				ch.sendMessage(sb.toString());
				
				
				break;
			}
		}
	}
	
	@SubscribeEvent
	public void onRemove(GuildMemberRoleRemoveEvent event) {
		for (TextChannel ch : event.getGuild().getTextChannels()) {
			if (ch.getName().equalsIgnoreCase("changelog")) {
				StringBuilder sb = new StringBuilder();
				sb.append(event.getUser().getUsername() + " had the following role removed: ");
				for (Role role : event.getRoles()) {
					sb.append(role.getName());
				}
				ch.sendMessage(sb.toString());			
				break;
			}
		}
	}
}
