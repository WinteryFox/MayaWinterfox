package com.winter.mayawinterfox.command.impl.util;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.ImageUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public class CommandUser extends Node<Command> {

	public CommandUser() {
		super(new Command(
				"user",
				"user-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					User target;
					if (args.length > 1)
						target = ParsingUtil.getUser(args[1]);
					else
						target = new TargetDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;

					Consumer<EmbedCreateSpec> embed = spec -> {
							spec.setColor(ColorUtil.withinTwoHues(0.2f, 0.8f))
									.setTitle(target.getUsername() + "#" + target.getDiscriminator())
									.setThumbnail(ImageUtil.getAvatar(target))
									.setTimestamp(Instant.now())
									.addField(Localisation.getMessage(e.getGuild().block(), "id"), target.getId().asString(), false)
									.addField("Discriminator", target.getDiscriminator(), true)
									.addField("Avatar URL", target.getAvatarUrl(), false);

						Member member = target.asMember(e.getGuildId().get()).block();
						if (member != null) {
							spec.addField("Join Date", member.getJoinTime().toString(), false);
							String roles = Arrays.toString(member.getRoles().map(Role::getName).collectList().block().toArray()).replace("[", "").replace("]", "");
							spec.addField(Localisation.getMessage(e.getGuild().block(), "roles"), StringUtils.abbreviate(roles, Math.min(roles.length(), 1024)), false);
						}
					};
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed);
					return true;
				}), Collections.emptyList());
	}
}
