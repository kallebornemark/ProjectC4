package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import projectc4.c4.R;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView emil = (TextView)view.findViewById(R.id.emil);
        TextView kalle = (TextView)view.findViewById(R.id.kalle);
        TextView jimmy = (TextView)view.findViewById(R.id.jimmy);
        TextView erik = (TextView)view.findViewById(R.id.erik);
        TextView emildesc = (TextView)view.findViewById(R.id.emildesc);
        TextView erikdesc = (TextView)view.findViewById(R.id.erikdesc);
        TextView jimmydesc = (TextView)view.findViewById(R.id.jimmydesc);
        TextView kalledesc = (TextView)view.findViewById(R.id.kalledesc);
        TextView freepick = (TextView)view.findViewById(R.id.freepick);
        TextView project = (TextView)view.findViewById(R.id.textViewProject);
        TextView version = (TextView)view.findViewById(R.id.textViewversion);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        emil.setTypeface(type,Typeface.BOLD);
        kalle.setTypeface(type,Typeface.BOLD);
        jimmy.setTypeface(type,Typeface.BOLD);
        erik.setTypeface(type,Typeface.BOLD);
        project.setTypeface(type,Typeface.BOLD);
        emildesc.setTypeface(type,Typeface.BOLD|Typeface.ITALIC);
        erikdesc.setTypeface(type,Typeface.BOLD|Typeface.ITALIC);
        jimmydesc.setTypeface(type,Typeface.BOLD|Typeface.ITALIC);
        kalledesc.setTypeface(type,Typeface.BOLD|Typeface.ITALIC);
        freepick.setTypeface(type,Typeface.BOLD);
        version.setTypeface(type,Typeface.BOLD);

        return view;
    }
}
