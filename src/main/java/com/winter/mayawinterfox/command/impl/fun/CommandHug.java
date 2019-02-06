package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.api.internal.json.objects.Consumer<EmbedCreateSpec>;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.Random;

public class CommandHug extends Node<Command> {

	public CommandHug() {
		super(new Command(
			"hug",
				"hug-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					IUser target;
					if (args.length == 2)
						target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("hug ".length()));
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
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed, "hug-from-yourself", e.getMember().get().getName());
					else
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed, "hug-from", target, e.getMember().get().getName());
					return true;
				}
		), Collections.emptyList());
	}
}
