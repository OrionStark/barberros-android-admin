package com.example.orionstark.barberrosadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    CardView add_barber, done_barber, add_event, logout;
    TextView admin_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initView();
    }

    private void initView() {
        add_barber = findViewById(R.id.add_barber_nav);
        done_barber = findViewById(R.id.complete_barber_nav);
        add_event = findViewById(R.id.add_event_nav);
        logout = findViewById(R.id.logout_nav);
        admin_name = findViewById(R.id.admin_name);

        add_barber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, GetLocationActivity.class));
            }
        });
    }
}
