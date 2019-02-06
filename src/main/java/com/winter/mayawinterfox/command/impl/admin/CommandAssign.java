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
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;


public class CommandAssign extends Node<Command> {

	public CommandAssign() {
		super(new Command(
				"assign",
				"assign-help",
				PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					IRole role;
					if (args.length > 1)
						role = ParsingUtil.getRole(e.getGuild().block(), args[1]);
					else
						role = new RoleDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (role == null)
						return false;

					if (args.length > 2)
						target = ParsingUtil.getUser(args[2]);
					else
						target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					GuildUtil.addRole(target, role);
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "assigned-role", role.getName(), target.getName()));
					return true;
				}
		), Collections.emptyList());
	}
}
