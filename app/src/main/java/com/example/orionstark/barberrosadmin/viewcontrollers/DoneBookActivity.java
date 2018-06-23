package com.example.orionstark.barberrosadmin.viewcontrollers;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.orionstark.barberrosadmin.R;
import com.example.orionstark.barberrosadmin.services.AdminServices;

import org.json.JSONException;

public class DoneBookActivity extends AppCompatActivity {
    Button submit;
    EditText book_id, barber_name;
    Spinner spinner;
    View root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_book);
        initView();
    }

    private void initView() {
        submit = findViewById(R.id.book_submit_button);
        book_id = findViewById(R.id.order_id_field);
        barber_name = findViewById(R.id.barberName_field);
        root = findViewById(R.id.root_view_book);
        spinner = findViewById(R.id.time_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.times, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        book_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( book_id.getText().toString().length() < 8 ) {
                    book_id.setError("Order ID should have at least 8 characters");
                } else {
                    book_id.setError(null);
                }
            }
        });

        barber_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( barber_name.getText().toString().length() < 6 ) {
                    barber_name.setError("Barber's name should have at least 6 characters");
                } else {
                    barber_name.setError(null);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( barber_name.getText().toString().equals("") || barber_name.getError() != null
                        || book_id.getText().toString().equals("") || book_id.getError() != null ) {
                    Snackbar.make(root, "Please check all the required fields", Snackbar.LENGTH_SHORT).show();
                } else {
                    try {
                        final ProgressDialog progressDialog = new ProgressDialog(DoneBookActivity.this);
                        progressDialog.setTitle("Uploading");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        AdminServices.doneBarber(
                                spinner.getSelectedItem().toString(),
                                barber_name.getText().toString(),
                                book_id.getText().toString(),
                                DoneBookActivity.this,
                                new AdminServices.BarberCallback() {
                                    @Override
                                    public void onSucceed(String message) {
                                        progressDialog.cancel();
                                        Toast.makeText(DoneBookActivity.this, message, Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(String message) {
                                        progressDialog.cancel();
                                        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (JSONException e) {
                        Snackbar.make(root, "Sorry, we had problems", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
