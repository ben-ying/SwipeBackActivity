package com.ben.yjh.swipebackactivity.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ben.yjh.swipebackactivity.R;
import com.ben.yjh.swipebackactivity.library.SwipeBackActivity;

public class ScreenSlidePageFragment extends Fragment {

    private static final String POSITION = "position";

    public ScreenSlidePageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pager, container, false);
        int position = getArguments().getInt(POSITION);
        switch (position) {
            case 0:
                rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                break;
            case 1:
                rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                break;
            case 2:
                rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case 3:
                rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                break;
            default:
                rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
                break;
        }

        rootView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SwipeBackActivity) getActivity()).startActivity(
                        new Intent(getActivity(), RecyclerViewActivity.class), true);
            }
        });

        return rootView;
    }
}
