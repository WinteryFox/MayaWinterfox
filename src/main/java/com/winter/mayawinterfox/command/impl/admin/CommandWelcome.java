package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.dialog.impl.ChannelDialog;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;

import java.util.Arrays;
import java.util.Collections;

public class CommandWelcome extends Node<Command> {
	
	public CommandWelcome() {
		super(new Command(
				"welcome",
				"welcome-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					GuildMeta guild = Caches.getGuild(e.getGuild().block()).block();
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), "welcome-message", guild.getWelcome(), guild.isWelcomeEnabled() ? "ENABLED" : "DISABLED", guild.getWelcomeChannel() != null ? guild.getWelcomeChannel().getMention() : "NOT SET");
					return true;
				}
		), Arrays.asList(new Node<>(new Command(
						"set",
						"welcome-set-help",
						PermissionChecks.hasPermission(Permission.MANAGE_GUILD),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String welcome;
							if (args.length > 1)
								welcome = MessageUtil.args(e.getMessage()).substring("welcome set ".length());
							else
								welcome = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-item").open();
							if (welcome == null)
								return false;

							Caches.getGuild(e.getGuild().block()).block()
									.setWelcome(welcome).block();
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), "welcome-set", welcome);
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"channel",
						"welcome-channel-help",
						PermissionChecks.hasPermission(Permission.MANAGE_GUILD),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							TextChannel channel;
							if (args.length > 1)
								channel = ParsingUtil.getChannel(e.getGuild().block(), MessageUtil.args(e.getMessage())
										.substring("welcome channel ".length()));
							else
								channel = new ChannelDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
							if (channel == null)
								return false;

							Caches.getGuild(e.getGuild().block()).block().setWelcomeChannel(channel).block();
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), "welcome-channel-set", channel.getMention());
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"toggle",
						"welcome-toggle-help",
						PermissionChecks.hasPermission(Permission.MANAGE_GUILD),
						e -> {
							boolean enabled = Caches.getGuild(e.getGuild().block()).block().toggleWelcomeEnabled().block().isWelcomeEnabled();
							if (enabled)
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "welcome-enabled");
							else
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "welcome-disabled");
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"embed",
						"welcome-embed-help",
						PermissionChecks.hasPermission(Permission.MANAGE_GUILD),
						e -> {
							boolean enabled = Caches.getGuild(e.getGuild().block()).block().toggleWelcomeEmbed().block().isWelcomeEnabled();
							if (enabled)
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "embed-enabled");
							else
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "embed-disabled");
							return true;
						}
				), Collections.emptyList())));
	}
}