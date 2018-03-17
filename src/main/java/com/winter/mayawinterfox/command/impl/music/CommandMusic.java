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
					MessageUtil.sendMessage(e.getChannel(), Commands.getHelp(e.getGuild(), "music"));
					return true;
				},
				new HashSet<>(Collections.singletonList("m"))
		), Arrays.asList(
				new Node<>(new Command(
						"join",
						"join-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							MusicUtils.join(e.getChannel(), e.getAuthor());
							return true;
						},
						new HashSet<>(Collections.singletonList("j"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"leave",
						"leave-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() != null && !m.getBoundChannel().equals(e.getChannel()))
								return false;

							if (e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild()).getChannel() != null)
								e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild()).getChannel().leave();
							MusicUtils.getGuildMusicManager(e.getGuild()).setBoundChannel(null);
							MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "left-channel"));
							return true;
						},
						new HashSet<>(Collections.singletonList("l"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"play",
						"play-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel() == null) {
								MessageUtil.sendMessage(e.getChannel(), "no-voice");
								return false;
							}

							if (e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild()).getChannel() == null)
								MusicUtils.join(e.getChannel(), e.getAuthor());

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getChannel()))
								return false;

							if (!GuildUtil.hasQueueSpaceLeft(e.getGuild(), e.getAuthor())) {
								if (!GuildUtil.isPremium(e.getGuild(), e.getAuthor()))
									MessageUtil.sendMessage(e.getChannel(), EmbedUtil.premiumEmbed(e.getGuild(), "no-queue-space"));
								else
									MessageUtil.sendMessage(e.getChannel(), "no-queue-space-premium");
								return false;
							}

							String[] args = MessageUtil.argsArray(e.getMessage());
							String track = null;
							if (args.length <= 1)
								track = new InputDialog(e.getChannel(), e.getAuthor(), "input-track").open();
							if (args.length > 1) {
								String query = MessageUtil.args(e.getMessage()).substring("music play ".length());
								if (new UrlValidator().isValid(query))
									track = query;
								else {
									try {
										JSONArray choices = HTTPHandler.getYoutube(MessageUtil.args(e.getMessage()).substring("music play ".length()));
										if (choices == null) {
											MessageUtil.sendMessage(e.getChannel(), "no-song-results");
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
										track = new TrackDialog(e.getChannel(), e.getAuthor(), c).open();
									} catch (UnsupportedEncodingException | UnirestException ex) {
										ErrorHandler.log(ex, e.getChannel());
									}
								}
							}
							if (track == null)
								return false;

							MusicUtils.loadAndPlay(e.getChannel(), track);
							return true;
						},
						new HashSet<>(Collections.singletonList("p"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"clear",
						"clear-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel() == null) {
								MessageUtil.sendMessage(e.getChannel(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getChannel()))
								return false;

							m.getScheduler().clearQueue();
							MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "cleared-queue"));
							return true;
						},
						new HashSet<>(Collections.singletonList("c"))
				), Collections.emptyList()),
				new Node<>(new Command(
						"skip",
						"skip-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel() == null) {
								MessageUtil.sendMessage(e.getChannel(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getChannel()))
								return false;

							m.getScheduler().nextTrack();
							MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "skipped-track"));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"song",
						"song-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel() == null) {
								MessageUtil.sendMessage(e.getChannel(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getChannel()))
								return false;

							MessageUtil.sendMessage(e.getChannel(), EmbedUtil.playEmbed(e.getGuild(), m.getPlayer().getPlayingTrack()));
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"repeat",
						"repeat-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel() == null) {
								MessageUtil.sendMessage(e.getChannel(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getChannel()))
								return false;

							if (!m.getScheduler().getRepeat()) {
								m.getScheduler().setRepeat(true);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "repeat-enabled"));
							} else {
								m.getScheduler().setRepeat(false);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "repeat-disabled"));
							}
							return true;
						}
				), Collections.emptyList()),
				new Node<>(new Command(
						"volume",
						"volume-help",
						PermissionChecks.hasPermission(Permissions.VOICE_CONNECT),
						e -> {
							if (e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel() == null) {
								MessageUtil.sendMessage(e.getChannel(), "no-voice");
								return false;
							}

							GuildMusicManager m = MusicUtils.getGuildMusicManager(e.getGuild());
							if (m.getBoundChannel() == null || !m.getBoundChannel().equals(e.getChannel()))
								return false;

							int volume = -1;
							try {
								volume = Integer.parseUnsignedInt(MessageUtil.args(e.getMessage()).substring("music volume ".length()));
							} catch (Exception ex) {
								ErrorHandler.log(ex, e.getChannel());
								return false;
							}

							if (volume >= 0 && volume <= 100) {
								m.getPlayer().setVolume(volume);
								MessageUtil.sendMessage(e.getChannel(), EmbedUtil.successEmbed(e.getGuild(), "volume-changed", volume));
								return true;
							} else {
								MessageUtil.sendMessage(e.getChannel(), "invalid-volume", volume);
								return false;
							}
						}
				), Collections.emptyList())));
	}
}
