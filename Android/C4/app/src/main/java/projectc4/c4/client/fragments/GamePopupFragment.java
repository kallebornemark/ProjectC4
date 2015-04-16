package projectc4.c4.client.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import projectc4.c4.R;
import projectc4.c4.client.*;
import projectc4.c4.util.C4Color;

/**
 * Created by Emil on 2015-04-09.
 */
 public class GamePopupFragment extends DialogFragment {
    private ClientController clientController;
    private View view;
    private TextView tvStatsPlayer1;
    private Typeface typeface;
    private int section;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static GamePopupFragment newInstance(int section) {
        GamePopupFragment f = new GamePopupFragment();

        // Supply section input as an argument
        Bundle args = new Bundle();
        args.putInt("section", section);
        f.setArguments(args);

        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        section = getArguments().getInt("section");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientController = ((MainActivity)getActivity()).getClientController();
        clientController.setGamePopupFragment(this);
        final View view = inflater.inflate(R.layout.fragment_gamepopup, container, false);
        this.view = view;
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        initGraphics(view);
        initListeners();

        return view;
    }

    public void initGraphics(View view) {

        // Make background clickable
        RelativeLayout popupBackground = (RelativeLayout)view.findViewById(R.id.popup_background);
        popupBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(GamePopupFragment.this).commit();
            }
        });

        // Profile
        if (section == 1) {
            TextView tvGameProfilePlayer1 = (TextView)view.findViewById(R.id.tvGameProfilePlayer1);

            tvGameProfilePlayer1.setText(clientController.getUser().getUsername());

            tvGameProfilePlayer1.setTypeface(typeface, Typeface.BOLD);
            tvGameProfilePlayer1.setTextColor(C4Color.WHITE);

            tvStatsPlayer1 = (TextView)view.findViewById(R.id.tvStatsPlayer1);

            tvStatsPlayer1.setTypeface(typeface, Typeface.BOLD);
            tvStatsPlayer1.setTextColor(C4Color.BLACK);

            tvStatsPlayer1.setText(clientController.getPlayerStats());
        }
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
}
