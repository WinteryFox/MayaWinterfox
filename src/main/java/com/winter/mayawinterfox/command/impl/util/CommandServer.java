package com.winter.mayawinterfox.command.impl.util;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Role;
import discord4j.core.object.util.Image;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Consumer;

public class CommandServer extends Node<Command> {

	public CommandServer() {
		super(new Command(
				"server",
				"server-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					Guild target = e.getGuild().block();

					String roles = Arrays.toString(target.getRoles().map(Role::getName).collectList().block().toArray()).replace("[", "").replace("]", "");
					String bans = Arrays.toString(target.getBans().map(ban -> ban.getUser().getMention()).collectList().block().toArray()).replace("[", "").replace("]", "");
					String emotes = Arrays.toString(target.getEmojis().map(GuildEmoji::asFormat).collectList().block().toArray()).replace("[", "").replace("]", "");

					Consumer<EmbedCreateSpec> embed = spec -> spec
							.setColor(ColorUtil.withinTwoHues(0.333333f, 0.888888f))
							.setTimestamp(Instant.now())
							.setTitle(target.getName())
							.setThumbnail(target.getIconUrl(Image.Format.PNG).orElse("https://cdn.discordapp.com/emojis/480535349576073228.png?v=1"))
							.addField(Localisation.getMessage(target, "id"), target.getId().asString(), false)
							.addField(Localisation.getMessage(target, "members"), String.valueOf(target.getMemberCount().getAsInt()), false)
							.addField(Localisation.getMessage(target, "owner"), target.getOwner().block().getMention(), false)
							.addField(Localisation.getMessage(target, "region"), target.getRegion().block().getName(), false)
							.addField("Bans", StringUtils.abbreviate(bans, Math.min(bans.length(), 1024)), false)
							.addField(Localisation.getMessage(target, "channels"), String.valueOf(target.getChannels().collectList().block().size()), false)
							.addField(Localisation.getMessage(target, "roles"), StringUtils.abbreviate(roles, Math.min(roles.length(), 1024)), false)
							.addField(Localisation.getMessage(target, "emotes"), StringUtils.abbreviate(emotes, Math.min(emotes.length(), 1024)), false);

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed);
					return true;
				},
				new HashSet<>(Collections.singletonList("serverinfo"))
		), Collections.emptyList());
	}
}