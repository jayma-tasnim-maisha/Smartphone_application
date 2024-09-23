package com.example.artify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color_2));

        Button btnInsertArt = findViewById(R.id.btn_insert_art);
        Button btnViewArt = findViewById(R.id.btn_view_art);

        btnInsertArt.setOnClickListener(v->{
            Intent intent = new Intent(AdminHomeActivity.this,InsertArtActivity.class);
            startActivity(intent);

                });

        btnViewArt.setOnClickListener(v->{
            Intent intent = new Intent(AdminHomeActivity.this,ViewArtActivity.class);
            startActivity(intent);

        });
    }
}