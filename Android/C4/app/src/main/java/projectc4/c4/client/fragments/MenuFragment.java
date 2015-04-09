package projectc4.c4.client.fragments;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import projectc4.c4.R;
import projectc4.c4.util.C4Color;

public class MenuFragment extends Fragment {

    public MenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);


        // Init listeners

        Button buttonLocalGame = (Button)view.findViewById(R.id.localGame);
        buttonLocalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, new GameFragment());
                transaction.commit();

            }
        });

        Button buttonMultiplayer = (Button)view.findViewById(R.id.buttonMultiplayer);
        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button buttonSocial = (Button)view.findViewById(R.id.buttonSocial);
        buttonSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button buttonHighscore = (Button)view.findViewById(R.id.buttonHigh);
        buttonHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button buttonHow = (Button)view.findViewById(R.id.buttonHow);
        buttonHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button buttonAbout = (Button)view.findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // InitGraphcs

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        Button button1 = (Button)view.findViewById(R.id.localGame);
        Button button2 = (Button)view.findViewById(R.id.buttonMultiplayer);
        Button button3 = (Button)view.findViewById(R.id.buttonSocial);
        Button button4 = (Button)view.findViewById(R.id.buttonHow);
        Button button5 = (Button)view.findViewById(R.id.buttonHigh);
        Button button6 = (Button)view.findViewById(R.id.buttonAbout);

        button1.setTypeface(type,Typeface.BOLD);
        button2.setTypeface(type,Typeface.BOLD);
        button3.setTypeface(type,Typeface.BOLD);
        button4.setTypeface(type,Typeface.BOLD);
        button5.setTypeface(type,Typeface.BOLD);
        button6.setTypeface(type,Typeface.BOLD);

        button1.setTextColor(C4Color.WHITE);
        button2.setTextColor(C4Color.WHITE);
        button3.setTextColor(C4Color.WHITE);
        button4.setTextColor(C4Color.WHITE);
        button5.setTextColor(C4Color.WHITE);
        button6.setTextColor(C4Color.WHITE);

        return view;
    }
}
