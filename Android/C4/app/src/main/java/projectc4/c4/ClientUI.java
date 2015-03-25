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
    private ArrayList<Button> buttonArrayList;

    public ClientUI(ClientController clientController) {
        this.clientController = clientController;
        mainActivity = clientController.getMainActivity();
        buttonArrayList = mainActivity.getButtonArrayList();
    }

    public void drawTile(int pos, int color) {
        mainActivity.getGrid().getChildAt(pos).setBackgroundColor(color);
    }


    public void winner(String winner) {
        mainActivity.setTextViewWinner(winner);
    }
}


