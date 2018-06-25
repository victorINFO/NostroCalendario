package com.example.yye5891.nostrocalendario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.graphics.Color.rgb;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    private java.util.Calendar month;
    public GregorianCalendar pmonth;

    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;
    Date date;


    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    TextView dayView, mattina, pomeriggio;
    LinearLayout lay;
    View v;


    ArrayList<Presenze> arrayP;

    public CalendarAdapter(Context c, GregorianCalendar monthCalendar, ArrayList<Presenze> arrayP) {
        this.arrayP = arrayP;
        CalendarAdapter.dayString = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        month = monthCalendar;
        Log.d("formaggio", "" + month);
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
        Log.d("margherita", "" + dayString);
    }



    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;

    }



    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);

        }


        dayView = (TextView) v.findViewById(R.id.date);
        mattina = (TextView) v.findViewById(R.id.mattina);
        pomeriggio = (TextView) v.findViewById(R.id.pomeriggio);
        lay = v.findViewById(R.id.lay_item);


        Colore(position);
        //ColoreProva(position);


        String[] separatedTime = dayString.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.GRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            dayView.setTextColor(Color.BLACK);

        }

        if (dayString.get(position).equals(curentDateString)) {
            setSelected(v);
            dayView.setTextColor(Color.parseColor("#2196F3"));
            previousView = v;
        } else {
            v.setBackgroundResource(R.drawable.item_background);

            lay.setBackgroundResource(R.drawable.item_background);

        }
        dayView.setText(gridvalue);
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        return v;
    }
/*

    public void ColoreProva(int position){

        String[] separatedTime = dayString.get(position).split("-");
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        if (dayString.get(position).contains("06")){

            mattina.setBackgroundColor(mContext.getResources().getColor(R.color.verde));
            mattina.setVisibility(View.VISIBLE);
            pomeriggio.setBackgroundColor(mContext.getResources().getColor(R.color.verde));
            pomeriggio.setVisibility(View.VISIBLE);
        }else if (dayString.get(position).contains("15")) {
            mattina.setBackgroundColor(mContext.getResources().getColor(R.color.rosso));
            mattina.setVisibility(View.VISIBLE);
            pomeriggio.setBackgroundColor(mContext.getResources().getColor(R.color.rosso));
            pomeriggio.setVisibility(View.VISIBLE);
        }else{

            refreshDays();
            refreshTextView();
            notifyDataSetChanged();
        }

    }

*/


    public void Colore(int position) {
           for (int i = 0; i < arrayP.size(); i++) {

                if (!(dayString.get(position).equals(arrayP.get(i).getGiorno()))) {
                        refreshDays();
                        refreshTextView();
                        notifyDataSetChanged();
                }
                else {


                    switch (arrayP.get(i).getMattina()) {
                        case "assente":
                            mattina.setBackgroundColor(mContext.getResources().getColor(R.color.rosso));
                            mattina.setVisibility(View.VISIBLE);
                            break;
                        case "presente":
                            mattina.setBackgroundColor(mContext.getResources().getColor(R.color.verde));
                            mattina.setVisibility(View.VISIBLE);
                            break;
                        case "trasferta":
                            mattina.setBackgroundColor(mContext.getResources().getColor(R.color.azzurro));
                            mattina.setVisibility(View.VISIBLE);
                            break;


                    }

                    switch (arrayP.get(i).getPomeriggio()) {
                        case "assente":
                            pomeriggio.setBackgroundColor(mContext.getResources().getColor(R.color.rosso));
                            pomeriggio.setVisibility(View.VISIBLE);
                            break;
                        case "presente":
                            pomeriggio.setBackgroundColor(mContext.getResources().getColor(R.color.verde));
                            pomeriggio.setVisibility(View.VISIBLE);

                            break;
                        case "trasferta":
                            pomeriggio.setBackgroundColor(mContext.getResources().getColor(R.color.azzurro));
                            pomeriggio.setVisibility(View.VISIBLE);

                            break;


                    }

                    i+= arrayP.size();
                    }


           }


    }

    public View setSelected(View view) {
        if (previousView != null) {
            previousView.setBackgroundResource(R.drawable.item_background);
        }
        previousView = view;
        return view;
    }


    public void refreshDays() {
        items.clear();
        dayString.clear();
        Locale.setDefault( Locale.ITALY );
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...

        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);


        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);



        }
    }

    private int getMaxP() {
        int maxP;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

    public void refreshTextView(){
        mattina.setVisibility(View.INVISIBLE);
        pomeriggio.setVisibility(View.INVISIBLE);
    }



}