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
import projectc4.c4.client.MainActivity;

/**
 * Created by Emil on 2015-05-06.
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamesettings, container, false);

        sixgrid  = (Button)view.findViewById(R.id.sixseven);
        nine  = (Button)view.findViewById(R.id.nineten);
        thirteen  = (Button)view.findViewById(R.id.twelethirteen);
        one  = (Button)view.findViewById(R.id.one);
        three  = (Button)view.findViewById(R.id.three);
        five  = (Button)view.findViewById(R.id.five);
        player1 = (Button)view.findViewById(R.id.startplayer1);
        player2  = (Button)view.findViewById(R.id.startplayer2);
        random  = (Button)view.findViewById(R.id.startrandom);
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
                    ((MainActivity)getActivity()).getGameController().setArraySize(6, 7);
                    break;

                case R.id.nineten:
                    sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    nine.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    thirteen.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    ((MainActivity)getActivity()).getGameController().setArraySize(9, 10);
                    break;

                case R.id.twelethirteen:
                    sixgrid.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    nine.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                    thirteen.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    ((MainActivity)getActivity()).getGameController().setArraySize(12, 13);
                    break;

                case R.id.one:
                    one.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    three.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    five.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    ((MainActivity)getActivity()).getGameController().setRounds(1);
                    break;

                case R.id.three:
                    one.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    three.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    five.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    ((MainActivity)getActivity()).getGameController().setRounds(3);
                    break;

                case R.id.five:
                    one.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    three.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                    five.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    ((MainActivity)getActivity()).getGameController().setRounds(5);
                    break;

                case R.id.startplayer1:
                    player1.setBackground(getActivity().getDrawable(R.drawable.colorblack));
                    player2.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    random.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    ((MainActivity)getActivity()).getGameController().setStartingPlayer(C4Constants.PLAYER1);
                    break;

                case R.id.startplayer2:
                    player1.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    player2.setBackground(getActivity().getDrawable(R.drawable.colorblack));
                    random.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    ((MainActivity)getActivity()).getGameController().setStartingPlayer(C4Constants.PLAYER2);
                    break;

                case R.id.startrandom:
                    player1.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    player2.setBackground(getActivity().getDrawable(R.drawable.colorblackpressed));
                    random.setBackground(getActivity().getDrawable(R.drawable.colorblack));
                    if(rand.nextInt(1) == 0) {
                        ((MainActivity) getActivity()).getGameController().setStartingPlayer(C4Constants.PLAYER1);
                    } else {
                        ((MainActivity) getActivity()).getGameController().setStartingPlayer(C4Constants.PLAYER2);
                    }
                    break;
            }

        }

    }
}
