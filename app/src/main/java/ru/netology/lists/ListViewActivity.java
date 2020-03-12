package ru.netology.lists;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListViewActivity extends AppCompatActivity {
	private static final int REQUEST_WRITE_EXTERNAL = 1;

	private TextListAdapter textListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);

		Button addElementButton = findViewById(R.id.addElementButton);
		addElementButton.setOnClickListener(v -> {
			textListAdapter.add(new TextListElement("Это пример добавленной строки.",
					"На меню с вводом кастомной строки не хватило бюджета \\_(ツ)_/",
					getResources()));
			Toast.makeText(this, "A new element was added to the end of the list",
					Toast.LENGTH_SHORT).show();
		});

		TextListManager.start(getResources());

		ListView list = findViewById(R.id.list);
		textListAdapter = new TextListAdapter(this);
		list.setAdapter(textListAdapter);

		SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(() -> {
			textListAdapter.restore();
			refreshLayout.setRefreshing(false);
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		TextListManager.storageAccess = ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

		if (!TextListManager.storageAccess) {
			ActivityCompat.requestPermissions(this, new String[]{
					Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL);
		}
		TextListManager.start(getResources());
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_WRITE_EXTERNAL:
				TextListManager.storageAccess = grantResults[0] ==
						PackageManager.PERMISSION_GRANTED;
				break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		TextListManager.sync();
	}
}
