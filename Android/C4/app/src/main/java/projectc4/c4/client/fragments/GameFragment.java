package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import c4.utils.C4Color;
import c4.utils.C4Constants;
import c4.utils.GameInfo;
import projectc4.c4.R;
import projectc4.c4.client.*;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
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
    private ImageView ivBlackArrow;
    private RelativeLayout rlBelowLine;
    private TranslateAnimation blackarrow_right;
    private TranslateAnimation blackarrow_left;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView tvPlayerElo;
    private TextView tvOpponentElo;
    private int winner;
    private boolean startup, timeLimit;

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
        if (gameMode == C4Constants.LOCAL) {
            clientController.newGame(gameMode);
        }

        return view;
    }
    public void initGraphics(View view) {
        RelativeLayout rlGameFragment = (RelativeLayout)view.findViewById(R.id.fragment_game);
        rlGameFragment.setBackgroundColor(C4Color.WHITE);

        rlBelowLine = (RelativeLayout)view.findViewById(R.id.belowLine);
        ivBlackArrow = (ImageView)view.findViewById(R.id.ivBlackArrow);
        textViewPlayer1 = (TextView)view.findViewById(R.id.textViewPlayer1);
        textViewPlayer2 = (TextView)view.findViewById(R.id.textViewPlayer2);
        TextView textViewVs = (TextView)view.findViewById(R.id.textViewVs);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        textViewPlayer1.setTypeface(type, Typeface.BOLD);
        textViewPlayer2.setTypeface(type, Typeface.BOLD);

        tvPlayerElo = (TextView)view.findViewById(R.id.tvPlayerElo);
        tvOpponentElo = (TextView)view.findViewById(R.id.tvOpponentElo);

        // Place black arrow on current player side
        textViewVs.setTextColor(C4Color.BLACK);
        startup = true;
        highlightPlayer(clientController.getPlayerTurn());
        if (gameMode == C4Constants.LOCAL) {
            animateArrowDelayed(C4Constants.PLAYER1);
        } else {
            animateArrowDelayed(clientController.getPlayerTurn());
        }

        textViewPlayer1.setTextColor(C4Color.WHITE);
        textViewPlayer2.setTextColor(C4Color.WHITE);


        if (gameMode == C4Constants.MATCHMAKING){
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

        // Display ELO
        if (gameMode == C4Constants.MATCHMAKING) {
            setElos();
        }
    }

    @Override
    public void onDestroyView() {
        if(clientController.getGameController().getTimer() != null)
           clientController.cancelTimer();
        super.onDestroyView();
    }

    public void initListeners() {

        // Set listeners if MM
        if (gameMode == C4Constants.MATCHMAKING) {

            // Player 1
            textViewPlayer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(1));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            // Player 2
            textViewPlayer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(5));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            /*// Settings Button
            ibPlayer1Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(2));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            // Friends Button
            ibPlayer1Friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(3));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            // Chat Button
            ibPlayer1Chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(4));
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

            // Opponent Chat Button
            ibPlayer2Chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(android.R.id.content, new GamePopupFragment().newInstance(6));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });*/

        }
    }

    public void animateArrow(final int direction) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (direction == C4Constants.PLAYER1) {
                    blackarrow_left = new TranslateAnimation(rlBelowLine.getWidth() - ivBlackArrow.getWidth(), 0.0f, 0.0f, 0.0f);
                    blackarrow_left.setDuration(220);
                    blackarrow_left.setFillAfter(true);
                    ivBlackArrow.startAnimation(blackarrow_left);
                    System.out.println("Animation blackarrow_right used");
                } else {
                    blackarrow_right = new TranslateAnimation(0.0f, rlBelowLine.getWidth() - ivBlackArrow.getWidth(), 0.0f, 0.0f);
                    blackarrow_right.setDuration(220);
                    blackarrow_right.setFillAfter(true);
                    ivBlackArrow.startAnimation(blackarrow_right);
                    System.out.println("Animation blackarrow_left used");
                }
            }
        });
    }

    public void setElos() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double playerElo = clientController.getUser().getElo();
                double opponentElo = clientController.getOpponentUser().getElo();
                tvPlayerElo.setText("Rating: " + String.format("%.2f", playerElo));
                tvOpponentElo.setText("Rating: " + String.format("%.2f", opponentElo));
            }
        });
    }

    public void animateArrowDelayed(final int direction) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                blackarrow_left = new TranslateAnimation(rlBelowLine.getWidth() - ivBlackArrow.getWidth(), 0.0f, 0.0f, 0.0f);
                blackarrow_left.setDuration(250);
                blackarrow_left.setFillAfter(true);

                blackarrow_right = new TranslateAnimation(0.0f, rlBelowLine.getWidth() - ivBlackArrow.getWidth(), 0.0f, 0.0f);
                blackarrow_right.setDuration(250);
                blackarrow_right.setFillAfter(true);

                if (direction == C4Constants.PLAYER1) {
                    ivBlackArrow.startAnimation(blackarrow_left);
                    System.out.println("Animation blackarrow_right used");
                } else {
                    ivBlackArrow.startAnimation(blackarrow_right);
                    System.out.println("Animation blackarrow_left used");
                }
            }
        }, 500);
    }

    public void enableBlackArrow() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ImageView ivBlackArrow = (ImageView)view.findViewById(R.id.ivBlackArrow);
                ivBlackArrow.setVisibility(View.VISIBLE);
            }
        });
    }

    public void disableBlackArrow() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ImageView ivBlackArrow = (ImageView)view.findViewById(R.id.ivBlackArrow);
                ivBlackArrow.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void highlightPlayer(final int player) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            //TODO stoppa och göra om animationen på extra runda
            public void run() {
                if (player == C4Constants.PLAYER1) {
                    if(gameMode == C4Constants.MATCHMAKING) {
                        if(startup) {
                            textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorred));
                            startup = false;
                        }else if (timeLimit) {
                            textViewPlayer1.setBackgroundResource(R.drawable.timer_animation3);
                            animation = (AnimationDrawable)textViewPlayer1.getBackground();
                            animation.start();
                            timeLimit = false;
                        }else {
                            textViewPlayer1.setBackgroundResource(R.drawable.timer_animation);
                            animation = (AnimationDrawable)textViewPlayer1.getBackground();
                            animation.start();

                        }

                    } else {
                        textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    }

                    textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellowpressed));

                } else if (player == C4Constants.PLAYER2) {
                    if(gameMode == C4Constants.MATCHMAKING) {
                        if(startup) {
                            textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                            startup = false;
                        }else if (timeLimit) {
                            textViewPlayer2.setBackgroundResource(R.drawable.timer_animation4);
                            animation = (AnimationDrawable)textViewPlayer2.getBackground();
                            animation.start();
                            timeLimit = false;
                        } else {
                            textViewPlayer2.setBackgroundResource(R.drawable.timer_animation2);
                            animation = (AnimationDrawable) textViewPlayer2.getBackground();
                            animation.start();

                        }
                    } else {
                        textViewPlayer2.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    }

                    textViewPlayer1.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));

                } else if (player == C4Constants.DRAW) {
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
                clientController.setOkayToLeave(false);
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
                startup = true;
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

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public void highlightWinnerPlayerStar(final int player) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivRedStar = (ImageView)view.findViewById(R.id.redStar);
                ImageView ivYellowStar = (ImageView)view.findViewById(R.id.yellowStar);
                if (player == C4Constants.PLAYER1) {
                    ivRedStar.setEnabled(true);
                    ivRedStar.setVisibility(View.VISIBLE);
                } else if (player == C4Constants.PLAYER2) {
                    ivYellowStar.setEnabled(true);
                    ivYellowStar.setVisibility(View.VISIBLE);
                } else if (player == C4Constants.DRAW) {
                    highlightWinnerPlayerStar(C4Constants.PLAYER1);
                    highlightWinnerPlayerStar(C4Constants.PLAYER2);
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

    public void setNewGame(final boolean gameInProgress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Button buttonNewGame = (Button)view.findViewById(R.id.buttonNewGame);
                if (gameInProgress) {
                    buttonNewGame.setText("Next round");
                } else {
                    buttonNewGame.setText("New game");
                }
                buttonNewGame.setEnabled(true);
                buttonNewGame.setVisibility(View.VISIBLE);
                buttonNewGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dehighlightWinners();
                        clientController.newGame(C4Constants.LOCAL);
                        buttonNewGame.setEnabled(false);
                        buttonNewGame.setVisibility(View.INVISIBLE);
                        RelativeLayout relativeLayoutPlayers = (RelativeLayout) view.findViewById(R.id.relativeLayoutPlayers);
                        relativeLayoutPlayers.setVisibility(View.VISIBLE);
                        highlightPlayer(C4Constants.PLAYER1);
                        if (winner == C4Constants.PLAYER2) {
                            animateArrow(C4Constants.PLAYER1);
                        }
                    }
                });
            }
        });
    }

    public void setTimeLimit(boolean timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void stopAnimation() {
        if(animation != null)
            animation.stop();
    }
}
