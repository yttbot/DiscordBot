package Messages;

import java.io.IOException;
import java.util.List;

import API.DeprecatedMethods;
import FileStorage.Commands;
import Main.YttBot;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.SubscribeEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class MessageEvent {
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void message(MessageReceivedEvent event) throws IOException {
		String msg = event.getMessage().getContent();
		if (event.getChannel() instanceof PrivateChannel && !event.getAuthor().isBot()) {
			if (msg.startsWith("/help")) {
				Commands.pmHelp(event.getAuthor()); return;
			}
			else {
				event.getPrivateChannel().sendMessage("You cannot send this command in this channel!"); return;
			}
		}
		else {
			if (msg.startsWith("/online")) {
				int online = 0;
				for (User user : event.getGuild().getUsers()) {
					if (user.getOnlineStatus() == OnlineStatus.ONLINE || user.getOnlineStatus() == OnlineStatus.AWAY) online++;
				}
				event.getChannel().sendMessage("Online users: " + online);
			}	
			else if (msg.startsWith("/members")) {
				if (msg.equalsIgnoreCase("/members list")) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < event.getGuild().getUsers().size(); i++) {
						StringBuilder usb = new StringBuilder();
						usb.append(event.getGuild().getUsers().get(i).getUsername());
						if (i < event.getGuild().getUsers().size() - 1) {
							usb.append(", ");
						}
						sb.append(usb);
					}
					event.getChannel().sendMessage("```*List of members*: " + sb.toString() + "```");
				}
				else {
					int members = event.getGuild().getUsers().size();
					event.getTextChannel().sendMessage("```Total members: " + members + "```");
				}
			}
			else if (msg.startsWith("/status")) {
				event.getTextChannel().sendMessage("http://www.picgifs.com/graphics/o/ok/graphics-ok-134103.gif");
			}
			else if (msg.startsWith("/list")) {
				DeprecatedMethods.list(event);	
			}
			else if (msg.startsWith("/tag")) {
				TagHandler.passTag(event);
			}
			else if (msg.startsWith("/help")) {
				Commands.pmHelp(event.getAuthor());
			}
			else if (msg.startsWith("/clear")) {
				if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
					event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
					return;
				}
				else {
					if (!PermissionUtil.checkPermission(YttBot.jda.getUserById(YttBot.jda.getSelfInfo().getId()), Permission.MESSAGE_MANAGE, event.getGuild())) {
						event.getTextChannel().sendMessage("I don't have permission to delete messages here! :(");
						return;
					}
					MessageHistory mh = new MessageHistory(event.getChannel());
					if (msg.equalsIgnoreCase("/clear")) {
						List<Message> ml = mh.retrieve(10);
						for (Message message : ml) {
							message.deleteMessage();
						}
						event.getTextChannel().sendMessage("Successfully deleted the last 10 messages");
					}
					else {
						String amt = msg.replace("/clear ", "");
						try {
							int mamt = Integer.parseInt(amt);
							List<Message> ml = mh.retrieve(mamt);
							for (Message message : ml) {
								message.deleteMessage();
							}
							event.getTextChannel().sendMessage("Successfully deleted the last " + mamt + " messages");	
						}
						catch (NumberFormatException ex) {
							event.getTextChannel().sendMessage("Invalid number! Retry command pls");
						}
					}
				}
			}
		}
	}
}
