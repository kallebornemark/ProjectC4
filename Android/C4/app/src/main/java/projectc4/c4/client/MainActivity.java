package projectc4.c4.client;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import projectc4.c4.R;
import projectc4.c4.client.fragments.*;

import static projectc4.c4.util.C4Constants.*;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class MainActivity extends FragmentActivity {

    private MenuFragment menuFragment;
    private ClientController clientController;
    private GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            clientController = new ClientController();
            gameController = new GameController(clientController);
            clientController.setGameController(gameController);

            this.menuFragment = new MenuFragment();
            getFragmentManager().beginTransaction().add(R.id.activity_layout_fragmentpos, menuFragment).addToBackStack("Menu").commit();
        }
    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().findFragmentById(R.id.activity_layout_fragmentpos) instanceof GameFragment &&
                !(gameController.getGameMode()==LOCAL && gameController.getPlayedTiles() == 0)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to cancel the game?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FragmentManager fm = getFragmentManager();
                    if (clientController.getGameMode() == LOCAL) {
                        fm.popBackStackImmediate("Menu", 0);
                    } else if (clientController.getGameMode() == MATCHMAKING) {
                        fm.popBackStackImmediate("Matchmaking", 0);
                        if(!clientController.isOkayToLeave()) {
                            System.out.println("Gå till matchmaking från gameGrid och du förlorade");
                            clientController.updateUser(SURRENDER, false);
                        }
                        clientController.setOkayToLeave(false);

                    } else {
                        fm.popBackStack();
                    }
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (getFragmentManager().findFragmentById(R.id.activity_layout_fragmentpos) instanceof MatchmakingFragment) {
            getFragmentManager().popBackStackImmediate("Menu", 0);

        }else {
            FragmentManager fm = getFragmentManager();
            if (fm.findFragmentById(R.id.activity_layout_fragmentpos) instanceof MenuFragment) {
                finish();
            } else if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                finish();
            }

        }
    }

    public ClientController getClientController() {
        return clientController;
    }
}
