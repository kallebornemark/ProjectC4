package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import projectc4.c4.R;

/**
 * Created by Emil on 2015-05-06.
 */
public class GameSettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamesettings, container, false);


        Button sixgrid  = (Button)view.findViewById(R.id.sixseven);
        Button nine  = (Button)view.findViewById(R.id.nineten);
        Button thirteen  = (Button)view.findViewById(R.id.twelethirteen);
        Button one  = (Button)view.findViewById(R.id.one);
        Button three  = (Button)view.findViewById(R.id.three);
        Button six  = (Button)view.findViewById(R.id.six);
        Button player1 = (Button)view.findViewById(R.id.startplayer1);
        Button player2  = (Button)view.findViewById(R.id.startplayer2);
        Button random  = (Button)view.findViewById(R.id.startrandom);
        TextView board  = (TextView)view.findViewById(R.id.boardsize);
        TextView rounds = (TextView)view.findViewById(R.id.rounds);
        TextView startingplayer  = (TextView)view.findViewById(R.id.startingplayer);


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        sixgrid.setTypeface(type, Typeface.BOLD);
        nine.setTypeface(type, Typeface.BOLD);
        thirteen.setTypeface(type, Typeface.BOLD);
        one.setTypeface(type, Typeface.BOLD);
        three.setTypeface(type, Typeface.BOLD);
        six.setTypeface(type, Typeface.BOLD);
        player1.setTypeface(type, Typeface.BOLD);
        player2.setTypeface(type, Typeface.BOLD);
        random.setTypeface(type, Typeface.BOLD);
        startingplayer.setTypeface(type, Typeface.BOLD);
        board.setTypeface(type, Typeface.BOLD);
        rounds.setTypeface(type, Typeface.BOLD);

        return view;
    }
}
