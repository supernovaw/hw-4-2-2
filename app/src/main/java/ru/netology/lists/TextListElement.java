package ru.netology.lists;

import android.content.res.Resources;

public class TextListElement {
	private String title;
	private String text;
	private String info;

	public TextListElement(String title, String text, Resources r) {
		this.title = title;
		this.text = text;
		initInfo(r);
	}

	private void initInfo(Resources r) {
		String[] splitText = text.split(" ");
		int words = splitText.length;
		if (words == 1 && splitText[0].isEmpty())
			words = 0;
		// TODO: replace with codePoints iteration to support chars outside BMP
		int chars = text.length();
		info = r.getString(R.string.text_length, words, chars);
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getInfo() {
		return info;
	}
}
