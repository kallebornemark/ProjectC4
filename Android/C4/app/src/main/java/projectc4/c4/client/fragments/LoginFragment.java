package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;
import projectc4.c4.util.C4Color;

/**
 * Created by Erik on 2015-04-09.
 */
public class LoginFragment extends Fragment {
    private View view;
    private Button buttonLogin;
    private EditText etUsername;
    private ClientController clientController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        clientController = ((MainActivity)getActivity()).getClientController();
        clientController.setLoginFragment(this);

        initComponents();
        styleComponents();
        initListeners();

        return view;
    }

    public void initComponents() {
        buttonLogin = (Button)view.findViewById(R.id.buttonLogin);
        etUsername = (EditText)view.findViewById(R.id.etUsername);
    }

    public void styleComponents() {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        buttonLogin.setTypeface(type,Typeface.BOLD);
        buttonLogin.setTextColor(C4Color.WHITE);
    }

    public void initListeners() {
        // Login Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientController.requestUsername(etUsername.getText().toString());
            }
        });
    }

    public void goToMatchmaking() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, new MatchmakingFragment()).addToBackStack("Matchmaking").commit();
    }
}
