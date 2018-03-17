package com.winter.mayawinterfox.data.cache.meta;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.item.Item;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import sx.blah.discord.api.IShard;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IPresence;

import java.io.Serializable;
import java.sql.Date;

public class UserMeta extends User implements Serializable {

	private String description;
	private int level;
	private int xp;
	private int maxXP;
	private long totalXP;
	private int coins;
	private int gems;
	private Item background;
	private boolean notifications;
	private boolean premium;
	private Date premiumExpiry;

	public UserMeta(IShard shard, String name, long id, String discriminator, String avatar, IPresence presence, boolean isBot, String description, int level, int xp, int maxXP, long totalXP, int coins, int gems, Item background, boolean notifications, boolean premium, Date premiumExpiry) {
		super(shard, name, id, discriminator, avatar, presence, isBot);
		this.description = description;
		this.level = level;
		this.xp = xp;
		this.maxXP = maxXP;
		this.totalXP = totalXP;
		this.coins = coins;
		this.gems = gems;
		this.background = background;
		this.notifications = notifications;
		this.premium = premium;
		this.premiumExpiry = premiumExpiry;
	}

	public String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		if (!Database.set("UPDATE user SET description=? WHERE id=?;", description, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public synchronized void setLevel(int level) {
		if (!Database.set("UPDATE user SET level=? WHERE id=?;", level, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.level = level;
	}

	public int getXP() {
		return xp;
	}

	public synchronized void setXp(int xp) {
		if (!Database.set("UPDATE user SET xp=? WHERE id=?;", xp, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.xp = xp;
	}

	public int getMaxXP() {
		return maxXP;
	}

	public synchronized void setMaxXP(int maxXP) {
		if (!Database.set("UPDATE user SET maxxp=? WHERE id=?;", maxXP, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.maxXP = maxXP;
	}

	public long getTotalXP() {
		return totalXP;
	}

	public synchronized void setTotalXP(long totalXP) {
		if (!Database.set("UPDATE user SET totalxp=? WHERE id=?;", totalXP, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.totalXP = totalXP;
	}

	public int getCoins() {
		return coins;
	}

	public synchronized void setCoins(int coins) {
		if (!Database.set("UPDATE user SET coins=? WHERE id=?;", coins, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.coins = coins;
	}

	public int getGems() {
		return gems;
	}

	public synchronized void setGems(int gems) {
		if (!Database.set("UPDATE user SET gems=? WHERE id=?;", gems, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.gems = gems;
	}

	public Item getBackground() {
		return background;
	}

	public synchronized void setBackground(Item background) {
		if (!Database.set("UPDATE user SET background=? WHERE id=?;", background.getId(), id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.background = background;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public synchronized void setNotifications(boolean notifications) {
		if (!Database.set("UPDATE user SET lvlup=? WHERE id=?;", notifications, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.notifications = notifications;
	}

	public boolean isPremium() {
		return premium;
	}

	public synchronized void setPremium(boolean premium) {
		if (!Database.set("UPDATE user SET premium=? WHERE id=?;", premium, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.premium = premium;
	}

	public Date getPremiumExpiry() {
		return premiumExpiry;
	}

	public synchronized void setPremiumExpiry(Date premiumExpiry) {
		if (!Database.set("UPDATE user SET premiumexpiry=? WHERE id=?;", premiumExpiry, id))
			throw new UpdateFailedException("Failed to update user in database.");
		this.premiumExpiry = premiumExpiry;
	}
}