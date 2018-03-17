package com.winter.mayawinterfox.command.impl.util;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class CommandServer extends Node<Command> {

	public CommandServer() {
		super(new Command(
				"server",
				"server-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IGuild target = e.getGuild();

					String roles = Arrays.toString(target.getRoles().toArray()).replace("[", "").replace("]", "");
					String bans = Arrays.toString(target.getBannedUsers().toArray()).replace("[", "").replace("]", "");
					String emotes = Arrays.toString(target.getEmojis().toArray()).replace("[", "").replace("]", "");

					EmbedBuilder builder = new EmbedBuilder()
							.withColor(ColorUtil.withinTwoHues(0.333333f, 0.888888f))
							.withTimestamp(Instant.now())
							.withTitle(target.getName())
							.withThumbnail(target.getIconURL())
							.appendField(Localisation.getMessage(target, "id"), target.getStringID(), false)
							.appendField(Localisation.getMessage(target, "members"), String.valueOf(target.getTotalMemberCount()), false)
							.appendField(Localisation.getMessage(target, "owner"), target.getOwner().mention(), false)
							.appendField(Localisation.getMessage(target, "region"), target.getRegion().getName(), false)
							.appendField(Localisation.getMessage(target, "creation"), target.getCreationDate().toString(), false)
							.appendField(Localisation.getMessage(target, "default-channel"), target.getDefaultChannel().mention(), false)
							.appendField(Localisation.getMessage(target, "channels"), String.valueOf(target.getChannels().size()), false)
							.appendField(Localisation.getMessage(target, "roles"), StringUtils.abbreviate(roles, Math.min(roles.length(), 1024)), false);

					if (e.getClient().getOurUser().getPermissionsForGuild(target).contains(Permissions.BAN) && !target.getBans().isEmpty())
						builder.appendField(Localisation.getMessage(target, "bans"), StringUtils.abbreviate(bans, Math.min(bans.length(), 1024)), false);
					if (!target.getEmojis().isEmpty())
						builder.appendField(Localisation.getMessage(target, "emotes"), StringUtils.abbreviate(emotes, Math.min(emotes.length(), 1024)), false);
					MessageUtil.sendMessage(e.getChannel(), builder.build());
					return true;
				},
				new HashSet<>(Collections.singletonList("serverinfo"))
		), Collections.emptyList());
	}
}