package projectc4.c4;

import android.view.View;
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

    public void columnChooser() {
        ArrayList<Button> buttonArrayList = new ArrayList<>();
        buttonArrayList = mainActivity.getButtonArrayList();

        buttonArrayList.get(0).setOnClickListener( new Button.OnClickListener() {
            public void onClick(View v) {

            }
        });

    }

}


