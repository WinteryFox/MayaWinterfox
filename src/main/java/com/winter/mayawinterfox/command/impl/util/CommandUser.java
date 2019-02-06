package com.winter.mayawinterfox.command.impl.util;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ImageUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

public class CommandUser extends Node<Command> {

	public CommandUser() {
		super(new Command(
				"user",
				"user-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					if (args.length > 1)
						target = ParsingUtil.getUser(args[1]);
					else
						target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					EmbedBuilder builder = new EmbedBuilder()
							.withColor(target.getColorForGuild(e.getGuild().block()))
							.withTitle(target.getName() + "#" + target.getDiscriminator())
							.withThumbnail(ImageUtil.getAvatar(target))
							.withTimestamp(Instant.now())
							.appendField(Localisation.getMessage(e.getGuild().block(), "id"), target.getStringID(), false)
							.appendField(Localisation.getMessage(e.getGuild().block(), "creation"), target.getCreationDate().toString(), false);

					if (e.getGuild().block().getUsers().contains(target)) {
						String roles = Arrays.toString(target.getRolesForGuild(e.getGuild().block()).toArray()).replace("[", "").replace("]", "");
						String perms = Arrays.toString(target.getPermissionsForGuild(e.getGuild().block()).toArray()).replace("[", "").replace("]", "");
						builder.appendField(Localisation.getMessage(e.getGuild().block(), "roles"), StringUtils.abbreviate(roles, Math.min(roles.length(), 1024)), false);
						builder.appendField(Localisation.getMessage(e.getGuild().block(), "perms"), StringUtils.abbreviate(perms, Math.min(perms.length(), 1024)), false);
					}
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), builder.build());
					return true;
				}), Collections.emptyList());
	}
}
