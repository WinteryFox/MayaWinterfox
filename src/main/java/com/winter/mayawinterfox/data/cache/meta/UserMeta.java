package com.winter.mayawinterfox.data.cache.meta;

import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.item.Item;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import discord4j.core.object.entity.User;

import java.sql.Date;

public class UserMeta {
	private final User user;
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

	public UserMeta(User user, String description, int level, int xp, int maxXP, long totalXP, int coins, int gems, Item background, boolean notifications, boolean premium, Date premiumExpiry) {
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public String getDescription() {
		return description;
	}

	public synchronized void setDescription(String description) {
		if (!Database.set("UPDATE user SET description=? WHERE id=?;", description, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public synchronized void setLevel(int level) {
		if (!Database.set("UPDATE user SET level=? WHERE id=?;", level, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.level = level;
	}

	public int getXP() {
		return xp;
	}

	public synchronized void setXp(int xp) {
		if (!Database.set("UPDATE user SET xp=? WHERE id=?;", xp, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.xp = xp;
	}

	public int getMaxXP() {
		return maxXP;
	}

	public synchronized void setMaxXP(int maxXP) {
		if (!Database.set("UPDATE user SET maxxp=? WHERE id=?;", maxXP, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.maxXP = maxXP;
	}

	public long getTotalXP() {
		return totalXP;
	}

	public synchronized void setTotalXP(long totalXP) {
		if (!Database.set("UPDATE user SET totalxp=? WHERE id=?;", totalXP, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.totalXP = totalXP;
	}

	public int getCoins() {
		return coins;
	}

	public synchronized void setCoins(int coins) {
		if (!Database.set("UPDATE user SET coins=? WHERE id=?;", coins, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.coins = coins;
	}

	public int getGems() {
		return gems;
	}

	public synchronized void setGems(int gems) {
		if (!Database.set("UPDATE user SET gems=? WHERE id=?;", gems, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.gems = gems;
	}

	public Item getBackground() {
		return background;
	}

	public synchronized void setBackground(Item background) {
		if (!Database.set("UPDATE user SET background=? WHERE id=?;", background.getId(), user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.background = background;
	}

	public boolean isNotifications() {
		return notifications;
	}

	public synchronized void setNotifications(boolean notifications) {
		if (!Database.set("UPDATE user SET lvlup=? WHERE id=?;", notifications, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.notifications = notifications;
	}

	public boolean isPremium() {
		return premium;
	}

	public synchronized void setPremium(boolean premium) {
		if (!Database.set("UPDATE user SET premium=? WHERE id=?;", premium, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.premium = premium;
	}

	public Date getPremiumExpiry() {
		return premiumExpiry;
	}

	public synchronized void setPremiumExpiry(Date premiumExpiry) {
		if (!Database.set("UPDATE user SET premiumexpiry=? WHERE id=?;", premiumExpiry, user.getId().asLong()))
			throw new UpdateFailedException("Failed to update user in database.");
		this.premiumExpiry = premiumExpiry;
	}
}