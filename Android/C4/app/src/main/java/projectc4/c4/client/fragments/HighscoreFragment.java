package projectc4.c4.client.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import c4.utils.C4Constants;
import c4.utils.Highscore;
import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;

/**
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class HighscoreFragment extends Fragment {
    private ClientController clientController;
    Highscore highscore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the clientControlle
        clientController = ((MainActivity)getActivity()).getClientController();
        // Request the highscore
        clientController.getClient().requestHighscore(C4Constants.HIGHSCORE);
        clientController.setHighscoreFragment(this);
    }

    public void setHighscore(Highscore highscore){
        this.highscore = highscore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);
        return view;
    }

    /**
     * Private class to handel the buttons.
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.??:
//                    break;

//                case R.id.??:
//                    break;

            }
        }
    }
}
