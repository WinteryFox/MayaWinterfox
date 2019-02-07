/*package com.winter.mayawinterfox.util;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.music.GuildMusicManager;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.VoiceChannel;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Member;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class MusicUtils {

	public static synchronized void join(VoiceChannel c, Member u) {
		VoiceState state =  u.getVoiceState().block();
		if (state == null) {
			MessageUtil.sendMessage(c, Commands.getHelp(c.getGuild(), "no-voice"));
			return;
		}
		VoiceChannel v = state.getChannel().block();
		if (v != null) {
			v.join(null);
			MusicUtils.getGuildMusicManager(c.getGuild()).setBoundChannel(c);
			MessageUtil.sendMessage(c, EmbedUtil.successEmbed(c.getGuild(), "joined-voice", v.getName(), c.getName()));
		} else {
			MessageUtil.sendMessage(c, Commands.getHelp(c.getGuild(), "no-voice"));
		}
	}

	public static synchronized void leave(IChannel c) {
		if (c.getGuild().getConnectedVoiceChannel().isConnected()) {
			c.getGuild().getConnectedVoiceChannel().leave();
			MusicUtils.getGuildMusicManager(c.getGuild()).setBoundChannel(null);
		}
		MessageUtil.sendMessage(c, "left-voice");
	}

	public static synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
		GuildMusicManager musicManager = null;
		try {
			musicManager = Main.musicManagers.get(guild);
		} catch (Exception ignored) {
		}

		if (musicManager == null) {
			musicManager = new GuildMusicManager(guild, Main.playerManager);
			Main.musicManagers.put(guild, musicManager);
		}

		guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());
		return musicManager;
	}

	public static void loadAndPlay(IChannel channel, String trackURL) {
		GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
		musicManager.getPlayer().setVolume(25);

		if (musicManager.getScheduler().getQueueSize() < 10) {
			Main.playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
				public void trackLoaded(AudioTrack audioTrack) {
					MessageUtil.sendMessage(channel, EmbedUtil.successEmbed(channel.getGuild(), "song-queued", audioTrack.getInfo().title, audioTrack.getInfo().uri, audioTrack.getInfo().author, ParsingUtil.formatTime(audioTrack.getInfo().length)));
					play(channel.getGuild(), audioTrack);
				}

				public void playlistLoaded(AudioPlaylist audioPlaylist) {
					for (AudioTrack track : audioPlaylist.getTracks()) {
						play(channel.getGuild(), track);
					}
				}

				public void noMatches() {
					MessageUtil.sendMessage(channel, "no-matches");
				}

				public void loadFailed(FriendlyException ex) {
					ErrorHandler.log(ex, channel);
				}
			});
		} else {
			MessageUtil.sendMessage(channel, "queue-full");
		}
	}

	public static void play(IGuild guild, AudioTrack track) {
		getGuildMusicManager(guild).getScheduler().queue(track);
	}

	public static void pause(IGuild guild) {
		getGuildMusicManager(guild).getPlayer().setPaused(true);
	}

	public static void unpause(IGuild guild) {
		getGuildMusicManager(guild).getPlayer().setPaused(false);
	}

	public static void skipTrack(IChannel channel) {
		getGuildMusicManager(channel.getGuild()).getScheduler().nextTrack();
	}
}*/