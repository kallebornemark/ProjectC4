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
import c4.utils.User;
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
    private TextView textViewError;
    private ProgressBar progressBar;
    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextEmailAgain;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;

    private boolean newUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientController = ((MainActivity)getActivity()).getClientController();
        clientController.setLoginFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

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
        textViewError = (TextView)view.findViewById(R.id.textViewError);

        //TableRows
        editTextFirstName = (EditText)view.findViewById(R.id.editTextFirstName);
        editTextFirstName.setVisibility(View.GONE);
        editTextLastName = (EditText)view.findViewById(R.id.editTextLastName);
        editTextLastName.setVisibility(View.GONE);
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
                textViewError.setText(msg);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBarLarge2);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void connect() {
        clientController.connect();
    }

    public void login() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newUser) {
                    newUser = false;
                    clientController.newUser( new User(
                                    editTextUsername.getText().toString(),
                                    editTextFirstName.getText().toString(),
                                    editTextLastName.getText().toString(),
                                    editTextEmail.getText().toString(),
                                    editTextPassword.getText().toString(),
                                    true)
                    );
                } else {
                    clientController.login(editTextUsername.getText().toString(), editTextPassword.getText().toString());
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

    public void enableLoginButton() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonLogin.setEnabled(true);
                buttonLogin.setBackground(getActivity().getDrawable(R.drawable.colorred));
                editTextPassword.setText("");
            }
        });
    }

    public void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
                            hideKeyboard();
                            connect();
                        } else {
                            // Om användarnamnet/lösenordet inte innehåller några tecken
                            if(editTextUsername.getText().length() == 0) {
                                clientController.getContext().showToast("Username can't be empty!");
                            } else if  (editTextPassword.getText().length() == 0) {
                                clientController.getContext().showToast("Password can't be empty!");
                            } else {
                                clientController.getContext().showToast("Username/Password can't be empty!");
                            }
                        }
                    } else if (buttonLogin.getText().equals("CREATE NEW ACCOUNT")) {

                        //Checks that the fields doesn't contain spaces in the end.
                        editTextUsername.setText(deleteSpacesAfter( editTextUsername.getText().toString() ));
                        editTextFirstName.setText(deleteSpacesAfter( editTextFirstName.getText().toString() ));
                        editTextLastName.setText(deleteSpacesAfter(editTextLastName.getText().toString()));
                        editTextEmail.setText(deleteSpacesAfter( editTextEmail.getText().toString() ));
                        editTextEmailAgain.setText(deleteSpacesAfter( editTextEmailAgain.getText().toString() ));

                        //checks that necessary fields contains text.
                        if (editTextUsername.getText().length() > 0 &&
                                editTextUsername.getText().length() <= 14 &&
                                editTextEmail.getText().length() > 0 &&
                                editTextEmailAgain.getText().length() > 0 &&
                                editTextPassword.getText().length() > 0 &&
                                editTextPasswordAgain.getText().length() > 0
                                ) {

                            //Checks that the emails are the same.
                            if ( editTextEmail.getText().toString().equals(editTextEmailAgain.getText().toString()) ) {

                                if ( editTextPassword.getText().toString().equals(editTextPasswordAgain.getText().toString()) ) {
                                    //Shows the progressbar and disables the button
                                    progressBar.setEnabled(true);
                                    progressBar.setVisibility(View.VISIBLE);
                                    buttonLogin.setEnabled(false);
                                    //New user sent to server.
                                    clientController.connect();
                                    newUser = true;
                                } else {
                                    editTextPassword.setText("");
                                    editTextPasswordAgain.setText("");
                                    clientController.getContext().showToast("Password doesn't match\nTry again");
                                }

                            } else {
                                editTextEmailAgain.setText("");
                                clientController.getContext().showToast("Email-adresses doesn't match\nTry again");
                            }
                        } else {

                            if (editTextUsername.getText().length() > 14) {
                                editTextUsername.setText("");
                                clientController.getContext().showToast("Maximum length of username is 14 chars\nTry again");
                            } else {
                                clientController.getContext().showToast("Fields with (*) must contain text\nTry again");
                            }
                        }
                    }
                    break;
                case R.id.textViewNewAccount:
                    if (textViewNewAccount.getText().equals("Create a new account?")){
                        editTextUsername.setHint("Username *");
                        editTextPassword.setHint("Password *");
                        textViewNewAccount.setText("Back to login");
                        editTextFirstName.setVisibility(View.VISIBLE);
                        editTextLastName.setVisibility(View.VISIBLE);
                        editTextEmail.setVisibility(View.VISIBLE);
                        editTextEmailAgain.setVisibility(View.VISIBLE);
                        editTextPasswordAgain.setVisibility(View.VISIBLE);
                        textViewError.setText("");
                        buttonLogin.setText("CREATE NEW ACCOUNT");
                    } else if (textViewNewAccount.getText().equals("Back to login")){
                        editTextUsername.setHint("Username");
                        editTextPassword.setHint("Password");
                        textViewNewAccount.setText("Create a new account?");
                        editTextFirstName.setVisibility(View.GONE);
                        editTextLastName.setVisibility(View.GONE);
                        editTextEmail.setVisibility(View.GONE);
                        editTextEmailAgain.setVisibility(View.GONE);
                        editTextPasswordAgain.setVisibility(View.GONE);
                        textViewError.setText("");
                        buttonLogin.setText("LOGIN");
                    } else {
                        System.out.println("något fel loginfragment.");
                    }
                    break;
            }

        }

    }
}
