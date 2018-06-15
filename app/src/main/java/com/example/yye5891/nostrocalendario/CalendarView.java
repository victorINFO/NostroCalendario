package com.example.yye5891.nostrocalendario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarView extends Activity {

    public GregorianCalendar month, itemmonth;

    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items;

    public ArrayList<Presenze> arrayP = new ArrayList<>();
    Date giorno1, giorno2, giorno3,giorno4;

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar2);
        Locale.setDefault( Locale.ITALY );
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        final ImageView pulse_left = findViewById(R.id.pulse_left);
        final ImageView pulse_right = findViewById(R.id.pulse_right);

        pulse_left.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse_left_out));
        pulse_right.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse_right_out));

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this, month, arrayP);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);


        String dtStart = "2018-03-18";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            giorno1 = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dtStart2 = "2018-04-16";
        try {
            giorno2 = format.parse(dtStart2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dtStart3 = "2018-06-14";
        try {
            giorno3 = format.parse(dtStart3);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dtStart4 = "2018-05-11";
        try {
            giorno4 = format.parse(dtStart4);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        arrayP.add(new Presenze(giorno1, "presente", "trasferta"));
        arrayP.add(new Presenze(giorno2, "assente", "presente"));
        arrayP.add( new Presenze(giorno3, "trasferta", "assente"));
        arrayP.add( new Presenze(giorno4, "assente", "presente"));


        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));


        gridview.setOnTouchListener(new OnSwipeTouchListener(CalendarView.this){


            public void onSwipeRight(){
                setPreviousMonth();
                refreshCalendar();
                pulse_left.clearAnimation();
                pulse_right.clearAnimation();
                pulse_left.setVisibility(View.GONE);
                pulse_right.setVisibility(View.GONE);
            }

            public void onSwipeLeft(){
                setNextMonth();
                refreshCalendar();
                pulse_left.clearAnimation();
                pulse_right.clearAnimation();
                pulse_left.setVisibility(View.GONE);
                pulse_right.setVisibility(View.GONE);
            }
        });



        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                //selectedGridDate
            }
        });
    }

    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);

        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

        adapter.refreshTextView();

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALY);
            String itemvalue;

        }
    };
}
