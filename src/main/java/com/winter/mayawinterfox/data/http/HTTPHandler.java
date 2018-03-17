package com.winter.mayawinterfox.data.http;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.data.locale.Localisation;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.ColorUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HTTPHandler {

	/**
	 * Get a random cat image~ Nyan~!
	 *
	 * @return URL to the cat image
	 * @throws UnirestException      Upon failure of the getGuild request
	 * @throws MalformedURLException When the URL is invalid
	 */
	public static URL requestCat() throws UnirestException, MalformedURLException {
		return new URL(Unirest.get("http://www.random.cat/meow").asJson().getBody().getObject().getString("file"));
	}

	/**
	 * Post the stats of the bot to all bot listing sites
	 *
	 * @param shard The shard to post stats for
	 */
	public static void postStats(int shard) {
		JSONObject o = new JSONObject();
		o.put("server_count", Main.getClient().getGuilds().size());
		o.put("shard_id", shard);
		o.put("shard_count", Main.getClient().getShardCount());

		try {
			Unirest.post("https://bots.discord.pw/api/bots/289381714885869568/stats")
					.header("Authorization", Main.config.get(Main.ConfigValue.BOTS_PW_TOKEN))
					.header("Content-Type", "application/json")
					.body(o.toString())
					.asJson();
		} catch (UnirestException e) {
			ErrorHandler.log(e, "Something went wrong while posting stats to bots.discord.pw!");
		}
		try {
			Unirest.post("https://discordbots.org/api/bots/289381714885869568/stats")
					.header("Authorization", Main.config.get(Main.ConfigValue.BOTS_ORG_TOKEN))
					.header("Content-Type", "application/json")
					.body(o.toString())
					.asJson();
		} catch (UnirestException e) {
			ErrorHandler.log(e, "Something went wrong while posting stats to discordbots.org!");
		}
	}

	/**
	 * Search youtube with a query
	 *
	 * @param query The query to search youtube for
	 * @return JSONArray containing a list of results to choose from
	 * @throws UnirestException             Upon getGuild request failure
	 * @throws UnsupportedEncodingException Upon failing to encode the query
	 */
	public static JSONArray getYoutube(String query) throws UnirestException, UnsupportedEncodingException {
		HttpResponse<JsonNode> response = Unirest.get("https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + URLEncoder.encode(query, "UTF-8") + "&maxResults=10&key=" + Main.config.get(Main.ConfigValue.YOUTUBE_KEY))
				.header("Accept", "text/plain")
				.asJson();
		return response.getStatus() == 200 ? response.getBody().getObject().getJSONArray("items") : null;
	}

	/**
	 * Request a definition from urban dictionary
	 *
	 * @param guild The guild to getGuild the definition from
	 * @param query The query to search for
	 * @return The definition in an embed
	 * @throws UnirestException             Upon failure of the getGuild request
	 * @throws UnsupportedEncodingException When there's an unknown encoding type
	 */
	public static EmbedObject requestUrban(IGuild guild, String query) throws UnirestException, UnsupportedEncodingException {
		HttpResponse<JsonNode> response = Unirest.get("https://mashape-community-urban-dictionary.p.mashape.com/define?term=" + URLEncoder.encode(query, "UTF-8"))
				.header("X-Mashape-Key", Main.config.get(Main.ConfigValue.MASHAPE_KEY))
				.header("Accept", "text/plain")
				.asJson();
		if (response.getBody().getObject().getString("result_type").equalsIgnoreCase("no_results"))
			throw new IllegalArgumentException("No results");

		JSONObject o = response.getBody().getObject().getJSONArray("list").getJSONObject(0);
		String d = o.getString("definition");
		String e = o.getString("example");
		return new EmbedBuilder()
				.withColor(ColorUtil.withinTwoHues(0.3333333f, 0.88888888f))
				.withTitle("Source")
				.withUrl(o.getString("permalink"))
				.appendField(Localisation.getMessage(guild, "word"), "[" + query + "](" + o.getString("permalink") + ")", false)
				.appendField(Localisation.getMessage(guild, "rating"), ":thumbsup: " + o.getInt("thumbs_up") + "       :thumbsdown: " + o.getInt("thumbs_down"), false)
				.appendField(Localisation.getMessage(guild, "definition"), StringUtils.abbreviate(d, Math.max(d.length(), 1024)), false)
				.appendField(Localisation.getMessage(guild, "example"), StringUtils.abbreviate(e, Math.max(e.length(), 1024)), false)
				.build();
	}

	public static JSONArray requestAnime(String query) {
		try {
			HttpResponse<String> response = Unirest.get("https://myanimelist.net/api/anime/search.json?q=" + URLEncoder.encode(query, "UTF-8")).basicAuth("HoroBot1", "HoloBot123").asString();
			if (response.getStatus() == 200) {
				JSONObject o = XML.toJSONObject(response.getBody()).getJSONObject("anime");
				return o.getJSONArray("entry");
			}
		} catch (UnirestException | UnsupportedEncodingException e) {
			ErrorHandler.log(e, "getting-anime");
		}
		return null;
	}
}
