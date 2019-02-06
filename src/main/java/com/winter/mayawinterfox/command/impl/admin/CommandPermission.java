package com.winter.mayawinterfox.command.impl.admin;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.data.permissions.Group;
import com.winter.mayawinterfox.data.permissions.Guild;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import org.apache.commons.lang3.text.WordUtils;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class CommandPermission extends Node<Command> {

	public CommandPermission() {
		super(new Command(
			"permission",
			"permission-help",
				PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
				e -> {
					EmbedBuilder builder = new EmbedBuilder()
							.withColor(ColorUtil.withinTwoHues(0.333f, 0.8888f))
							.withTitle(Localisation.getMessage(e.getGuild().block(), "permissions-list"));
					for (Map.Entry<Commands.Category, List<Node<Command>>> entry : Commands.COMMAND_MAP.entrySet()) {
						List<String> permissions = entry.getValue().stream().map(Commands::getPermission).collect(Collectors.toList());
						builder.appendField(WordUtils.capitalizeFully(entry.getKey().getName()), Arrays.toString(permissions.toArray()).replace("[", "").replace("]", ""), false);
					}
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), builder.build());
					return true;
				}
		), Arrays.asList(
				new Node<>(new Command(
						"add",
						"permission-add-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String permission;
							IUser target;
							if (args.length <= 2) {
								permission = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-permission").open();
								if (!Commands.getPermissions().contains(permission)) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
									return false;
								}
								target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
							} else if (args.length == 3) {
								permission = args[2];
								if (!Commands.getPermissions().contains(permission)) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
									return false;
								}
								target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
							} else {
								permission = args[2];
								if (!Commands.getPermissions().contains(permission)) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
									return false;
								}
								target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring(("permission add " + permission + " ").length()));
							}
							if (permission == null || target == null)
								return false;
							permission = permission.toLowerCase();

							new Guild(e.getGuild().block()).getUser(target).addPermission(permission);
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "added-permission", permission, target.getName()));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"remove",
						"permission-remove-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String permission = null;
							IUser target = null;
							if (args.length <= 2) {
								permission = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-permission").open();
								if (!Commands.getPermissions().contains(permission)) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
									return false;
								}
								target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
							} else if (args.length == 3) {
								permission = args[2];
								if (!Commands.getPermissions().contains(permission)) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
									return false;
								}
								target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
							} else {
								permission = args[2];
								if (!Commands.getPermissions().contains(permission)) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
									return false;
								}
								target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring(("permission remove " + permission + " ").length()));
							}
							if (permission == null || target == null)
								return false;
							permission = permission.toLowerCase();

							new Guild(e.getGuild().block()).getUser(target).removePermission(permission);
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "removed-permission", permission, target.getName()));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"group",
						"permission-group-help",
						PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String target = null;
							if (args.length > 2)
								target = MessageUtil.args(e.getMessage()).substring("permission group ".length());

							if (target == null) {
								Set<Group> groups = new Guild(e.getGuild().block()).getGroups();
								if (groups.isEmpty()) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), new EmbedBuilder()
											.withColor(ColorUtil.withinTwoHues(0.333f, 0.888f))
											.withTitle(Localisation.getMessage(e.getGuild().block(), "groups"))
											.withDescription(Localisation.getMessage(e.getGuild().block(), "no-results"))
											.build());
								} else {
									String list = Arrays.toString(groups.stream().map(Group::getName).collect(Collectors.toSet()).toArray()).replace("[", "").replace("]", "");
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), new EmbedBuilder()
											.withColor(ColorUtil.withinTwoHues(0.333f, 0.888f))
											.withTitle(Localisation.getMessage(e.getGuild().block(), "groups"))
											.withDescription(list)
											.build());
								}
								return true;
							} else {
								Guild guild = new Guild(e.getGuild().block());
								Group group = guild.getGroup(target);
								if (group == null) {
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-results");
									return false;
								}
								Set<IUser> members = group.getMembers();
								IRole role = group.getRole();

								MessageUtil.sendMessage(e.getMessage().getChannel().block(), new EmbedBuilder()
										.withColor(ColorUtil.withinTwoHues(0.333f, 0.888f))
										.withTitle(group.getName())
										.appendField(Localisation.getMessage(e.getGuild().block(), "permissions"), Arrays.toString(group.getPermissions().toArray()).replace("[", "").replace("]", ""), false)
										.appendField(Localisation.getMessage(e.getGuild().block(), "members"), !members.isEmpty() ? Arrays.toString(members.toArray()).replace("[", "").replace("]", "") : Localisation.getMessage(e.getGuild().block(), "no-results"), false)
										.appendField(Localisation.getMessage(e.getGuild().block(), "bound-role"), role != null ? role.getName() : Localisation.getMessage(e.getGuild().block(), "link-a-role"), false)
										.build());
								return true;
							}
						}
				), Arrays.asList(
						new Node<>(new Command(
								"add",
								"permission-group-add-help",
								PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
								e -> {
									Guild guild = new Guild(e.getGuild().block());
									String[] args = MessageUtil.argsArray(e.getMessage());
									String permission = null;
									Group group = null;
									if (args.length <= 3) {
										permission = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-permission").open();
										if (!Commands.getPermissions().contains(permission)) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
											return false;
										}
										group = guild.getGroup(new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-group").open());
									} else if (args.length == 4) {
										permission = args[3];
										if (!Commands.getPermissions().contains(permission)) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
											return false;
										}
										group = guild.getGroup(new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-group").open());
									} else {
										permission = args[3];
										System.out.println(permission);
										if (!Commands.getPermissions().contains(permission)) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
											return false;
										}
										group = guild.getGroup(MessageUtil.args(e.getMessage()).substring(("permission group add " + permission + " ").length()));
									}
									if (permission == null || group == null)
										return false;
									permission = permission.toLowerCase();

									group.addPermission(permission);
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "added-permission", permission, group.getName()));
									return true;
								}
						), Collections.emptyList()),
						new Node<>(new Command(
								"remove",
								"permission-group-remove-help",
								PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
								e -> {
									Guild guild = new Guild(e.getGuild().block());
									String[] args = MessageUtil.argsArray(e.getMessage());
									String permission = null;
									Group group = null;
									if (args.length <= 3) {
										permission = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-permission").open();
										if (!Commands.getPermissions().contains(permission)) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
											return false;
										}
										group = guild.getGroup(new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-group").open());
									} else if (args.length == 4) {
										permission = MessageUtil.args(e.getMessage()).substring("permission group remove ".length());
										if (!Commands.getPermissions().contains(permission)) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
											return false;
										}
										group = guild.getGroup(new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-group").open());
									} else {
										permission = MessageUtil.args(e.getMessage()).substring("permission group remove ".length());
										if (!Commands.getPermissions().contains(permission)) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-permission", permission);
											return false;
										}
										group = guild.getGroup(MessageUtil.args(e.getMessage()).substring(("permission group remove " + permission + " ").length()));
									}
									if (permission == null || group == null)
										return false;
									permission = permission.toLowerCase();

									group.removePermission(permission);
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "removed-permission", permission, group.getName()));
									return true;
								}
						), Collections.emptyList()),
						new Node<>(new Command(
								"adduser",
								"permission-group-adduser-help",
								PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
								e -> {
									Guild guild = new Guild(e.getGuild().block());
									String[] args = MessageUtil.argsArray(e.getMessage());
									Group group = null;
									IUser target = null;
									if (args.length <= 3) {
										group = guild.getGroup(new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-group").open());
										if (group == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-group");
											return false;
										}
										target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
									} else if (args.length == 4) {
										group = guild.getGroup(args[3]);
										if (group == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-group");
											return false;
										}
										target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
									} else {
										group = guild.getGroup(args[3]);
										if (group == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-group");
											return false;
										}
										target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring(("permission group adduser " + group.getName() + " ").length()));
									}
									if (group == null || target == null)
										return false;

									group.addMember(target);
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "member-added", target.getName(), group.getName()));
									return true;
								}
						), Collections.emptyList()),
						new Node<>(new Command(
								"removeuser",
								"permission-group-removeuser-help",
								PermissionChecks.hasPermission(Permissions.MANAGE_ROLES),
								e -> {
									Guild guild = new Guild(e.getGuild().block());
									String[] args = MessageUtil.argsArray(e.getMessage());
									Group group = null;
									IUser target = null;
									if (args.length <= 3) {
										group = guild.getGroup(new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-group").open());
										if (group == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-group");
											return false;
										}
										target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
									} else if (args.length == 4) {
										group = guild.getGroup(args[3]);
										if (group == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-group");
											return false;
										}
										target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
									} else {
										group = guild.getGroup(args[3]);
										if (group == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-group");
											return false;
										}
										target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring(("permission group removeuser " + group.getName() + " ").length()));
									}
									if (group == null || target == null)
										return false;

									group.removeMember(target);
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "member-removed", target.getName(), group.getName()));
									return true;
								}
						), Collections.emptyList())))
		));
	}
}
