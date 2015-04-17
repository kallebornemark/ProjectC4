package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import projectc4.c4.R;
import projectc4.c4.client.*;
import projectc4.c4.util.C4Color;
import projectc4.c4.util.GameInfo;

import static projectc4.c4.util.C4Constants.*;


/**
 * Created by Emil on 2015-04-09.
 */
 public class GameFragment extends Fragment {
    private ClientController clientController;
    private int gameMode;
    private View view;
    private AnimationDrawable animation;
    private ImageButton ibPlayer1Profile;
    private ImageButton ibPlayer1Settings;
    private ImageButton ibPlayer1Friends;
    private ImageButton ibPlayer1Chat;
    private ImageButton ibPlayer2Profile;
    private ImageButton ibPlayer2Chat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientController = ((MainActivity)getActivity()).getClientController();
        final View view = inflater.inflate(R.layout.fragment_game, container, false);
        this.view = view;

        GameGridView ggView = (GameGridView)view.findViewById(R.id.gameGridView);
        GameGridShowPointer ggShowPointer = (GameGridShowPointer)view.findViewById(R.id.gameGridShowPointer);
        GameGridForeground ggForeground = (GameGridForeground)view.findViewById(R.id.gameGridForeground);
        GameGridAnimation ggAmination = (GameGridAnimation)view.findViewById(R.id.gameGridAnimation);

        clientController.getGameController().setViews(ggView, ggAmination, ggShowPointer , ggForeground);
        clientController.setGameFragment(this);
        System.out.println(clientController.getPlayerTurn());
        gameMode = clientController.getGameMode();

        initGraphics(view);
        initListeners();

        clientController.newGame(gameMode);

        return view;
    }
    public void initGraphics(View view) {
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.fragment_game);
        relativeLayout.setBackgroundColor(C4Color.WHITE);

        TextView textViewPlayer1 = (TextView)view.findViewById(R.id.textViewPlayer1);

//        textViewPlayer1.setBackgroundResource(R.drawable.timer_animation);
//        animation = (AnimationDrawable)textViewPlayer1.getBackground();


        TextView textViewPlayer2 = (TextView)view.findViewById(R.id.textViewPlayer2);
        TextView textViewVs = (TextView)view.findViewById(R.id.textViewVs);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        textViewPlayer1.setTypeface(type, Typeface.BOLD);
        textViewPlayer2.setTypeface(type, Typeface.BOLD);

        textViewVs.setTextColor(C4Color.BLACK);
        highlightPlayer(clientController.getPlayerTurn());
        textViewPlayer1.setTextColor(C4Color.WHITE);
        textViewPlayer2.setTextColor(C4Color.WHITE);


        if (gameMode == MATCHMAKING){
            GameInfo gameInfo = clientController.getGameInfo();
            textViewPlayer1.setText(clientController.getUser().getUsername());
            textViewPlayer2.setText(gameInfo.getOpponentUserName());
        }

        Button buttonNewgame = (Button)view.findViewById(R.id.buttonNewGame);
        buttonNewgame.setBackground(getActivity().getDrawable(R.drawable.altbutton));
        buttonNewgame.setTypeface(type, Typeface.BOLD);
        buttonNewgame.setTextColor(C4Color.WHITE);

        Button buttonRematch = (Button)view.findViewById(R.id.buttonRematch);
        buttonRematch.setBackground(getActivity().getDrawable(R.drawable.altbutton));
        buttonRematch.setTypeface(type, Typeface.BOLD);
        buttonRematch.setTextColor(C4Color.WHITE);

        // Load icon panels if gamemode is matchmaking
        if (gameMode == MATCHMAKING) {
            GridLayout glIcons1 = (GridLayout)view.findViewById(R.id.iconButtons);
            GridLayout glIcons2 = (GridLayout)view.findViewById(R.id.iconButtons2);

            glIcons1.setEnabled(true);
            glIcons1.setVisibility(View.VISIBLE);
            glIcons2.setEnabled(true);
            glIcons2.setVisibility(View.VISIBLE);

            // Player 1
            ibPlayer1Profile = (ImageButton)view.findViewById(R.id.ibPlayer1Profile);
            ibPlayer1Settings = (ImageButton)view.findViewById(R.id.ibPlayer1Settings);
            ibPlayer1Friends = (ImageButton)view.findViewById(R.id.ibPlayer1Friends);
            ibPlayer1Chat = (ImageButton)view.findViewById(R.id.ibPlayer1Chat);

            // Player 2
            ibPlayer2Profile = (ImageButton)view.findViewById(R.id.ibPlayer2Profile);
            ibPlayer2Chat = (ImageButton)view.findViewById(R.id.ibPlayer2Chat);
        }
    }

    @Override
    public void onDestroyView() {
        if(clientController.getGameController().getTimer() != null)
           clientController.cancelTimer();

        //Todo fixa minneshantering så det inte kraschar vid rematch
        System.out.println("DESTROY VIEW");
        super.onDestroyView();
    }

    public void initListeners() {

        // Set listeners if MM
        if (gameMode == MATCHMAKING) {

            // Profile button
            ibPlayer1Profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(1));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            // Opponent Profile button
            ibPlayer2Profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(5));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

        }
    }

    // Används inte längre eftersom vi inte visar vinnaren med text

    /*public void setTextViewWinner(final String winner) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textViewWinner = (TextView)findViewById(R.id.textViewWinner);
                textViewWinner.setText(enableGameButton);
            }
        });
    }*/

    public void highlightPlayer(final int player) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textViewPlayer1 = (TextView) view.findViewById(R.id.textViewPlayer1);
                TextView textViewPlayer2 = (TextView) view.findViewById(R.id.textViewPlayer2);

                if (player == PLAYER1) {
                    if(gameMode == MATCHMAKING) {
                        textViewPlayer1.setBackgroundResource(R.drawable.timer_animation);
                        animation = (AnimationDrawable)textViewPlayer1.getBackground();
                        animation.start();
                    } else {
                        textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    }

                    textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));

                } else if (player == PLAYER2) {
                    if(gameMode == MATCHMAKING) {
                        textViewPlayer2.setBackgroundResource(R.drawable.timer_animation2);
                        animation = (AnimationDrawable)textViewPlayer2.getBackground();
                        animation.start();
                    } else {
                        textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    }

                    textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));

                } else if (player == DRAW) {
                    textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                }
            }
        });
    }

    public void unpromptRematch() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Button buttonRematch = (Button)view.findViewById(R.id.buttonRematch);
                buttonRematch.setEnabled(false);
                buttonRematch.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void promptRematch() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Button buttonRematch = (Button)view.findViewById(R.id.buttonRematch);
                buttonRematch.getBackground().setAlpha(255);
                buttonRematch.setEnabled(true);
                buttonRematch.setVisibility(View.VISIBLE);
                buttonRematch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clientController.requestRematch();
                        buttonRematch.getBackground().setAlpha(40);
                    }
                });
            }
        });
    }

    public void highlightWinnerPlayerStar(final int player) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivRedStar = (ImageView)view.findViewById(R.id.redStar);
                ImageView ivYellowStar = (ImageView)view.findViewById(R.id.yellowStar);
                if (player == PLAYER1) {
                    ivRedStar.setEnabled(true);
                    ivRedStar.setVisibility(View.VISIBLE);
                } else if (player == PLAYER2) {
                    ivYellowStar.setEnabled(true);
                    ivYellowStar.setVisibility(View.VISIBLE);
                } else if (player == DRAW) {
                    highlightWinnerPlayerStar(PLAYER1);
                    highlightWinnerPlayerStar(PLAYER2);
                }
            }
        });
    }

    public void disableStars() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivRedStar = (ImageView)view.findViewById(R.id.redStar);
                ImageView ivYellowStar = (ImageView)view.findViewById(R.id.yellowStar);
                ivRedStar.setVisibility(View.INVISIBLE);
                ivYellowStar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void dehighlightWinners() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivRedStar = (ImageView)view.findViewById(R.id.redStar);
                ImageView ivYellowStar = (ImageView)view.findViewById(R.id.yellowStar);
                ivRedStar.setEnabled(false);
                ivRedStar.setVisibility(View.INVISIBLE);
                ivYellowStar.setEnabled(false);
                ivYellowStar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void setNewGame() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                        RelativeLayout relativeLayoutPlayers = (RelativeLayout) view.findViewById(R.id.relativeLayoutPlayers);
                        relativeLayoutPlayers.setVisibility(View.VISIBLE);
                        highlightPlayer(PLAYER1);
                    }
                });
            }
        });
    }

    public void stopAnimation() {
        if(animation != null)
            animation.stop();
    }
}
