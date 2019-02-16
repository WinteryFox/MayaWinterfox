package com.winter.mayawinterfox.data.locale;

import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.exceptions.impl.UpdateFailedException;
import discord4j.core.object.entity.Guild;

import java.util.ResourceBundle;

public class Localisation {

	/**
	 * All of the languages available, some with UTF8Control for out of range characters
	 */
	private static ResourceBundle enLang = ResourceBundle.getBundle("locale.en");
	private static ResourceBundle esLang = ResourceBundle.getBundle("locale.es", new UTF8Control());
	private static ResourceBundle thLang = ResourceBundle.getBundle("locale.th", new UTF8Control());

	/**
	 * Check a guilds language
	 * @param guild The guild to check the language of
	 * @return Returns the 2 letter code for the language
	 */
	private static String checkLanguage(Guild guild) {
		return Caches.getGuild(guild).block().getLanguage();
	}

	/**
	 * Change a guilds language to something else
	 * @param guild The guild to change
	 * @param language The language to change to
	 * @return true on success, false on failure
	 */
	public static boolean changeLanguage(Guild guild, String language) {
		try {
			switch (language) {
				case "en":
					updateGuildLanguage(guild, "en");
					return true;
				case "es":
					updateGuildLanguage(guild, "es");
					return true;
				case "th":
					updateGuildLanguage(guild, "th");
					return true;
				default:
					return false;
			}
		} catch (UpdateFailedException e) {
			return false;
		}
	}

	/**
	 * Update a guilds language
	 * @param guild The guild to update
	 * @param language The language to update to
	 */
	private static void updateGuildLanguage(Guild guild, String language) throws UpdateFailedException {
		Caches.getGuild(guild).block().setLanguage(language).block();
	}

	/**
	 * Get a localised message
	 * @param guild The guild to getGuild the message for
	 * @param messageKey The key of the message to getGuild
	 * @param params The params to replace in the message
	 * @return Returns a localised message
	 */
	public static String getMessage(Guild guild, String messageKey, Object... params) {
		String lang = checkLanguage(guild);
		switch (lang) {
			case "en":
				return enLang.containsKey(messageKey) ? String.format(enLang.getString(messageKey), params) : "Missing key '" + messageKey + "' in translation '" + lang + "'.";
			case "es":
				return esLang.containsKey(messageKey) ? String.format(esLang.getString(messageKey), params) : "Missing key '" + messageKey + "' in translation '" + lang + "'.";
			case "th":
				return thLang.containsKey(messageKey) ? String.format(thLang.getString(messageKey), params) : "Missing key '" + messageKey + "' in translation '" + lang + "'.";
			default:
				return enLang.containsKey(messageKey) ? String.format(enLang.getString(messageKey), params) : "Missing key '" + messageKey + "' in translation '" + lang + "'.";
		}
	}

	/**
	 * Shortcut for getMessage
	 * @param guild The guild to grab the message for
	 * @param messageKey The key of the message to getGuild
	 * @param params The params to replace in the message
	 * @return localised message
	 */
	public static String of(Guild guild, String messageKey, Object... params) {
		return getMessage(guild, messageKey, params);
	}

	/**
	 * If it's not a guild you wish to getGuild the message for, getGuild the default English localisation for the message
	 * @param messageKey The key of the message you wish to getGuild
	 * @return Returns the localised message
	 */
	public static String getDefaultMessage(String messageKey) {
		if(enLang.containsKey(messageKey))
			return enLang.getString(messageKey);
		return null;
	}
}