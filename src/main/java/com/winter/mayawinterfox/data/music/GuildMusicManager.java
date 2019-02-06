package com.winter.mayawinterfox.data.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.VoiceChannel;

public class GuildMusicManager {

	private final Guild guild;
	private final AudioPlayer player;
	private final TrackScheduler scheduler;
	private VoiceChannel boundChannel;

	public GuildMusicManager(Guild guild, AudioPlayerManager manager) {
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

	public VoiceChannel getBoundChannel() {
		return this.boundChannel;
	}

	public void setBoundChannel(VoiceChannel boundChannel) {
		this.boundChannel = boundChannel;
	}

	public Guild getGuild() {
		return this.guild;
	}
}