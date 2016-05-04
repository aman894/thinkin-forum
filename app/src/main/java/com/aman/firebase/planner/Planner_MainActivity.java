package com.aman.firebase.planner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.aman.firebase.R;
import com.aman.firebase.planner.adapter.Planner_SLMAdapter;
import com.aman.firebase.planner.planner_components.Planner_Reminder;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.SectionLayoutManager;

import java.util.Random;

public class Planner_MainActivity extends AppCompatActivity {
    private RecyclerView plannner_RecyclerView;
    private RecyclerView.Adapter planner_Adapter;
    private RecyclerView.LayoutManager planner_LayoutManager;
    private boolean mAreMarginsFixed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        plannner_RecyclerView= (RecyclerView) findViewById(R.id.slm_calender_recyclerview);

        planner_LayoutManager=new LinearLayoutManager(this);
        plannner_RecyclerView.setLayoutManager(planner_LayoutManager);

        planner_Adapter=new Planner_SLMAdapter(this, LayoutManager.LayoutParams.HEADER_INLINE);
        plannner_RecyclerView.setAdapter(planner_Adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reminder and Events", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(Planner_MainActivity.this, Planner_Reminder.class));

            }
        });
    }

}
