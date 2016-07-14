package Music;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Main.YttBot;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.Playlist;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.AudioTimestamp;

public abstract class Voice {
	public static boolean isSetup = false;
	static MusicPlayer player;
	static AudioManager manager;
	static List<AudioSource> queue;
	static Thread thread;
	public static final float DEFAULT_VOLUME = 0.35f;
	static TextChannel ch;
	public static void reset() {
		player.stop();
		player = new MusicPlayer();
		player.setVolume(DEFAULT_VOLUME);
		manager.setSendingHandler(player);
		isSetup = true;
	}
	public static void setup(Guild guild) {
		if(guild.getAudioManager().isConnected()) guild.getAudioManager().moveAudioConnection(YttBot.jda.getVoiceChannelByName("Music").get(0));
		else {
			guild.getAudioManager().openAudioConnection(YttBot.jda.getVoiceChannelByName("Music").get(0));
			for (TextChannel channel : guild.getTextChannels()) {
				if (channel.getName().contains("music")) {
					ch = channel;
					break;
				}
			}
			ch.sendMessage("Setup!");
			manager = guild.getAudioManager();
			player = new MusicPlayer();
			player.setVolume(DEFAULT_VOLUME);
			manager.setSendingHandler(player);
			queue = player.getAudioQueue();
		}
		isSetup = true;
	}

	public static void addToQueue(MessageReceivedEvent event, String url) {
		try {
			Playlist playlist = Playlist.getPlaylist(url);
			List<AudioSource> sources = new LinkedList<AudioSource>(playlist.getSources());
			if (sources.isEmpty()) {
				event.getChannel().sendMessage("Invalid URL");
			}
			else {
				final MusicPlayer fPlayer = player;
				queue = fPlayer.getAudioQueue();
				if (sources.size() == 1) {
					AudioSource source = sources.get(0);
					AudioInfo info = source.getInfo();
					if (info.getError() == null) {
						event.getChannel().sendMessage("Added " + source.getInfo().getTitle() + " to queue: it is #" + (queue.size() + 1) + " in line");
						queue.add(source);
						if (fPlayer.isStopped())
							fPlayer.play();
					}
					else {
						event.getChannel().sendMessage("Error adding song " + source.getInfo().getTitle() + " to queue! Recheck the URL");
					}
				}

				else {
					for (Iterator<AudioSource> it = sources.iterator(); it.hasNext();) {
						AudioSource source = it.next();
						AudioInfo info = source.getInfo();
						if (info.getError() == null) {
							queue.add(source);
							if (fPlayer.isStopped())
								fPlayer.play();
						}
						else {
							event.getChannel().sendMessage("Error adding song " + source.getInfo().getTitle() + " to queue! Recheck the URL");
						}
					}
					event.getChannel().sendMessage("Added **" + sources.size() + "** songs to queue");
				}
			}
		}	
		catch (Exception ex) {			
			event.getChannel().sendMessage("Oh no! Wrong URL or some other error happened :(");
		}
	}

	public static void nowPlaying(TextChannel ch) {
		if (player.isPlaying()) {
			AudioTimestamp currentTime = player.getCurrentTimestamp();
			AudioInfo info = player.getCurrentAudioSource().getInfo();
			if (info.getError() == null) {
				ch.sendMessage(
						"Playing: [" + info.getTitle() + "]\n" +
								"Time: [" + currentTime.getTimestamp() + " / " + info.getDuration().getTimestamp() + "]");
			}
			else {
				ch.sendMessage(
						"Error with current source: " + player.getCurrentAudioSource().getSource() + "\n" +
								"Time: [" + currentTime.getTimestamp() + " / (N/A)]");
			}
		}
		else {
			ch.sendMessage("I'm not currently playing anything!");
		}
	}

	public static void queue() {
		StringBuilder sb = new StringBuilder();
		sb.append("**Current Queue**```");
		for (int i = 0; i < 11; i++) {
			AudioInfo info = Voice.queue.get(i).getInfo();
			sb.append("\n#" + (i + 1) + ": " + info.getTitle() + " [" + info.getDuration().getTimestamp() + "]");
		}
		if (Voice.queue.size() > 10) {
			sb.append("\n**These are the next 10 songs, the queue might be larger**");
		}
		sb.append("```");
		ch.sendMessage(sb.toString());
	}

	public static void setVolume(float volume) {
		player.setVolume(volume);
		ch.sendMessage("Set Volume to " + volume);
	}

	public static void skip() {
		ch.sendMessage("Skipped " + player.getCurrentAudioSource().getInfo().getTitle());
		player.skipToNext();
	}

}
