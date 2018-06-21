package com.example.yye5891.nostrocalendario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;

public class CalendarView extends Activity {

    public GregorianCalendar month, itemmonth;

    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public ArrayList<Presenze> arrayP = new ArrayList<>();
    String giorno1, giorno2, giorno3, giorno4, giorno5;
    Date date1;
    int anno_cercato = 0;
    ImageView left, right;

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar2);
        Locale.setDefault(Locale.US);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this, month, arrayP);

        final GridView gridview = (GridView) findViewById(R.id.gridview);

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);

        gridview.setAdapter(adapter);


        giorno1 = "2018-06-18";
        giorno2 = "2018-04-16";
        giorno3 = "2018-05-14";
        giorno4 = "2018-04-11";
        giorno5 = "2018-05-10";


        arrayP.add(new Presenze(giorno1, "presente", "trasferta"));
        arrayP.add(new Presenze(giorno2, "assente", "presente"));
        arrayP.add(new Presenze(giorno3, "trasferta", "assente"));
        arrayP.add(new Presenze(giorno4, "assente", "assente"));
        arrayP.add(new Presenze(giorno5, "presente", "presente"));
        arrayP.add(new Presenze("2018-03-12", "trasferta", "trasferta"));
        arrayP.add(new Presenze("2018-03-03", "presente", "assente"));
        arrayP.add(new Presenze("2018-03-31", "trasferta", "presente"));

        handler = new Handler();
        handler.post(calendarUpdater);

        final TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));



        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });
/*
        gridview.setOnTouchListener(new OnSwipeTouchListener(CalendarView.this) {


            public void onSwipeRight() {
                setPreviousMonth();
                refreshCalendar();
            }

            public void onSwipeLeft() {
                setNextMonth();
                refreshCalendar();
            }
        });

*/
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

                DialogFragment df = new DialogFragment();
            }
        });


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int p_year = cal.get(Calendar.YEAR);
                int p_month = cal.get(Calendar.MONTH);
                int p_day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(CalendarView.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        mDateSetListener, p_year, p_month, p_day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int p_year, int p_month, int p_day) {
                p_month = p_month +1;
                String date = p_year + "-" + p_month + "-" + p_day;

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date1 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                title.setText(android.text.format.DateFormat.format("MMMM yyyy", date1));

                anno_cercato = p_year;
                setMonth();
                refreshCalendar();
            }

        };
    }


    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);

        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

        adapter.refreshTextView();

    }

    protected void setMonth() {
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);

        } else {

            month.set(GregorianCalendar.MONTH, date1.getMonth());
            month.set(GregorianCalendar.YEAR, anno_cercato);

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
            for (int i = 0; i < 7; i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add("2012-09-12");
                items.add("2012-10-07");
                items.add("2012-10-15");
                items.add("2012-10-20");
                items.add("2012-11-30");
                items.add("2012-11-28");
            }

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };
}
