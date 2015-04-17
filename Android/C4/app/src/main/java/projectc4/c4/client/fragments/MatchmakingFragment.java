package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;

import static projectc4.c4.util.C4Constants.MATCHMAKING;

/**
 * Created by Erik on 2015-04-09.
 */
public class MatchmakingFragment extends Fragment {
    private View view;
    private ClientController clientController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.clientController = ((MainActivity)getActivity()).getClientController();
        View view = inflater.inflate(R.layout.fragment_matchmaking, container, false);
        this.view = view;

        initGraphics();
        final Button buttonFindOpponent = (Button)view.findViewById(R.id.buttonFindOpponent);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBarLarge);
        clientController.setMatchmakingFragment(this);

        buttonFindOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientController.requestGame(MATCHMAKING);

                progressBar.setEnabled(true);
                progressBar.setVisibility(View.VISIBLE);
                buttonFindOpponent.setEnabled(false);
                buttonFindOpponent.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
            }
        });

        Button buttonMyProfile = (Button)view.findViewById(R.id.buttonMyProfile);

        buttonMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                transaction.replace(R.id.container, new ProfileFragment()).addToBackStack(null).commit();
            }
        });

        Button buttonLeaderboard = (Button)view.findViewById(R.id.buttonLeaderboard);
        Button buttonLogout = (Button)view.findViewById(R.id.buttonLogout);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        buttonMyProfile.setTypeface(type, Typeface.BOLD);
        buttonFindOpponent.setTypeface(type, Typeface.BOLD);
        buttonLeaderboard.setTypeface(type, Typeface.BOLD);
        buttonLogout.setTypeface(type, Typeface.BOLD);

    return view;


    }

    private void initGraphics() {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        Button button = (Button)view.findViewById(R.id.buttonFindOpponent);
        button.setTypeface(type, Typeface.BOLD);
    }

    public void startGameUI() {
//        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarLarge);
//        progressBar.setEnabled(false);
//        progressBar.setVisibility(View.INVISIBLE);
        System.out.println("startGameUI");
        clientController.setGameMode(MATCHMAKING);
        clientController.newGame(MATCHMAKING);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        System.out.println("Gör transaktion");
//        fragmentManager.popBackStack();
        transaction.replace(R.id.container, new GameFragment()).addToBackStack("multi-game").commit();
        System.out.println("Har gjort transaktion");


    }




}
