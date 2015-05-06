package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView imageViewProfile;
    private boolean clicked = false;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        clientController = ((MainActivity)getActivity()).getClientController();

        TextView tvHeader = (TextView)view.findViewById(R.id.tvHeader);
        TextView tvContent = (TextView)view.findViewById(R.id.tvContent);

        final TextView tvFirstname = (TextView)view.findViewById(R.id.tvFirstName);
        final TextView tvLastname = (TextView)view.findViewById(R.id.tvLastName);
        final TextView tvEmail = (TextView)view.findViewById(R.id.tvEmail);

        final EditText etFirstname = (EditText)view.findViewById(R.id.etFirstname);
        final EditText etLastname = (EditText)view.findViewById(R.id.etLastname);
        final EditText etEmail = (EditText)view.findViewById(R.id.etEmail);

        final ImageView tvWrench = (ImageView)view.findViewById(R.id.ivWrench);
        final ImageView tvCheck = (ImageView)view.findViewById(R.id.ivCheck);


        user = clientController.getUser();


        // Style texts
        tvHeader.setTypeface(type, Typeface.BOLD);
        tvContent.setTypeface(type, Typeface.BOLD);

        tvFirstname.setTypeface(type, Typeface.BOLD);
        tvLastname.setTypeface(type, Typeface.BOLD);
        tvEmail.setTypeface(type, Typeface.BOLD);

        etFirstname.setTypeface(type);
        etLastname.setTypeface(type);
        etEmail.setTypeface(type);


        // Set texts
        tvHeader.setText(user.getUsername().toUpperCase());
        tvContent.setText(clientController.getPlayerStats(false));

        tvFirstname.setText(user.getFirstName());
        tvLastname.setText(user.getLastName());
        tvEmail.setText(user.getEmail());

        etFirstname.setText(user.getFirstName());
        etLastname.setText(user.getLastName());
        etEmail.setText(user.getEmail());


        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        // Detta är välja bild
      /*  imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select profile-picture"), 1);
            }
        });*/

        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    etFirstname.setVisibility(View.VISIBLE);
                    etLastname.setVisibility(View.VISIBLE);
                    etEmail.setVisibility(View.VISIBLE);

                    tvFirstname.setVisibility(View.INVISIBLE);
                    tvLastname.setVisibility(View.INVISIBLE);
                    tvEmail.setVisibility(View.INVISIBLE);

                    etFirstname.requestFocus();
                    etFirstname.setSelection(etFirstname.getText().length());
                    etLastname.setSelection(etLastname.getText().length());
                    etEmail.setSelection(etEmail.getText().length());
                    imm.showSoftInput(etFirstname, InputMethodManager.SHOW_IMPLICIT);

                    tvWrench.setVisibility(View.INVISIBLE);
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

                    etFirstname.setVisibility(View.INVISIBLE);
                    etLastname.setVisibility(View.INVISIBLE);
                    etEmail.setVisibility(View.INVISIBLE);

                    tvFirstname.setVisibility(View.VISIBLE);
                    tvLastname.setVisibility(View.VISIBLE);
                    tvEmail.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etFirstname.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etLastname.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);

                    tvCheck.setVisibility(View.INVISIBLE);
                    tvWrench.setVisibility(View.VISIBLE);

                    clicked = false;
                }
            }
        });

        /*tvWrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                etFirstname.setVisibility(View.VISIBLE);
                etLastname.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);

                tvFirstname.setVisibility(View.INVISIBLE);
                tvLastname.setVisibility(View.INVISIBLE);
                tvEmail.setVisibility(View.INVISIBLE);

                etFirstname.requestFocus();
                etFirstname.setSelection(etFirstname.getText().length());
                etLastname.setSelection(etLastname.getText().length());
                etEmail.setSelection(etEmail.getText().length());
                imm.showSoftInput(etFirstname, InputMethodManager.SHOW_IMPLICIT);

                tvWrench.setVisibility(View.INVISIBLE);
                tvCheck.setVisibility(View.VISIBLE);
            }
        });


        tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFirstname.getText().toString().equals(user.getFirstName()) || !etEmail.getText().toString().equals(user.getLastName())) {
                    user.setFirstName(etFirstname.getText().toString());
                    user.setLastName(etLastname.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    tvFirstname.setText(user.getFirstName());
                    tvLastname.setText(user.getLastName());
                    tvEmail.setText(user.getEmail());

                    // Send update to server/database
                    clientController.getClient().updateUserObject(user);
                }

                etFirstname.setVisibility(View.INVISIBLE);
                etLastname.setVisibility(View.INVISIBLE);
                etEmail.setVisibility(View.INVISIBLE);

                tvFirstname.setVisibility(View.VISIBLE);
                tvLastname.setVisibility(View.VISIBLE);
                tvEmail.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etFirstname.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etLastname.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);

                tvCheck.setVisibility(View.INVISIBLE);
                tvWrench.setVisibility(View.VISIBLE);
            }
        });*/



        return view;
     }


    //Detta är välja bild med kameran
   /* public void onActivityResult(int reqCode, int resCode, Intent data) {
       if (data == null) {
           getActivity().getFragmentManager().popBackStackImmediate("Profilefragment" , 0);
       } else if (reqCode == 1) {
           imageViewProfile.setImageURI(clientController.getUsername().getProfilePicture().getProfileImage());
       }
    }*/
}
