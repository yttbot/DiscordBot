package Messages;

import java.io.IOException;

import FileStorage.Tags;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public abstract class TagHandler {
	public static void passTag(MessageReceivedEvent event) throws IOException {
		String msg = event.getMessage().getContent();
		String[] strippedContent = msg.split(" ");
		if (msg.equalsIgnoreCase("/tag")) {
			Tags.pmTagSyntax(event.getAuthor());
		}
		else if (msg.equalsIgnoreCase("/tag add") || msg.equalsIgnoreCase("/tag remove")) {
			event.getChannel().sendMessage("Incorrect syntax! Use **/tag** to learn the command usage!");
		}
		else if (msg.equalsIgnoreCase("/tag list")) {
			StringBuilder sb = new StringBuilder();
			sb.append("```**Tag List**\n");
			for (String s : Tags.getTags()) sb.append(s + "\n");
			sb.append("```");
			event.getChannel().sendMessage(sb.toString());
		}
		else if (msg.startsWith("/tag add")) {
			if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
				event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
				return;
			}
			String tagName = strippedContent[2];
			StringBuilder sb = new StringBuilder();
			for (int i = 3; i < strippedContent.length; i++) sb.append(strippedContent[i] + " ");
			String tagContent = sb.toString();

			if (tagName.equalsIgnoreCase("add") || tagName.equalsIgnoreCase("remove") || tagName.contains(":")) {
				event.getChannel().sendMessage("Blacklisted tag name!");
				return;
			}

			boolean success = Tags.addTag(tagName, tagContent);
			if (success == false) event.getChannel().sendMessage("Tag **" + tagName + "** is already in use, " + event.getAuthor().getAsMention());
			else event.getChannel().sendMessage("Tag **" + tagName + "** was added! Use /tag " + tagName + " to call it");
		}
		else if (msg.startsWith("/tag remove")) {
			if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
				event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
				return;
			}
			else {
				String tagName = strippedContent[2];
				boolean success = Tags.removeTag(tagName);			
				if (!success) {
					event.getChannel().sendMessage("Tag " + tagName + " not found in database :(");
				}
				else {
					event.getChannel().sendMessage("Removed tag **" + tagName + "**");
				}

			}
		}
		else {
			if (strippedContent.length > 2) event.getChannel().sendMessage("Invalid syntax! Use /tag for help");
			else if (strippedContent.length == 2) {
				String value = strippedContent[1];
				if (Tags.getTagContent(value) != null) event.getChannel().sendMessage(Tags.getTagContent(value));
				else event.getChannel().sendMessage("Invalid tag!");
			}
		}
	}
}
