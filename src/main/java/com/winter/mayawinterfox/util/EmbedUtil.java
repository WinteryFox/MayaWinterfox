package com.winter.mayawinterfox.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.animal.Wolf;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.locale.Localisation;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.function.Consumer;

public class EmbedUtil {

	/**
	 * Generate a success embed
	 * @param guild The guild to generate the embed for
	 * @param description The description message key
	 * @param replacements The replacements for the message
	 * @return A success embed
	 */
	public static Consumer<EmbedCreateSpec> successEmbed(Guild guild, String description, Object... replacements) {
		return embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.333333333f, 0.8333333333f))
				.setTitle(Localisation.getMessage(guild, "success"))
				.setDescription(Localisation.getMessage(guild, description, replacements))
				.setTimestamp(Instant.now());
	}

	/**
	 * Generate a kick embed, this does not actually kick the target!
	 * @param guild The guild the user was kicked from
	 * @param target The target user
	 * @param by The user who initiated the kick
	 * @param reason The reason given by the initiator
	 * @return Consumer<EmbedCreateSpec> containing the kick information
	 */
	public static Consumer<EmbedCreateSpec> kickEmbed(Guild guild, Member target, Member by, String reason) {
		Consumer<EmbedCreateSpec> builder = embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.0f, 1.5f / 15.0f))
				.setTitle(Localisation.getMessage(guild, "user-kicked", target.getDisplayName(), target.getDiscriminator()))
				.setThumbnail(ImageUtil.getAvatar(target))
				.addField(Localisation.getMessage(guild, "name"), target.getDisplayName(), true)
				.addField("ID", target.getId().asString(), true)
				.addField(Localisation.getMessage(guild, "by"), by.getDisplayName(), true)
				.addField("ID", by.getId().asString(), true)
				.setTimestamp(Instant.now());
		if (reason != null)
			builder.andThen(embed -> embed.addField(Localisation.getMessage(guild, "reason"), reason, false));
		return builder;
	}

	/**
	 * Generate a ban embed, this does not actually ban the target!
	 * @param guild The guild the user was banned from
	 * @param target The target user
	 * @param by The user who initiated the ban
	 * @param reason The reason given by the initiator
	 * @return Consumer<EmbedCreateSpec> containing the ban information
	 */
	public static Consumer<EmbedCreateSpec> banEmbed(Guild guild, Member target, Member by, String reason) {
		Consumer<EmbedCreateSpec> builder = embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.0f, 1.5f / 15.0f))
				.setTitle(Localisation.getMessage(guild, "user-banned", target.getDisplayName(), target.getDiscriminator()))
				.setThumbnail(ImageUtil.getAvatar(target))
				.addField(Localisation.getMessage(guild, "name"), target.getDisplayName(), true)
				.addField("ID", target.getId().asString(), true)
				.addField(Localisation.getMessage(guild, "by"), by.getDisplayName(), true)
				.addField("ID", by.getId().asString(), true)
				.setTimestamp(Instant.now());
		if (reason != null)
			builder.andThen(embed -> embed.addField(Localisation.getMessage(guild, "reason"), reason, false));
		return builder;
	}

	/**
	 * Generate an image embed
	 * @param guild The guild
	 * @param image The image link
	 * @return Consumer<EmbedCreateSpec> with the image
	 */
	public static Consumer<EmbedCreateSpec> imageEmbed(Guild guild, String image) {
		return embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.333333333f, 0.8333333333f))
				.setDescription("[" + Localisation.getMessage(guild, "source") + "](" + image + ")")
				.setImage(image)
				.setTimestamp(Instant.now());
	}

	/**
	 * Generate an image embed
	 *
	 * @param guild The guild
	 * @param image The image link
	 * @return Consumer<EmbedCreateSpec> with the image
	 */
	public static Consumer<EmbedCreateSpec> imageEmbed(Guild guild, URL image) {
		return embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.333333333f, 0.8333333333f))
				.setDescription("[" + Localisation.getMessage(guild, "source") + "](" + image + ")")
				.setImage(image.toExternalForm())
				.setTimestamp(Instant.now());
	}

	/**
	 * I gotta advertise premium someway :shrug:
	 * @param guild       The guild wew
	 * @param description The description
	 * @return Consumer<EmbedCreateSpec> advertising premium and making me money
	 */
	public static Consumer<EmbedCreateSpec> premiumEmbed(Guild guild, String description) {
		return embed -> embed
				.setColor(Color.ORANGE)
				.setTitle(Localisation.getMessage(guild, "purchase-premium-you-cheap-fuck"))
				.setDescription(Localisation.getMessage(guild, description))
				.setUrl("https://mayawinterfox.com/premium")
				.setThumbnail("https://i.imgur.com/VFM8cBu.png");
	}

	/**
	 * Display the song that is playing
	 * @param guild The guild to display the song for
	 * @param track The track to display
	 * @return Consumer<EmbedCreateSpec> with the track info
	 */
	public static Consumer<EmbedCreateSpec> playEmbed(Guild guild, AudioTrack track) {
		AudioTrackInfo t = track.getInfo();
		StringBuilder time = new StringBuilder("----------");
		time.setCharAt((int) (track.getPosition() / track.getDuration() * (long) 10), 'x');

		return embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.333333f, 0.888888f))
				.setUrl(t.uri)
				.setTitle(Localisation.getMessage(guild, "now-playing", t.title, ParsingUtil.formatTime(t.length)))
				.setDescription(ParsingUtil.formatTime(track.getPosition()) + " **" + time.toString().replace("x", ":radio_button:") + "** " + ParsingUtil.formatTime(t.length));
	}

	public static Consumer<EmbedCreateSpec> welcomeEmbed() {
		return embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.333333f, 0.88888f))
				.setThumbnail(Main.getClient().getSelf().block().getAvatarUrl())
				.setTitle("Hello, I'm Maya!")
				.setDescription("Thank you for inviting me to this cozy server!\nYou can type **.mayahelp** to see a list of commands and type **.mayahelp <command>** e.g. **.mayahelp wolf** to get more information on a specific command.\n\nVisit the [website](https://mayawinterfox.com/) for more information on me and my story, there is also an admin dashboard and a shop on there too!\n\nJoin the [Maya Discord](https://discord.gg/MCUTSZz) server for regular updates on new features and changes or to get any questions answered.\n\nEnjoy using me!");
	}

	public static Consumer<EmbedCreateSpec> levelEmbed(User user) {
		UserMeta u = Caches.getUser(user);
		if (u.getBackground().getPath().endsWith(".gif")) {
			return embed -> embed
					.setImage("attachment://levelup.gif")
					.setColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f));
		} else {
			return embed -> embed
					.setImage("attachment://levelup.png")
					.setColor(ColorUtil.withinTwoHues(0.3333f, 0.88888f));
		}
	}

	public static Consumer<EmbedCreateSpec> wolfEmbed(User user) {
		Wolf wolf = new Wolf(user);
		if (wolf.getBackground().getPath().endsWith(".gif")) {
			return embed -> embed
					.setImage("attachment://wolf.gif")
					.setColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f));
		} else {
			return embed -> embed
					.setImage("attachment://wolf.png")
					.setColor(ColorUtil.withinTwoHues(0.3333f, 0.88888f));
		}
	}

	public static Consumer<EmbedCreateSpec> profileEmbed(User user) {
		UserMeta u = Caches.getUser(user);
		if (u.getBackground().getPath().endsWith(".gif")) {
			return embed -> embed
					.setImage("attachment://profile.gif")
					.setColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f));
		} else {
			return embed -> embed
					.setImage("attachment://profile.png")
					.setColor(ColorUtil.withinTwoHues(0.3333f, 0.88888f));
		}
	}

	public static Consumer<EmbedCreateSpec> animeEmbed(Guild guild, JSONObject anime) {
		return embed -> embed
				.setColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f))
				.setThumbnail(anime.getString("image"))
				.setUrl("https://myanimelist.net/anime/" + anime.getInt("id"))
				.setTitle(anime.getString("title") + " (" + anime.getString("english") + ")")
				.addField(Localisation.getMessage(guild, "score"), String.valueOf(anime.getDouble("score")), false)
				.addField(Localisation.getMessage(guild, "type"), anime.getString("type"), false)
				.addField(Localisation.getMessage(guild, "episodes"), String.valueOf(anime.getInt("episodes")), false)
				.addField(Localisation.getMessage(guild, "status"), anime.getString("status"), false)
				.addField(Localisation.getMessage(guild, "synopsis"), StringUtils.abbreviate(anime.getString("synopsis").replaceAll("<.*?>", "").replaceAll("[.*?]", ""), 1024), false);
	}
}
