package com.myapp.adorg.simplecalculatorv2;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class History extends AppCompatActivity implements ClearFragment.OnFragmentInteractionListener, DeleteHistoryItemDialogActivity.OnFragmentInteractionListener{


    private RecyclerView mCrimeRecyclerView;
    private TimeCardAdapter mAdapter;
    private boolean mClearAll;
    private TextView textView;
    private ImageView sadFace;
    private int adapterPosition;
    private Toolbar historyToolbar;
    private MenuItem menuClear, menuDelete, menuEdit, menuHome;
    private boolean longPressed = false;
    private View selectedView;
    private List<TimeCard> mTimeCards;
    private List<TimeCard> timeCards;
    private ArrayList<TimeCard> deletedTimeCards = new ArrayList<>();
    private TimeCard timeCard;
    private ArrayList<UUID> uuidArrayList = new ArrayList<>();
    private ArrayList<View> viewArrayList = new ArrayList<>();
    private ArrayList<Double>timeWorkedArray = new ArrayList<>();
    private ArrayList<Double>prodArray = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Preferences.getDarkMode(getApplicationContext())) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_history);


        historyToolbar = findViewById(R.id.historyToolbar);
        setSupportActionBar(historyToolbar);
        final ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);


        textView = findViewById(R.id.noHistoryTextView);
        sadFace = findViewById(R.id.sadFace);

        //If history is empty, show No History smiley
        SQLiteDatabase myDB = this.openOrCreateDatabase("timeCard.db", MODE_PRIVATE, null);
        Cursor cur = myDB.rawQuery("SELECT COUNT(*) FROM \"history_menu\"", null);
        if (cur != null){
            cur.moveToFirst();
            if (cur.getInt(0) != 0) {
                textView.setVisibility(View.GONE);
                sadFace.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                sadFace.setVisibility(View.VISIBLE);
            }
            cur.close();
        }





        mCrimeRecyclerView = findViewById(R.id.recyclerView);

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        TimeCardLab timeCardLab = TimeCardLab.get(getApplicationContext());
        timeCards = timeCardLab.getTimeCards();
        if (mAdapter == null) {
            mAdapter = new TimeCardAdapter(timeCards);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(timeCards);
        }
        mCrimeRecyclerView.setItemViewCacheSize(timeCards.size());
        mCrimeRecyclerView.scrollToPosition(timeCards.size() - 1);


    }

    @Override
    public void isCleared(boolean cleared) {
        mClearAll = cleared;
        if(mClearAll){
            TimeCardLab timeCardLab = TimeCardLab.get(getApplicationContext());
            timeCardLab.deleteAll();
            Toast.makeText(this, "History cleared.", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }
    }

    @Override
    public void isDeleted(boolean cleared) {
        TimeCardLab timeCardLab = TimeCardLab.get(getApplicationContext());
        for(int i = 0; i < deletedTimeCards.size(); i++) {
            timeCardLab.searchAndDelete(deletedTimeCards.get(i).getId());

        }

        TypedValue outValue = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        for(int i = 0; i < viewArrayList.size()-1; i++) {
            viewArrayList.get(i).setBackgroundResource(outValue.resourceId);
        }
        viewArrayList.clear();
        uuidArrayList.clear();
        menuDelete.setVisible(false);
        menuEdit.setVisible(false);
        menuClear.setVisible(true);
        longPressed = false;
        historyToolbar.setTitle("History");
        historyToolbar.setNavigationIcon(R.drawable.arrow_back);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private class TimeCardAdapter extends RecyclerView.Adapter<TimeCardAdapter.TimeCardHolder> {




        TimeCardAdapter(List<TimeCard> timeCards) {
            mTimeCards = timeCards;
        }

        @NonNull
        @Override
        public TimeCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new TimeCardHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeCardHolder holder, int position) {


            timeCard = mTimeCards.get(position);
            adapterPosition = position;
            //timeCard.setmPosition(position);

            holder.bind(timeCard);


        }

        @Override
        public int getItemCount() {
            return mTimeCards.size();
        }

        void setCrimes(List<TimeCard> crimes) {
            mTimeCards = crimes;
        }


        private class TimeCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private TextView mStartTimeTextView;
            private TextView mDateTextView;
            private TextView mProductivityTextView;
            private TextView mPaidTime;
            private TimeCard mTimeCard;




            TimeCardHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_time_card, parent, false));

                ConstraintLayout listItemLayout = itemView.findViewById(R.id.listItemConstraintLayout);
                mDateTextView = itemView.findViewById(R.id.list_date);
                mStartTimeTextView = itemView.findViewById(R.id.unPaidTime);
                mProductivityTextView = itemView.findViewById(R.id.productivityTextView);
                mPaidTime = itemView.findViewById(R.id.list_paid);
                if(Preferences.getDarkMode(getApplicationContext())){
                    mDateTextView.setTextColor(getResources().getColor(R.color.colorWgite));
                    mStartTimeTextView.setTextColor(getResources().getColor(R.color.colorWgite));
                    mProductivityTextView.setTextColor(getResources().getColor(R.color.colorWgite));
                    mPaidTime.setTextColor(getResources().getColor(R.color.colorWgite));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        listItemLayout.setBackground(getDrawable(R.drawable.ripple_textbox));
                    }

                }
                else{
                    mDateTextView.setTextColor(getResources().getColor(R.color.colorBlack));
                    mStartTimeTextView.setTextColor(getResources().getColor(R.color.darkGray));
                    mProductivityTextView.setTextColor(getResources().getColor(R.color.colorBlack));
                    mPaidTime.setTextColor(getResources().getColor(R.color.darkGray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        listItemLayout.setBackground(getDrawable(R.drawable.ripple_textbox));
                    }
                }
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);

            }

            void bind(TimeCard crime) {

                mTimeCard = crime;
                mDateTextView.setText(mTimeCard.getDate());
                mStartTimeTextView.setText(mTimeCard.getStartTime() + " - " + mTimeCard.getEndTime());
                mProductivityTextView.setText(mTimeCard.getProductivity() + "%");
                mPaidTime.setText(mTimeCard.getPaidTime() + "");

            }


            @Override
            public void onClick(View v) {

                if(!longPressed) {
                    adapterPosition = getAdapterPosition();
                    Intent intent = HistoryTimeCardActivity.newIntent(getApplicationContext(), mTimeCard.getId());
                    startActivity(intent);
                }
                else {
                    updateShade(mTimeCard.getId(), itemView, mTimeCard, mTimeCard.getmPaidTimeInt(), mTimeCard.getmProductivityDouble());

                }

            }

            @Override
            public boolean onLongClick(View view) {

                if(!longPressed) {
                    uuidArrayList.add(mTimeCard.getId());
                    deletedTimeCards.add(mTimeCard);
                    longPressed = true;
                    menuDelete.setVisible(true);
                    viewArrayList.add(itemView);
                    menuEdit.setVisible(true);
                    menuClear.setVisible(false);
                    historyToolbar.setTitle(null);

                    if (Preferences.getDarkMode(getApplicationContext())) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        view.setBackgroundColor(getResources().getColor(R.color.highlighted));
                    }
                    historyToolbar.setNavigationIcon(R.drawable.ex_icon);
                    //selectedView = v;

                }
                return true;
            }

        } void updateShade(UUID uuid, View view, TimeCard mTimeCard, Integer paidTime, Double prod){

            if (uuidArrayList.contains(uuid)){
                view.setBackgroundColor(0);
                uuidArrayList.remove(uuid);
                deletedTimeCards.remove(mTimeCard);
            } else {
                uuidArrayList.add(uuid);
                if (Preferences.getDarkMode(getApplicationContext())) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.highlighted));
                }

                viewArrayList.add(view);
                deletedTimeCards.add(mTimeCard);
            }
            if(uuidArrayList.size()==0) onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if(Preferences.getPrefDelete(this)) {
            Preferences.setPrefDelete(this, false);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else if(Preferences.getDateChange(this)){
            Preferences.setDateChange(this, false);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        else {
            TimeCardLab crimeLab = TimeCardLab.get(getApplicationContext());
            List<TimeCard> crimes = crimeLab.getTimeCards();

            mAdapter.setCrimes(crimes);
            mAdapter.notifyItemChanged(adapterPosition);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_menu, menu);

        menuClear = menu.findItem(R.id.historyMenuClear);
        menuDelete = menu.findItem(R.id.historyMenuDelete);
        menuEdit = menu.findItem(R.id.historyMenuEdit);
        menuDelete.setVisible(false);
        menuEdit.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.historyMenuClear: {
                FragmentManager fm = getSupportFragmentManager();
                ClearFragment cm = new ClearFragment();
                fm.beginTransaction().add(cm, "clear").commit();

                return true;
            }
            case android.R.id.home: {

                if(longPressed){
                    TypedValue outValue = new TypedValue();
                    getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                    for(int i = 0; i < viewArrayList.size(); i++) {
                        viewArrayList.get(i).setBackgroundResource(outValue.resourceId);
                    }
                    viewArrayList.clear();
                    uuidArrayList.clear();
                    timeWorkedArray.clear();
                    menuDelete.setVisible(false);
                    menuEdit.setVisible(false);
                    menuClear.setVisible(true);
                    longPressed = false;
                    historyToolbar.setTitle("History");
                    historyToolbar.setNavigationIcon(R.drawable.arrow_back);
                    prodArray.clear();
                    deletedTimeCards.clear();

                    return true;
                }else {
                    onBackPressed();
                    return true;
                }

            }case R.id.historyMenuDelete:{
                FragmentManager fm = getSupportFragmentManager();
                DeleteHistoryItemDialogActivity cm = new DeleteHistoryItemDialogActivity();
                fm.beginTransaction().add(cm, "delete_item").commit();
                return true;
            }case R.id.historyMenuEdit:{

                for(int i = 0; i<deletedTimeCards.size(); i++){
                    double p = Double.parseDouble(deletedTimeCards.get(i).getProductivity());
                    double h = deletedTimeCards.get(i).getmPaidTimeInt();
                    prodArray.add(p*h);
                    timeWorkedArray.add(h);
                }
                double totalProd = 0;
                for(int i = 0; i < prodArray.size(); i++){
                    totalProd = totalProd + prodArray.get(i);
                }

                double totalTimeWorked = 0;
                for(int i = 0; i < timeWorkedArray.size(); i++){
                    totalTimeWorked = totalTimeWorked + timeWorkedArray.get(i);
                }

                FragmentManager fm = getSupportFragmentManager();
                StatsFragment cm = StatsFragment.newInstance(totalTimeWorked, totalProd);
                fm.beginTransaction().add(cm, "stats_get").commit();

                prodArray.clear();
                timeWorkedArray.clear();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(longPressed){
            TypedValue outValue = new TypedValue();
            getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            for(int i = 0; i < viewArrayList.size(); i++) {
                viewArrayList.get(i).setBackgroundResource(outValue.resourceId);
            }
            timeWorkedArray.clear();
            uuidArrayList.clear();
            viewArrayList.clear();
            menuDelete.setVisible(false);
            menuEdit.setVisible(false);
            menuClear.setVisible(true);
            longPressed = false;
            historyToolbar.setTitle("History");
            historyToolbar.setNavigationIcon(R.drawable.arrow_back);
            prodArray.clear();
            deletedTimeCards.clear();
        }else {
            super.onBackPressed();
        }
    }




}
