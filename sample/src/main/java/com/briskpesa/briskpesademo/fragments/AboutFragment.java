package com.briskpesa.briskpesademo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.briskpesa.briskpesademo.R;

/**
 * Created by rodgers on 05/05/16.
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        return v;
    }
}
