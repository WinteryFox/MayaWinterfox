package com.winter.mayawinterfox.data.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class GuildMusicManager {

	private final IGuild guild;
	private final AudioPlayer player;
	private final TrackScheduler scheduler;
	private IChannel boundChannel;

	public GuildMusicManager(IGuild guild, AudioPlayerManager manager) {
		this.player = manager.createPlayer();
		this.scheduler = new TrackScheduler(player, guild);
		this.player.addListener(scheduler);
		this.guild = guild;
	}

	public AudioProvider getAudioProvider() {
		return new AudioProvider(player);
	}

	public AudioPlayer getPlayer() {
		return this.player;
	}

	public TrackScheduler getScheduler() {
		return this.scheduler;
	}

	public IChannel getBoundChannel() {
		return this.boundChannel;
	}

	public void setBoundChannel(IChannel boundChannel) {
		this.boundChannel = boundChannel;
	}

	public IGuild getGuild() {
		return this.guild;
	}
}