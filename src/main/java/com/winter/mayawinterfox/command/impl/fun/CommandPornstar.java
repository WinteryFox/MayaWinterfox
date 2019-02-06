package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class CommandPornstar extends Node<Command> {

	public CommandPornstar() {
		super(new Command(
				"pornstar",
				"pornstar-help",
				PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
				e -> {
					String[] pre = { "Wild", "Slay", "Young", "Super", "Mega", "Pro", "Waka",  "Freaky", "OT", "Unreal", "Focused", "8-Inch", "Cool", "Big", "Latin", "Spicy", "Extra" ,"Little" ,"Mr.", "Alpha", "", "Lonely", "Ultra", "MC.", "DJ.", "Grip", "Sad", "Cyber", "Black", "12-Inch", "White", "6-Inch", "Crazy" ,"Orgasmic", "3-Inch", "Tender", "Fresh", "Smooth", "Mean", "Dangerous", "Tiny", "Extended", "Amazing","Long", "Wobbly" };
					String[] suf = { "God", "Hang", "Lad", "Penis", "Black", "RattleSnake", "Boy","Willie", "Missle", "Stick", "King", "Wanker", "Cock", "Queen", "BackSeat", "Johnson","Honey","Wiggle", "Ass" ,"Fat","Dick",  "Splash", "Titties", "Chode", "Commando" , "Shaft", "Swang", "Wizard", "Cobra", "Nasty", "Kardashian", "Slop", "Machine", "Cracker", "Sexy", "Slim", "Wet", "Drop", "Dip" };

					MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "pornstar-name", e.getMember().get().getName(), pre[new Random(e.getMember().get().getLongID()).nextInt(pre.length)], suf[new Random(e.getMember().get().getLongID()).nextInt(suf.length)]));
					return true;
				},
				new HashSet<>(Collections.singletonList("ps"))
		), Collections.emptyList());
	}
}