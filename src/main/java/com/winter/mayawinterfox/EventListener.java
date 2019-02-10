package com.winter.mayawinterfox;

import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.schedule.Cooldowns;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.GuildUtil;
import com.winter.mayawinterfox.util.MessageUtil;
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
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EventListener {

	private final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

	static Mono<Void> onUserJoined(MemberJoinEvent e) {
		Mono<GuildMeta> meta = e.getGuild().flatMap(Caches::getGuild);
		Mono<TextChannel> channel = meta.map(GuildMeta::getWelcomeChannel);
		Mono<String> message = meta.map(m -> m.getWelcome(e.getMember()));

		return meta.filter(GuildMeta::isWelcomeEnabled)
				.flatMap(v -> meta.filter(GuildMeta::isWelcomeEmbed)
						.flatMap(send -> Mono.zip(channel, message)
								.map(aa -> MessageUtil.sendMessage(aa.getT1(), spec -> spec
										.setDescription(aa.getT2())
										.setColor(ColorUtil.withinTwoHues(0.333f, 0.888f))
										.setTimestamp(Instant.now()))))
						.switchIfEmpty(Mono.zip(channel, message, MessageUtil::sendRawMessage)))
				.then();
	}

	public void onReconnect(ReconnectEvent e) {
		e.getClient().updatePresence(Presence.online(Activity.playing(".mayahelp | .mayainvite"))).block();
	}

	public void onUserLeftVoice(VoiceChannelUpdateEvent e) {
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

	public void onGuildCreated(GuildCreateEvent e) {
		try {
			Commands.THREAD_POOL.submit(() -> {
				GuildMeta guild = Caches.getGuild(e.getGuild()).block();
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

	public void onGuildDeleted(GuildDeleteEvent e) {
		LOGGER.info("Lost a guild! Currently in " + e.getClient().getGuilds().collectList().block().size() + " guilds!");
		Database.set("UPDATE guild SET newguild=TRUE WHERE id=?;", e.getGuild().get().getId().asLong());
	}

	public void onMessageReceived(MessageCreateEvent e) {
		if (e.getMessage().getChannel().block() instanceof GuildChannel && !e.getMember().get().isBot()) {
			try {
				Commands.THREAD_POOL.submit(() -> {
					UserMeta user = Caches.getUser(e.getMember().get()).block();
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
}