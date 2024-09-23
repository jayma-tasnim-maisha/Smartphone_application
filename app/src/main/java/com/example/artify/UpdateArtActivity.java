package com.example.artify;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateArtActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private ImageView imageViewArt;
    private Button buttonUpdate;
    private Button buttonSelectImage;
    private Button buttonSearch;
    private TextView textViewArtId;

    private DatabaseHelper databaseHelper;
    private byte[] artImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_art);

        editTextName = findViewById(R.id.edit_text_art_name);
        editTextPrice = findViewById(R.id.edit_text_art_price);
        editTextQuantity = findViewById(R.id.edit_text_art_quantity);
        imageViewArt =  findViewById(R.id.image_view_art);
        buttonUpdate = findViewById(R.id.button_update);
        buttonSelectImage = findViewById(R.id.button_select_image);
        buttonSearch = findViewById(R.id.button_search);
        textViewArtId = findViewById(R.id.text_view_art_id);

        databaseHelper = new DatabaseHelper(this);

        buttonSearch.setOnClickListener(view -> searchArt());
        buttonSelectImage.setOnClickListener(view -> selectImage());
        buttonUpdate.setOnClickListener(view -> updateArt());

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

            editTextPrice.setText(String.valueOf(price));
            editTextQuantity.setText(String.valueOf(quantity));
            textViewArtId.setText("Art ID: " + artId);

            if(image != null) {
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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewArt.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                artImageByteArray = byteArrayOutputStream.toByteArray();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void updateArt() {
        String artName = editTextName.getText().toString().trim();
        String artPrice = editTextPrice.getText().toString().trim();
        String artQuantity = editTextQuantity.getText().toString().trim();
        
        if (artName.isEmpty() || artPrice.isEmpty() || artQuantity.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(artPrice);
        int quantity = Integer.parseInt(artQuantity);

        String artIdText = textViewArtId.getText().toString();
        int artId = Integer.parseInt(artIdText.replaceAll("\\D+", ""));

        databaseHelper.updateArt(artId, artName, price, quantity, artImageByteArray);

        Toast.makeText(this, "Your image is updated", Toast.LENGTH_SHORT).show();
    }


}