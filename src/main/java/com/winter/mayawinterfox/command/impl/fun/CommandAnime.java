package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.AnimeDialog;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.http.HTTPHandler;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandAnime extends Node<Command> {

	public CommandAnime() {
		super(new Command(
				"anime",
				"anime-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String tags;
					if (args.length > 1)
						tags = MessageUtil.args(e.getMessage()).substring("anime ".length());
					else
						tags = new InputDialog(e.getChannel(), e.getAuthor(), "input-anime").open();

					JSONArray animes = HTTPHandler.requestAnime(tags);
					if (animes == null) {
						MessageUtil.sendMessage(e.getChannel(), "no-results");
						return true;
					}
					if (animes.length() > 1) {
						Map<String, JSONObject> choices = new HashMap<>();
						for (int i = 0; i < 3 || i > animes.length(); i++)
							choices.put(animes.getJSONObject(i).getString("title") + " (" + animes.getJSONObject(i).getString("english") + ")", animes.getJSONObject(i));
						JSONObject anime = new AnimeDialog(e.getChannel(), e.getAuthor(), choices).open();
						if (anime == null)
							return true;
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.animeEmbed(e.getGuild(), anime));
					} else {
						MessageUtil.sendMessage(e.getChannel(), EmbedUtil.animeEmbed(e.getGuild(), animes.getJSONObject(0)));
					}
					return true;
				}
		), Collections.emptyList());
	}
}
