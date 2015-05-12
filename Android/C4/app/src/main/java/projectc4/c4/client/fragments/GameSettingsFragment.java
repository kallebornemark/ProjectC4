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
import android.widget.TextView;

import java.net.Socket;
import java.util.Random;

import c4.utils.C4Constants;
import projectc4.c4.R;
import projectc4.c4.client.GameController;
import projectc4.c4.client.MainActivity;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class GameSettingsFragment extends Fragment {
    private Random rand = new Random();
    private Button sixgrid;
    private Button nine;
    private Button thirteen;
    private Button one;
    private Button three;
    private Button five;
    private Button player1;
    private Button player2;
    private Button random;
    private Button startGame;
    private GameController gameController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamesettings, container, false);
        gameController =  ((MainActivity)getActivity()).getGameController();

        sixgrid  = (Button)view.findViewById(R.id.sixseven);
        nine  = (Button)view.findViewById(R.id.nineten);
        thirteen  = (Button)view.findViewById(R.id.twelethirteen);
        one  = (Button)view.findViewById(R.id.one);
        three  = (Button)view.findViewById(R.id.three);
        five  = (Button)view.findViewById(R.id.five);
        player1 = (Button)view.findViewById(R.id.startplayer1);
        player2  = (Button)view.findViewById(R.id.startplayer2);
        random  = (Button)view.findViewById(R.id.startrandom);
        startGame = (Button)view.findViewById((R.id.startGame));
        TextView board  = (TextView)view.findViewById(R.id.boardsize);
        TextView rounds = (TextView)view.findViewById(R.id.rounds);
        TextView startingplayer  = (TextView)view.findViewById(R.id.startingplayer);



        ButtonClickListener buttonClickListener = new ButtonClickListener();

        sixgrid.setOnClickListener(buttonClickListener);
        nine.setOnClickListener(buttonClickListener);
        thirteen.setOnClickListener(buttonClickListener);
        one.setOnClickListener(buttonClickListener);
        three.setOnClickListener(buttonClickListener);
        five.setOnClickListener(buttonClickListener);
        player1.setOnClickListener(buttonClickListener);
        player2.setOnClickListener(buttonClickListener);
        random.setOnClickListener(buttonClickListener);
        startGame.setOnClickListener(buttonClickListener);



        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        sixgrid.setTypeface(type, Typeface.BOLD);
        nine.setTypeface(type, Typeface.BOLD);
        thirteen.setTypeface(type, Typeface.BOLD);
        one.setTypeface(type, Typeface.BOLD);
        three.setTypeface(type, Typeface.BOLD);
        five.setTypeface(type, Typeface.BOLD);
        player1.setTypeface(type, Typeface.BOLD);
        player2.setTypeface(type, Typeface.BOLD);
        random.setTypeface(type, Typeface.BOLD);
        startingplayer.setTypeface(type, Typeface.BOLD);
        board.setTypeface(type, Typeface.BOLD);
        rounds.setTypeface(type, Typeface.BOLD);
        startGame.setTypeface(type, Typeface.BOLD);

        if(gameController.getRows() == 6) {
            sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorred));
            nine.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
            thirteen.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));

        } else if(gameController.getRows() == 9) {
            sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
            nine.setBackground(getActivity().getDrawable(R.drawable.colorred));
            thirteen.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));

        } else if(gameController.getRows() == 12) {
            sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
            nine.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
            thirteen.setBackground(getActivity().getDrawable(R.drawable.colorred));

        }

        if(gameController.getRounds() == 1) {
            one.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
            three.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
            five.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));

        } else if((gameController.getRounds() == 3)) {
            one.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
            three.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
            five.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));

        } else if ((gameController.getRounds() == 5)) {
            one.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
            three.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
            five.setBackground(getActivity().getDrawable(R.drawable.coloryellow));

        }

        if (gameController.getRandom()) {
            player1.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
            player2.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
            random.setBackground(getActivity().getDrawable(R.drawable.colorblack));

            if (rand.nextInt(2) == 1) {
                gameController.setStartingPlayer(C4Constants.PLAYER1);
            } else {
                gameController.setStartingPlayer(C4Constants.PLAYER2);
            }

        } else if (gameController.getStartingPlayer() == C4Constants.PLAYER2) {
            player1.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
            player2.setBackground(getActivity().getDrawable(R.drawable.colorblack));
            random.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));

        } else if (gameController.getStartingPlayer() == C4Constants.PLAYER1) {
            player1.setBackground(getActivity().getDrawable(R.drawable.colorblack));
            player2.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
            random.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));

        }

        return view;
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);

            switch (v.getId()) {
                case R.id.sixseven:
                    sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    nine.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    thirteen.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                   gameController.setArraySize(6, 7);
                    break;

                case R.id.nineten:
                    sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    nine.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    thirteen.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                   gameController.setArraySize(9, 10);
                    break;

                case R.id.twelethirteen:
                    sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    nine.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    thirteen.setBackground(getActivity().getDrawable(R.drawable.colorred));
                   gameController.setArraySize(12, 13);
                    break;

                case R.id.one:
                    one.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    three.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    five.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                   gameController.setRounds(1);
                    break;

                case R.id.three:
                    one.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    three.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    five.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                   gameController.setRounds(3);
                    break;

                case R.id.five:
                    one.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    three.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    five.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                   gameController.setRounds(5);
                    break;

                case R.id.startplayer1:
                    player1.setBackground(getActivity().getDrawable(R.drawable.colorblack));
                    player2.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    random.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    gameController.setRandom(false);
                   gameController.setStartingPlayer(C4Constants.PLAYER1);
                    break;

                case R.id.startplayer2:
                    player1.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    player2.setBackground(getActivity().getDrawable(R.drawable.colorblack));
                    random.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    gameController.setRandom(false);
                   gameController.setStartingPlayer(C4Constants.PLAYER2);
                    break;

                case R.id.startrandom:
                    player1.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    player2.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    random.setBackground(getActivity().getDrawable(R.drawable.colorblack));
                    gameController.setRandom(true);
                    if(rand.nextInt(2) == 0) {
                        ((MainActivity) getActivity()).getGameController().setStartingPlayer(C4Constants.PLAYER1);
                    } else {
                        ((MainActivity) getActivity()).getGameController().setStartingPlayer(C4Constants.PLAYER2);
                    }
                    break;

                case R.id.startGame:
                    ((MainActivity)getActivity()).getClientController().setGameMode(C4Constants.LOCAL);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
                    transaction.replace(R.id.activity_layout_fragmentpos, new GameFragment()).addToBackStack("Local").commit();
                    gameController.setPlayer2Points(0);
                    gameController.setPlayer1Points(0);
                    break;
            }

        }

    }
}
