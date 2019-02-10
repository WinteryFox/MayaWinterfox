package com.winter.mayawinterfox.command.impl.profile;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.item.Inventory;
import com.winter.mayawinterfox.data.item.Item;
import com.winter.mayawinterfox.data.profile.Profile;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;

import java.util.Arrays;
import java.util.Collections;

public class CommandProfile extends Node<Command> {

	public CommandProfile() {
		super(new Command(
				"profile",
				"profile-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					Profile profile = new Profile(e.getMember().get());
					if (profile.getUser().getBackground().getPath().endsWith(".gif"))
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.profileEmbed(e.getMember().get()), profile.render(), "profile.gif");
					else
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.profileEmbed(e.getMember().get()), profile.render(), "profile.png");
					return true;
				}
		), Arrays.asList(
				new Node<>(new Command(
						"background",
						"profile-background-help",
						PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("profile background ".length());
							else
								i = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-item").open();
							if (i == null)
								return false;

							Profile profile = new Profile(e.getMember().get());
							Inventory inventory = Inventory.create(e.getMember().get()).block();
							Item item = inventory.getItem(i);
							if (item != null) {
								profile.getUser().setBackground(item);
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "equipped-item", item.getName()));
							}
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"info",
						"profile-info-help",
						PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("profile info ".length());
							else
								i = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-item").open();
							if (i == null)
								return false;

							if (i.length() < 54) {
								Caches.getUser(e.getMember().get()).block().setDescription(i);
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "changed-info"));
							} else {
								throw new RuntimeException("Profile description must be less than 54 characters including spaces.");
							}
							return true;
						}
				), Collections.emptyList())
		));
	}
}