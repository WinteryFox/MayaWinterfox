package com.winter.mayawinterfox.util

import com.winter.mayawinterfox.data.locale.Localisation
import discord4j.core.`object`.entity.Guild
import discord4j.core.spec.EmbedCreateSpec
import java.awt.Color
import java.util.function.Consumer

object Embeds {
	fun botPermissions(guild: Guild, permissions: String?): Consumer<EmbedCreateSpec> {
		return Consumer {
			it.setColor(Color.RED)
			it.setTitle(Localisation.getMessage(guild, "missing-permissions"))
			it.setDescription(Localisation.getMessage(guild, "bot-missing-permissions", permissions))
		}
	}

	fun memberPermissions(guild: Guild, permissions: String?): Consumer<EmbedCreateSpec> {
		return Consumer {
			it.setColor(Color.RED)
			it.setTitle(Localisation.getMessage(guild, "missing-permissions"))
			it.setDescription(Localisation.getMessage(guild, "member-missing-permissions", permissions))
		}
	}
}