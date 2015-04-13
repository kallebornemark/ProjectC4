package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.GameController;
import projectc4.c4.client.GameGridAnimation;
import projectc4.c4.client.GameGridForeground;
import projectc4.c4.client.GameGridView;
import projectc4.c4.client.MainActivity;
import projectc4.c4.util.C4Color;
import static projectc4.c4.util.C4Constants.*;


/**
 * Created by Emil on 2015-04-09.
 */
 public class GameFragment extends Fragment {
    private ClientController clientController;
    private int gameMode;
    private View view;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientController = ((MainActivity)getActivity()).getClientController();
        final View view = inflater.inflate(R.layout.fragment_game, container, false);
        this.view = view;
        GameGridView ggView = (GameGridView)view.findViewById(R.id.gameGridView);
        GameGridAnimation ggAnimation = (GameGridAnimation)view.findViewById(R.id.gameGridAnimation);
        GameGridForeground ggForeground = (GameGridForeground)view.findViewById(R.id.gameGridForeground);
        GameController gameController = new GameController(clientController,ggView, ggAnimation, ggForeground);
        clientController.setGameController(gameController);
        ggForeground.setGameController(gameController);

        clientController.setFragment(this);
        System.out.println(clientController.getPlayerTurn());
        gameMode = clientController.getGameMode();
        initGraphics(view);

        clientController.newGame(gameMode);


    return view;
    }

    public void initGraphics(View view) {
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.gameFragment);
        relativeLayout.setBackgroundColor(C4Color.WHITE);

        TextView textViewPlayer1 = (TextView)view.findViewById(R.id.textViewPlayer1);
        TextView textViewPlayer2 = (TextView)view.findViewById(R.id.textViewPlayer2);
        TextView textViewVs = (TextView)view.findViewById(R.id.textViewVs);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        textViewPlayer1.setTypeface(type, Typeface.BOLD);
        textViewPlayer2.setTypeface(type, Typeface.BOLD);

        textViewVs.setTextColor(C4Color.BLACK);
        highlightPlayer(clientController.getPlayerTurn());
        textViewPlayer1.setTextColor(C4Color.WHITE);
        textViewPlayer2.setTextColor(C4Color.WHITE);

        Button buttonNewgame = (Button)view.findViewById(R.id.buttonNewGame);
        buttonNewgame.setBackground(getActivity().getDrawable(R.drawable.altbutton));
        buttonNewgame.setTypeface(type, Typeface.BOLD);
        buttonNewgame.setTextColor(C4Color.WHITE);

        Button buttonRematch = (Button)view.findViewById(R.id.buttonRematch);
        buttonNewgame.setBackground(getActivity().getDrawable(R.drawable.altbutton));
        buttonNewgame.setTypeface(type, Typeface.BOLD);
        buttonRematch.setTextColor(C4Color.WHITE);
    }

    public void setTextViewWinner(final String winner) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                TextView textViewWinner = (TextView)findViewById(R.id.textViewWinner);
//                textViewWinner.setText(winner);

            }
        });
    }

    public void highlightPlayer(final int player) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textViewPlayer1 = (TextView) view.findViewById(R.id.textViewPlayer1);
                TextView textViewPlayer2 = (TextView) view.findViewById(R.id.textViewPlayer2);

                if (player == PLAYER1) {
                    textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));
                } else if (player == PLAYER2) {
                    textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                }
            }
        });
    }

    public void promptRematch() {
        final Button buttonRematch = (Button)view.findViewById(R.id.buttonRematch);
        buttonRematch.setEnabled(true);
        buttonRematch.setVisibility(View.VISIBLE);
        buttonRematch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientController.requestRematch();
            }
        });
    }

    public void highlightWinnerPlayer(int player) {
        ImageView ivRedStar = (ImageView)view.findViewById(R.id.redStar);
        ImageView ivYellowStar = (ImageView)view.findViewById(R.id.yellowStar);
        if (player == PLAYER1) {
            ivRedStar.setEnabled(true);
            ivRedStar.setVisibility(View.VISIBLE);
        } else if (player == PLAYER2) {
            ivYellowStar.setEnabled(true);
            ivYellowStar.setVisibility(View.VISIBLE);
        }
    }

    public void dehighlightWinners() {
        ImageView ivRedStar = (ImageView)view.findViewById(R.id.redStar);
        ImageView ivYellowStar = (ImageView)view.findViewById(R.id.yellowStar);
        ivRedStar.setEnabled(false);
        ivRedStar.setVisibility(View.INVISIBLE);
        ivYellowStar.setEnabled(true);
        ivYellowStar.setVisibility(View.INVISIBLE);
    }

    public void newRematch () {
        final Button buttonRematch = (Button)view.findViewById(R.id.buttonRematch);
        clientController.newGame(MATCHMAKING);
        buttonRematch.setEnabled(false);
        buttonRematch.setVisibility(View.INVISIBLE);
//        TextView textViewWinner = (TextView)findViewById(R.id.textViewWinner);
//        textViewWinner.setText("");
        clientController.newGame(MATCHMAKING);
        RelativeLayout relativeLayoutPlayers = (RelativeLayout)view.findViewById(R.id.relativeLayoutPlayers);
        relativeLayoutPlayers.setVisibility(View.VISIBLE);
        highlightPlayer(clientController.getPlayerTurn());
    }

    public void setNewGame() {
        final Button buttonNewGame = (Button)view.findViewById(R.id.buttonNewGame);
        buttonNewGame.setEnabled(true);
        buttonNewGame.setVisibility(View.VISIBLE);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dehighlightWinners();
                clientController.newGame(LOCAL);
                buttonNewGame.setEnabled(false);
                buttonNewGame.setVisibility(View.INVISIBLE);
//                TextView textViewWinner = (TextView)findViewById(R.id.textViewWinner);
//                textViewWinner.setText("");
//                clientController.newGame(LOCAL);
                RelativeLayout relativeLayoutPlayers = (RelativeLayout)view.findViewById(R.id.relativeLayoutPlayers);
                relativeLayoutPlayers.setVisibility(View.VISIBLE);
                highlightPlayer(PLAYER1);
            }
        });
    }



}
