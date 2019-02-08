package com.winter.mayawinterfox.data.dialog;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.*;
import discord4j.core.object.reaction.ReactionEmoji;
import org.apache.commons.lang3.ObjectUtils;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;
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
			Message message = MessageUtil.sendMessage(this.getChannel(), spec -> spec
					.setThumbnail(this.getThumbnail())
					.setTitle(Localisation.getMessage(this.getChannel().getGuild().block(), this.getTitle()))
					.setDescription(Localisation.getMessage(this.getChannel().getGuild().block(), this.getDescription()) + "\n\n" + list.toString())
					.setFooter(Localisation.getMessage(this.getChannel().getGuild().block(), "pick-reaction"), null)
					.setColor(this.getColor()));
			MessageUtil.addReaction(message, ReactionEmoji.unicode("x"));
			HashMap<String, T> c = new HashMap<>();
			int i = 1;
			for (Map.Entry<String, T> entry : choices.entrySet()) {
				ReactionEmoji emoji = ReactionEmoji.unicode(Reactions.valueOf(i).getEmoji());
				c.put(Reactions.valueOf(i).getEmoji(), entry.getValue());
				MessageUtil.addReaction(message, emoji);
				i++;
			}
			MessageEvent response = null;

			/*response = Main.getClient().getEventDispatcher().on(MessageEvent.class).filter(e -> {
				System.out.println("hello!");
				if (e instanceof ReactionAddEvent) {
					System.out.println("react!");
					return ((ReactionAddEvent) e).getUser().block().equals(user) && ((ReactionAddEvent) e).getMessage().block().getChannel().block().equals(channel);
				} else if (e instanceof MessageCreateEvent) {
					System.out.println("say!");
					return ((MessageCreateEvent) e).getMember().get().equals(user) && ((MessageCreateEvent) e).getMessage().getChannel().block().equals(channel);
				}
				return false;
			}).next().block(Duration.ofMinutes(1));*/
			response = Main.getClient().getEventDispatcher().on(MessageEvent.class)
					.map(e -> {
						if (e instanceof ReactionAddEvent) {
							return e;
						} else if (e instanceof MessageCreateEvent) {
							return e;
						}
						return null;
					})
					.next()
					.timeout(Duration.ofMinutes(1))
					.block();

			MessageUtil.delete(message);
			if (response == null)
				throw new NullPointerException("No input received");

			if (response instanceof ReactionAddEvent) {
				if (((ReactionAddEvent) response).getEmoji().asUnicodeEmoji().get().getRaw().equalsIgnoreCase("x"))
					return null;
				return c.get(ReactionEmoji.unicode(((ReactionAddEvent) response).getEmoji().asUnicodeEmoji().get().getRaw()).getRaw());
			} else if (response instanceof MessageCreateEvent) {
				if (((MessageCreateEvent) response).getMessage().getContent().get().equalsIgnoreCase("x") || ((MessageCreateEvent) response).getMessage().getContent().get().equalsIgnoreCase("none") || ((MessageCreateEvent) response).getMessage().getContent().get().equalsIgnoreCase("quit") || ((MessageCreateEvent) response).getMessage().getContent().get().equalsIgnoreCase("close"))
					return null;
				int emoji;
				try {
					emoji = Integer.parseUnsignedInt(((MessageCreateEvent) response).getMessage().getContent().get());
				} catch (NumberFormatException e) {
					return null;
				}
				return c.get(Reactions.valueOf(emoji).getEmoji());
			}
			return null;
		} else if (this.getType() == DialogType.OPEN) {
			Message message = MessageUtil.sendMessage(this.getChannel(), spec -> spec
					.setThumbnail(this.getThumbnail())
					.setTitle(Localisation.getMessage(this.getChannel().getGuild().block(), this.getTitle(), this.getTitleReplacements()))
					.setDescription(Localisation.getMessage(this.getChannel().getGuild().block(), this.getDescription(), this.getDescriptionReplacements()))
					.setFooter(Localisation.getMessage(this.getChannel().getGuild().block(), "type-response"), "")
					.setColor(this.getColor()));

			Message response = Main.getClient().getEventDispatcher().on(MessageCreateEvent.class)
					.map(MessageCreateEvent::getMessage)
					.filterWhen(m -> m.getChannel().map(c -> c.equals(channel)))
					.filterWhen(m -> m.getAuthor().map(a -> a.equals(user)))
					.next()
					.timeout(Duration.ofMinutes(1), Mono.empty())
					.block();

			MessageUtil.delete(message);
			if (response == null)
				throw new NullPointerException("No input received");

			String content = response.getContent().get();
			MessageUtil.delete(response);
			if (response.getContent().get().equalsIgnoreCase("x") || response.getContent().get().equalsIgnoreCase("none") || response.getContent().get().equalsIgnoreCase("quit") || response.getContent().get().equalsIgnoreCase("close"))
				return null;
			return this.getFunction().apply(content);
		} else
			throw new IllegalStateException("Attempted to open a dialog with an unknown type");
	}
}