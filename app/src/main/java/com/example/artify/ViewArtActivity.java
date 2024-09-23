package com.example.artify;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewArtActivity extends AppCompatActivity {

    private ListView listViewArts;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_art);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color_3));

        listViewArts = findViewById(R.id.list_view_arts);
        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);

        databaseHelper = new DatabaseHelper(this);

        displayArts();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdate();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDelete();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        displayArts();
    }

    private void displayArts(){
        Cursor cursor = databaseHelper.getAllArts();
        if(cursor != null && cursor.getCount() > 0){
            ArtAdapter adapter = new ArtAdapter(this, cursor, 0);
            listViewArts.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No arts found", Toast.LENGTH_SHORT).show();
        }

    }

    private void handleUpdate() {
        // Logic for updating a product
        Intent intent = new Intent(ViewArtActivity.this, UpdateArtActivity.class); // Assuming HomeActivity is the activity after login
        startActivity(intent);
    }
    //
    private void handleDelete() {
        Intent intent = new Intent(ViewArtActivity.this, DeleteArtActivity.class); // Assuming HomeActivity is the activity after login
        startActivity(intent);
        Toast.makeText(this, "Delete button clicked", Toast.LENGTH_SHORT).show();
    }
}