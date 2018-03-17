package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.Collections;
import java.util.Random;

public class CommandCookie extends Node<Command> {

    public CommandCookie() {
        super(new Command(
                "cookie",
                "cookie-help",
                PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
                e -> {
                    String[] args = MessageUtil.argsArray(e.getMessage());
                    IUser target;
                    if (args.length == 2)
                        target = ParsingUtil.getUser(MessageUtil.args(e.getMessage()).substring("cookie ".length()));
                    else
                        target = new TargetDialog(e.getChannel(), e.getAuthor()).open();
                    if (target == null)
                        return false;
                    String[] images = {
                            "https://i.imgur.com/2p9y5zv.png",
                            "https://i.imgur.com/R2TkNH2.jpg",
                            "https://i.imgur.com/vecyYPV.jpg",
                            "https://i.imgur.com/0XhuZwt.jpg"
                    };

                    EmbedObject embed = EmbedUtil.imageEmbed(e.getGuild(), images[new Random().nextInt(images.length)]);

                    if (e.getAuthor().equals(target))
                        MessageUtil.sendMessage(e.getChannel(), embed, "cookie-from-yourself", e.getAuthor().getName());
                    else
                        MessageUtil.sendMessage(e.getChannel(), embed, "cookie-from", target.getName(), e.getAuthor().getName());
                    return true;
                }
        ), Collections.emptyList());
    }
}
