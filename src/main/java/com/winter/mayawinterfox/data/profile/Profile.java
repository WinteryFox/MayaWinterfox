package com.winter.mayawinterfox.data.profile;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.winter.mayawinterfox.data.animal.Wolf;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.ImageUtil;
import discord4j.core.object.entity.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Profile {

	private UserMeta user;

	public Profile(User user) {
		this.user = Caches.getUser(user).block();
	}

	public Profile(UserMeta user) {
		this.user = user;
	}

	public UserMeta getUser() {
		return user;
	}

	public InputStream render() {
		final int w = 300;
		final int h = 300;

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		if (user.getBackground().getPath().endsWith(".gif")) {
			o = drawProfileGif(o, w, h);
		} else {
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			BufferedImage template = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			BufferedImage background = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Image avatar = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			try (InputStream back = Wolf.class.getResourceAsStream(user.getBackground().getPath());
			     InputStream temp = Wolf.class.getResourceAsStream("/profile/template.png")) {
				background = ImageIO.read(back);
				template = ImageIO.read(temp);
				avatar = ImageUtil.imageFromURL(new URL(ImageUtil.getAvatar(user.getUser()).replace(".webp", ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}

			BufferedImage avatarRender = new BufferedImage(75, 75, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = avatarRender.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2.setComposite(AlphaComposite.Src);
			g2.setColor(Color.WHITE);
			g2.fill(new Ellipse2D.Double(0, 0, 75, 75));
			g2.setComposite(AlphaComposite.SrcIn);
			g2.drawImage(avatar, 0, 0, 75, 75, null);
			g2.dispose();

			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g.drawImage(background, 0, 0, w, h, null);
			g.drawImage(template, 0, 0, w, h, null);

			g.drawImage(avatarRender, 33, 30, 75, 75, null);

			g.setFont(new Font("Roboto Black", Font.BOLD, 20));
			g.setColor(new Color(186, 187, 189));
			g.drawString(user.getUser().getUsername(), 193 - (int) g.getFontMetrics().getStringBounds(user.getUser().getUsername(), g).getWidth() / 2, 50);

			g.setColor(new Color(96, 115, 145));
			g.fillRoundRect(122, 62, (int) (((float) user.getXP() / (float) user.getMaxXP()) * (float) 146), 18, 10, 10);

			g.setFont(new Font("Roboto Bold", Font.BOLD, 14));
			g.setColor(new Color(186, 187, 189));
			g.drawString(user.getXP() + " / " + user.getMaxXP(), 230 - (int) g.getFontMetrics().getStringBounds(user.getXP() + " / " + user.getMaxXP(), g).getWidth(), 76);

			g.setFont(new Font("Roboto Medium", Font.PLAIN, 18));
			g.drawString("Level " + user.getLevel(), 230 - (int) g.getFontMetrics().getStringBounds("Level " + user.getLevel(), g).getWidth(), 100);

			g.drawString("$" + user.getCoins(), 63, 165);

			try {
				//g.drawString("#" + (long) Database.get("SELECT RANK() OVER (PARTITION BY level ORDER BY level DESC) AS rank FROM user ORDER BY rank DESC;").get(0).get("rank"), 63, 213);
				// TODO
			} catch (Exception ex) {
				ErrorHandler.log(ex, "rank-getting");
			}

			g.drawString(String.valueOf(user.getTotalXP()), 63, 261);

			g.setFont(new Font("Roboto Light", Font.PLAIN, 12));
			String info[] = user.getDescription().split("(?<=\\G.{28})");
			int y = 160 - g.getFontMetrics().getHeight();
			for (String line : info)
				g.drawString(line, 135, y += g.getFontMetrics().getHeight());

			Wolf wolf = new Wolf(user.getUser());
			wolf.drawAnimal(g, 150, 185, 180, 120);

			g.drawString(wolf.getXP() + " / " + wolf.getMaxXP() + " XP", 138, 240);

			g.dispose();
			try {
				ImageIO.write(image, "png", o);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ByteArrayInputStream(o.toByteArray());
	}

	public InputStream renderIcon() {
		return null;
	}

	/**
	 * Draw the entire profile with a gif item
	 *
	 * @param o The OutputStream to write the image to
	 * @return ByteArrayOutputStream containing the profile image
	 */
	private ByteArrayOutputStream drawProfileGif(ByteArrayOutputStream o, int w, int h) {
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		encoder.start(o);
		encoder.setRepeat(0);
		encoder.setQuality(1);

		List<ImageUtil.GifFrame> frames = new ArrayList<>();
		BufferedImage avatar = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		BufferedImage template = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		try (InputStream stream = Wolf.class.getResourceAsStream(user.getBackground().getPath());
		     InputStream profileStream = Wolf.class.getResourceAsStream("/profile/template.png")) {
			template = ImageIO.read(profileStream);
			frames = ImageUtil.getGifFramesFromStream(stream);
			avatar = ImageUtil.imageFromURL(new URL(ImageUtil.getAvatar(user.getUser()).replace(".webp", ".png")));
		} catch (IOException ex) {
			ErrorHandler.log(ex, "Failed to read background stream");
		}
		BufferedImage avatarRender = new BufferedImage(75, 75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = avatarRender.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.setComposite(AlphaComposite.Src);
		g2.setColor(Color.WHITE);
		g2.fill(new Ellipse2D.Double(0, 0, 75, 75));
		g2.setComposite(AlphaComposite.SrcIn);
		g2.drawImage(avatar, 0, 0, 75, 75, null);
		g2.dispose();
		for (ImageUtil.GifFrame frame1 : frames) {
			Image frame = frame1.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
			BufferedImage bgFrame = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bgFrame.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g.drawImage(frame, 0, 0, null);
			g.drawImage(template, 0, 0, null);
			g.drawImage(avatarRender, 33, 30, null);

			g.setFont(new Font("Roboto Black", Font.BOLD, 20));
			g.setColor(new Color(186, 187, 189));
			g.drawString(user.getUser().getUsername(), 193 - (int) g.getFontMetrics().getStringBounds(user.getUser().getUsername(), g).getWidth() / 2, 50);

			g.setColor(new Color(96, 115, 145));
			g.fillRoundRect(122, 62, (int) (((float) user.getXP() / (float) user.getMaxXP()) * (float) 146), 18, 10, 10);

			g.setFont(new Font("Roboto Bold", Font.BOLD, 14));
			g.setColor(new Color(186, 187, 189));
			g.drawString(user.getXP() + " / " + user.getMaxXP(), 230 - (int) g.getFontMetrics().getStringBounds(user.getXP() + " / " + user.getMaxXP(), g).getWidth(), 76);

			g.setFont(new Font("Roboto Medium", Font.PLAIN, 18));
			g.drawString("Level " + user.getLevel(), 230 - (int) g.getFontMetrics().getStringBounds("Level " + user.getLevel(), g).getWidth(), 100);

			g.drawString("$" + user.getCoins(), 63, 165);

			try {
				//g.drawString("#" + (long) Database.get("SELECT ROW_NUMBER() OVER (ORDER BY level DESC) AS rank FROM user;").get(0).get("rank"), 63, 213);
				// TODO
			} catch (Exception e) {
				ErrorHandler.log(e, "rank-getting");
			}

			g.drawString(String.valueOf(user.getTotalXP()), 63, 261);

			g.setFont(new Font("Roboto Light", Font.PLAIN, 12));
			String info[] = user.getDescription().split("(?<=\\G.{28})");
			int y = 160 - g.getFontMetrics().getHeight();
			for (String line : info)
				g.drawString(line, 135, y += g.getFontMetrics().getHeight());

			Wolf wolf = new Wolf(user.getUser());
			wolf.drawAnimal(g, 150, 185, 180, 120);

			g.drawString(wolf.getXP() + " / " + wolf.getMaxXP() + " XP", 138, 240);

			g.dispose();
			encoder.setDelay(frame1.getDelay());
			encoder.addFrame(bgFrame);
		}
		encoder.finish();
		return o;
	}
}