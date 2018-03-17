package com.winter.mayawinterfox.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.animal.Wolf;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.locale.Localisation;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.net.URL;
import java.time.LocalDateTime;

public class EmbedUtil {

	/**
	 * Generate a success embed
	 * @param guild The guild to generate the embed for
	 * @param description The description message key
	 * @param replacements The replacements for the message
	 * @return A success embed
	 */
	public static EmbedObject successEmbed(IGuild guild, String description, Object... replacements) {
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.333333333f, 0.8333333333f))
				.withTitle(Localisation.getMessage(guild, "success"))
				.withDescription(Localisation.getMessage(guild, description, replacements))
				.withTimestamp(LocalDateTime.now())
				.build();
	}

	/**
	 * Generate a kick embed, this does not actually kick the target!
	 * @param guild The guild the user was kicked from
	 * @param target The target user
	 * @param by The user who initiated the kick
	 * @param reason The reason given by the initiator
	 * @return EmbedObject containing the kick information
	 */
	public static EmbedObject kickEmbed(IGuild guild, IUser target, IUser by, String reason) {
		EmbedBuilder eb = new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.0f, 1.5f / 15.0f))
				.withTitle(Localisation.getMessage(guild, "user-kicked", target.getName(), target.getDiscriminator()))
				.withThumbnail(ImageUtil.getAvatar(target))
				.appendField(Localisation.getMessage(guild, "name"), target.getName(), true)
				.appendField("ID", target.getStringID(), true)
				.appendField(Localisation.getMessage(guild, "by"), by.getName(), true)
				.appendField("ID", by.getStringID(), true)
				.withTimestamp(LocalDateTime.now());
		if (reason != null)
			eb.appendField(Localisation.getMessage(guild, "reason"), reason, false);
		return eb.build();
	}

	/**
	 * Generate a ban embed, this does not actually ban the target!
	 * @param guild The guild the user was banned from
	 * @param target The target user
	 * @param by The user who initiated the ban
	 * @param reason The reason given by the initiator
	 * @return EmbedObject containing the ban information
	 */
	public static EmbedObject banEmbed(IGuild guild, IUser target, IUser by, String reason) {
		EmbedBuilder eb = new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.0f, 1.5f / 15.0f))
				.withTitle(Localisation.getMessage(guild, "user-banned", target.getName(), target.getDiscriminator()))
				.withThumbnail(ImageUtil.getAvatar(target))
				.appendField(Localisation.getMessage(guild, "name"), target.getName(), true)
				.appendField("ID", target.getStringID(), true)
				.appendField(Localisation.getMessage(guild, "by"), by.getName(), true)
				.appendField("ID", by.getStringID(), true)
				.withTimestamp(LocalDateTime.now());
		if (reason != null)
			eb.appendField(Localisation.getMessage(guild, "reason"), reason, false);
		return eb.build();
	}

	/**
	 * Generate an image embed
	 * @param guild The guild
	 * @param image The image link
	 * @return EmbedObject with the image
	 */
	public static EmbedObject imageEmbed(IGuild guild, String image) {
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.333333333f, 0.8333333333f))
				.withDescription("[" + Localisation.getMessage(guild, "source") + "](" + image + ")")
				.withImage(image)
				.withTimestamp(LocalDateTime.now())
				.build();
	}

	/**
	 * Generate an image embed
	 *
	 * @param guild The guild
	 * @param image The image link
	 * @return EmbedObject with the image
	 */
	public static EmbedObject imageEmbed(IGuild guild, URL image) {
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.333333333f, 0.8333333333f))
				.withDescription("[" + Localisation.getMessage(guild, "source") + "](" + image + ")")
				.withImage(image.toExternalForm())
				.withTimestamp(LocalDateTime.now())
				.build();
	}

	/**
	 * I gotta advertise premium someway :shrug:
	 * @param guild       The guild wew
	 * @param description The description
	 * @return EmbedObject advertising premium and making me money
	 */
	public static EmbedObject premiumEmbed(IGuild guild, String description) {
		return new EmbedBuilder()
				.withColor(Color.ORANGE)
				.withTitle(Localisation.getMessage(guild, "purchase-premium-you-cheap-fuck"))
				.withDescription(Localisation.getMessage(guild, description))
				.withUrl("https://mayawinterfox.com/premium")
				.withThumbnail("https://i.imgur.com/VFM8cBu.png")
				.build();
	}

	/**
	 * Display the song that is playing
	 * @param guild The guild to display the song for
	 * @param track The track to display
	 * @return EmbedObject with the track info
	 */
	public static EmbedObject playEmbed(IGuild guild, AudioTrack track) {
		AudioTrackInfo t = track.getInfo();
		StringBuilder time = new StringBuilder("----------");
		time.setCharAt((int) (track.getPosition() / track.getDuration() * (long) 10), 'x');

		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.333333f, 0.888888f))
				.withUrl(t.uri)
				.withTitle(Localisation.getMessage(guild, "now-playing", t.title, ParsingUtil.formatTime(t.length)))
				.appendDescription(ParsingUtil.formatTime(track.getPosition()) + " **" + time.toString().replace("x", ":radio_button:") + "** " + ParsingUtil.formatTime(t.length))
				.build();
	}

	/**
	 * Display the embed that gets shown once upon Maya joining the server
	 * @return EmbedObject with the welcoming message
	 */
	public static EmbedObject mayaEmbed() {
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f))
				.withTitle("Say hello to Maya!")
				.appendField("Maya Winterfox replaces HoroBot", "What does this mean for HoroBot? HoroBot is gone and is no longer a thing, in her place is Maya Winterfox, a new bot written from scratch that has the same commands and functionality as HoroBot did but with improved performance and a dialog system. Maya will be at your service from now on.", false)
				.appendField("Default Prefix Change", "The prefix changed from **.horo** to **.maya** so that means **.horo**ping is now **.maya**ping, the same goes for all other commands. You can set a custom prefix by doing **.mayaprefix add <prefix>**, you can now also set multiple prefixes instead of just 1, you can remove them by doing **.mayaprefix remove <prefix>**.", false)
				.appendField("Functionality Changes", "All commands remain the same however the wolf did see some changes. Your wolf no longer levels up by feeding it x times, instead there is an XP system. XP can be gained through feeding and playing. There are a couple of factors that determine how fast you gain XP, such as happiness, how hungry your wolf is and when the last time you played with your wolf was.", false)
				.appendField("Data", "All data has been wiped, meaning that your level, wolf, items and guild settings have been reset. This is irreversible and universal for everyone. Everyone gets a clean start with Maya.", false)
				.appendField("New Website and New Domain", "There is a new [website](https://mayawinterfox.com/) that will replace the old one, all traffic coming in from https://mayawinterfox.pw/ will now be redirected to https://mayawinterfox.com/ . You can find information about Maya and Coco, her companion, on there and a short story about Maya and Coco's lives and adventures.", false)
				.appendField("Premium", "There is also a new premium service which you can find information on [here](https://mayawinterfox.com/premium). It is a subscription based service that offers a range of things and new premium-only commands and other exclusives. Premium can be bought using gems, a new currency that can only be bought with real world money. Gems can be used to buy premium and wolf items at a much cheaper and faster rate than coins. Visit https://mayawinterfox.com/store if you are interested.", false)
				.appendField("Maya Winterfox, About The Character", "Maya Winterfox and Coco, her companion, are original characters designed by [Ava](https://zizi-arts.deviantart.com/) and [Le Winter-y Fox](https://github.com/WinteryFox/). The [story](https://mayawinterfox.com/story) was written by [Tawink](https://www.patreon.com/tawink).", false)
				.appendField("More Help and Commands", "Type **.mayaahelp** for a list of commands and type **.mayahelp <command>** e.g. **.mayahelp ping** for more information on a command.", false)
				.appendField("Questions?", "Visit the [guild](https://discord.gg/MCUTSZz) to getGuild any questions answered that you may have.", false)
				.build();
	}

	public static EmbedObject welcomeEmbed() {
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.333333f, 0.88888f))
				.withThumbnail(Main.getClient().getOurUser().getAvatarURL())
				.withTitle("Hello, I'm Maya!")
				.withDescription("Thank you for inviting me to this cozy server!\nYou can type **.mayahelp** to see a list of commands and type **.mayahelp <command>** e.g. **.mayahelp wolf** to getGuild more information on a specific command.\n\nVisit the [website](https://mayawinterfox.com/) for more information on me and my story, there is also an admin dashboard and a shop on there too!\n\nJoin the [Maya Discord](https://discord.gg/MCUTSZz) server for regular updates on new features and changes or to getGuild any questions answered.\n\nEnjoy using me!")
				.build();
	}

	public static EmbedObject levelEmbed(IUser user) {
		UserMeta u = Caches.getUser(user);
		if (u.getBackground().getPath().endsWith(".gif")) {
			return new EmbedBuilder()
					.withImage("attachment://levelup.gif")
					.withColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f))
					.build();
		} else {
			return new EmbedBuilder()
					.withImage("attachment://levelup.png")
					.withColor(ColorUtil.withinTwoHues(0.3333f, 0.88888f))
					.build();
		}
	}

	public static EmbedObject wolfEmbed(IUser user) {
		Wolf wolf = new Wolf(user);
		if (wolf.getBackground().getPath().endsWith(".gif")) {
			return new EmbedBuilder()
					.withImage("attachment://wolf.gif")
					.withColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f))
					.build();
		} else {
			return new EmbedBuilder()
					.withImage("attachment://wolf.png")
					.withColor(ColorUtil.withinTwoHues(0.3333f, 0.88888f))
					.build();
		}
	}

	public static EmbedObject profileEmbed(IUser user) {
		UserMeta u = Caches.getUser(user);
		if (u.getBackground().getPath().endsWith(".gif")) {
			return new EmbedBuilder()
					.withImage("attachment://profile.gif")
					.withColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f))
					.build();
		} else {
			return new EmbedBuilder()
					.withImage("attachment://profile.png")
					.withColor(ColorUtil.withinTwoHues(0.3333f, 0.88888f))
					.build();
		}
	}

	public static EmbedObject animeEmbed(IGuild guild, JSONObject anime) {
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.33333f, 0.88888f))
				.withThumbnail(anime.getString("image"))
				.withUrl("https://myanimelist.net/anime/" + anime.getInt("id"))
				.withTitle(anime.getString("title") + " (" + anime.getString("english") + ")")
				.appendField(Localisation.getMessage(guild, "score"), String.valueOf(anime.getDouble("score")), false)
				.appendField(Localisation.getMessage(guild, "type"), anime.getString("type"), false)
				.appendField(Localisation.getMessage(guild, "episodes"), String.valueOf(anime.getInt("episodes")), false)
				.appendField(Localisation.getMessage(guild, "status"), anime.getString("status"), false)
				.appendField(Localisation.getMessage(guild, "synopsis"), StringUtils.abbreviate(anime.getString("synopsis").replaceAll("<.*?>", "").replaceAll("[.*?]", ""), 1024), false)
				.build();
	}
}
