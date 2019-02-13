package com.winter.mayawinterfox.command.impl.image;

//import com.tsunderebug.iaatmt.jsonapis.Rule34;
import com.winter.mayawinterfox.checks.ChannelChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;

import java.net.URI;
import java.util.Collections;

public class CommandRule34 extends Node<Command> {

	public CommandRule34() {
		super(new Command(
				"rule34",
				"rule34-help",
				ChannelChecks.isNSFW(),
				e -> {
					String[] args = MessageUtil.argsArray(e.getMessage());
					String[] tags;
					if (args.length <= 1) {
						String temp = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-tags").open();
						if (temp == null)
							return false;
						tags = MessageUtil.argsArray(e.getGuild().block(), temp);
					} else {
						tags = new String[args.length - 1];
						System.arraycopy(args, 1, tags, 0, args.length - 1);
					}
					if (tags.length < 1)
						return false;

					//URI url = new Rule34.Builder().withTags(tags).build().randomURL();
					//if (url == null) {
					//	MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-results");
					//	return false;
					//}
					//MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.imageEmbed(e.getGuild().block(), url.toASCIIString()));
					return true;
				}
		), Collections.emptyList());
	}
}
