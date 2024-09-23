package com.example.artify;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeleteArtActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName;
    private TextView textViewArtPrice;
    private TextView textViewArtQuantity;
    private ImageView imageViewArt;
    private Button buttonDelete;
    private Button buttonSelectImage;
    private Button buttonSearch;
    private TextView textViewArtId;

    private DatabaseHelper databaseHelper;
    private byte[] artImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_art);

        editTextName = findViewById(R.id.text_view_art_name);
        textViewArtPrice = findViewById(R.id.text_view_art_price);
        textViewArtQuantity = findViewById(R.id.text_view_art_quantity);
        textViewArtId = findViewById(R.id.text_view_art_id);
        imageViewArt = findViewById(R.id.image_view_art);
        buttonDelete = findViewById(R.id.button_delete);

        buttonSearch = findViewById(R.id.button_search);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchArt());
        buttonDelete.setOnClickListener(view -> deleteArt());
    }

    private void searchArt() {
        String artName = editTextName.getText().toString().trim();
        if (artName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getArtByName(artName);
        if (cursor != null && cursor.moveToFirst()) {
            int artId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_QUANTITY));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_IMAGE_URI));

            textViewArtPrice.setText(String.valueOf(price));
            textViewArtQuantity.setText(String.valueOf(quantity));
            textViewArtId.setText("Art ID: " + artId);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewArt.setImageBitmap(bitmap);
                artImageByteArray = image;
            }
            cursor.close();
        }

        else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteArt() {
        String artName = editTextName.getText().toString().trim();

        databaseHelper.deleteArt(artName);

        Toast.makeText(this, "Image has been deleted", Toast.LENGTH_SHORT).show();
    }

}