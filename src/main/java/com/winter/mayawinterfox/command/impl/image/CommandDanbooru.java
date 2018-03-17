package com.winter.mayawinterfox.command.impl.image;

import com.tsunderebug.iaatmt.jsonapis.Danbooru;
import com.winter.mayawinterfox.checks.ChannelChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;

import java.net.URI;
import java.util.Collections;

public class CommandDanbooru extends Node<Command> {

	public CommandDanbooru() {
		super(new Command(
				"danbooru",
				"danbooru-help",
				ChannelChecks.isNSFW(),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String[] tags;
					if (args.length <= 1) {
						String temp = new InputDialog(e.getChannel(), e.getAuthor(), "input-tags").open();
						if (temp == null)
							return false;
						tags = MessageUtil.argsArray(e.getGuild(), temp);
					} else {
						tags = new String[args.length - 1];
						System.arraycopy(args, 1, tags, 0, args.length - 1);
					}
					if (tags.length < 1)
						return false;

					URI url = new Danbooru.Builder().withTags(tags).build().randomURL();
					if (url == null) {
						MessageUtil.sendMessage(e.getChannel(), "no-results");
						return false;
					}
					MessageUtil.sendMessage(e.getChannel(), EmbedUtil.imageEmbed(e.getGuild(), url.toASCIIString()));
					return true;
				}
		), Collections.emptyList());
	}
}
