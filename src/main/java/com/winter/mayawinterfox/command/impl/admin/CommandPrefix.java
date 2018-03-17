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
					MessageUtil.sendMessage(e.getChannel(), "prefixes", Arrays.toString(Caches.getGuild(e.getGuild()).getPrefixes().toArray()).replace("[", "").replace("]", ""));
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
										prefix = new InputDialog(e.getChannel(), e.getAuthor(), "input-prefix").open();
									}
								} else
									prefix = new InputDialog(e.getChannel(), e.getAuthor(), "input-prefix").open();
								if (prefix == null)
									return false;
								if (prefix.equalsIgnoreCase(Main.config.get(Main.ConfigValue.PREFIX)))
									throw new UpdateFailedException(Localisation.getMessage(e.getGuild(), "default-prefix"));
								GuildMeta guild = Caches.getGuild(e.getGuild());
								if (GuildUtil.hasPrefixesLeft(e.getGuild(), e.getAuthor())) {
									guild.addPrefix(prefix);
									MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "added-prefix", prefix));
								} else {
									if (!GuildUtil.isPremium(e.getGuild(), e.getAuthor()))
										MessageUtil.sendMessage(e.getChannel(), EmbedUtil.premiumEmbed(e.getGuild(), "max-prefixes"));
									else
										MessageUtil.sendMessage(e.getChannel(), EmbedUtil.premiumEmbed(e.getGuild(), "max-prefixes-premium"));
								}
							} catch (UpdateFailedException ex) {
								ErrorHandler.log(ex, e.getChannel());
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
										prefix = new InputDialog(e.getChannel(), e.getAuthor(), "input-prefix").open();
									}
								else
									prefix = new InputDialog(e.getChannel(), e.getAuthor(), "input-prefix").open();
								if (prefix == null)
									return false;
								if (prefix.equalsIgnoreCase(Main.config.get(Main.ConfigValue.PREFIX)))
									throw new UpdateFailedException(Localisation.getMessage(e.getGuild(), "default-prefix"));
								GuildMeta guild = Caches.getGuild(e.getGuild());
								guild.removePrefix(prefix);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "removed-prefix", prefix));
							} catch (UpdateFailedException ex) {
								ErrorHandler.log(ex, e.getChannel());
								return false;
							}
							return true;
						}), Collections.emptyList())));
	}
}
