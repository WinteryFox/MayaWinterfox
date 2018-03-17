package com.winter.mayawinterfox.command.impl.animal;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.animal.Wolf;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.item.Food;
import com.winter.mayawinterfox.data.item.Item;
import com.winter.mayawinterfox.data.item.ItemProvider;
import com.winter.mayawinterfox.data.schedule.Cooldowns;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Arrays;
import java.util.Collections;

public class CommandWolf extends Node<Command> {

	public CommandWolf() {
		super(new Command(
				"wolf",
				"wolf-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					Wolf wolf = new Wolf(e.getAuthor());
					wolf.update();
					if (wolf.getBackground().getPath().endsWith(".gif"))
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.wolfEmbed(e.getAuthor()), wolf.render(), "wolf.gif");
					else
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.wolfEmbed(e.getAuthor()), wolf.render(), "wolf.png");
					return true;
				}
		), Arrays.asList(
				new Node<>(new Command(
						"feed",
						"wolf-feed-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("wolf feed ".length());
							else
								i = new InputDialog(e.getChannel(), e.getAuthor(), "input-food").open();
							if (i == null)
								return false;

							if (!Cooldowns.onCooldown(e.getAuthor(), "wolf-feed")) {
								Food food = ItemProvider.getFood(i);
								Cooldowns.putOnCooldown(e.getAuthor(), "wolf-feed", food.getCooldown());

								Wolf wolf = new Wolf(e.getAuthor());
								wolf.update();
								wolf.feed(food);
								if (wolf.getBackground().getPath().endsWith(".gif"))
									MessageUtil.sendMessage(e.getChannel(), "feed", EmbedUtil.wolfEmbed(e.getAuthor()), wolf.render(), "wolf.gif", food.getName());
								else
									MessageUtil.sendMessage(e.getChannel(), "feed", EmbedUtil.wolfEmbed(e.getAuthor()), wolf.render(), "wolf.png", food.getName());
							} else {
								MessageUtil.sendMessage(e.getChannel(), "wolf-cooldown", ParsingUtil.formatTime(Cooldowns.getRemaining(e.getAuthor(), "wolf-feed")));
							}
							return true;
						}
				), Collections.emptyList()),
				/*new Node<>(new Command(
						"play",
						"wolf-play-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							Wolf wolf = new Wolf(e.getAuthor());
							wolf.update();
							wolf.play();
							if (wolf.getBackground().getPath().endsWith(".gif"))
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.wolfEmbed(e.getAuthor()), wolf.render(), "wolf.gif");
							else
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.wolfEmbed(e.getAuthor()), wolf.render(), "wolf.png");
							return true;
						}
				), Collections.emptyList()),*/
				new Node<>(new Command(
						"equip",
						"wolf-equip-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("wolf equip ".length());
							else
								i = new InputDialog(e.getChannel(), e.getAuthor(), "input-item").open();
							if (i == null)
								return false;

							Wolf wolf = new Wolf(e.getAuthor());
							wolf.update();
							Item item = wolf.getInventory().getItem(i);
							if (item != null) {
								wolf.equip(item);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "equipped-item", item.getName()));
							}
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"rename",
						"wolf-rename-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							String[] args = MessageUtil.argsArray(e.getMessage());
							String i;
							if (args.length > 1)
								i = MessageUtil.args(e.getMessage()).substring("wolf equip ".length());
							else
								i = new InputDialog(e.getChannel(), e.getAuthor(), "input-item").open();
							if (i == null)
								return false;

							if (i.length() > 24)
								throw new IllegalArgumentException("Name cannot be longer than 24 characters.");

							Wolf wolf = new Wolf(e.getAuthor());
							wolf.setName(i);
							MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "changed-name", i));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"foods",
						"wolf-foods-help",
						PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
						e -> {
							EmbedBuilder builder = new EmbedBuilder()
									.withColor(ColorUtil.withinTwoHues(0.333333f, 0.88888f));
							for (Food food : ItemProvider.foods)
								builder.appendField(food.getName(), "Value: " + food.getValue() + "\nCooldown: " + ParsingUtil.formatTime(food.getCooldown()), true);
							MessageUtil.sendMessage(e.getChannel(), builder.build());
							return true;
						}
				), Collections.emptyList())));
	}
}
