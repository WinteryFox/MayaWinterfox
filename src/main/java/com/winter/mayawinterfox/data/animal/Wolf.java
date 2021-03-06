package com.winter.mayawinterfox.data.animal;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.winter.mayawinterfox.data.Database;
import com.winter.mayawinterfox.data.Row;
import com.winter.mayawinterfox.data.cache.Caches;
import com.winter.mayawinterfox.data.cache.meta.UserMeta;
import com.winter.mayawinterfox.data.item.Food;
import com.winter.mayawinterfox.data.item.Inventory;
import com.winter.mayawinterfox.data.item.Item;
import com.winter.mayawinterfox.data.item.ItemProvider;
import com.winter.mayawinterfox.exceptions.ErrorHandler;
import com.winter.mayawinterfox.util.ImageUtil;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Wolf {

	private final long hungerLossRate = 2700000L;

	private final User user;
	private final String name;
	private final State state;
	private final int level;
	private final int happiness;
	private final int energy;
	private final int xp;
	private final int maxXp;
	private final int hunger;
	private final int fedTimes;
	private final long lastFedTime;
	private final Inventory inventory;
	private final Item background;
	private final Item hat;
	private final Item body;
	private final Item paws;
	private final Item tail;
	private final Item shirt;
	private final Item nose;
	private final Item eye;
	private final Item neck;

	public Wolf(User user) {
		this.user = user;
		var stats = Database.get("SELECT * FROM wolf WHERE id=?;", user.getId().asLong())
				.map(Row::getColumns)
				.collectList()
				.block();
		this.name = (String) stats.get(0).get("name");
		this.level = (int) stats.get(0).get("level");
		this.state = State.values()[(int) stats.get(0).get("state")];
		this.happiness = (int) stats.get(0).get("happiness");
		this.energy = (int) stats.get(0).get("energy");
		this.xp = (int) stats.get(0).get("xp");
		this.maxXp = (int) stats.get(0).get("maxxp");
		this.hunger = (int) stats.get(0).get("hunger");
		this.lastFedTime = (long) stats.get(0).get("lastfedtime");
		this.fedTimes = (int) stats.get(0).get("fedtimes");
		this.inventory = Inventory.create(user).block();
		this.background = ItemProvider.getItemById((int) stats.get(0).get("background"));
		this.hat = ItemProvider.getItemById((int) stats.get(0).get("hat"));
		this.body = ItemProvider.getItemById((int) stats.get(0).get("body"));
		this.paws = ItemProvider.getItemById((int) stats.get(0).get("paws"));
		this.tail = ItemProvider.getItemById((int) stats.get(0).get("tail"));
		this.shirt = ItemProvider.getItemById((int) stats.get(0).get("shirt"));
		this.nose = ItemProvider.getItemById((int) stats.get(0).get("nose"));
		this.eye = ItemProvider.getItemById((int) stats.get(0).get("eye"));
		this.neck = ItemProvider.getItemById((int) stats.get(0).get("neck"));
	}

	// TODO:
	public void update() {
		/*long timePassed = System.currentTimeMillis() - getLastFedTime();
		int lost = (int) (timePassed / hungerLossRate);
		if (getHunger() - lost < 0)
			hunger = 0;
		else
			hunger = hunger - lost;
		Database.set("UPDATE wolf SET hunger=? WHERE id=?;", hunger, user.getLongID());*/

	}

	public void feed(Food food) {
		setHunger(hunger + food.getValue());
		setLastFedTime(System.currentTimeMillis());
		setXp(xp + food.getValue());
		if (xp >= maxXp) {
			setXp(0);
			setMaxXp((level - 1) * 60 + 200);
			setLevel(level + 1);
		}
		setFedTimes(fedTimes + 1);
		UserMeta u = Caches.getUser(user).block();
		u.setCoins(u.getCoins() + 50).block();
	}

	public void play() {

	}

	public User getUser() {
		return user;
	}

	public String getName() {
		return name;
	}

	public Mono<Wolf> setName(String name) {
		return Database.set("UPDATE wolf SET name = ? WHERE id = ?", name, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getLevel() {
		return level;
	}

	public Mono<Wolf> setLevel(int level) {
		return Database.set("UPDATE wolf SET level = ? WHERE id = ?", level, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Mono<Wolf> incrementLevel() {
		return Database.set("UPDATE wolf SET level = ? WHERE id = ?", level + 1, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getHunger() {
		return hunger;
	}

	public Mono<Wolf> setHunger(int hunger) {
		return Database.set("UPDATE wolf SET hunger = ? WHERE id = ?", hunger, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public long getLastFedTime() {
		return lastFedTime;
	}

	public Mono<Wolf> setLastFedTime(long lastFedTime) {
		return Database.set("UPDATE wolf SET lastfedtime = ? WHERE id = ?", lastFedTime, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public State getState() {
		return state;
	}

	public Mono<Wolf> setState(State state) {
		return Database.set("UPDATE wolf SET state = ? WHERE id = ?", state.ordinal(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getHappiness() {
		return happiness;
	}

	public Mono<Wolf> setHappiness(int happiness) {
		return Database.set("UPDATE wolf SET happiness = ? WHERE id = ?", happiness, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getEnergy() {
		return energy;
	}

	public Mono<Wolf> setEnergy(int energy) {
		return Database.set("UPDATE wolf SET energy = ? WHERE id = ?", energy, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getXP() {
		return xp;
	}

	public Mono<Wolf> setXp(int xp) {
		return Database.set("UPDATE wolf SET xp = ? WHERE id = ?", xp, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getMaxXP() {
		return maxXp;
	}

	public Mono<Wolf> setMaxXp(int maxXp) {
		return Database.set("UPDATE wolf SET maxXp = ? WHERE id = ?", maxXp, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public int getFedTimes() {
		return fedTimes;
	}

	public Mono<Wolf> setFedTimes(int fedTimes) {
		return Database.set("UPDATE wolf SET fedtimes = ? WHERE id = ?", fedTimes, user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public void equip(Item item) {
		switch (item.getType()) {
			case BACKGROUND:
				setBackground(item);
				break;
			case HAT:
				setHat(item);
				break;
			case BODY:
				setBody(item);
				break;
			case PAWS:
				setPaws(item);
				break;
			case TAIL:
				setTail(item);
				break;
			case SHIRT:
				setShirt(item);
				break;
			case NOSE:
				setNose(item);
				break;
			case EYES:
				setEye(item);
				break;
			case NECK:
				setNeck(item);
				break;
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Item getBackground() {
		return background;
	}

	public Mono<Wolf> setBackground(Item background) {
		return Database.set("UPDATE wolf SET background = ? WHERE id = ?", background.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getHat() {
		return hat;
	}

	public Mono<Wolf> setHat(Item hat) {
		return Database.set("UPDATE wolf SET hat = ? WHERE id = ?", hat.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getBody() {
		return body;
	}

	public Mono<Wolf> setBody(Item body) {
		return Database.set("UPDATE wolf SET body = ? WHERE id = ?", body.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getPaws() {
		return paws;
	}

	public Mono<Wolf> setPaws(Item paws) {
		return Database.set("UPDATE wolf SET paws = ? WHERE id = ?", paws.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getTail() {
		return tail;
	}

	public Mono<Wolf> setTail(Item tail) {
		return Database.set("UPDATE wolf SET tail = ? WHERE id = ?", tail.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getShirt() {
		return shirt;
	}

	public Mono<Wolf> setShirt(Item shirt) {
		return Database.set("UPDATE wolf SET shirt = ? WHERE id = ?", shirt.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getNose() {
		return nose;
	}

	public Mono<Wolf> setNose(Item nose) {
		return Database.set("UPDATE wolf SET nose = ? WHERE id = ?", nose.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getEye() {
		return eye;
	}

	public Mono<Wolf> setEye(Item eye) {
		return Database.set("UPDATE wolf SET eye = ? WHERE id = ?", eye.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public Item getNeck() {
		return neck;
	}

	public Mono<Wolf> setNeck(Item neck) {
		return Database.set("UPDATE wolf SET neck = ? WHERE id = ?", neck.getId(), user.getId().asLong())
				.thenReturn(new Wolf(user));
	}

	public InputStream render() {
		final int w = 300;
		final int h = 150;

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		if (getBackground().getPath().endsWith(".gif")) {
			o = drawGif(o, w, h);
		} else {
			BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			BufferedImage background = null;
			try {
				background = ImageIO.read(Wolf.class.getResourceAsStream(getBackground().getPath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(background, 0, 0, w, h, null);
			drawProfile(g, w, h);
			drawAnimal(g, 60, 0, w, h);
			try {
				ImageIO.write(image, "png", o);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ByteArrayInputStream(o.toByteArray());
	}

	/**
	 * Draw the entire animal with a gif item
	 * @param o The OutputStream to write the image to
	 * @return ByteArrayOutputStream containing the animal image
	 */
	private ByteArrayOutputStream drawGif(ByteArrayOutputStream o, int w, int h) {
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		encoder.start(o);
		encoder.setRepeat(0);
		encoder.setQuality(1);

		List<ImageUtil.GifFrame> frames = new ArrayList<>();
		try (InputStream stream = Wolf.class.getResourceAsStream(getBackground().getPath())) {
			frames = ImageUtil.getGifFramesFromStream(stream);
		} catch (IOException ex) {
			ErrorHandler.log(ex, "Failed to read background stream");
		}
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
			drawProfile(g, w, h);
			drawAnimal(g, 60, 0, w, h);
			g.dispose();
			encoder.setDelay(frame1.getDelay());
			encoder.addFrame(bgFrame);
		}
		encoder.finish();
		return o;
	}

	/**
	 * Draw the entire animal
	 * @param g The graphics to draw with
	 */
	public void drawAnimal(Graphics2D g, int x, int y, int w, int h) {
		BufferedImage template = null;
		BufferedImage eye1 = null;
		BufferedImage nose1 = null;
		BufferedImage shirt1 = null;
		BufferedImage neck1 = null;
		BufferedImage tail1 = null;
		BufferedImage paws1 = null;
		BufferedImage hat1 = null;
		try (InputStream te = Wolf.class.getResourceAsStream("/wolf/wolf-base.png");
		     InputStream ey = Wolf.class.getResourceAsStream(eye.getPath());
		     InputStream no = Wolf.class.getResourceAsStream(nose.getPath());
		     InputStream sh = Wolf.class.getResourceAsStream(shirt.getPath());
		     InputStream ne = Wolf.class.getResourceAsStream(neck.getPath());
		     InputStream ta = Wolf.class.getResourceAsStream(tail.getPath());
		     InputStream pa = Wolf.class.getResourceAsStream(paws.getPath());
		     InputStream ha = Wolf.class.getResourceAsStream(hat.getPath())) {
			template = ImageIO.read(te);
			eye1 = ImageIO.read(ey);
			nose1 = ImageIO.read(no);
			shirt1 = ImageIO.read(sh);
			neck1 = ImageIO.read(ne);
			tail1 = ImageIO.read(ta);
			paws1 = ImageIO.read(pa);
			hat1 = ImageIO.read(ha);
		} catch (IOException ex) {
			ErrorHandler.log(ex, "Failed to get wolf images");
		}
		g.drawImage(template,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(eye1,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(nose1,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(shirt1,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(neck1,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(tail1,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(paws1,
				x,
				y,
				w,
				h,
				null);
		g.drawImage(hat1,
				x,
				y,
				w,
				h,
				null);
	}

	/**
	 * Draw the animal's profile background
	 * @param g The graphics to draw with
	 */
	private void drawProfile(Graphics2D g, int w, int h) {
		BufferedImage template = null;
		try (InputStream stream = Wolf.class.getResourceAsStream("/wolf/template.png")) {
			template = ImageIO.read(stream);
		} catch (IOException ex) {
			ErrorHandler.log(ex, "Failed to getGuild profile template");
		}
		g.drawImage(template, 0, 0, w, h, null);

		g.setFont(new Font("Roboto Regular", Font.PLAIN, 18));
		g.setColor(new Color(186, 187, 189));
		final int nameX = (w / 4 + 15) - (int) (g.getFontMetrics().getStringBounds(getName(), g).getWidth() / 2);
		final int nameY = 60;
		g.drawString(getName(), nameX, nameY);

		g.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
		final int levelX = (w / 4 + w / 12) - (int) (g.getFontMetrics().getStringBounds("Level " + getLevel(), g).getWidth() / 2);
		final int levelY = 105;
		g.drawString("Level " + getLevel(), levelX, levelY);

		final int barX = 17;
		final int barY = 67;
		final int barHeight = 20;
		final int size = (w / 2 - 2);
		final double percent = ((double) getXP() / (double) getMaxXP()) * (double) size;

		final int barWidth = (int) percent;
		g.setColor(new Color(150, 117, 96));
		g.setClip(new RoundRectangle2D.Float(barX, barY, barWidth, barHeight, 10, 10));
		g.fillRect(barX, barY, barWidth, barHeight);
		g.setClip(null);

		g.setFont(new Font("Roboto Bold", Font.BOLD, 14));
		g.setColor(new Color(216, 216, 216));
		final int textX = barX + (size / 2) - (int) (g.getFontMetrics().getStringBounds(getXP() + " / " + getMaxXP(), g).getWidth() / 2);
		final int textY = 82;
		g.drawString(getXP() + " / " + getMaxXP(), textX, textY);
	}

	enum State {
		HAPPY("happy", 1.2),
		CONTENT("content", 1.0),
		INDIFFERENT("indifferent", 0.8),
		UPSET("upset", 0.5),
		SICK("sick", 0.3);

		String stateName;
		double multiplier;

		State(String stateName, double multiplier) {
			this.stateName = stateName;
			this.multiplier = multiplier;
		}
	}
}
