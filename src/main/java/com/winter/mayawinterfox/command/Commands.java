package com.winter.mayawinterfox.command;

import com.winter.mayawinterfox.command.impl.admin.*;
import com.winter.mayawinterfox.command.impl.animal.CommandInventory;
import com.winter.mayawinterfox.command.impl.animal.CommandShop;
import com.winter.mayawinterfox.command.impl.animal.CommandWolf;
import com.winter.mayawinterfox.command.impl.developer.CommandEval;
import com.winter.mayawinterfox.command.impl.developer.CommandSet;
import com.winter.mayawinterfox.command.impl.developer.CommandUpdate;
import com.winter.mayawinterfox.command.impl.fun.*;
import com.winter.mayawinterfox.command.impl.image.*;
import com.winter.mayawinterfox.command.impl.misc.CommandInvite;
import com.winter.mayawinterfox.command.impl.misc.CommandLinks;
import com.winter.mayawinterfox.command.impl.music.CommandMusic;
import com.winter.mayawinterfox.command.impl.profile.CommandProfile;
import com.winter.mayawinterfox.command.impl.status.CommandHi;
import com.winter.mayawinterfox.command.impl.status.CommandPing;
import com.winter.mayawinterfox.command.impl.util.CommandHelp;
import com.winter.mayawinterfox.command.impl.util.CommandServer;
import com.winter.mayawinterfox.command.impl.util.CommandUser;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.GuildMeta;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.ColorUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Commands implements IListener<MessageReceivedEvent> {

	public static final List<Node<Command>> COMMANDS = new ArrayList<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(Commands.class);
	public static final Map<Category, List<Node<Command>>> COMMAND_MAP = new EnumMap<>(Category.class);

	static {
		COMMAND_MAP.put(Category.DEV, new ArrayList<>(Arrays.asList(new CommandSet(), new CommandEval(), new CommandUpdate())));
		COMMAND_MAP.put(Category.STATUS, new ArrayList<>(Arrays.asList(new CommandPing(), new CommandHi())));
		COMMAND_MAP.put(Category.FUN, new ArrayList<>(Arrays.asList(new CommandColor(), new CommandCoinFlip(), new CommandEightball(), new CommandHug(), /*new CommandKiss(), new CommandPat(),*/ new CommandCookie(), new CommandKawaii(), new CommandPornstar(), new CommandRate(), new CommandShoot(), new CommandWoop(), new CommandSay(), new CommandUrban(), new CommandSilentSay(), new CommandAnime())));
		COMMAND_MAP.put(Category.ADMIN, new ArrayList<>(Arrays.asList(new CommandKick(), new CommandBan(), new CommandPrefix(), new CommandPurge(), new CommandAssign(), new CommandRemove(), new CommandLanguage(), new CommandPermission())));
		COMMAND_MAP.put(Category.ANIMAL, new ArrayList<>(Arrays.asList(new CommandWolf(), new CommandInventory())));
		COMMAND_MAP.put(Category.PROFILE, new ArrayList<>(Arrays.asList(new CommandProfile())));
		COMMAND_MAP.put(Category.MISC, new ArrayList<>(Arrays.asList(new CommandLinks(), new CommandInvite())));
		COMMAND_MAP.put(Category.UTIL, new ArrayList<>(Arrays.asList(new CommandHelp(), new CommandServer(), new CommandUser(), new CommandShop())));
		COMMAND_MAP.put(Category.MUSIC, Collections.singletonList(new CommandMusic()));
		COMMAND_MAP.put(Category.IMAGE, new ArrayList<>(Arrays.asList(new CommandImgur(), new CommandCat(), new CommandKona(), new CommandDanbooru(), new CommandYandere(), new CommandGelbooru(), new CommandRule34())));

		COMMAND_MAP.values().forEach(COMMANDS::addAll);
	}

	public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	/**
	 * Sends the help
	 *
	 * @param e The event that triggered it
	 * @return True on success, false on failure
	 */
	public static boolean sendHelp(MessageReceivedEvent e) {
		try {
			EmbedBuilder eb = new EmbedBuilder();
			eb.withColor(ColorUtil.withinTwoHues(0.3f, 0.8f));
			eb.withTitle(Localisation.getMessage(e.getGuild(), "command-list"));
			eb.withDescription(Localisation.getMessage(e.getGuild(), "command-list-desc"));
			for (Map.Entry<Category, List<Node<Command>>> c : COMMAND_MAP.entrySet()) {
				StringBuilder desc = new StringBuilder();
				for (Node<Command> n : c.getValue()) {
					desc.append("**").append(n.getData().getName()).append("**, ");
				}
				eb.appendField(WordUtils.capitalize(c.getKey().getName()), desc.toString().substring(0, desc.toString().length() - 2), false);
			}
			MessageUtil.sendMessage(e.getChannel(), eb.build());
			return true;
		} catch (DiscordException de) {
			return false;
		}
	}

	public static Node<Command> getCommand(Node<Command> n, String lookingFor) {
		return n.traverseThis(node -> node.getData().getAliases().stream().map(s -> {
			if (node.getParent() != null) {
				return node.getParent().compileTopDown(Command::getName, (s1, s2) -> s1 + " " + s2) + " " + s;
			} else {
				return s;
			}
		}).collect(Collectors.toSet()), lookingFor, (t, m) -> m.startsWith(t + " "), false);
	}

	private static void getHelp(IGuild guild, EmbedBuilder builder, List<Node<Command>> children) {
		for (Node<Command> command : children) {
			builder.appendField("**.maya" + command.compileTopDown(Command::getName, (a, b) -> a + " " + b) + "**", Localisation.getMessage(guild, command.getData().getHelp()), false);
			if (!command.getChildren().isEmpty())
				getHelp(guild, builder, command.getChildren());
		}
	}

	/**
	 * Get the entire help message for a command, including sub-commands
	 * @param guild      The guild to get the help for
	 * @param lookingFor The command to look for in the command tree
	 * @return The help for the command or null if that command doesn't exist
	 */
	public static EmbedObject getHelp(IGuild guild, String lookingFor) {
		for (Node<Command> n : Commands.COMMANDS) {
			Node<Command> command = getCommand(n, lookingFor + " ");
			if (command != null && command.getData().getName().equalsIgnoreCase(lookingFor)) {
				EmbedBuilder builder = new EmbedBuilder()
						.withColor(ColorUtil.withinTwoHues(0.33333333f, 0.888888888f))
						.withTitle("Help for " + command.getData().getName())
						.withTimestamp(Instant.now());
				builder.appendField("**.maya" + command.getData().getName() + "**", Localisation.getMessage(guild, command.getData().getHelp()), false);
				getHelp(guild, builder, command.getChildren());

				if (command.getData().getAliases().size() > 0)
					builder.appendField(Localisation.getMessage(guild, "aliases"), Arrays.toString(command.getData().getAliases().toArray()).replace("[", "").replace("]", ""), false);
				builder.appendField(Localisation.getMessage(guild, "permission"), getPermission(command), false);
				return builder.build();
			}
		}
		return null;
	}

	public static String getCategory(Node<Command> command) throws IllegalArgumentException {
		while (command.getParent() != null) {
			command = command.getParent();
		}
		for (Map.Entry<Category, List<Node<Command>>> c : COMMAND_MAP.entrySet()) {
			for (Node<Command> value : c.getValue()) {
				if (command.getData().getName().equals(value.getData().getName()))
					return c.getKey().getName().toLowerCase();
			}
		}
		throw new IllegalArgumentException("No such category");
	}

	public static String getPermission(Node<Command> command) {
		return ("maya." + getCategory(command) + "." + command.compileTopDown(Command::getName, (a, b) -> a + "." + b)).toLowerCase();
	}

	public static Set<String> getPermissions() {
		return COMMANDS.stream().map(Commands::getPermission).collect(Collectors.toSet());
	}

	/**
	 * Handles MessageReceivedEvent and processes it
	 *
	 * @param e The event
	 */
	@Override
	public void handle(MessageReceivedEvent e) {
		if (e.getClient().isReady() && !e.getAuthor().isBot()) {
			GuildMeta guild = Caches.getGuild(e.getGuild());
			Optional<String> o = guild.getPrefixes().stream().filter(e.getMessage().getContent()::startsWith).findFirst();
			if (o.isPresent()) {
				try {
					THREAD_POOL.submit(() -> {
						try {
							String lookingFor = Arrays.stream(e.getMessage().getContent().substring(o.get().length()).split("\\s+")).collect(Collectors.joining(" "));
							for (Node<Command> n : COMMANDS) {
								Node<Command> gotten = getCommand(n, lookingFor + " ");
								if (gotten != null) {
									LOGGER.debug(String.format("Found `%s`", gotten.getData().getName()));
									if (Caches.getGuild(e.getGuild()).hasCustomPermissions()) {
										/*String perm = getPermission(gotten);
										if (PermissionChecks.hasPermission(perm).test(e)) {
											e.getChannel().setTypingStatus(true);
											gotten.getData().call(e);
											e.getChannel().setTypingStatus(false);
										} else {
											RequestBuffer.request(() -> e.getMessage().addReaction(ReactionEmoji.of("\uD83D\uDEAB")));
										}*/
									} else {
										if (gotten.getData().getCheck().test(e)) {
											e.getChannel().setTypingStatus(true);
											gotten.getData().call(e);
											e.getChannel().setTypingStatus(false);
										} else {
											RequestBuffer.request(() -> e.getMessage().addReaction(ReactionEmoji.of("\uD83D\uDEAB")));
										}
									}
									break;
								}
							}
						} catch (Exception ex) {
							ErrorHandler.log(ex, e.getChannel());
							e.getChannel().setTypingStatus(false);
						}
					}).get(120, TimeUnit.SECONDS);
				} catch (TimeoutException ex) {
					ErrorHandler.log(ex, "thread-timeout");
					e.getChannel().setTypingStatus(false);
				} catch (InterruptedException ex) {
					ErrorHandler.log(ex, "thread-interrupted");
					e.getChannel().setTypingStatus(false);
				} catch (ExecutionException ex) {
					ErrorHandler.log(ex, "thread-execution");
					e.getChannel().setTypingStatus(false);
				}
			}
		}
	}

	public enum Category {
		ADMIN("admin"),
		FUN("fun"),
		DEV("developer"),
		STATUS("status"),
		MISC("miscellaneous"),
		ANIMAL("animal"),
		PROFILE("profile"),
		UTIL("utility"),
		MUSIC("music"),
		IMAGE("image");

		private final String name;

		Category(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}
}
