package com.winter.mayawinterfox.command.impl.image;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.http.HTTPHandler;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;

public class CommandCat extends Node<Command> {

	public CommandCat() {
		super(new Command(
				"cat",
				"cat-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					try {
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.imageEmbed(e.getGuild(), HTTPHandler.requestCat()));
					} catch (MalformedURLException | UnirestException ex) {
						ErrorHandler.log(ex, e.getChannel());
					}
					return true;
				},
				new HashSet<>(Collections.singletonList("kitty"))
		), Collections.emptyList());
	}
}