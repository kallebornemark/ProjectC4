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
        Button buttonSettings = (Button)view.findViewById(R.id.buttonSocial);
        Button buttonHowToPlay = (Button)view.findViewById(R.id.buttonHigh);
        Button buttonAbout = (Button)view.findViewById(R.id.buttonAbout);

        // InitGraphcs
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        buttonLocalGame.setTypeface(type,Typeface.BOLD);
        buttonMultiplayer.setTypeface(type,Typeface.BOLD);
        buttonSettings.setTypeface(type, Typeface.BOLD);
        buttonHowToPlay.setTypeface(type, Typeface.BOLD);
        buttonAbout.setTypeface(type, Typeface.BOLD);

        buttonLocalGame.setTextColor(C4Color.WHITE);
        buttonMultiplayer.setTextColor(C4Color.WHITE);
        buttonSettings.setTextColor(C4Color.WHITE);
        buttonHowToPlay.setTextColor(C4Color.WHITE);
        buttonAbout.setTextColor(C4Color.WHITE);

        // Init listeners
        buttonLocalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getClientController().setGameMode(LOCAL);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                transaction.replace(R.id.activity_layout_fragmentpos, new GameFragment()).addToBackStack(null).commit();
            }
        });

        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((((MainActivity)getActivity()).getClientController().getClient() != null)){
                    System.out.println("Ändrar till matchmaking");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                    transaction.replace(R.id.activity_layout_fragmentpos, new MatchmakingFragment()).addToBackStack(null).commit();
                } else {
                    System.out.println("Ändrar till Login");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                    transaction.replace(R.id.activity_layout_fragmentpos, new LoginFragment()).addToBackStack("LogIn").commit();
                }
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                transaction.replace(R.id.activity_layout_fragmentpos, new SettingsFragment()).addToBackStack(null).commit();
            }
        });

        buttonHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                transaction.replace(R.id.activity_layout_fragmentpos, new HowToPlayFragment()).addToBackStack(null).commit();
            }
        });

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                transaction.replace(R.id.activity_layout_fragmentpos, new AboutFragment()).addToBackStack(null).commit();
            }
        });
        return view;
    }
}
