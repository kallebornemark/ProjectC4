package projectc4.c4.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MyApplication;
import projectc4.c4.util.C4Color;

import static projectc4.c4.util.C4Constants.*;


public class MenuActivity extends Activity {
//    private ClientController clientController;
    protected MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get the application instance
        app = (MyApplication)getApplication();
        initGraphics();

        // Call a custom application method
//        app.customAppMethod();

        // Call a custom method in MySingleton
//        ClientController.getInstance().connect();

        // Read the value of a variable in MySingleton
//        String singletonVar = ClientController.getInstance().customVar;

//        this.clientController = new ClientController();
        Button button = (Button)findViewById(R.id.localGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientController.getInstance().setGameMode(LOCAL);
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        Button button2 = (Button)findViewById(R.id.buttonMultiplayer);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MenuActivity.this, MatchmakingActivity.class);
//                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);       // <-------- BACKUPS EMILS!
//                intent.putExtra("clientController",clientController);
//                startActivity(intent);                                                    // <-------- BACKUPS EMILS!


                // För kunna testa rematch
                ClientController.getInstance().connect();
                Intent intent = new Intent(MenuActivity.this, MatchmakingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    public void initGraphics() {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/msyi.ttf");

        Button button1 = (Button)findViewById(R.id.localGame);
        Button button2 = (Button)findViewById(R.id.buttonMultiplayer);
        Button button3 = (Button)findViewById(R.id.buttonSocial);
        Button button4 = (Button)findViewById(R.id.buttonHow);
        Button button5 = (Button)findViewById(R.id.buttonHigh);
        Button button6 = (Button)findViewById(R.id.buttonAbout);

        button1.setTypeface(type,Typeface.BOLD);
        button2.setTypeface(type,Typeface.BOLD);
        button3.setTypeface(type,Typeface.BOLD);
        button4.setTypeface(type,Typeface.BOLD);
        button5.setTypeface(type,Typeface.BOLD);
        button6.setTypeface(type,Typeface.BOLD);

        button1.setTextColor(C4Color.WHITE);
        button2.setTextColor(C4Color.WHITE);
        button3.setTextColor(C4Color.WHITE);
        button4.setTextColor(C4Color.WHITE);
        button5.setTextColor(C4Color.WHITE);
        button6.setTextColor(C4Color.WHITE);


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
    }
}
