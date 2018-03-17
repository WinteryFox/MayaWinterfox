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
import sx.blah.discord.handle.obj.Permissions;

import java.util.Arrays;
import java.util.Collections;

public class CommandProfile extends Node<Command> {

	public CommandProfile() {
		super(new Command(
				"profile",
				"profile-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					Profile profile = new Profile(e.getAuthor());
					if (profile.getUser().getBackground().getPath().endsWith(".gif"))
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.profileEmbed(e.getAuthor()), profile.render(), "profile.gif");
					else
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.profileEmbed(e.getAuthor()), profile.render(), "profile.png");
					return true;
				}
		), Arrays.asList(
				new Node<>(new Command(
						"background",
						"profile-background-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("profile background ".length());
							else
								i = new InputDialog(e.getChannel(), e.getAuthor(), "input-item").open();
							if (i == null)
								return false;

							Profile profile = new Profile(e.getAuthor());
							Inventory inventory = new Inventory(e.getAuthor());
							Item item = inventory.getItem(i);
							if (item != null) {
								profile.getUser().setBackground(item);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "equipped-item", item.getName()));
							}
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"info",
						"profile-info-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("profile info ".length());
							else
								i = new InputDialog(e.getChannel(), e.getAuthor(), "input-item").open();
							if (i == null)
								return false;

							if (i.length() < 54) {
								Caches.getUser(e.getAuthor()).setDescription(i);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "changed-info"));
							} else {
								throw new RuntimeException("Profile description must be less than 54 characters including spaces.");
							}
							return true;
						}
				), Collections.emptyList())
		));
	}
}