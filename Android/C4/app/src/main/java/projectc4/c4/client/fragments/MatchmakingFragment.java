package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import projectc4.c4.R;

/**
 * Created by Erik on 2015-04-09.
 */
public class MatchmakingFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matchmaking, container, false);
        this.view = view;

        return view;


    }
}
