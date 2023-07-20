package com.police.demonstrationservice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.police.demonstrationservice.R;

public class Current_Place_M extends AppCompatActivity  {

    TextView State_Text;


    MapsFragment mapsFragment;

    AppCompatTextView setting;
    AppCompatImageButton back;

    Bundle send_data;

    TextView current_position;

    private int code = 0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_place_m);

        mapsFragment = new MapsFragment();
        current_position = findViewById(R.id.Current_Location);
        back = findViewById(R.id.backButton);
        State_Text = findViewById(R.id.State_Text);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.maps_, mapsFragment);
        fragmentTransaction.commit();



        setting = findViewById(R.id.setting_position);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code == 0){
                    if(mapsFragment.setCode()){
                        State_Text.setText("집회장소");
                        code = 1;
                    }


                } else if (code == 1) {

                    if(mapsFragment.setCode()){
                        Intent intent = new Intent(Current_Place_M.this, InputNotificationActivity.class);
                        intent.putExtra("result",send_data);
                        setResult(RESULT_OK, intent);
                        finish();
                    }


                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void OkFinsih(){
        Intent intent = new Intent(Current_Place_M.this, InputNotificationActivity.class);
        intent.putExtra("result",send_data);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void DataHandle(Bundle data) {
        send_data = data;
    }

    public void setText(String data){
        current_position.setText(data);
    }


}