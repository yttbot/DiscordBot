package Messages;

import java.util.List;

import Main.YttBot;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public abstract class ClearHandler {
	public static void passClear(MessageReceivedEvent event, String msg) {
		if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.MESSAGE_MANAGE, event.getGuild())) {
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
					try {
						message.deleteMessage();		
					}
					catch (Exception x) {
						event.getChannel().sendMessage("I don't have permission to delete messages here .-.");
					}
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
