package projectc4.c4.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;
import projectc4.c4.R;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void initGraphics() {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/msyi.ttf");

        TextView textUsername = (TextView)findViewById(R.id.textUSERNAME);
        TextView textPassword = (TextView)findViewById(R.id.textPASSWORD);
        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setTypeface(type,Typeface.BOLD);
        textUsername.setTypeface(type,Typeface.BOLD);
        textPassword.setTypeface(type,Typeface.BOLD);
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
