package com.winter.mayawinterfox.data.http;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XMLHandler extends DefaultHandler {

	private List<Entry> entries = new ArrayList<>();
	private Entry entry = new Entry();
	private StringBuilder data;
	private boolean feed;
	private boolean author = false;
	private boolean category = false;
	private boolean content = false;
	private boolean id = false;
	private boolean link = false;
	private boolean title = false;

	public List<Entry> getEntries() {
		return entries;
	}

	public boolean isFeed() {
		return feed;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase("feed"))
			feed = true;
		else if (qName.equalsIgnoreCase("entry"))
			entry = new Entry();
		else if (qName.equalsIgnoreCase("name"))
			author = true;
		else if (qName.equalsIgnoreCase("category"))
			category = true;
		else if (qName.equalsIgnoreCase("content"))
			content = true;
		else if (qName.equalsIgnoreCase("id"))
			id = true;
		else if (qName.equalsIgnoreCase("link"))
			link = true;
		else if (qName.equalsIgnoreCase("title"))
			title = true;

		data = new StringBuilder();
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if (author) {
			entry.setAuthor(data.toString());
			author = false;
		} else if (category) {
			entry.setCategory(data.toString());
			category = false;
		} else if (content) {
			entry.setContent(data.toString());
			content = false;
		} else if (id) {
			entry.setId(data.toString());
			id = false;
		} else if (link) {
			entry.setLink(data.toString());
			link = false;
		} else if (title) {
			entry.setTitle(data.toString());
			title = false;
		}

		if (qName.equalsIgnoreCase("entry")) {
			entries.add(entry);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		data.append(new String(ch, start, length));
	}
}
