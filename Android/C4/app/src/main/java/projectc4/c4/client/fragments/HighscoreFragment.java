package projectc4.c4.client.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

import c4.utils.C4Constants;
import c4.utils.Highscore;
import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;

/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class HighscoreFragment extends Fragment {
    private ClientController clientController;
    private Highscore highscore;
    private ListView listView;
    private ListViewAdapter elo;
    private ListViewAdapter wins;
    private ListViewAdapter losses;
    private ListViewAdapter draws;
    private TextView textViewElo;
    private TextView textViewWins;
    private TextView textViewLosses;
    private TextView textViewDraws;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the clientControlle
        clientController = ((MainActivity) getActivity()).getClientController();
        // Request the highscore
        clientController.getClient().requestHighscore(C4Constants.HIGHSCORE);
        clientController.setHighscoreFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);

        listView = (ListView) view.findViewById(R.id.listViewHighscore);
        TextView textViewUsername = (TextView) view.findViewById(R.id.textViewUsername);
        textViewUsername.setTextSize(18);
        textViewElo = (TextView) view.findViewById(R.id.textViewElo);
        textViewElo.setTextSize(18);
        textViewWins = (TextView) view.findViewById(R.id.textViewWins);
        textViewWins.setTextSize(18);
        textViewLosses = (TextView) view.findViewById(R.id.textViewLosses);
        textViewLosses.setTextSize(18);
        textViewDraws = (TextView) view.findViewById(R.id.textViewDraws);
        textViewDraws.setTextSize(18);

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        textViewElo.setOnClickListener(buttonClickListener);
        textViewWins.setOnClickListener(buttonClickListener);
        textViewLosses.setOnClickListener(buttonClickListener);
        textViewDraws.setOnClickListener(buttonClickListener);

        return view;
    }


    public void setHighscore(Highscore highscore) {
        this.highscore = highscore;
        this.elo = new ListViewAdapter(highscore.getHighScoreElo());
        this.listView.setAdapter(elo);
        this.wins = new ListViewAdapter(highscore.getHighscoreWins());
        this.losses  = new ListViewAdapter(highscore.getHighScoreLosses());
        this.draws = new ListViewAdapter(highscore.getHighScoreDraws());
    }

    /**
     * Private class to handel the buttons.
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textViewElo:
                    listView.setAdapter(elo);
                    break;
                case R.id.textViewWins:
                    listView.setAdapter(wins);
                    break;
                case R.id.textViewLosses:
                    listView.setAdapter(losses);
                    break;
                case R.id.textViewDraws:
                    listView.setAdapter(draws);
                    break;

            }
        }
    }

    public class ListViewAdapter extends BaseAdapter {

        public ArrayList<HashMap<String, String>> list;
        private TextView txtPosition;
        private TextView txtUsername;
        private TextView txtElo;
        private TextView txtWins;
        private TextView txtLosses;
        private TextView txtDraws;

        public ListViewAdapter(ArrayList<HashMap<String, String>> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.highscore_row, null);
                txtPosition = (TextView) convertView.findViewById(R.id.textViewPosition);
                txtUsername = (TextView) convertView.findViewById(R.id.textViewUsername);
                txtElo = (TextView) convertView.findViewById(R.id.textViewElo);
                txtWins = (TextView) convertView.findViewById(R.id.textViewWins);
                txtLosses = (TextView) convertView.findViewById(R.id.textViewLosses);
                txtDraws = (TextView) convertView.findViewById(R.id.textViewDraws);
            }

            HashMap<String, String> map = list.get(position);

            txtPosition.setText(map.get(C4Constants.POSITION_COLUMN));
            txtUsername.setText(map.get(C4Constants.USERNAME_COLUMN));
            txtElo.setText(map.get(C4Constants.ELO_COLUMN));
            txtWins.setText(map.get(C4Constants.WINS_COLUMN));
            txtLosses.setText(map.get(C4Constants.LOSSES_COLUMN));
            txtDraws.setText(map.get(C4Constants.DRAWS_COLUMN));

            return convertView;
        }
    }
}
