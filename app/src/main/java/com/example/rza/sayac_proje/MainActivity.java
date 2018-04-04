package com.example.rza.sayac_proje;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    int count;
    Button buttonSayac;
    SharedPreferences preferences,ayarlar;
    RelativeLayout rl;
    Boolean ses_durumu,titresim_durumu;

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("count_anahtarı",count);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlar= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        buttonSayac= (Button) findViewById(R.id.btnSayac);
        rl= (RelativeLayout) findViewById(R.id.relative);

        final MediaPlayer ses = MediaPlayer.create(getApplicationContext(),R.raw.ses);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        ayarlariYukle();

        count=preferences.getInt("count_anahtarı",0);
        buttonSayac.setText(count+"");

        buttonSayac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ses_durumu){ses.start();}
                if(titresim_durumu){vibrator.vibrate(250);}
                count++;
                buttonSayac.setText(count+"");
            }
        });
    }

    private void ayarlariYukle() {
        String pos=ayarlar.getString("arkaplan","3");
        switch (Integer.valueOf(pos))
        {
            case 0:
                rl.setBackgroundColor(Color.RED);
                break;
            case 1:
                rl.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                rl.setBackgroundColor(Color.BLUE);
                break;
            case 3:
                rl.setBackgroundColor(Color.DKGRAY);
                break;
            case 4:
                rl.setBackgroundColor(Color.LTGRAY);
                break;
        }
        ses_durumu=ayarlar.getBoolean("ses",false);
        titresim_durumu=ayarlar.getBoolean("titresim",false);
        ayarlar.registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_context,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_Ayarlar)
        {
            Intent intent = new Intent(getApplicationContext(),Ayarlar.class);
            startActivity(intent);
        }
        if(id==R.id.action_Reset)
        {
            count=0;
            buttonSayac.setText(count+"");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        ayarlariYukle();
    }
}
