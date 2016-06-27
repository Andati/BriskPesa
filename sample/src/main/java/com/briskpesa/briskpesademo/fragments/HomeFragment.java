package com.briskpesa.briskpesademo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.briskpesa.briskpesademo.R;
import com.mobiworld.briskpesa.BriskPesaCallBack;
import com.mobiworld.briskpesa.BriskPesaException;
import com.mobiworld.briskpesa.BriskPesaView;

import com.briskpesa.briskpesademo.DBHelper;

/**
 * Created by rodgers on 05/05/16.
 */
public class HomeFragment extends Fragment {
    DBHelper mydb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mydb = new DBHelper(getActivity());

        BriskPesaView briskPesa = (BriskPesaView) v.findViewById(R.id.briskpesa);

        try {
            briskPesa.setPhoneNumber(mydb.getPhoneNumber());

            briskPesa.bpCallBack = new BriskPesaCallBack() {
                public void callbackCall(int status, String mpesaCode, String desc) {
                    //TODO use params as necessary
                    Log.d("callBack", status + " " + mpesaCode + " " + desc);
                }
            };
        }
        catch (BriskPesaException e) {
            e.printStackTrace();
        }

        return v;
    }

}
