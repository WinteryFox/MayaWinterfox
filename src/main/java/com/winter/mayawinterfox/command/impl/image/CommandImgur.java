package com.winter.mayawinterfox.command.impl.image;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.utils.Base64Coder;
import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.Permissions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class CommandImgur extends Node<Command> {
    public CommandImgur() {
        super(new Command(
                "imgur",
                "imgur-help",
                PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
                e -> {
                    MessageUtil.sendMessage(e.getChannel(), Commands.getHelp(e.getGuild(), "imgur"));
                    return true;
                }
        ), Arrays.asList(
                new Node<>(new Command(
                        "search",
                        "imgur-search-help",
                        PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
                        e -> {
                            String[] args = MessageUtil.argsArray(e.getMessage());
                            String tags;
                            if (args.length > 2)
                                tags = MessageUtil.args(e.getMessage()).substring("imgur search ".length());
                            else
                                tags = new InputDialog(e.getChannel(), e.getAuthor(), "input-tags").open();
                            if (tags == null)
                                return false;
                            try {
                                tags = tags.replaceAll("(/)|(\\\\)", "");
                                tags = URLEncoder.encode(tags, "US-ASCII");
                            } catch (UnsupportedEncodingException ex) {
                                ErrorHandler.log(ex, e.getChannel());
                                return false;
                            }
                            Unirest.get("https://api.imgur.com/3/gallery/search?q=" + tags)
                                    .header("authorization", "Client-ID " + Main.config.get(Main.ConfigValue.IMGUR_CID))
                                    .asJsonAsync(new Callback<JsonNode>() {
                                        @Override
                                        public void completed(HttpResponse<JsonNode> httpResponse) {
                                            int statusCode = httpResponse.getStatus();
                                            if (statusCode != 200 || !httpResponse.getBody().getObject().getBoolean("success")) {
                                                ErrorHandler.log(new Exception(httpResponse.getBody().getObject().getJSONObject("data").getString("error")), e.getChannel());
                                                return;
                                            }
                                            JSONArray dataArr = httpResponse.getBody().getObject().getJSONArray("data");
                                            if (dataArr.length() == 0) {
                                                MessageUtil.sendMessage(e.getChannel(), "imgur-no-results");
                                                return;
                                            }
                                            JSONObject randomImage = dataArr.getJSONObject(new Random().nextInt(dataArr.length() - 1));
                                            JSONArray images = randomImage.getJSONArray("images");
                                            String link;
                                            if (images.length() > 1)
                                                link = randomImage.getString("link");
                                            else
                                                link = images.getJSONObject(0).getString("link");
                                            EmbedObject embed = EmbedUtil.imageEmbed(e.getGuild(), link);
                                            MessageUtil.sendMessage(e.getChannel(), embed);
                                        }

                                        @Override
                                        public void failed(UnirestException ex) {
                                            ErrorHandler.log(ex, e.getChannel());
                                        }

                                        @Override
                                        public void cancelled() {}
                                    });
                            return true;
                        }
                ), Collections.emptyList()),
                new Node<>(new Command(
                        "upload",
                        "imgur-upload-help",
                        PermissionChecks.hasPermission(Permissions.SEND_MESSAGES),
                        e -> {
                            String[] args = MessageUtil.argsArray(e.getMessage());
                            String temp;
                            if (args.length > 2)
                                temp = args[2];
                            else
                                temp = new InputDialog(e.getChannel(), e.getAuthor(), "input-imgur-upload").open();
                            if (temp == null)
                                return false;
                            String toUpload = temp;
                            if (!temp.matches("(?i)^https?://.*$"))
                                try {
                                    Base64Coder.decode(temp);
                                } catch (Exception ex) {
                                    ErrorHandler.log(ex, e.getChannel());
                                    return false;
                                }
                            Unirest.post("https://api.imgur.com/3/image")
                                    .header("authorization", "Client-ID " + Main.config.get(Main.ConfigValue.IMGUR_CID))
                                    .body(toUpload)
                                    .asJsonAsync(new Callback<JsonNode>() {
                                        @Override
                                        public void completed(HttpResponse<JsonNode> httpResponse) {
                                            int statusCode = httpResponse.getStatus();
                                            if (statusCode != 200 || !httpResponse.getBody().getObject().getBoolean("success")) {
                                                ErrorHandler.log(new Exception(httpResponse.getBody().getObject().getJSONObject("data").getString("error")), e.getChannel());
                                                return;
                                            }
                                            String link = httpResponse.getBody().getObject().getJSONObject("data").getString("link");
                                            EmbedObject embed = EmbedUtil.imageEmbed(e.getGuild(), link);
                                            MessageUtil.sendMessage(e.getChannel(), embed, "imgur-uploaded", e.getAuthor().getName());
                                        }

                                        @Override
                                        public void failed(UnirestException ex) {
                                            ErrorHandler.log(ex, e.getChannel());
                                        }

                                        @Override
                                        public void cancelled() {}
                                    });
                            return true;
                        }
                ), Collections.emptyList())
        ));
    }
}
