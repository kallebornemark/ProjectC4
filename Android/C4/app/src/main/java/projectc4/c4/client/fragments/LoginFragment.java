package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import c4.utils.C4Color;
import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class LoginFragment extends Fragment {
    private View view;
    private Button buttonLogin;
    private ClientController clientController;
    private TextView textViewNewAccount;
    private ProgressBar progressBar;
    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextEmailAgain;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;


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
        progressBar = (ProgressBar)view.findViewById(R.id.progressBarLarge2);

        //EditText
        editTextUsername = (EditText)view.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)view.findViewById(R.id.editTextPassword);

        //TextViews
        textViewNewAccount = (TextView)view.findViewById(R.id.textViewNewAccount);

        //TableRows
        editTextFirstName = (EditText)view.findViewById(R.id.editTextFirstName);
        editTextFirstName.setVisibility(View.GONE);
        editTextSurname = (EditText)view.findViewById(R.id.editTextSurname);
        editTextSurname.setVisibility(View.GONE);
        editTextEmail = (EditText)view.findViewById(R.id.editTextEmail);
        editTextEmail.setVisibility(View.GONE);
        editTextEmailAgain = (EditText)view.findViewById(R.id.editTextEmailAgain);
        editTextEmailAgain.setVisibility(View.GONE);
        editTextPasswordAgain = (EditText)view.findViewById(R.id.editTextPasswordAgain);
        editTextPasswordAgain.setVisibility(View.GONE);


    }

    public void styleComponents() {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        buttonLogin.setTypeface(type, Typeface.BOLD);
        buttonLogin.setTextColor(C4Color.WHITE);
    }

    public void initListeners() {
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        buttonLogin.setOnClickListener(buttonClickListener);
        textViewNewAccount.setOnClickListener(buttonClickListener);
    }

    public void loginErrorMessage(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView error = (TextView)view.findViewById(R.id.textViewError);
                error.setText(msg);
                progressBar = (ProgressBar)view.findViewById(R.id.progressBarLarge2);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void login() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clientController.login(editTextUsername.getText().toString(), editTextPassword.getText().toString());
            }
        });
    }

    public void goToMatchmaking() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.transition1, R.anim.transition2, R.anim.transition1, R.anim.transition2);
        transaction.replace(R.id.activity_layout_fragmentpos, new MatchmakingFragment()).addToBackStack("Matchmaking").commit();
    }

    /**
     * Removes the spaces in the end of a string.
     *
     * @param text Text that is going to be checked if there are any spaces in the end of the string.
     * @return returns the text without spaces in the end of the string.
     */
    public String deleteSpacesAfter(String text){
        while (text.length() > 0 && text.charAt(text.length()-1) == ' ') {
            text = text.substring(0, text.length()-1);
        }
        return text;
    }

    /**
     * Private class to handel the buttons in the LoginFragment.
     */
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonLogin:
                    if (buttonLogin.getText().equals("LOGIN")) {
                        //Checks that the username doesn't contain spaces in the end.
                        editTextUsername.setText(deleteSpacesAfter( editTextUsername.getText().toString() ));

                        if (editTextUsername.getText().length() > 0 && editTextPassword.getText().length() > 0) {
                            //Körs om användarnamnet och lösenordet innehåller minst ett tecken
                            progressBar.setEnabled(true);
                            progressBar.setVisibility(View.VISIBLE);
                            buttonLogin.setEnabled(false);
                            buttonLogin.setBackground(getActivity().getDrawable(R.drawable.colorredpressed));
                            InputMethodManager inputManager = (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            if (((MainActivity) getActivity()).getClientController().getClient() == null) {
                                try {
                                    ((MainActivity) getActivity()).getClientController().connect();
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            // Om användarnamnet/lösenordet inte innehåller några tecken
                            Toast toast;
                            if(editTextUsername.getText().length() == 0) {
                                toast = Toast.makeText(getActivity().getApplicationContext(), "Username can't be empty!", Toast.LENGTH_SHORT);
                            } else if  (editTextPassword.getText().length() == 0) {
                                toast = Toast.makeText(getActivity().getApplicationContext(), "Password can't be empty!", Toast.LENGTH_SHORT);
                            } else {
                                toast = Toast.makeText(getActivity().getApplicationContext(), "Username/Password can't be empty!", Toast.LENGTH_SHORT);
                            }
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else if (buttonLogin.getText().equals("CREATE NEW ACCOUNT")) {
                        System.out.println("SKAPA NYTT KONTO");

                    }
                    break;

                case R.id.textViewNewAccount:
                    if (textViewNewAccount.getText().equals("Create a new account?")){
                        textViewNewAccount.setText("Back to login");
                        editTextFirstName.setVisibility(View.VISIBLE);
                        editTextSurname.setVisibility(View.VISIBLE);
                        editTextEmail.setVisibility(View.VISIBLE);
                        editTextEmailAgain.setVisibility(View.VISIBLE);
                        editTextPasswordAgain.setVisibility(View.VISIBLE);
                        buttonLogin.setText("CREATE NEW ACCOUNT");
                    } else if (textViewNewAccount.getText().equals("Back to login")){
                        textViewNewAccount.setText("Create a new account?");
                        editTextFirstName.setVisibility(View.GONE);
                        editTextSurname.setVisibility(View.GONE);
                        editTextEmail.setVisibility(View.GONE);
                        editTextEmailAgain.setVisibility(View.GONE);
                        editTextPasswordAgain.setVisibility(View.GONE);
                        buttonLogin.setText("LOGIN");
                    } else {
                        System.out.println("något fel loginfragment.");
                    }
                    break;
//
//                case R.id.??:
//                    break;
//
//                case R.id.??:
//                    break;
//
//                case R.id.??:
//
//                    break;
            }

        }

    }
}
