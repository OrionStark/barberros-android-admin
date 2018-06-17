package com.example.orionstark.barberrosadmin.viewcontrollers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.orionstark.barberrosadmin.R;
import com.example.orionstark.barberrosadmin.utils.BarberPreference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddBarberActivity extends AppCompatActivity {
    EditText barberName, barberPhone, barberDescription, barberImage;
    TextView image_err;
    Button selectLocation;
    LinearLayout imageBarberView;
    final int IMAGE_PICKER_CODE = 666;
    byte[] imageByteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barber);
        viewInit();
    }

    private void viewInit() {
        barberName = findViewById(R.id.barber_name);
        barberPhone = findViewById(R.id.phone_number);
        barberDescription = findViewById(R.id.description_barber);
        barberImage = findViewById(R.id.image_barber);
        selectLocation = findViewById(R.id.selectLocationBtn);
        image_err = findViewById(R.id.image_error);
        imageBarberView = findViewById(R.id.image_barber_view);

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( imageByteArray == null || imageByteArray.length < 1 ) {
                    barberImage.setError("Image not selected");
                    image_err.setText("Image not selected");
                } else {
                    Log.d("IMAGE ARRAY", Base64.encodeToString(imageByteArray, Base64.DEFAULT));
                    BarberPreference.getInstance(AddBarberActivity.this).createPreference(
                            barberName.getText().toString(),
                            barberPhone.getText().toString(),
                            barberDescription.getText().toString(),
                            Base64.encodeToString(imageByteArray, Base64.DEFAULT)
                    );
                    startActivity(new Intent(AddBarberActivity.this, GetLocationActivity.class));
                    finish();
                }
            }
        });

        barberImage.setFocusableInTouchMode(false);
        barberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_CODE);
            }
        });

    }

    private byte[] getByteArrayFromImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream .toByteArray();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == IMAGE_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            if ( data != null ) {
                barberImage.setText(getFileName(data.getData()));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    barberImage.setError(null);
                    image_err.setText("");
                    imageBarberView.setBackground(bitmapDrawable);
                    imageBarberView.setVisibility(View.VISIBLE);
                    imageByteArray = getByteArrayFromImage(bitmap);
                } catch (IOException e) {
                    barberImage.setError("Failed to attach image");
                    image_err.setText("Failed to attach image");
                }
            } else {
                barberImage.setError("Failed to attach image");
                image_err.setText("Failed to attach image");
            }
        }
    }
}
