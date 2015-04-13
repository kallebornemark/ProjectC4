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
import projectc4.c4.client.MainActivity;
import projectc4.c4.util.C4Color;

import static projectc4.c4.util.C4Constants.LOCAL;

public class MenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // init buttons
        Button buttonLocalGame = (Button)view.findViewById(R.id.localGame);
        Button buttonMultiplayer = (Button)view.findViewById(R.id.buttonMultiplayer);
        Button buttonSocial = (Button)view.findViewById(R.id.buttonSocial);
        Button buttonHighscore = (Button)view.findViewById(R.id.buttonHigh);
        Button buttonHow = (Button)view.findViewById(R.id.buttonAbout);

        // InitGraphcs
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        buttonLocalGame.setTypeface(type,Typeface.BOLD);
        buttonMultiplayer.setTypeface(type,Typeface.BOLD);
        buttonSocial.setTypeface(type,Typeface.BOLD);
        buttonHighscore.setTypeface(type,Typeface.BOLD);
        buttonHow.setTypeface(type,Typeface.BOLD);

        buttonLocalGame.setTextColor(C4Color.WHITE);
        buttonMultiplayer.setTextColor(C4Color.WHITE);
        buttonSocial.setTextColor(C4Color.WHITE);
        buttonHighscore.setTextColor(C4Color.WHITE);
        buttonHow.setTextColor(C4Color.WHITE);

        // Init listeners
        buttonLocalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getClientController().setGameMode(LOCAL);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                transaction.replace(R.id.container, new GameFragment()).addToBackStack(null).commit();
            }
        });

        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getClientController().connect();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, new MatchmakingFragment()).addToBackStack(null).commit();
            }
        });

        buttonSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
