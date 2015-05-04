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
import android.widget.ProgressBar;

import c4.utils.C4Constants;
import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
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

        clientController.setMatchmakingFragment(this);

        // Init buttons
        final Button buttonFindOpponent = (Button)view.findViewById(R.id.buttonFindOpponent);
        Button buttonMyProfile = (Button)view.findViewById(R.id.buttonMyProfile);
        Button buttonLeaderboard = (Button)view.findViewById(R.id.buttonLeaderboard);
        Button buttonLogout = (Button)view.findViewById(R.id.buttonLogout);

        // Init buttongraphics
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        buttonMyProfile.setTypeface(type, Typeface.BOLD);
        buttonFindOpponent.setTypeface(type, Typeface.BOLD);
        buttonLeaderboard.setTypeface(type, Typeface.BOLD);
        buttonLogout.setTypeface(type, Typeface.BOLD);

        // Init progressbar
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBarLarge);

        // Init listeners
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        buttonFindOpponent.setOnClickListener(buttonClickListener);
        buttonMyProfile.setOnClickListener(buttonClickListener);
        buttonLeaderboard.setOnClickListener(buttonClickListener);
        buttonLogout.setOnClickListener(buttonClickListener);

        return view;
    }

    public void startGameUI() {
//        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBarLarge);
//        progressBar.setEnabled(false);
//        progressBar.setVisibility(View.INVISIBLE);
        System.out.println("startGameUI");
        clientController.setGameMode(C4Constants.MATCHMAKING);
        clientController.newGame(C4Constants.MATCHMAKING);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        System.out.println("Gör transaktion");
//        fragmentManager.popBackStack();
        transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
        transaction.replace(R.id.activity_layout_fragmentpos, new GameFragment()).addToBackStack("multi-game").commit();
        System.out.println("Har gjort transaktion");


    }

    /**
     * Private class that handels the buttons in the MatchmakingFragment.
     */
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);

            switch (v.getId()) {
                case R.id.buttonFindOpponent:
                    Button buttonFindOpponent = (Button) v;
                    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBarLarge);
                    if (buttonFindOpponent.getText() == "Cancel search") {
                        System.out.println("Cancel search!!!");
                        clientController.cancelSearch();
                        buttonFindOpponent.setBackground(getActivity().getDrawable(R.drawable.colorred));
                        buttonFindOpponent.setText("Find Opponent");
                        progressBar.setEnabled(false);
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        clientController.requestGame(C4Constants.MATCHMAKING);
                        progressBar.setEnabled(true);
                        progressBar.setVisibility(View.VISIBLE);
//                        buttonFindOpponent.setEnabled(false);
                        buttonFindOpponent.setText("Cancel search");
                        buttonFindOpponent.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    }
                    break;

                case R.id.buttonMyProfile:
                    transaction.replace(R.id.activity_layout_fragmentpos, new ProfileFragment()).addToBackStack("Profilefragment").commit();
                    break;

                case R.id.buttonLeaderboard:

                    break;

                case R.id.buttonLogout:
                    try {
                        ((MainActivity) getActivity()).getClientController().getClient().disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fragmentManager.popBackStackImmediate("Menu", 0);

                    break;
            }

        }

    }


}
