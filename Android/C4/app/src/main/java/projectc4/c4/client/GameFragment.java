package projectc4.c4.client;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import projectc4.c4.R;
import projectc4.c4.util.C4Color;

import static projectc4.c4.util.C4Constants.LOCAL;
import static projectc4.c4.util.C4Constants.MATCHMAKING;
import static projectc4.c4.util.C4Constants.PLAYER1;
import static projectc4.c4.util.C4Constants.PLAYER2;

/**
 * Created by Emil on 2015-04-09.
 */
    public class GameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        return view;
    }
}
