package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import c4.utils.User;
import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;


/**
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class ProfileFragment extends Fragment {
    private ClientController clientController;
    private boolean clicked = false;
    private User user;
    private TextView tvContent;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientController = ((MainActivity)getActivity()).getClientController();
        user = clientController.getUser();
        clientController.setProfileFragment(this);
        clientController.requestGameResult();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");

        progressBar = (ProgressBar)view.findViewById(R.id.progressBarLoadingProfile);
        progressBar.setVisibility(View.VISIBLE);

        TextView tvHeader = (TextView)view.findViewById(R.id.tvHeader);
        tvContent = (TextView)view.findViewById(R.id.tvContent);

        final TextView tvFirstname = (TextView)view.findViewById(R.id.tvFirstName);
        final TextView tvLastname = (TextView)view.findViewById(R.id.tvLastName);
        final TextView tvEmail = (TextView)view.findViewById(R.id.tvEmail);

        final EditText etFirstname = (EditText)view.findViewById(R.id.etFirstname);
        final EditText etLastname = (EditText)view.findViewById(R.id.etLastname);
        final EditText etEmail = (EditText)view.findViewById(R.id.etEmail);

        final ImageView tvWrench = (ImageView)view.findViewById(R.id.ivWrench);
        final ImageView tvCheck = (ImageView)view.findViewById(R.id.ivCheck);

        // Style texts
        tvHeader.setTypeface(type, Typeface.BOLD);
        tvContent.setTypeface(type, Typeface.BOLD);

        tvFirstname.setTypeface(type, Typeface.BOLD);
        tvLastname.setTypeface(type, Typeface.BOLD);
        tvEmail.setTypeface(type, Typeface.BOLD);

        etFirstname.setTypeface(type, Typeface.BOLD);
        etLastname.setTypeface(type, Typeface.BOLD);
        etEmail.setTypeface(type, Typeface.BOLD);


        // Set texts
        tvHeader.setText(user.getUsername().toUpperCase());

        tvFirstname.setText(user.getFirstName());
        tvLastname.setText(user.getLastName());
        tvEmail.setText(user.getEmail());

        etFirstname.setText(user.getFirstName());
        etLastname.setText(user.getLastName());
        etEmail.setText(user.getEmail());

        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!clicked) {

                    etFirstname.setVisibility(View.VISIBLE);
                    etLastname.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.VISIBLE);

                    tvFirstname.setVisibility(View.GONE);
                    tvLastname.setVisibility(View.GONE);
                    tvEmail.setVisibility(View.GONE);

                    etFirstname.requestFocus();
                    etFirstname.setSelection(etFirstname.getText().length());
                    etLastname.setSelection(etLastname.getText().length());
                    etEmail.setSelection(etEmail.getText().length());
                    imm.showSoftInput(etFirstname, InputMethodManager.SHOW_IMPLICIT);

                    tvWrench.setVisibility(View.GONE);
                    tvCheck.setVisibility(View.VISIBLE);

                    clicked = true;

                } else {

                    if (!etFirstname.getText().toString().equals(user.getFirstName()) ||
                            !etLastname.getText().toString().equals(user.getLastName()) ||
                            !etEmail.getText().toString().equals(user.getEmail())) {
                        user.setFirstName(etFirstname.getText().toString());
                        user.setLastName(etLastname.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        tvFirstname.setText(user.getFirstName());
                        tvLastname.setText(user.getLastName());
                        tvEmail.setText(user.getEmail());

                        // Send update to server/database
                        clientController.getClient().updateUserObject(user);
                    }

                    etFirstname.setVisibility(View.GONE);
                    etLastname.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);

                    tvFirstname.setVisibility(View.VISIBLE);
                    tvLastname.setVisibility(View.VISIBLE);
                    tvEmail.setVisibility(View.VISIBLE);

                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etFirstname.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etLastname.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);

                    tvCheck.setVisibility(View.GONE);
                    tvWrench.setVisibility(View.VISIBLE);

                    clicked = false;
                }
            }
        });
        return view;
     }

    public void newGameResult(String gameResult) {
        progressBar.setVisibility(View.GONE);
        tvContent.setText(gameResult);
    }
}
