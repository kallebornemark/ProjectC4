package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.SocketException;

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
        TextView error = (TextView)view.findViewById(R.id.textViewError);
        error.setText("");
    }

    public void styleComponents() {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        buttonLogin.setTypeface(type,Typeface.BOLD);
        buttonLogin.setTextColor(C4Color.WHITE);
    }

    public void initListeners() {
        // Login Button
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBarLarge2);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setEnabled(true);
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                buttonLogin.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                try {
                    clientController.requestUsername(etUsername.getText().toString());
                } catch (Exception e) {
                    TextView error = (TextView)view.findViewById(R.id.textViewError);
                    error.setText("SERVER OFFLINE");
                    buttonLogin.setBackground(getActivity().getDrawable(R.drawable.colorred));
                    progressBar.setEnabled(false);
                }
            }
        });
    }

    public void goToMatchmaking() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
        transaction.replace(R.id.activity_layout_fragmentpos, new MatchmakingFragment()).addToBackStack("Matchmaking").commit();
    }
}
