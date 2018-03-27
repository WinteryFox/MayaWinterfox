package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.util.MessageUtil;
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
								welcome =
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"set",
						"set-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER),
						e -> {
							
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"set",
						"set-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_SERVER),
						e -> {
							
							return true;
						}
				), Collections.emptyList())));
	}
}