package com.winter.mayawinterfox.data.dialog;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.*;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.json.request.EmbedFooterRequest;
import discord4j.rest.json.request.EmbedRequest;
import discord4j.rest.json.request.EmbedThumbnailRequest;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Dialog<T> {
	private DialogType type;
	private TextChannel channel;
	private User user;
	private String thumbnail;
	private String title;
	private Object[] titleReplacements;
	private String description;
	private Object[] descriptionReplacements;
	private Map<String, T> choices = new HashMap<>();
	private Color color = Color.gray;
	private Long timeout = 1L;
	private TimeUnit timeUnit = TimeUnit.MINUTES;
	private Function<String, T> function;

	public Dialog(DialogType type, TextChannel channel, User user, String thumbnail, String title, Object[] titleReplacements, String description, Object[] descriptionReplacements, Map<String, T> choices, Color color, Long timeout, TimeUnit timeUnit, Function<String, T> function) {
		this.type = type;
		this.channel = channel;
		this.user = user;
		this.thumbnail = thumbnail;
		this.title = title;
		this.titleReplacements = titleReplacements;
		this.description = description;
		this.descriptionReplacements = descriptionReplacements;
		this.choices = choices;
		this.color = color;
		this.timeout = timeout;
		this.timeUnit = timeUnit;
		this.function = function;
	}

	private DialogType getType() {
		return type;
	}

	private TextChannel getChannel() {
		return channel;
	}

	private User getUser() {
		return user;
	}

	private String getTitle() {
		return title;
	}

	private Object[] getTitleReplacements() {
		return titleReplacements;
	}

	private String getDescription() {
		return description;
	}

	private Object[] getDescriptionReplacements() {
		return descriptionReplacements;
	}

	private Map<String, T> getChoices() {
		return choices;
	}

	private Color getColor() {
		return color;
	}

	private Long getTimeout() {
		return timeout;
	}

	private TimeUnit getTimeUnit() {
		return timeUnit;
	}

	private Function<String, T> getFunction() {
		return function;
	}

	private String getThumbnail() {
		return thumbnail;
	}

	public T open() throws NullPointerException {
		if (this.getType() == DialogType.CHOICE) {
			StringBuilder list = new StringBuilder();
			int n = 1;
			for (Map.Entry<String, T> entry : choices.entrySet()) {
				list.append("**").append(n).append(".**").append(" ").append(entry.getKey()).append("\n");
				n++;
			}
			Message message = MessageUtil.sendMessage(this.getChannel(), new EmbedCreateSpec()
					.setThumbnail(this.getThumbnail())
					.setTitle(Localisation.getMessage(this.getChannel().getGuild().block(), this.getTitle()))
					.setDescription(Localisation.getMessage(this.getChannel().getGuild().block(), this.getDescription()) + "\n\n" + list.toString())
					.setFooter(Localisation.getMessage(this.getChannel().getGuild().block(), "pick-reaction"), null)
					.setColor(this.getColor())
					.asRequest());
			MessageUtil.addReaction(message, EmojiManager.getForAlias("x"));
			HashMap<String, T> c = new HashMap<>();
			int i = 1;
			for (Map.Entry<String, T> entry : choices.entrySet()) {
				ReactionEmoji emoji = ReactionEmoji.of(EmojiManager.getForAlias(Reactions.valueOf(i).getEmoji()).getUnicode());
				c.put(Reactions.valueOf(i).getEmoji(), entry.getValue());
				MessageUtil.addReaction(message, emoji);
				i++;
			}
			MessageEvent response = null;
			try {
				response = Main.getClient().getDispatcher().waitFor((MessageEvent e) -> {
					if (e instanceof ReactionAddEvent) {
						return ((ReactionAddEvent) e).getUser().equals(user) && e.getChannel().equals(channel);
					} else if (e instanceof MessageReceivedEvent) {
						return e.getAuthor().equals(user) && e.getChannel().equals(channel);
					}
					return false;
				}, timeout, timeUnit);
			} catch (InterruptedException e) {
				ErrorHandler.log(e, this.getChannel());
			}
			MessageUtil.delete(message);
			if (response == null)
				return null;
			if (response instanceof ReactionAddEvent) {
				if (((ReactionAddEvent) response).getReaction().getEmoji().getName().equalsIgnoreCase("x"))
					return null;
				return c.get(EmojiManager.getByUnicode(((ReactionAddEvent) response).getReaction().getEmoji().getName()).getAliases().get(0));
			} else if (response instanceof MessageReceivedEvent) {
				if (response.getMessage().getContent().equalsIgnoreCase("x") || response.getMessage().getContent().equalsIgnoreCase("none") || response.getMessage().getContent().equalsIgnoreCase("quit") || response.getMessage().getContent().equalsIgnoreCase("close"))
					return null;
				int emoji;
				try {
					emoji = Integer.parseUnsignedInt(response.getMessage().getContent());
				} catch (NumberFormatException e) {
					return null;
				}
				return c.get(Reactions.valueOf(emoji).getEmoji());
			}
			return null;
		} else if (this.getType() == DialogType.OPEN) {
			IMessage message = MessageUtil.sendMessage(this.getChannel(), new EmbedBuilder()
					.withThumbnail(this.getThumbnail())
					.withTitle(Localisation.getMessage(this.getChannel().getGuild(), this.getTitle(), this.getTitleReplacements()))
					.withDescription(Localisation.getMessage(this.getChannel().getGuild(), this.getDescription(), this.getDescriptionReplacements()))
					.withFooterText(Localisation.getMessage(this.getChannel().getGuild(), "type-response"))
					.withColor(this.getColor())
					.build());
			IMessage response = null;
			try {
				response = Main.getClient().getDispatcher().waitFor((MessageReceivedEvent e) -> e.getChannel().equals(this.getChannel()) && e.getAuthor().equals(this.getUser()), timeout, timeUnit).getMessage();
			} catch (InterruptedException e) {
				ErrorHandler.log(e, this.getChannel());
			}
			if (response == null)
				throw new NullPointerException("No input received");
			MessageUtil.delete(message);
			String content = response.getContent();
			MessageUtil.delete(response);
			if (response.getContent().equalsIgnoreCase("x") || response.getContent().equalsIgnoreCase("none") || response.getContent().equalsIgnoreCase("quit") || response.getContent().equalsIgnoreCase("close"))
				return null;
			return this.getFunction().apply(content);
		} else
			throw new IllegalStateException("Attempted to open a dialog with an unknown type");
	}
}