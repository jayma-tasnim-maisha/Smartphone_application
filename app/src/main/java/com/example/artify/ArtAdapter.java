package com.example.artify;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArtAdapter extends CursorAdapter {


    public ArtAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_art, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.text_view_art_name);
        TextView priceTextView = view.findViewById(R.id.text_view_art_price);
        TextView quantityTextView = view.findViewById(R.id.text_view_art_quantity);
        ImageView artImageView = view.findViewById(R.id.image_view_art);

        final int artId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_NAME));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_QUANTITY));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ART_IMAGE_URI));

        nameTextView.setText(name);
        priceTextView.setText(String.valueOf(price));
        quantityTextView.setText(String.valueOf(quantity));
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        artImageView.setImageBitmap(bitmap);


    }

}

