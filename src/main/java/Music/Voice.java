package Music;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Main.YttBot;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
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
	@SuppressWarnings("deprecation")
	public static void reset() {
		player.stop();
		player = new MusicPlayer();
		player.setVolume(DEFAULT_VOLUME);
		manager.setSendingHandler(player);
		if (thread.isAlive()) {
			thread.destroy();
		}
		
		playPersonalPlaylist(manager.getGuild().getTextChannels().get(manager.getGuild().getTextChannels().size() - 1));
	}

	public static void setup(Guild guild) {
		if(guild.getAudioManager().isConnected()) guild.getAudioManager().moveAudioConnection(YttBot.jda.getVoiceChannelByName("Music").get(0));

		else {
			guild.getAudioManager().openAudioConnection(YttBot.jda.getVoiceChannelByName("Music").get(0));

			TextChannel ch = guild.getTextChannels().get(guild.getTextChannels().size() - 1);
			manager = guild.getAudioManager();
			player = new MusicPlayer();
			player.setVolume(DEFAULT_VOLUME);
			manager.setSendingHandler(player);

			playPersonalPlaylist(ch);
		}
	}

	public static void nowPlaying(TextChannel ch) {
		if (player.isPlaying())
        {
            AudioTimestamp currentTime = player.getCurrentTimestamp();
            AudioInfo info = player.getCurrentAudioSource().getInfo();
            if (info.getError() == null)
            {
                ch.sendMessage(
                        "**Playing:** " + info.getTitle() + "\n" +
                        "**Time:**    [" + currentTime.getTimestamp() + " / " + info.getDuration().getTimestamp() + "]");
            }
            else
            {
                ch.sendMessage(
                        "**Playing:** Info Error. Known source: " + player.getCurrentAudioSource().getSource() + "\n" +
                        "**Time:**    [" + currentTime.getTimestamp() + " / (N/A)]");
            }
        }
        else
        {
            ch.sendMessage("The player is not currently playing anything!");
        }
	}
	
	public static void playPersonalPlaylist(TextChannel ch) {
		Playlist playlist = Playlist.getPlaylist("https://soundcloud.com/adam-ratzman/sets/songs");
		List<AudioSource> sources = new LinkedList<AudioSource>(playlist.getSources());


		if (sources.size() > 1)
		{
			ch.sendMessage("Found a playlist with **" + sources.size() + "** entries.\n" +
					"Proceeding to gather information and queue sources. This may take some time...");
			final MusicPlayer fPlayer = player;
			thread = new Thread()
			{
				@Override
				public void run()
				{
					for (Iterator<AudioSource> it = sources.iterator(); it.hasNext();)
					{
						AudioSource source = it.next();
						AudioInfo info = source.getInfo();
						queue = fPlayer.getAudioQueue();
						if (info.getError() == null)
						{
							queue.add(source);
							if (fPlayer.isStopped())
								fPlayer.play();
						}
						else
						{
							ch.sendMessage("Error detected, skipping source. Error:\n" + info.getError());
							it.remove();
						}
					}
					boolean error = false;

					MessageBuilder builder = new MessageBuilder();
					builder.appendString("__Current Queue.  Entries: " + queue.size() + "__\n");
					for (int i = 0; i < queue.size() && i < 10; i++)
					{
						AudioInfo info = queue.get(i).getInfo();
						//		                builder.appendString("**(" + (i + 1) + ")** ");
						if (info == null)
							builder.appendString("*Could not get info for this song.*");
						else
						{
							AudioTimestamp duration = info.getDuration();
							builder.appendString("`[");
							if (duration == null)
								builder.appendString("N/A");
							else
								builder.appendString(duration.getTimestamp());
							builder.appendString("]` " + info.getTitle() + "\n");
						}
					}
					int totalSeconds = 0;
					for (AudioSource source : queue)
					{
						AudioInfo info = source.getInfo();
						if (info == null || info.getDuration() == null)
						{
							error = true;
							continue;
						}
						totalSeconds += info.getDuration().getTotalSeconds();
					}

					builder.appendString("\nTotal Queue Time Length: " + AudioTimestamp.fromSeconds(totalSeconds).getTimestamp());
					if (error)
						builder.appendString("`An error occured calculating total time. Might not be completely valid.");
					ch.sendMessage(builder.build());
					ch.sendMessage("Finished queuing provided playlist. Successfully queued **" + sources.size() + "** sources");
				}
			};
			thread.start();
		}
	}
}
