package ru.netology.lists;

import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextListManager {
	static boolean storageAccess;

	private static final String LOG_TAG = TextListManager.class.getName();
	public static final String TEXT_FILE = "Text List.txt";

	private static List<TextListElement> elements;

	public static void start(Resources r) {
		byte[] loaded = load();
		if (loaded == null) {
			restore(r);
			Log.v(LOG_TAG, "Set list to default (length " + elements.size() + ")");
		} else {
			read(loaded, r);
			Log.v(LOG_TAG, "Read " + elements.size() + " elements");
		}
	}

	public static List<TextListElement> getElements() {
		return elements;
	}

	public static void remove(int i) {
		elements.remove(i);
	}

	public static void add(TextListElement element) {
		elements.add(element);
	}

	public static void restore(Resources r) {
		elements = new ArrayList<>(Arrays.asList(getDefaultList(r)));
	}

	public static byte[] write() {
		StringBuilder result = new StringBuilder();

		for (TextListElement element : elements)
			result.append("\n\n").append(element.getTitle()).
					append("\n").append(element.getText());

		// substring(2) removes first "\n\n" that shouldn't be in the start
		return result.substring(2).getBytes(StandardCharsets.UTF_8);
	}

	public static void read(byte[] from, Resources r) {
		String[] elementParts = new String(from, StandardCharsets.UTF_8).split("\n\n");
		elements = new ArrayList<>();
		for (String elementString : elementParts) {
			int separator = elementString.indexOf('\n');
			TextListElement element;

			if (separator == -1 || separator == elementString.length() - 1) {
				element = new TextListElement(r.getString(R.string.no_title), elementString, r);
			} else {
				String title = elementString.substring(0, separator);
				String text = elementString.substring(separator + 1);
				element = new TextListElement(title, text, r);
			}

			elements.add(element);
		}
	}

	private static byte[] load() {
		File f = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOCUMENTS), TEXT_FILE);
		FileInputStream in;
		try {
			in = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			return null;
		}

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read;
			byte[] buffer = new byte[4096];
			while ((read = in.read(buffer)) != -1)
				out.write(buffer, 0, read);
			in.close();

			Log.v(LOG_TAG, "Loaded successfully");
			return out.toByteArray();
		} catch (IOException e) {
			Log.w(LOG_TAG, "IOException while reading: " + e.getMessage());
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				Log.w(LOG_TAG, "IOException while closing: " + e.getMessage());
			}
		}
	}

	private static void save(byte[] bs) {
		File f = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOCUMENTS), TEXT_FILE);
		if (f.isDirectory()) {
			Log.v(LOG_TAG, "Can't save to " + f.getAbsolutePath() +
					" because this path is occupied by a folder");
			return;
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			Log.v(LOG_TAG, "Can't save: " + e.getMessage());
			return;
		}
		try {
			out.write(bs);
			Log.v(LOG_TAG, "Saved successfully");
		} catch (IOException e) {
			Log.w(LOG_TAG, "IOException while writing: " + e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				Log.w(LOG_TAG, "IOException while writing: " + e.getMessage());
			}
		}
	}

	public static void sync() {
		if (storageAccess) {
			Log.v(LOG_TAG, "Syncing " + elements.size() + " elements");
			save(write());
		}
	}

	private static TextListElement[] getDefaultList(Resources r) {
		String src = r.getString(R.string.large_text);
		String[] split = src.split("\n\n");

		TextListElement[] result = new TextListElement[split.length / 2];
		for (int i = 0; i < result.length; i++)
			result[i] = new TextListElement(split[2 * i], split[2 * i + 1], r);

		return result;
	}
}
