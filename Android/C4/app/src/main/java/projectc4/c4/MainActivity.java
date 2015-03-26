package projectc4.c4;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private GridLayout grd;
    private ArrayList<Button> buttonArrayList = new ArrayList<>();
    private ClientController clientController;
    private int currentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clientController = new ClientController(this);
        initGraphics(); // get children at
        for (int i = 0; i < buttonArrayList.size(); i++) {
            currentIndex = i;
            buttonArrayList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    int col = Integer.parseInt(button.getText().toString());
                    //System.out.println(col);
                    clientController.newMove(col);

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void initGraphics() {
        grd = (GridLayout)findViewById(R.id.gridLayoutCenter); //Länka mitt grid
        //grd.getChildAt(0).setBackgroundColor(C4Color.RED); // get children at
        //grd.getChildAt(1).setBackgroundColor(C4Color.YELLOW);
        //grd.getChildAt(39).setBackgroundColor(C4Color.YELLOW);
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(C4Color.WHITE);

        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);


        button1.setBackgroundColor(C4Color.BLACK);
        button2.setBackgroundColor(C4Color.BLACK);
        button3.setBackgroundColor(C4Color.BLACK);
        button4.setBackgroundColor(C4Color.BLACK);
        button5.setBackgroundColor(C4Color.BLACK);
        button6.setBackgroundColor(C4Color.BLACK);

        buttonArrayList.add(button1);
        buttonArrayList.add(button2);
        buttonArrayList.add(button3);
        buttonArrayList.add(button4);
        buttonArrayList.add(button5);
        buttonArrayList.add(button6);

        TextView textViewPlayer1 = (TextView)findViewById(R.id.textViewPlayer1);
        TextView textViewPlayer2 = (TextView)findViewById(R.id.textViewPlayer2);
        TextView textViewVs = (TextView)findViewById(R.id.textViewVs);

        textViewVs.setTextColor(C4Color.BLACK);
//        textViewPlayer1.setBackgroundColor(C4Color.RED);
//        textViewPlayer2.setBackgroundColor(C4Color.YELLOW);
        highlightPlayer(1);
        textViewPlayer1.setTextColor(C4Color.WHITE);
        textViewPlayer2.setTextColor(C4Color.WHITE);
        textViewPlayer2.setTextColor(C4Color.WHITE);

        Button buttonNewgame = (Button)findViewById(R.id.buttonNewGame);
        buttonNewgame.setBackgroundColor(C4Color.BLACK);
        buttonNewgame.setTextColor(C4Color.WHITE);


    }

    public GridLayout getGrid() {
        return this.grd;
    }

    public void setTextViewWinner(String winner) {
        TextView textViewWinner = (TextView)findViewById(R.id.textViewWinner);
        textViewWinner.setText(winner);
        setNewGame();
    }

    public void highlightPlayer(int  player) {
        TextView textViewPlayer1 = (TextView)findViewById(R.id.textViewPlayer1);
        TextView textViewPlayer2 = (TextView)findViewById(R.id.textViewPlayer2);

        if (player == 1) {
            textViewPlayer1.setBackgroundColor(C4Color.RED);
            textViewPlayer2.setBackgroundColor(C4Color.YELLOWPRESSED);
        }
        else if (player == 2){
            textViewPlayer2.setBackgroundColor(C4Color.YELLOW);
            textViewPlayer1.setBackgroundColor(C4Color.REDPRESSED);
        }
    }

    public void setNewGame() {
        final Button buttonNewGame = (Button)findViewById(R.id.buttonNewGame);
        buttonNewGame.setEnabled(true);
        buttonNewGame.setVisibility(View.VISIBLE);
                for (int i = 0; i < buttonArrayList.size(); i++) {
                    buttonArrayList.get(i).setEnabled(false);
                }
        TableRow tableRowPlayers = (TableRow)findViewById(R.id.tableRowPlayers);
        tableRowPlayers.setVisibility(View.INVISIBLE);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientController.newGame();
                buttonNewGame.setEnabled(false);
                buttonNewGame.setVisibility(View.INVISIBLE);
                TextView textViewWinner = (TextView)findViewById(R.id.textViewWinner);
                textViewWinner.setText("");
                clientController.newGame();
                TableRow tableRowPlayers = (TableRow)findViewById(R.id.tableRowPlayers);
                tableRowPlayers.setVisibility(View.VISIBLE);

//                TextView textViewPlayer1 = (TextView)findViewById(R.id.textViewPlayer1);
//                TextView textViewPlayer2 = (TextView)findViewById(R.id.textViewPlayer2);
//                textViewPlayer2.setBackgroundColor(C4Color.YELLOW);
//                textViewPlayer1.setBackgroundColor(C4Color.RED);
                highlightPlayer(1);
            }
        });
    }

    public ArrayList<Button> getButtonArrayList() {
        return this.buttonArrayList;
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
