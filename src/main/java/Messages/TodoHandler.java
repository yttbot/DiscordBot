package Messages;

import java.io.IOException;

import FileStorage.Todo;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public abstract class TodoHandler {
	public static void passTodo(MessageReceivedEvent event, String msg) throws IOException {
		if (msg.equalsIgnoreCase("/todo list")) {
			StringBuilder sb = new StringBuilder();
			sb.append("```" + event.getGuild().getName() + " development TODOs:\n");
			for (int i = 0; i < Todo.getTodos().size(); i++) {
				sb.append("#" + i + ": " + Todo.getTodo(i) + "\n");
			}
			sb.append("```");
			event.getChannel().sendMessage(sb.toString());	
		}
		else {
			if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
				event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
				return;
			}
			else {
				if (msg.equalsIgnoreCase("/todo")) {
					Todo.pmTodoSyntax(event.getAuthor());
				} 
				else if (msg.startsWith("/todo add")) {
					if (msg.equalsIgnoreCase("/todo add") || msg.equalsIgnoreCase("/todo add ")) {
						event.getChannel().sendMessage("Add a todo to your message :P!");
						return;
					}
					String toAdd = msg.replace("/todo add ", "");
					Todo.addtodo(toAdd);
					event.getTextChannel().sendMessage("Added TODO: " + toAdd);
				}
				else if (msg.startsWith("/todo remove")) {
					if (msg.equalsIgnoreCase("/todo remove") || msg.equalsIgnoreCase("/todo remove ")) {
						event.getChannel().sendMessage("Add a number to your message :P!");
						return;
					}
					String toRemove = msg.replace("/todo remove ", "");
					try {
						int removal = Integer.parseInt(toRemove);
						if (removal >= Todo.getTodos().size() || removal < 0) {
							event.getChannel().sendMessage("Invalid number! Too high or too low!");
							return;
						}
						boolean removed = Todo.removeTodo(removal);
						if (!removed) event.getChannel().sendMessage("Unable to remove todo.");
						else event.getChannel().sendMessage("Successfully removed TODO #" + removal);
					}
					catch (NumberFormatException ex) {
						event.getTextChannel().sendMessage("Invalid todo number!");
					}				
				}
			}
		}
	}
}
