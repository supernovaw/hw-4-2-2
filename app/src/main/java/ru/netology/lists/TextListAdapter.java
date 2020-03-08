package ru.netology.lists;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TextListAdapter extends BaseAdapter {
	private static final String EXCLUDED = "excluded";

	private List<Entry> entriesOriginal;
	private List<Entry> entries;
	private List<Integer> excludedIndices;
	private LayoutInflater inflater;
	private SharedPreferences sharedPreferences;
	private Context context;

	public TextListAdapter(Context context, SharedPreferences sp) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sharedPreferences = sp;

		String[] content = context.getString(R.string.large_text).split("\n\n");
		entriesOriginal = new ArrayList<>();
		for (int i = 0; i < content.length / 2; i++) {
			String text = content[i * 2 + 1];
			int wordsAmt = text.split(" ").length;
			String length = context.getString(R.string.text_length, wordsAmt, text.length());

			entriesOriginal.add(new Entry(content[i * 2], text, length));
		}

		entries = new ArrayList<>(entriesOriginal);
		readExcluded();
		exclude(entries);
	}

	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Object getItem(int i) {
		return entries.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null)
			view = inflater.inflate(R.layout.list_element, viewGroup, false);

		TextView titleView = view.findViewById(R.id.elementTitle);
		TextView textView = view.findViewById(R.id.elementText);
		TextView infoView = view.findViewById(R.id.elementTextLength);
		Button deleteButton = view.findViewById(R.id.deleteElementButton);

		Entry e = entries.get(i);

		titleView.setText(e.title);
		textView.setText(e.text);
		infoView.setText(e.info);
		deleteButton.setOnClickListener(v -> remove(i));

		view.setOnLongClickListener(v -> {
			Toast.makeText(context, "- " + entries.get(i).title + " -", Toast.LENGTH_SHORT).show();
			return true;
		});

		return view;
	}

	private void remove(int i) {
		excludedIndices.add(entriesOriginal.indexOf(entries.get(i))); // cancel shift
		entries.remove(i);
		saveExcludedIndices();
		notifyDataSetChanged();
	}

	public void restore() {
		excludedIndices.clear();
		entries.clear();
		entries.addAll(entriesOriginal);
		saveExcludedIndices();
		notifyDataSetChanged();
	}

	private void readExcluded() {
		String data = sharedPreferences.getString(EXCLUDED, "");
		excludedIndices = new ArrayList<>();
		if (data != null && !data.isEmpty()) {
			String[] split = data.split(";");
			for (String s : split)
				excludedIndices.add(Integer.parseInt(s));
		}
	}

	private void saveExcludedIndices() {
		StringBuilder toSave;
		if (excludedIndices.isEmpty())
			toSave = new StringBuilder();
		else {
			toSave = new StringBuilder(Integer.toString(excludedIndices.get(0)));
			for (int i = 1; i < excludedIndices.size(); i++)
				toSave.append(";").append(excludedIndices.get(i));
		}
		sharedPreferences.edit().putString(EXCLUDED, toSave.toString()).apply();
	}

	private void exclude(List<Entry> list) {
		List<Entry> resultList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (!excludedIndices.contains(i))
				resultList.add(list.get(i));
		}
		list.clear();
		list.addAll(resultList);
	}

	private static class Entry {
		String title;
		String text;
		String info;

		public Entry(String title, String text, String info) {
			this.title = title;
			this.text = text;
			this.info = info;
		}
	}
}
