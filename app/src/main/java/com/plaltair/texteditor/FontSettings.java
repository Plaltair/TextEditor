package com.plaltair.texteditor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class FontSettings extends AppCompatActivity {

    private int value;

    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = findViewById(R.id.title);
        final TextView example = findViewById(R.id.example);
        seekBar = findViewById(R.id.seekBar);

        ScaledFont.fixTextSize(title, 15);

        int dataValue = new Data(FontSettings.this).read("font", 5);
        value = dataValue;
        seekBar.setProgress(dataValue);
        setFont(example, dataValue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
                setFont(example, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_font, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Data data = new Data(FontSettings.this);
        int id = item.getItemId();

        if (id == R.id.action_save) {
            data.write("font", value);
            Toast.makeText(FontSettings.this, getResources().getString(R.string.fontSaved), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else if (id == R.id.action_default){
            seekBar.setProgress(5);
        }
        else if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFont(TextView textView, int seekBarValue) {

        switch (seekBarValue) {
            case 0:
                ScaledFont.fixTextSize(textView, 25);
                break;
            case 1:
                ScaledFont.fixTextSize(textView, 24);
                break;
            case 2:
                ScaledFont.fixTextSize(textView, 23);
                break;
            case 3:
                ScaledFont.fixTextSize(textView, 22);
                break;
            case 4:
                ScaledFont.fixTextSize(textView, 21);
                break;
            case 5:
                ScaledFont.fixTextSize(textView, 20);
                break;
            case 6:
                ScaledFont.fixTextSize(textView, 19);
                break;
            case 7:
                ScaledFont.fixTextSize(textView, 18);
                break;
            case 8:
                ScaledFont.fixTextSize(textView, 17);
                break;
            case 9:
                ScaledFont.fixTextSize(textView, 16);
                break;
            case 10:
                ScaledFont.fixTextSize(textView, 15);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FontSettings.this, Main.class));
        finish();
    }
}
