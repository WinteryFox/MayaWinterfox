package com.winter.mayawinterfox.data.cache.meta;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.impl.obj.Guild;
import sx.blah.discord.handle.obj.IChannel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public class GuildMeta extends Guild implements Serializable {

	private Set<String> prefixes;
	private Set<Long> autoroles;
	private IChannel welcomeChannel;
	private String language;
	private String welcome;
	private String pm;
	private boolean levelupNotifications;
	private boolean premium;
	private boolean newGuild;
	private boolean permissions;
	private boolean welcomeEnabled;
	private boolean welcomeEmbed;

	public GuildMeta(IShard shard,
	                 String name,
	                 long id,
	                 String icon,
	                 long ownerID,
	                 long afkChannel,
	                 int afkTimeout,
	                 String region,
	                 int verification,
	                 long systemChannelId,
	                 Set<String> prefixes,
	                 Set<Long> autoroles,
	                 IChannel welcomeChannel,
	                 String language,
	                 String welcome,
	                 String pm,
	                 boolean levelupNotifications,
	                 boolean premium,
	                 boolean newGuild,
	                 boolean permissions,
	                 boolean welcomeEnabled,
	                 boolean welcomeEmbed) {
		super(shard, name, id, icon, ownerID, afkChannel, afkTimeout, region, verification, systemChannelId);
		this.prefixes = prefixes;
		this.autoroles = autoroles;
		this.welcomeChannel = welcomeChannel;
		this.language = language;
		this.welcome = welcome;
		this.pm = pm;
		this.levelupNotifications = levelupNotifications;
		this.premium = premium;
		this.newGuild = newGuild;
		this.permissions = permissions;
		this.welcomeEnabled = welcomeEnabled;
		this.welcomeEmbed = welcomeEmbed;
	}

	public Set<String> getPrefixes() {
		if (prefixes.isEmpty()) {
			return Collections.singleton(Main.config.get(Main.ConfigValue.PREFIX));
		} else {
			Set<String> prefix = prefixes;
			prefix.add(Main.config.get(Main.ConfigValue.PREFIX));
			return prefix;
		}
	}

	public void addPrefix(String prefix) {
		if (!Database.set("INSERT IGNORE INTO prefixes (id, prefix) VALUES (?, ?);", id, prefix))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.prefixes.add(prefix);
	}

	public void removePrefix(String prefix) {
		if (this.prefixes.contains(prefix)) {
			if (!Database.set("DELETE FROM prefixes WHERE id=? AND prefix=?", id, prefix))
				throw new UpdateFailedException("Failed to update guild in database.");
			this.prefixes.remove(prefix);
		}
	}

	public Set<Long> getAutoroles() {
		return autoroles;
	}

	public IChannel getWelcomeChannel() {
		return welcomeChannel;
	}

	public void setWelcomeChannel(IChannel channel) {
		if (!Database.set("UPDATE guild SET welcomeChannel=? WHERE id=?;", channel.getLongID(), id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.welcomeChannel = channel;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		if (!Database.set("UPDATE guild SET language=? WHERE id=?;", language, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.language = language;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		if (!Database.set("UPDATE guild SET welcome=? WHERE id=?;", welcome, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.welcome = welcome;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		if (!Database.set("UPDATE guild SET pm=? WHERE id=?;", pm, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.pm = pm;
	}

	public boolean isLevelupNotifications() {
		return levelupNotifications;
	}

	public void setLevelupNotifications(boolean levelupNotifications) {
		if (!Database.set("UPDATE lvlup SET pm=? WHERE id=?;", levelupNotifications, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.levelupNotifications = levelupNotifications;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		if (!Database.set("UPDATE guild SET premium=? WHERE id=?;", premium, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.premium = premium;
	}

	public boolean isNewGuild() {
		return newGuild;
	}

	public void setNewGuild(boolean newGuild) {
		if (!Database.set("UPDATE guild SET newguild=? WHERE id=?;", newGuild, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.newGuild = newGuild;
	}

	public boolean hasCustomPermissions() {
		return permissions;
	}

	public void setCustomPermissions(boolean permissions) {
		if (!Database.set("UPDATE guild SET permissions=? WHERE id=?", permissions, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.permissions = permissions;
	}

	public boolean isWelcomeEnabled() {
		return welcomeEnabled;
	}

	public void toggleWelcomeEnabled() {
		if (!Database.set("UPDATE guild SET welcomeEnabled=? WHERE id=?", !welcomeEnabled, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		welcomeEnabled = !welcomeEnabled;
	}

	public boolean isWelcomeEmbed() {
		return welcomeEmbed;
	}

	public void toggleWelcomeEmbed() {
		if (!Database.set("UPDATE guild SET welcomeEmbed=? WHERE id=?", !welcomeEmbed, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		welcomeEmbed = !welcomeEmbed;
	}
}