package projectc4.c4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import projectc4.c4.client.ClientController;
import projectc4.c4.client.MyApplication;


public class MenuActivity extends Activity {
//    private ClientController clientController;
    protected MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Get the application instance
        app = (MyApplication)getApplication();

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
                ClientController.getInstance().gameMode = 0;
                Intent intent = new Intent(MenuActivity.this, LocalGameActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button)findViewById(R.id.buttonMultiplayer);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientController.getInstance().connect();
                Intent intent = new Intent(MenuActivity.this, MatchmakingActivity.class);
//                intent.putExtra("clientController",clientController);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
    }
}
