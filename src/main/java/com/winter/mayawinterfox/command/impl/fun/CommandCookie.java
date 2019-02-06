package com.winter.mayawinterfox.command.impl.fun;

import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.TargetDialog;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.ParsingUtil;
import sx.blah.discord.api.internal.json.objects.Consumer<EmbedCreateSpec>;
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
                        target = new TargetDialog(e.getMessage().getChannel().block(), e.getMember().get()).open();
                    if (target == null)
                        return false;
                    String[] images = {
                            "https://i.imgur.com/2p9y5zv.png",
                            "https://i.imgur.com/R2TkNH2.jpg",
                            "https://i.imgur.com/vecyYPV.jpg",
                            "https://i.imgur.com/0XhuZwt.jpg"
                    };

                    Consumer<EmbedCreateSpec> embed = EmbedUtil.imageEmbed(e.getGuild().block(), images[new Random().nextInt(images.length)]);

                    if (e.getMember().get().equals(target))
                        MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed, "cookie-from-yourself", e.getMember().get().getName());
                    else
                        MessageUtil.sendMessage(e.getMessage().getChannel().block(), embed, "cookie-from", target.getName(), e.getMember().get().getName());
                    return true;
                }
        ), Collections.emptyList());
    }
}
