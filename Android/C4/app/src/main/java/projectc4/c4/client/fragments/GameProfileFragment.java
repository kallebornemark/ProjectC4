package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import projectc4.c4.R;
import projectc4.c4.client.*;
import projectc4.c4.util.C4Color;


/**
 * Created by Emil on 2015-04-09.
 */
 public class GameProfileFragment extends Fragment {
    private ClientController clientController;
    private View view;
    private TextView tvStatsPlayer1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientController = ((MainActivity)getActivity()).getClientController();
        final View view = inflater.inflate(R.layout.fragment_gameProfile, container, false);
        this.view = view;

        initGraphics(view);
        initListeners();

        return view;
    }

    public void initGraphics(View view) {
        TextView tvGameProfilePlayer1 = (TextView)view.findViewById(R.id.tvGameProfilePlayer1);
        tvStatsPlayer1 = (TextView)view.findViewById(R.id.tvStatsPlayer1);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        tvGameProfilePlayer1.setTypeface(type, Typeface.BOLD);
        tvGameProfilePlayer1.setTextColor(C4Color.WHITE);
        tvGameProfilePlayer1.setText(clientController.getUser().getUsername());

        tvStatsPlayer1.setTypeface(type, Typeface.BOLD);
        tvStatsPlayer1.setTextColor(C4Color.BLACK);

        // Load stats for popup screen
        setPlayerStats(clientController.getPlayerStats());
    }

    public void initListeners() {
        // In-game profile button
        /*buttonGameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(android.R.id.content, GameProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/
    }

    public void setPlayerStats(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvStatsPlayer1.setText(text);
            }
        });
    }
}
