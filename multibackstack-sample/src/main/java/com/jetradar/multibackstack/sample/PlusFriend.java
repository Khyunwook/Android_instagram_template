package com.jetradar.multibackstack.sample;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PlusFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_friend);
        setUpActionBar();

        ListView listView = (ListView)findViewById(R.id.listView5);
        ArrayList<String> arrName = new ArrayList<String>();
        for(int i=0;i<20; i++){
            arrName.add("");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.plusfriend_item,R.id.textView4, arrName);
        listView.setAdapter(adapter);
    }
    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.friend_bar);
    }
}
