package ru.netology.lists;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListViewActivity extends AppCompatActivity {
	private TextListAdapter textListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.list);

		textListAdapter = new TextListAdapter(this, getPreferences(MODE_PRIVATE));
		list.setAdapter(textListAdapter);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> {
            textListAdapter.restore();
            refreshLayout.setRefreshing(false);
        });
    }
}
