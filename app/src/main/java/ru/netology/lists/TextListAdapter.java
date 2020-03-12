package ru.netology.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TextListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;

	public TextListAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return TextListManager.getElements().size();
	}

	@Override
	public Object getItem(int i) {
		return TextListManager.getElements().get(i);
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

		TextListElement e = (TextListElement) getItem(i);
		titleView.setText(e.getTitle());
		textView.setText(e.getText());
		infoView.setText(e.getInfo());
		deleteButton.setOnClickListener(v -> remove(i));

		view.setOnLongClickListener(v -> {
			TextListElement el = (TextListElement) getItem(i);
			Toast.makeText(context, "- " + el.getTitle() + " -", Toast.LENGTH_SHORT).show();
			return true;
		});

		return view;
	}

	private void remove(int i) {
		TextListManager.remove(i);
		notifyDataSetChanged();
	}

	public void restore() {
		TextListManager.restore(context.getResources());
		notifyDataSetChanged();
	}

	public void add(TextListElement element) {
		TextListManager.add(element);
		notifyDataSetChanged();
	}
}
