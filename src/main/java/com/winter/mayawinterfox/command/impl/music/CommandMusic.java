package com.winter.mayawinterfox.command.impl.music;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.command.Commands;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.data.dialog.impl.InputDialog;
import com.winter.mayawinterfox.data.dialog.impl.TrackDialog;
import com.winter.mayawinterfox.data.http.HTTPHandler;
import com.winter.mayawinterfox.data.music.GuildMusicManager;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.EmbedUtil;
import com.winter.mayawinterfox.util.GuildUtil;
import com.winter.mayawinterfox.util.MessageUtil;
import com.winter.mayawinterfox.util.MusicUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.Permissions;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class CommandMusic extends Node<Command> {

	public CommandMusic() {
		super(new Command(
				"music",
				"music-help",
				PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
				e -> {
					MessageUtil.sendMessage(e.getMessage().getChannel().block(), Commands.getHelp(e.getGuild().block(), "music"));
					return true;
				},
				new HashSet<>(Collections.singletonList("m"))
		), Arrays.asList(
				new Node<>(new Command(
						"join",
						"join-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							MusicUtils.join(e.getMessage().getChannel().block(), e.getMember().get());
							return true;
						},
						new HashSet<>(Collections.singletonList("j"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"leave",
						"leave-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() != null && !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							if (e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild().block()).getChannel() != null)
								e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild().block()).getChannel().leave();
							MusicUtils.getGuildMusicManager(e.getGuild().block()).setBoundChannel(null);
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "left-channel"));
							return true;
						},
						new HashSet<>(Collections.singletonList("l"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"play",
						"play-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getMember().get().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null) {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-voice");
								return false;
							}

							if (e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null)
								MusicUtils.join(e.getMessage().getChannel().block(), e.getMember().get());

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							if (!GuildUtil.hasQueueSpaceLeft(e.getGuild().block(), e.getMember().get())) {
								if (GuildUtil.isPremium(e.getGuild().block(), e.getMember().get()))
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.premiumEmbed(e.getGuild().block(), "no-queue-space"));
								else
									MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-queue-space-premium");
								return false;
							}

							String[] args = MessageUtil.argsArray(e.getMessage());
							String track = null;
							if (args.length <= 1)
								track = new InputDialog(e.getMessage().getChannel().block(), e.getMember().get(), "input-track").open();
							if (args.length > 1) {
								String query = MessageUtil.args(e.getMessage()).substring("music play ".length());
								if (new UrlValidator().isValid(query))
									track = query;
								else {
									try {
										JSONArray choices = HTTPHandler.getYoutube(MessageUtil.args(e.getMessage()).substring("music play ".length()));
										if (choices == null) {
											MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-song-results");
											return false;
										}
										int l = choices.length();
										if (l < 1)
											return false;
										Map<String, String> c = new HashMap<>();
										for (int i = 0; i < Math.max(l, 10); i++) {
											JSONObject o = (JSONObject) choices.get(i);
											try {
												c.put("[" + o.getJSONObject("snippet").getString("title") + "](https://youtube.com/watch?v=" + o.getJSONObject("id").getString("videoId") + ")", o.getJSONObject("id").getString("videoId"));
											} catch (JSONException ignored) {
											}
										}
										track = new TrackDialog(e.getMessage().getChannel().block(), e.getMember().get(), c).open();
									} catch (UnsupportedEncodingException | UnirestException ex) {
										ErrorHandler.log(ex, e.getMessage().getChannel().block());
									}
								}
							}
							if (track == null)
								return false;

							MusicUtils.loadAndPlay(e.getMessage().getChannel().block(), track);
							return true;
						},
						new HashSet<>(Collections.singletonList("p"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"clear",
						"clear-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getMember().get().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null) {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							m.getScheduler().clearQueue();
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "cleared-queue"));
							return true;
						},
						new HashSet<>(Collections.singletonList("c"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"skip",
						"skip-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getMember().get().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null) {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							m.getScheduler().nextTrack();
							MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "skipped-track"));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"song",
						"song-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getMember().get().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null) {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.playEmbed(e.getGuild().block(), m.getPlayer().getPlayingTrack()));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"repeat",
						"repeat-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getMember().get().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null) {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							if (!m.getScheduler().getRepeat()) {
								m.getScheduler().setRepeat(true);
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "repeat-enabled"));
							} else {
								m.getScheduler().setRepeat(false);
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "repeat-disabled"));
							}
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"volume",
						"volume-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getMember().get().getVoiceStateForGuild(e.getGuild().block()).getChannel() == null) {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild().block());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getMessage().getChannel().block()))
								return false;

							int volume = -1;
							try {
								volume = Integer.parseUnsignedInt(MessageUtil.args(e.getMessage()).substring("music volume ".length()));
							} catch (Exception ex) {
								ErrorHandler.log(ex, e.getMessage().getChannel().block());
								return false;
							}

							if (volume >= 0 && volume <= 100) {
								m.getPlayer().setVolume(volume);
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), EmbedUtil.successEmbed(e.getGuild().block(), "volume-changed", volume));
								return true;
							} else {
								MessageUtil.sendMessage(e.getMessage().getChannel().block(), "invalid-volume", volume);
								return false;
							}
						}
				), Collections.emptyList())));
	}
}
