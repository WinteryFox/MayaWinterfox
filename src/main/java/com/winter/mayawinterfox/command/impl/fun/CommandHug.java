package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import discord4j.core.object.entity.Member;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;

public class CommandHug extends Node<Command> {

	public CommandHug() {
		super(new Command(
			"hug",
				"hug-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					Member target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("hug ".length())).asMember(e.getGuildId().get()).block();
					else
						target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
					if (target == null)
						return false;
					String[] images = {
							//"https://imgur.com/a/mZ4jJ"
							"http://imgur.com/scNkWcj.gif",
							"http://imgur.com/KAxcvC4.gif",
							"http://imgur.com/OG6Xkgx.gif",
							"http://imgur.com/alBP2Hq.gif",
							"http://imgur.com/dMvK13X.gif",
							"http://imgur.com/X87fp5U.gif",
							"http://imgur.com/I3RsJk1.gif"
					};

					Consumer<EmbedCreateSpec> embed = EmbedUtil.imageEmbed(e.getGuild().block(), images[new Random().nextInt(images.length)]);

					if (e.getMember().get().equals(target))
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed, "hug-from-yourself", e.getMember().get().getDisplayName());
					else
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed, "hug-from", target, e.getMember().get().getDisplayName());
					return true;
				}
		), Collections.emptyList());
	}
}
