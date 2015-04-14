package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import projectc4.c4.R;

/**
 * Created by Emil on 2015-04-14.
 */
public class HowToPlayFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_howtoplay, container, false);

        TextView textViewTap = (TextView)view.findViewById(R.id.textViewTap);
        TextView textViewSwipe = (TextView)view.findViewById(R.id.textViewSwipe);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        textViewTap.setTypeface(type, Typeface.BOLD);
        textViewSwipe.setTypeface(type, Typeface.BOLD);

        return view;
    }
}
