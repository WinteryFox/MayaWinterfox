package com.winter.mayawinterfox;

import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.schedule.Cooldowns;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.*;
import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.channel.VoiceChannelUpdateEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.GuildChannel;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EventListener {
	
	private final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

	public EventListener(DiscordClient client) {
		EventDispatcher dispatch = client.getEventDispatcher();
		dispatch.on(ReconnectEvent.class).subscribe(this::onReconnect);
		dispatch.on(GuildCreateEvent.class).subscribe(this::onGuildCreated);
		dispatch.on(GuildDeleteEvent.class).subscribe(this::onGuildDeleted);
		dispatch.on(MessageCreateEvent.class).subscribe(this::onMessageReceived);
		dispatch.on(MemberJoinEvent.class).subscribe(this::onUserJoined);
	}

	private void onReconnect(ReconnectEvent e) {
		e.getClient().updatePresence(Presence.online(Activity.playing(".mayahelp | .mayainvite"))).block();
	}

	private void onUserLeftVoice(VoiceChannelUpdateEvent e) {
		/*try {
			Commands.THREAD_POOL.submit(() -> {
				Guild guild = e.getCurrent().getGuild().block();
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
		}*/
	}

	private void onGuildCreated(GuildCreateEvent e) {
		try {
			Commands.THREAD_POOL.submit(() -> {
				GuildMeta guild = Caches.getGuild(e.getGuild());
				if (guild.isNewGuild()) {
					TextChannel channel = GuildUtil.getFirstChannel(e.getGuild());
					if (channel != null) {
						MessageUtil.sendMessage(channel, EmbedUtil.welcomeEmbed());
						guild.setNewGuild(false);
					}
				}
				LOGGER.info("Gained a guild! Currently in " + e.getClient().getGuilds().collectList().block().size() + " guilds!");
			}).get(30, TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			ErrorHandler.log(ex, "thread-timeout");
		} catch (InterruptedException ex) {
			ErrorHandler.log(ex, "thread-interrupted");
		} catch (ExecutionException ex) {
			ErrorHandler.log(ex, "thread-execution");
		}
	}

	private void onGuildDeleted(GuildDeleteEvent e) {
		LOGGER.info("Lost a guild! Currently in " + e.getClient().getGuilds().collectList().block().size() + " guilds!");
		Database.set("UPDATE guild SET newguild=TRUE WHERE id=?;", e.getGuild().get().getId().asLong());
	}

	private void onMessageReceived(MessageCreateEvent e) {
		if (e.getMessage().getChannel().block() instanceof GuildChannel && !e.getMember().get().isBot()) {
			try {
				Commands.THREAD_POOL.submit(() -> {
					UserMeta user = Caches.getUser(e.getMember().get());
					if (!Cooldowns.onCooldown(e.getMember().get(), "message-xp")) {
						Cooldowns.putOnCooldown(e.getMember().get(), "message-xp", 120000);
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

	private void onUserJoined(MemberJoinEvent e) {
		try {
			Commands.THREAD_POOL.submit(() -> {
				GuildMeta guild = Caches.getGuild(e.getGuild().block());
				if (guild.isWelcomeEnabled() && guild.getWelcomeChannel() != null && guild.getWelcome() != null && !guild.getWelcome().equals("")) {
					String message = guild.getWelcome()
					                      .replace("[user]", e.getMember().getDisplayName())
					                      .replace("[mention]", e.getMember().getMention())
					                      .replace("[server]", e.getGuild().block().getName());
					if (guild.isWelcomeEmbed())
						MessageUtil.sendMessage(guild.getWelcomeChannel(), embed -> embed.setColor(ColorUtil.withinTwoHues(0.3333f, 0.8888f))
						                                                                     .setDescription(message)
						                                                                     .setTimestamp(e.getMember().getJoinTime()));
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