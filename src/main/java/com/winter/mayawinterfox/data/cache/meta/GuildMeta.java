package com.winter.mayawinterfox.data.cache.meta;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.impl.obj.Guild;

import java.io.Serializable;
import java.util.*;

public class GuildMeta extends Guild implements Serializable {

	private Set<String> prefixes;
	private Set<Long> autoroles;
	private String language;
	private String welcome;
	private String pm;
	private boolean levelupNotifications;
	private boolean premium;
	private boolean newGuild;
	private boolean permissions;

	public GuildMeta(IShard shard, String name, long id, String icon, long ownerID, long afkChannel, int afkTimeout, String region, int verification, Set<String> prefixes, Set<Long> autoroles, String language, String welcome, String pm, boolean levelupNotifications, boolean premium, boolean newGuild, boolean permissions) {
		super(shard, name, id, icon, ownerID, afkChannel, afkTimeout, region, verification);
		this.prefixes = prefixes;
		this.autoroles = autoroles;
		this.language = language;
		this.welcome = welcome;
		this.pm = pm;
		this.levelupNotifications = levelupNotifications;
		this.premium = premium;
		this.newGuild = newGuild;
		this.permissions = permissions;
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

	public synchronized void removePrefix(String prefix) {
		if (this.prefixes.contains(prefix)) {
			if (!Database.set("DELETE FROM prefixes WHERE id=? AND prefix=?", id, prefix))
				throw new UpdateFailedException("Failed to update guild in database.");
			this.prefixes.remove(prefix);
		}
	}

	public Set<Long> getAutoroles() {
		return autoroles;
	}

	public String getLanguage() {
		return language;
	}

	public synchronized void setLanguage(String language) {
		if (!Database.set("UPDATE guild SET language=? WHERE id=?;", language, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.language = language;
	}

	public String getWelcome() {
		return welcome;
	}

	public synchronized void setWelcome(String welcome) {
		if (!Database.set("UPDATE guild SET welcome=? WHERE id=?;", welcome, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.welcome = welcome;
	}

	public String getPm() {
		return pm;
	}

	public synchronized void setPm(String pm) {
		if (!Database.set("UPDATE guild SET pm=? WHERE id=?;", pm, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.pm = pm;
	}

	public boolean isLevelupNotifications() {
		return levelupNotifications;
	}

	public synchronized void setLevelupNotifications(boolean levelupNotifications) {
		if (!Database.set("UPDATE lvlup SET pm=? WHERE id=?;", levelupNotifications, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.levelupNotifications = levelupNotifications;
	}

	public boolean isPremium() {
		return premium;
	}

	public synchronized void setPremium(boolean premium) {
		if (!Database.set("UPDATE guild SET premium=? WHERE id=?;", premium, id))
			throw new UpdateFailedException("Failed to update guild in database.");
		this.premium = premium;
	}

	public boolean isNewGuild() {
		return newGuild;
	}

	public synchronized void setNewGuild(boolean newGuild) {
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
}