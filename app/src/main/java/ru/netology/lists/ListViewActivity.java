package ru.netology.lists;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    private static final String TITLE_KEY = "title";
    private static final String SUBTITLE_KEY = "subtitle";
    private static final String LENGTH_KEY = "length";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.list);

        List<HashMap<String, String>> values = prepareContent();

        BaseAdapter listContentAdapter = createAdapter(values);

        list.setAdapter(listContentAdapter);
    }

    @NonNull
    private BaseAdapter createAdapter(List<HashMap<String, String>> values) {
        String[] from = {TITLE_KEY, SUBTITLE_KEY, LENGTH_KEY};
        int[] to = {R.id.elementTitle, R.id.elementText, R.id.elementTextLength};
        return new SimpleAdapter(this, values, R.layout.list_element, from, to);
    }

    @NonNull
    private List<HashMap<String, String>> prepareContent() {
        String[] content = getString(R.string.large_text).split("\n\n");
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for (int i = 0; i < content.length / 2; i++) {
            String text = content[i * 2 + 1];
            int wordsAmt = text.split(" ").length;
            String length = getString(R.string.text_length, wordsAmt, text.length());

            HashMap<String, String> map = new HashMap<>(2);
            map.put(TITLE_KEY, content[i * 2]);
            map.put(SUBTITLE_KEY, text);
            map.put(LENGTH_KEY, length);
            list.add(map);
        }
        return list;
    }
}
