package com.example.wakewake.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.wakewake.CalendarViewModel;
import com.example.wakewake.R;
import com.example.wakewake.calendar.Event;
import com.example.wakewake.calendar.Utils;
import com.example.wakewake.data.CalendarDay;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerDaysView;
    LinearLayoutManager layoutDaysManager;
    CalendarDaysAdapter daysAdapter;

    private CalendarViewModel viewModel;


    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaldendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(this).get(CalendarViewModel .class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_calendar, container, false);
        return viewRoot;
    }

    private void displayfragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerDaysView = view.findViewById(R.id.recyclerDayView);


        // Set up the days recycler view
        layoutDaysManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        daysAdapter = new CalendarDaysAdapter(viewModel.getDaysString());
        recyclerDaysView.setLayoutManager(layoutDaysManager);
        recyclerDaysView.setAdapter(daysAdapter);

        // Set up the view pager
        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(getContext(), viewModel.getDays());
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPosition = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (currentPosition < position) {
                    recyclerDaysView.smoothScrollToPosition(position+1);
                } else {
                    if (position-1 >= 0)
                        recyclerDaysView.smoothScrollToPosition(position-1);
                    else
                        recyclerDaysView.smoothScrollToPosition(0);
                }
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

    }

    class CalendarDaysAdapter extends RecyclerView.Adapter<CalendarDaysAdapter.ViewHolder> {

        List<String> days;
        public CalendarDaysAdapter(List<String> days) {
            super();
            this.days = days;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.day.setText(days.get(position));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager viewPager = getActivity().findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            TextView day;
            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                day = itemView.findViewById(R.id.dayText);
            }
        }
    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context context;
        ArrayList<CalendarDay> days;

        public CustomPagerAdapter(Context context, ArrayList<CalendarDay> days) {
            this.context = context;
            this.days = days;
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.day_events_item, container, false);
            LinearLayout events = view.findViewById(R.id.eventsLayout);
            CalendarDay day = days.get(position);
            for (Event event : day.getEvents()) {
                View eventView = inflater.inflate(R.layout.event_item, events, false);
                TextView eventTitle = eventView.findViewById(R.id.eventTitle);
                LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
                if (event.getDtStart().isBefore(now) && event.getDtEnd().isAfter(now)) {
                    eventTitle.setText(event.getSummary() + " (En cours)");
                } else {
                    Log.d("Event", LocalDateTime.now(ZoneId.of("UTC")).toString());
                    Log.d("Event", event.getDtStart().toString());
                    Log.d("Event", event.getDtEnd().toString());
                    eventTitle.setText(event.getSummary());
                }
                TextView eventTime = eventView.findViewById(R.id.eventTime);
                eventTime.setText(Utils.formatTime(event.getDtStart()) + " - " + Utils.formatTime(event.getDtEnd()));
                TextView eventDescription = eventView.findViewById(R.id.eventDescription);
                String location ="", description = "";
                if (event.getLocation() != null)
                    location = event.getLocation();
                if (event.getDescription() != null)
                    description = event.getDescription();
                String fullDescription = "";
                if (location.equals("")){
                    fullDescription = description;
                } else if (description.equals("")) {
                    fullDescription = location;
                } else {
                    fullDescription = location + " -" + description;
                }
                if (fullDescription.length() <= 45)
                    eventDescription.setText(fullDescription);
                else if (fullDescription.length() > 45)
                    eventDescription.setText(fullDescription.substring(0, 45) + "...");
                events.addView(eventView);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}