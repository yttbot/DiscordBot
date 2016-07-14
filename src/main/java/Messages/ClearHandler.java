package Messages;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
				deleteMessagesAsync(ml);
				event.getTextChannel().sendMessage("Successfully deleted the last 10 messages");
			}
			else {
				String amt = msg.replace("/clear ", "");
				try {
					int mamt = Integer.parseInt(amt);
					List<Message> ml = mh.retrieve(mamt);
					deleteMessagesAsync(ml);
					event.getTextChannel().sendMessage("Successfully deleted the last " + mamt + " messages");	
				}
				catch (NumberFormatException ex) {
					event.getTextChannel().sendMessage("Invalid number! Retry command pls");
				}
			}
		}
	}
	public static void deleteMessagesAsync(List<Message> ml) {
		int timesToRun = (int) Math.ceil(ml.size() / 5);
		AtomicInteger i = new AtomicInteger(1); 
		Runnable rn = new Runnable() {
			public void run() {
			while (i.get() < timesToRun)
				if (i.get() == (timesToRun - 0)) {
					for (Message m : ml) {
						m.deleteMessage();
					}
				}
				else {
					for (int i = 0; i <= 5; i++) {
						ml.get(0).deleteMessage();
					}
				}
				i.incrementAndGet();
			}
		};

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(rn, 0, 1, TimeUnit.SECONDS);

		final ScheduledFuture<?> rnHandle = executor.scheduleAtFixedRate(rn, 0, timesToRun, TimeUnit.SECONDS);
		executor.schedule(
				new Runnable() {
					
					public void run() { rnHandle.cancel(true); executor.shutdown(); }
				}, timesToRun, TimeUnit.MINUTES);

	}

}
