package Music;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

public class MusicHandler {
	public static void musicPass(MessageReceivedEvent event, String msg) {
		if (msg.equalsIgnoreCase("/music")) {
			event.getAuthor().getPrivateChannel().sendMessage("```Music Commands MUST BE SAID IN MUSIC CHANNEL:\n/music play default-playlist (plays default playlist, sick beats :D)\n"
					+ "/music add (url [only soundcloud/youtube allowed!]\n/music skip\n/music queue (shows upcoming songs)"
					+ "\n/music volume (shows current volume)\n/music volume [between 0-1] [changes volume]\n/music nowplaying (current song)\n/music admin reset (resets player [don't use!])```");
		}
		else {
			if (!event.getTextChannel().getName().contains("music")) {
				event.getChannel().sendMessage("Invalid channel!");
				return;
			}	
			if (msg.startsWith("/music admin")) {
				if (msg.equalsIgnoreCase("/music admin") || msg.equalsIgnoreCase("/music admin ")) {
					event.getChannel().sendMessage("Invalid use of the command! Use /music for help");
					return;
				}
				String arg2 = msg.replace("/music admin ", "");
				if (arg2.equalsIgnoreCase("reset")) {
					event.getChannel().sendMessage("Reset music player!");
					Voice.reset();
				}
				else {
					event.getChannel().sendMessage("Invalid use of the command! Use /music for help");
				}

			}
			else if (msg.equalsIgnoreCase("/music nowplaying")) {
				Voice.nowPlaying(event.getTextChannel());
			}
			else if (msg.startsWith("/music add")) {
				if (msg.equalsIgnoreCase("/music add") || msg.equalsIgnoreCase("/music add ")) {
					event.getChannel().sendMessage("Invalid syntax, add a song!");
				}
				else {
					String url = msg.replace("/music add ", "");
					Voice.addToQueue(event, url);
				}
			}
			else if (msg.startsWith("/music volume")) {
				if (msg.equalsIgnoreCase("/music volume") || msg.equalsIgnoreCase("/music volume ")) {
					if (Voice.player.isPlaying())
						event.getChannel().sendMessage("Current Volume: " + Voice.player.getVolume());
					else event.getChannel().sendMessage("The music player isn't playing anything right now!");
				}
				else {
					try {
						float volume = Float.parseFloat(msg.replace("/music volume ", ""));
						if (volume < 0 || volume > 1) {
							event.getChannel().sendMessage("Input must be between 0-1");
						}
						else Voice.setVolume(volume);
					}
					catch (NumberFormatException ex) {
						event.getChannel().sendMessage("Invalid numeric input :(");
					}

				}
			}
			else if (msg.equalsIgnoreCase("/music queue")) {
				if (Voice.player.isPlaying())
					Voice.queue();
				else event.getChannel().sendMessage("The music player isn't playing anything right now!");
			}
			else if (msg.equalsIgnoreCase("/music skip")) {
				if (!PermissionUtil.checkPermission(event.getAuthor(), Permission.ADMINISTRATOR, event.getGuild())) {
					event.getAuthor().getPrivateChannel().sendMessage("You aren't an administator so you can't use that command!");
					event.getMessage().deleteMessage();
					return;
				}
				else {
					Voice.skip();
				}
			}
			else if (msg.equalsIgnoreCase("/music play default-playlist")) {
				Voice.addToQueue(event, "https://www.youtube.com/playlist?list=PLnHjkOeSctAhoclvD2zCfj2SrHWJzwndZ");
			}
			else {
				event.getChannel().sendMessage("```Invalid command! Use /music for a list of all commands```");
			}
		}
	}
}
