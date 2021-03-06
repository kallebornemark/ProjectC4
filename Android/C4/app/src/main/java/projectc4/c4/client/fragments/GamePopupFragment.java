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

import c4.utils.C4Color;
import projectc4.c4.R;
import projectc4.c4.client.*;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
 public class GamePopupFragment extends DialogFragment {
    private ClientController clientController;
    private View view;
    private TextView tvHeader;
    private TextView tvContent;
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
        RelativeLayout popupWindow = (RelativeLayout)view.findViewById(R.id.popup_window);
        popupBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(GamePopupFragment.this).commit();
            }
        });

        // Init and style elements
        tvHeader = (TextView)view.findViewById(R.id.tvHeader);
        tvContent = (TextView)view.findViewById(R.id.tvContent);
        tvHeader.setTypeface(typeface, Typeface.BOLD);
        tvContent.setTypeface(typeface, Typeface.BOLD);
        tvHeader.setTextColor(C4Color.WHITE);
        tvContent.setTextColor(C4Color.BLACK);

        // Style popup window and content with red or yellow colors
        if (section >= 1 && section <= 4) {
            popupWindow.setBackground(getActivity().getDrawable(R.drawable.popupscreenred));
            tvHeader.setBackground(getActivity().getDrawable(R.drawable.colorred));
        } else {
            popupWindow.setBackground(getActivity().getDrawable(R.drawable.popupscreenyellow));
            tvHeader.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
        }


        // Profile
        if (section == 1) {
            tvHeader.setText(clientController.getUser().getUsername()); // Load username
            tvContent.setText(clientController.getPlayerStats(false)); // Load player stats
        }

        // Settings
        else if (section == 2) {
            tvHeader.setText(clientController.getGameInfo().getOpponentUserName());
            tvHeader.setText("Settings");
        }

        // Friends
        else if (section == 3) {
            tvHeader.setText(clientController.getGameInfo().getOpponentUserName());
            tvHeader.setText("Friends");
        }

        // Chat
        else if (section == 4) {
            tvHeader.setText(clientController.getGameInfo().getOpponentUserName());
            tvHeader.setText("Chat");
        }


        // Opponent Profile
        else if (section == 5) {
            tvHeader.setText(clientController.getGameInfo().getOpponentUserName()); // Load opponent name
            tvContent.setText(clientController.getPlayerStats(true)); // Load opponent stats
        }


        // Opponent Chat
        else if (section == 6) {
            tvHeader.setText(clientController.getGameInfo().getOpponentUserName());
            tvHeader.setText("Chat");
        }
    }

    public void initListeners() {
        // Lyssnar-mall ifall vi implementerar några lyssnare här sen. LÅT STÅ!

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
