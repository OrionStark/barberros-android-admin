package com.example.orionstark.barberrosadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddBarberActivity extends AppCompatActivity {
    EditText barberName, barberPhone, barberDescription, barberImage;
    Button selectLocation;
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

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
