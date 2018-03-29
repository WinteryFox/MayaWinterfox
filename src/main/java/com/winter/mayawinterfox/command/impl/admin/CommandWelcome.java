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
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Arrays;
import java.util.Collections;

public class CommandWelcome extends Node<Command> {
	
	public CommandWelcome() {
		super(new Command(
				"welcome",
				"welcome-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					GuildMeta guild = Caches.getGuild(e.getGuild());
					MessageUtil.sendMessage(e.getChannel(), "welcome-message", guild.getWelcome());
					return true;
				}
		), Arrays.asList(new Node<>(new Command(
						"set",
						"set-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String welcome;
							if (args.length > 1)
								welcome = MessageUtil.args(e.getMessage()).substring("welcome set ".length());
							else
								welcome = new InputDialog(e.getChannel(), e.getAuthor(), "input-item").open();
							if (welcome == null)
								return false;

							Caches.getGuild(e.getGuild())
							      .setWelcome(welcome);
							MessageUtil.sendMessage(e.getChannel(), "welcome-set", welcome);
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"channel",
						"channel-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							IChannel channel;
							if (args.length > 1)
								channel = ParsingUtil.getChannel(e.getGuild(), MessageUtil.args(e.getMessage())
										.substring("welcome channel ".length()));
							else
								channel = new ChannelDialog(e.getChannel(), e.getAuthor()).open();
							if (channel == null)
								return false;
							
							Caches.getGuild(e.getGuild()).setWelcomeChannel(channel);
							MessageUtil.sendMessage(e.getChannel(), "welcome-channel-set", channel.mention());
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"toggle",
						"toggle-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER),
						e -> {
							boolean enabled = Caches.getGuild(e.getGuild()).toggleWelcomeEnabled();
							if (enabled)
								MessageUtil.sendMessage(e.getChannel(), "welcome-enabled");
							else
								MessageUtil.sendMessage(e.getChannel(), "welcome-disabled");
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"embed",
						"embed-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER),
						e -> {
							boolean enabled = Caches.getGuild(e.getGuild()).toggleWelcomeEmbed();
							if (enabled)
								MessageUtil.sendMessage(e.getChannel(), "embed-enabled");
							else
								MessageUtil.sendMessage(e.getChannel(), "embed-disabled");
							return true;
						}
				), Collections.emptyList())));
	}
}