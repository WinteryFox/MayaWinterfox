package com.winter.mayawinterfox.util;

import com.madgag.gif.fmsware.GifDecoder;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

	private static String[] defaults = {
			"6debd47ed13483642cf09e832ed0bc1b",
			"322c936a8c8be1b803cd94861bdfa868",
			"dd4dbc0016779df1378e7812eabaa04d",
			"0e291f67c9274a1abdddeb3fd919cbaa",
			"1cbd08c76f8af6dddce02c5138971129"
	};

	/**
	 * Copy an image
	 * @param image The image to copy
	 * @return The copied image
	 */
	public static BufferedImage copyImage(BufferedImage image) {
		ColorModel model = image.getColorModel();
		boolean isAlphaPremultiplied = model.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(model, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Get all gif frames from stream
	 * @param stream The stream to getGuild the frames from
	 * @return A list with all gif frames
	 */
	public static List<GifFrame> getGifFramesFromStream(InputStream stream) {
		GifDecoder decoder = new GifDecoder();
		decoder.read(stream);
		List<GifFrame> frames = new ArrayList<>(decoder.getFrameCount());
		for (int i = 0; i < decoder.getFrameCount(); i++) {
			frames.add(new GifFrame(decoder.getFrame(i), decoder.getDelay(i)));
		}
		return frames;
	}

	public static class GifFrame {
		private BufferedImage image;
		private int delay;

		public GifFrame(BufferedImage im, int del) {
			this.image = im;
			this.delay = del;
		}

		public BufferedImage getImage() {
			return image;
		}

		public int getDelay() {
			return delay;
		}
	}

	/**
	 * Get an image from a URL
	 * @param url The URL to getGuild the image from
	 * @return The image for the given URL
	 * @throws IOException Upon failure to read the image
	 */
	public static BufferedImage imageFromURL(URL url) throws IOException {
		if (url == null)
			return null;
		URLConnection con = url.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla/5.0 HoroBot/2.0");
		con.connect();
		InputStream i = con.getInputStream();
		BufferedImage b = ImageIO.read(i);
		i.close();
		return b;
	}

	/**
	 * Get the avatar for the user, or a default avatar if the user has none
	 * @param user The user to getGuild the avatar for
	 * @return The avatar for the user
	 */
	public static String getAvatar(User user) {
		return user.getAvatarUrl();
	}

	/**
	 * Get the average image color
	 * @param i The image to average the color for
	 * @return The average RGB color
	 */
	public static int averageColor(BufferedImage i) {
		int[] pxs = i.getRaster().getPixels(0, 0, i.getWidth(), i.getHeight(), (int[]) null);
		int r = 0;
		int g = 0;
		int b = 0;
		for(int j = 0; j < pxs.length; j += 3) {
			r += pxs[j];
			g += pxs[j + 1];
			b += pxs[j + 2];
		}
		return new Color(r / (pxs.length / 3), g / (pxs.length / 3), b / (pxs.length / 3)).getRGB();
	}

}