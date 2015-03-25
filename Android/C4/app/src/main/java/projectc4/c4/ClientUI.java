package projectc4.c4;

import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author Kalle Bornemark
 */
public class ClientUI {
    private ClientController clientController;
    private TextView[][] textViews = new TextView[7][6];
    private MainActivity mainActivity;



    public ClientUI(ClientController clientController) {
        this.clientController = clientController;
        mainActivity = clientController.getMainActivity();


    }

    public void drawTile(int pos, int color) {
        mainActivity.getGrid().getChildAt(pos).setBackgroundColor(color);
    }

    public void winner(String winner) {
        mainActivity.setTextViewWinner(winner);
    }

    public void newGame() {
        for (int i = 0; i < 42; i++) {
            mainActivity.getGrid().getChildAt(i).setBackgroundColor(C4Color.WHITE);
        }

        ArrayList<Button> buttonArrayList;
        buttonArrayList = mainActivity.getButtonArrayList();

        for (int i = 0; i < buttonArrayList.size(); i++) {
            buttonArrayList.get(i).setEnabled(true);
        }

    }
}


