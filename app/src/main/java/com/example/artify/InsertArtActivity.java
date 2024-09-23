package com.example.artify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertArtActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText ArtNameEditText;
    private EditText ArtPriceEditText;
    private EditText ArtQuantityEditText;
    private ImageView selectedImageView;
    private Button selectImageButton;
    private Button insertArtButton;
    private DatabaseHelper databaseHelper;
    private byte[] imageByteArray;

    private ActivityResultLauncher<Intent> imagePickerLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_art);

        ArtNameEditText = findViewById(R.id.et_art_name);
        ArtPriceEditText = findViewById(R.id.et_art_price);
        ArtQuantityEditText = findViewById(R.id.et_art_quantity);
        selectedImageView = findViewById(R.id.iv_selected_image);
        selectImageButton = findViewById(R.id.btn_select_image);
        insertArtButton = findViewById(R.id.btn_insert_art);

        databaseHelper = new DatabaseHelper(this);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){

                Uri imageUri = result.getData().getData();
                try{
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    selectedImageView.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        selectImageButton.setOnClickListener(view -> showImageSelectionDialog());

        insertArtButton.setOnClickListener(view -> insertArt());

    }

    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void insertArt() {
        String name = ArtNameEditText.getText().toString();
        double price = Double.parseDouble(ArtPriceEditText.getText().toString());
        int quantity = Integer.parseInt(ArtQuantityEditText.getText().toString());

        if(name.isEmpty() || imageByteArray == null){
            Toast.makeText(this,"Please fill all the fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.insertArt(name, price, quantity, imageByteArray);
        Toast.makeText(this,"Inserted successfully", Toast.LENGTH_SHORT).show();

    }

}