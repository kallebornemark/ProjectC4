package projectc4.c4.client;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import projectc4.c4.R;
import projectc4.c4.client.fragments.*;

import static projectc4.c4.util.C4Constants.*;

public class MainActivity extends FragmentActivity {

    private MenuFragment menuFragment;
    private GameFragment gameFragment;
    private ClientController clientController;
    private GameController gameController;
    private MatchmakingFragment matchmakingFragment;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (savedInstanceState == null) {

            clientController = new ClientController();
            gameController = new GameController(clientController);
            clientController.setGameController(gameController);

            this.menuFragment = new MenuFragment();
//            this.gameFragment = new GameFragment();
//            this.matchmakingFragment = new MatchmakingFragment();
//            this.loginFragment = new LoginFragment();

//            getFragmentManager().beginTransaction().add(R.id.container, new LogoFragment());
            getFragmentManager().beginTransaction().add(R.id.activity_layout_fragmentpos, menuFragment).addToBackStack("Menu").commit();

//            getFragmentManager().beginTransaction().add(R.id.container, menuFragment, "menu");
//            getFragmentManager().beginTransaction().add(R.id.container, gameFragment, "game");
//            getFragmentManager().beginTransaction().add(R.id.container, matchmakingFragment, "matchmaking");
//            getFragmentManager().beginTransaction().add(R.id.container, loginFragment, "login");

        }
    }

    //Avmarkerat för att slippa settingsmenyn
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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
        } else {
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

    public GameFragment getGameFragment() {
        return gameFragment;
    }

    public MenuFragment getMenuFragment() {
        return menuFragment;
    }

    public MatchmakingFragment getMatchmakingFragment() {
        return matchmakingFragment;
    }

    public LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void setGameFragment(GameFragment gameFragment) {
        this.gameFragment = gameFragment;
    }
}
