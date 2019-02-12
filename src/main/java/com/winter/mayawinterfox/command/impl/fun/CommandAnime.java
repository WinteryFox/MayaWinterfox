package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.MessageUtil;
import discord4j.core.object.util.Permission;

import java.util.Collections;

public class CommandAnime extends Node<Command> {

	public CommandAnime() {
		super(new Command(
				"anime",
				"anime-help",
				PermissionChecks.hasPermission(Permission.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String tags;
					if (args.length > 1)
						tags = MessageUtil.args(e.getMessage()).substring("anime ".length());
					else
						tags = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-anime").open();

					//JSONArray animes = HTTPHandler.requestAnime(tags);
					//	if (animes == null) {
						MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-results");
						return true;
					//	}
					//	if (animes.length() > 1) {
					//		Map<String, JSONObject> choices = new HashMap<>();
					//		for (int i = 0; i < 3 || i > animes.length(); i++)
					//			choices.put(animes.getJSONObject(i).getString("title") + " (" + animes.getJSONObject(i).getString("english") + ")", animes.getJSONObject(i));
					//		JSONObject anime = new AnimeDialog((TextChannel) e.getMessage().getChannel().block(), e.getMember().get(), choices).open();
					//		if (anime == null)
					//			return true;
					//		MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.animeEmbed(e.getGuild().block(), anime));
					//	} else {
					//		MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.animeEmbed(e.getGuild().block(), animes.getJSONObject(0)));
					//	}
					//	return true;
				}
		), Collections.emptyList());
	}
}
