package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.GuildUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Arrays;
import java.util.Collections;

public class CommandPrefix extends Node<Command> {

	public CommandPrefix() {
		super(new Command(
				"prefix",
				"prefix-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), "prefixes", Arrays.toString(Caches.getGuild(e.getGuild().block()).getPrefixes().toArray()).replace("[", "").replace("]", ""));
					return true;
				}
		), Arrays.asList(
				new Node<>(new Command(
						"add",
						"prefix-add-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER, false),
						e -> {
							try {
								String[] args = MessageUtil.argsArray(e.getMessage());
								String prefix;
								if (args.length > 1) {
									try {
										prefix = MessageUtil.args(e.getMessage()).substring("prefix add ".length());
									} catch (StringIndexOutOfBoundsException ex) {
										prefix = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-prefix").open();
									}
								} else
									prefix = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-prefix").open();
								if (prefix == null)
									return false;
								if (prefix.equalsIgnoreCase(Main.config.get(Main.ConfigValue.PREFIX)))
									throw new UpdateFailedException(Localisation.getMessage(e.getGuild().block(), "default-prefix"));
								GuildMeta guild = Caches.getGuild(e.getGuild().block());
								if (GuildUtil.hasPrefixesLeft(e.getGuild().block(), e.getMember().get())) {
									guild.addPrefix(prefix);
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "added-prefix", prefix));
								} else {
									if (GuildUtil.isPremium(e.getGuild().block(), e.getMember().get()))
										MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.premiumEmbed(e.getGuild().block(), "max-prefixes"));
									else
										MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.premiumEmbed(e.getGuild().block(), "max-prefixes-premium"));
								}
							} catch (UpdateFailedException ex) {
								ErrorHandler.log(ex, e.getMessage().getChannel().block());
								return false;
							}
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"remove",
						"prefix-remove-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER, false),
						e -> {
							try {
								String[] args = MessageUtil.argsArray(e.getMessage());
								String prefix;
								if (args.length > 1)
									try {
										prefix = MessageUtil.args(e.getMessage()).substring("prefix remove ".length());
									} catch (StringIndexOutOfBoundsException ex) {
										prefix = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-prefix").open();
									}
								else
									prefix = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-prefix").open();
								if (prefix == null)
									return false;
								if (prefix.equalsIgnoreCase(Main.config.get(Main.ConfigValue.PREFIX)))
									throw new UpdateFailedException(Localisation.getMessage(e.getGuild().block(), "default-prefix"));
								GuildMeta guild = Caches.getGuild(e.getGuild().block());
								guild.removePrefix(prefix);
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "removed-prefix", prefix));
							} catch (UpdateFailedException ex) {
								ErrorHandler.log(ex, e.getMessage().getChannel().block());
								return false;
							}
							return true;
						}), Collections.emptyList())));
	}
}
