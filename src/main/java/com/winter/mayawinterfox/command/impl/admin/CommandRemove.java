package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.RoleDialog;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.GuildUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;

import java.util.Collections;

public class CommandRemove extends Node<Command> {

	public CommandRemove() {
		super(new Command(
				"remove",
				"remove-help",
				PermissionChecks.hasPermission(Permission.MANAGE_ROLES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					Member target;
					Role role;
					if (args.length > 1)
						role = ParsingUtil.getRole(e.getGuild().block(), args[1]);
					else
						role = new RoleDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (role == null)
						return false;

					if (args.length > 2)
						target = ParsingUtil.getUser(args[2]).asMember(e.getGuildId().get()).block();
					else
						target = new TargetDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					GuildUtil.removeRole(target, role);
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "removed-role", role.getName(), target.getUsername()));
					return true;
				}
		), Collections.emptyList());
	}
}