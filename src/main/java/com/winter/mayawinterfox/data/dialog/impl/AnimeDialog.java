package com.winter.mayawinterfox.data.dialog.impl;

import com.winter.mayawinterfox.data.dialog.Dialog;
import com.winter.mayawinterfox.data.dialog.DialogType;
import com.winter.mayawinterfox.util.ColorUtil;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AnimeDialog extends Dialog<JSONObject> {

	public AnimeDialog(IChannel channel, IUser user, Map<String, JSONObject> choices) {
		super(DialogType.CHOICE,
				channel,
				user,
				null,
				"choose-anime",
				new Object[0],
				"choose-anime-desc",
				new Object[0],
				choices,
				ColorUtil.withinTwoHues(0.5F, 0.833333F),
				1L,
				TimeUnit.MINUTES,
				null);
	}
}
