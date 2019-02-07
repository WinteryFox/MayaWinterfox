package com.winter.mayawinterfox.command.impl.image;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.http.HTTPHandler;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permission;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;

public class CommandCat extends Node<Command> {

	public CommandCat() {
		super(new Command(
				"cat",
				"cat-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					try {
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.imageEmbed(e.getGuild().block(), HTTPHandler.requestCat()));
					} catch (MalformedURLException | UnirestException ex) {
						ErrorHandler.log(ex, e.getMessage().getChannel().block());
					}
					return true;
				},
				new HashSet<>(Collections.singletonList("kitty"))
		), Collections.emptyList());
	}
}