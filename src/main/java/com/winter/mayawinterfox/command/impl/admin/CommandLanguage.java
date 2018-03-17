package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.HashSet;

public class CommandLanguage extends Node<Command> {

	public CommandLanguage() {
		super(new Command(
				"language",
				"language-help",
				PermissionChecks.hasPermission(Permissions.MANAGE_SERVER, false),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String lang;
					if (args.length > 1)
						lang = args[1];
					else
						lang = new InputDialog(e.getChannel(), e.getAuthor(), "input-language").open();
					if (lang == null)
						return false;

					if (!Localisation.changeLanguage(e.getGuild(), lang))
						throw new IllegalArgumentException(Localisation.getMessage(e.getGuild(), "invalid-language"));
					MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "changed-language", lang));
					return true;
				},
				new HashSet<>(Collections.singletonList("lang"))
		), Collections.emptyList());
	}
}