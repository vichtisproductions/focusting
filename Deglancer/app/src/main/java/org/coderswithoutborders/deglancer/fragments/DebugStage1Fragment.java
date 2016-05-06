package org.coderswithoutborders.deglancer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.coderswithoutborders.deglancer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DebugStage1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugStage1Fragment extends Fragment {


    public DebugStage1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DebugStage1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DebugStage1Fragment newInstance() {
        DebugStage1Fragment fragment = new DebugStage1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_debug_stage1, container, false);
    }

}
