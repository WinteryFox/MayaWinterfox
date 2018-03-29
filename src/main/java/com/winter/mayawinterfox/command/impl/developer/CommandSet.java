package com.winter.mayawinterfox.command.impl.developer;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.Image;

import java.util.Arrays;
import java.util.Collections;

public class CommandSet extends Node<Command> {

	public CommandSet() {
		super(new Command(
				"set",
				"dev-only",
				PermissionChecks.isGlobal(),
				e -> false
		), Arrays.asList(
				new Node<>(new Command(
						"playing",
						"",
						PermissionChecks.isGlobal(),
						e -> {
							Main.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, MessageUtil.args(e.getMessage()).substring("set playing".length()));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"icon",
						"",
						PermissionChecks.isGlobal(),
						e -> {
							if (e.getMessage().getAttachments().isEmpty())
								Main.getClient().changeAvatar(Image.forUrl("png", MessageUtil.args(e.getMessage()).substring("set icon".length())));
							else
								Main.getClient().changeAvatar(Image.forUrl("png", e.getMessage().getAttachments().get(0).getUrl()));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
					"name",
						"",
						PermissionChecks.isGlobal(),
						e -> {
							Main.getClient().changeUsername(MessageUtil.args(e.getMessage()).substring("set name".length()));
							return true;
						}
				), Collections.emptyList())));
	}

}