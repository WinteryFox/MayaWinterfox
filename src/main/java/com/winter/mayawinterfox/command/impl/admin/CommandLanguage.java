package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.HashSet;

public class CommandLanguage extends Node<Command> {

	public CommandLanguage() {
		super(new Command(
				"language",
				"language-help",
				PermissionChecks.hasPermission(Permission.MANAGE_GUILD),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String lang;
					if (args.length > 1)
						lang = args[1];
					else
						lang = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-language").open();
					if (lang == null)
						return false;

					if (!Localisation.changeLanguage(e.getGuild().block(), lang))
						throw new IllegalArgumentException(Localisation.getMessage(e.getGuild().block(), "invalid-language"));
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "changed-language", lang));
					return true;
				},
				new HashSet<>(Collections.singletonList("lang"))
		), Collections.emptyList());
	}
}