package projectc4.c4.client;

import android.widget.Button;
import android.widget.TextView;
import projectc4.c4.LocalGameActivity;
import projectc4.c4.util.C4Color;

import java.util.ArrayList;

/**
 * @author Kalle Bornemark
 */
public class ClientUI {
    private ClientController clientController;
    private TextView[][] textViews = new TextView[7][6];
    private LocalGameActivity localGameActivity;



    public ClientUI(ClientController clientController) {
        this.clientController = clientController;
        localGameActivity = clientController.getLocalGameActivity();
    }

    public void drawTile(int pos, int player) {
        localGameActivity.drawTile(pos,player);
        //mainActivity.getGrid().getChildAt(pos).setBackgroundColor(color);
    }

    public void winner(String winner) {
        localGameActivity.setTextViewWinner(winner);
    }

    public void newGame() {
        for (int i = 0; i < 42; i++) {
            localGameActivity.getGrid().getChildAt(i).setBackgroundColor(C4Color.WHITE);
        }

        ArrayList<Button> buttonArrayList;
        buttonArrayList = localGameActivity.getButtonArrayList();

        for (int i = 0; i < buttonArrayList.size(); i++) {
            buttonArrayList.get(i).setEnabled(true);
        }
    }

    public void highlightPlayer(int player) {
        localGameActivity.highlightPlayer(player);
    }

    public void changeHighlight() {
        localGameActivity.changeHighlight();
    }

    public void highLightTiles(ArrayList<Integer> pos) {
        localGameActivity.highlightTiles(pos);
    }

}


