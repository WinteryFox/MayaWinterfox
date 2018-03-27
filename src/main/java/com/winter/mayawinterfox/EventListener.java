package com.winter.mayawinterfox;

import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.http.HTTPHandler;
import com.winter.mayawinterfox.data.music.GuildMusicManager;
import com.winter.mayawinterfox.data.schedule.Cooldowns;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EventListener {
	
	private final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);
	
	@EventSubscriber
	public void onReady(ReadyEvent e) {
		LOGGER.info("Ready! Total guilds: " + e.getClient().getGuilds().size());
		if (Main.config.get(Main.ConfigValue.DEBUG).equalsIgnoreCase("false")) {
			HTTPHandler.postStats(e.getClient().getOurUser().getShard().getInfo()[0]);
			LOGGER.info("Posted stats!");
		}
	}
	
	@EventSubscriber
	public void onUserLeftVoice(UserVoiceChannelLeaveEvent e) {
		try {
			Commands.THREAD_POOL.submit(() -> {
				IGuild guild = e.getGuild();
				if (guild.getConnectedVoiceChannel() != null && guild.getConnectedVoiceChannel().getConnectedUsers().size() <= 1) {
					GuildMusicManager manager = MusicUtils.getGuildMusicManager(guild);
					manager.getScheduler().clearQueue();
					manager.getPlayer().stopTrack();
					guild.getConnectedVoiceChannel().leave();
					MessageUtil.sendMessage(manager.getBoundChannel(), "inactive-disconnected");
					MusicUtils.getGuildMusicManager(guild).setBoundChannel(null);
				}
			}).get(60, TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			ErrorHandler.log(ex, "thread-timeout");
		} catch (InterruptedException ex) {
			ErrorHandler.log(ex, "thread-interrupted");
		} catch (ExecutionException ex) {
			ErrorHandler.log(ex, "thread-execution");
		}
	}
	
	@EventSubscriber
	public void onGuildCreated(GuildCreateEvent e) {
		if (e.getClient().isReady()) {
			try {
				Commands.THREAD_POOL.submit(() -> {
					GuildMeta guild = Caches.getGuild(e.getGuild());
					if (guild.isNewGuild()) {
						IChannel channel = GuildUtil.getFirstChannel(e.getGuild());
						if (channel != null) {
							MessageUtil.sendMessage(channel, EmbedUtil.welcomeEmbed());
							guild.setNewGuild(false);
						}
					}
					HTTPHandler.postStats(e.getClient().getShards().indexOf(e.getClient().getOurUser().getShard()));
				}).get(30, TimeUnit.SECONDS);
			} catch (TimeoutException ex) {
				ErrorHandler.log(ex, "thread-timeout");
			} catch (InterruptedException ex) {
				ErrorHandler.log(ex, "thread-interrupted");
			} catch (ExecutionException ex) {
				ErrorHandler.log(ex, "thread-execution");
			} catch (DiscordException ignored) { }
		}
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent e) {
		if (!e.getChannel().isPrivate() && !e.getAuthor().isBot()) {
			try {
				Commands.THREAD_POOL.submit(() -> {
					UserMeta user = Caches.getUser(e.getAuthor());
					if (!Cooldowns.onCooldown(e.getAuthor(), "message-xp")) {
						Cooldowns.putOnCooldown(e.getAuthor(), "message-xp", 120000);
						if ((user.getXP() + 30) >= user.getMaxXP()) {
							user.setXp(0);
							user.setMaxXP(user.getMaxXP() + 60);
							user.setTotalXP(user.getTotalXP() + 30);
							user.setCoins(user.getCoins() + 50);
							user.setLevel(user.getLevel() + 1);
						} else {
							user.setXp(user.getXP() + 30);
							user.setTotalXP(user.getTotalXP() + 30);
						}
					}
				}).get(15, TimeUnit.SECONDS);
			} catch (TimeoutException ex) {
				ErrorHandler.log(ex, "thread-timeout");
			} catch (InterruptedException ex) {
				ErrorHandler.log(ex, "thread-interrupted");
			} catch (ExecutionException ex) {
				ErrorHandler.log(ex, "thread-execution");
			}
		}
	}

	@EventSubscriber
	public void onUserJoined(UserJoinEvent e) {
		try {
			Commands.THREAD_POOL.submit(() -> {
				GuildMeta guild = Caches.getGuild(e.getGuild());
				if (guild.isWelcomeEnabled() && guild.getWelcomeChannel() != null) {
					UserMeta user = Caches.getUser(e.getUser());
					String message = guild.getWelcome()
					                      .replace("[user]", user.getName())
					                      .replace("[mention]", user.mention())
					                      .replace("[server]", guild.getName());
					if (guild.isWelcomeEmbed())
						MessageUtil.sendMessage(guild.getWelcomeChannel(), new EmbedBuilder().withColor(ColorUtil.withinTwoHues(0.3333f, 0.8888f))
						                                                                     .withDescription(message)
						                                                                     .withTimestamp(e.getJoinTime())
						                                                                     .build());
					else
						MessageUtil.sendRawMessage(guild.getWelcomeChannel(), message);
				}
			}).get(15, TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			ErrorHandler.log(ex, "thread-timeout");
		} catch (InterruptedException ex) {
			ErrorHandler.log(ex, "thread-interrupted");
		} catch (ExecutionException ex) {
			ErrorHandler.log(ex, "thread-execution");
		}
	}
}