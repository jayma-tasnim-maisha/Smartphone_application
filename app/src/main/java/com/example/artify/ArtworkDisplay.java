package com.example.artify;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArtworkDisplay extends AppCompatActivity {

    private ListView listViewArts;
    private ArtCursorAdapter artCursorAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_display);

        listViewArts = findViewById(R.id.list_view_arts);
        databaseHelper = new DatabaseHelper(this);

        loadArts();
    }

    private void loadArts() {
        Cursor cursor = databaseHelper.getAllArts();
        artCursorAdapter = new ArtCursorAdapter(this, cursor, 0);
        listViewArts.setAdapter(artCursorAdapter);
    }


}
