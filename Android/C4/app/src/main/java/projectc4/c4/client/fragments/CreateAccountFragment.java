package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import projectc4.c4.R;
import projectc4.c4.util.C4Color;

/**
 * Created by Emil on 2015-04-29.
 */
public class CreateAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createaccount, container, false);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        TextView textHeader = (TextView)view.findViewById(R.id.textViewHeader);
        textHeader.setTypeface(type,Typeface.BOLD);
        textHeader.setTextColor(C4Color.BLACK) ;
        return view;
    }
}
