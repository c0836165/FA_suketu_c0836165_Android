package com.example.fa_suketu_c0836165_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class FavouriteList extends AppCompatActivity {
    RecyclerView  recyclerView;
    DbHelper dbHelper;
    List<DataModel> allPlaces;

    Adapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
       getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        findIds();
        getData();
    }

    private void getData() {
        dbHelper=new DbHelper(FavouriteList.this);
        allPlaces =dbHelper.getAllPlaces();

        adapter = new Adapter(FavouriteList.this, allPlaces);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void findIds() {
        recyclerView=findViewById(R.id.recycler);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

        Context context;
        List<DataModel> list;


        public Adapter(Context context, List<DataModel> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            DataModel model = list.get(position);
            holder.name.setText("Latitude: "+model.getLat()+"\n"+"Longitude: "+model.getLng()+"\n"+ "Address :"+model.getPlaceName());



            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dbHelper.deletePlace(model);
                    getData();

                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(FavouriteList.this, MainActivity.class);
                    intent.putExtra("TYPE","");
                    intent.putExtra("MODEL",model);
                    startActivity(intent);
                    finish();

                }
            });




        }




        @Override
        public int getItemCount() {
            return list.size();
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            Button delete,edit;


            public MyViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                delete = itemView.findViewById(R.id.delete);
                edit = itemView.findViewById(R.id.edit);


            }
        }
    }
}