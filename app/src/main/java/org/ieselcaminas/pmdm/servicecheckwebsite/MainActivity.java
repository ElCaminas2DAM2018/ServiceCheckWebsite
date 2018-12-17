package org.ieselcaminas.pmdm.servicecheckwebsite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        EditText editTextWebsite = (EditText) findViewById(R.id.editWebsite);
        EditText editTextSeconds = (EditText) findViewById(R.id.editSeconds);
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("website",editTextWebsite.getText().toString());
        intent.putExtra("seconds", editTextSeconds.getText().toString());
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }
}
