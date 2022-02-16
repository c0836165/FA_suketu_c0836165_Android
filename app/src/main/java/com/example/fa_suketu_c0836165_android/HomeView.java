package com.example.fa_suketu_c0836165_android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class HomeView extends AppCompatActivity {
    RadioButton normalView, terrainView,none, hybridView, satelliteView;
    Button currentLocation,places;
    String selectedMapStyle ="";
    RadioGroup radioGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findIds();


        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    selectedMapStyle =checkedRadioButton.getText()+"";
                }
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(none.isChecked()==false&& normalView.isChecked()==false
                        && terrainView.isChecked()==false&& satelliteView.isChecked()==false
                        && hybridView.isChecked()==false){
                    Toast.makeText(HomeView.this, "PLEASE CHOOSE ANY MAP STYLE TO PROCEED ", Toast.LENGTH_SHORT).show();

                }else if(!getPermissison()){
                    getPermissison();
                }
                else{
                    Intent intent=new Intent(HomeView.this, MainActivity.class);
                    intent.putExtra("TYPE", selectedMapStyle);
                    intent.putExtra("MODEL","");
                    startActivity(intent);
                }
            }
        });

        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeView.this, FavouriteList.class));
            }
        });
    }

    public boolean getPermissison(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity( new Intent(HomeView.this, MainActivity.class));
                    finish();
                }
                break;
        }
    }

    private void findIds() {
        none=findViewById(R.id.none);
        normalView =findViewById(R.id.normal);
        terrainView =findViewById(R.id.terrain);
        hybridView =findViewById(R.id.hybrid);
        satelliteView =findViewById(R.id.satellite);
        currentLocation=findViewById(R.id.currentLocation);
        places=findViewById(R.id.savedPlaces);
        radioGrp=findViewById(R.id.radioGrp);




    }
}