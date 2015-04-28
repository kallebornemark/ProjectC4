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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        clientController = ((MainActivity)getActivity()).getClientController();

        TextView textViewProfileName = (TextView)view.findViewById(R.id.textViewProfileName);
        final TextView textViewFirstName = (TextView)view.findViewById(R.id.textViewFirstName);
        final TextView textViewLastName = (TextView)view.findViewById(R.id.textViewLastName);
        final EditText editText = (EditText)view.findViewById(R.id.editText);
        final EditText editText2 = (EditText)view.findViewById(R.id.editText2);
        final View blackLine2 = (View)view.findViewById(R.id.lineBlack2);
        final View blackLine3 = (View)view.findViewById(R.id.lineBlack3);
        /*
            The 2 if-else checks if the first and last name is null or not.
         */
        if (clientController.getUser().getFirstName() == null) {
            textViewFirstName.setText(" - ");
            editText.setText("");
        } else {
            textViewFirstName.setText(clientController.getUser().getFirstName());
            editText.setText(clientController.getUser().getFirstName());
        }

        if (clientController.getUser().getLastName() == null) {
            textViewLastName.setText(" - ");
            editText2.setText("");
        } else {
            textViewLastName.setText(clientController.getUser().getLastName());
            editText2.setText(clientController.getUser().getLastName());
        }

        textViewProfileName.setText(clientController.getUser().getUsername());
        textViewProfileName.setTypeface(type, Typeface.BOLD);
        textViewFirstName.setTypeface(type, Typeface.BOLD);
        textViewLastName.setTypeface(type,Typeface.BOLD);
        editText.setTypeface(type,Typeface.BOLD);
        editText2.setTypeface(type,Typeface.BOLD);

        TextView tvHeader = (TextView)view.findViewById(R.id.tvHeader);
        tvHeader.setTypeface(type,Typeface.BOLD);

        TextView tvContent = (TextView)view.findViewById(R.id.tvContent);
        tvContent.setText(clientController.getPlayerStats(false));
        tvContent.setTypeface(type, Typeface.BOLD);

        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        //Detta är välja bild
      /*  imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select profile-picture"), 1);
            }
        });
*/
        final Button button = (Button)view.findViewById(R.id.button);
        button.setTypeface(type, Typeface.BOLD|Typeface.ITALIC);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    textViewFirstName.setVisibility(View.INVISIBLE);
                    textViewLastName.setVisibility(View.INVISIBLE);
                    editText2.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    button.setText("SAVE");
                    blackLine2.setVisibility(View.INVISIBLE);
                    blackLine3.setVisibility(View.INVISIBLE);
                    button.setBackground(getActivity().getDrawable(R.drawable.coloryellow));
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                    editText2.setSelection(editText2.getText().length());
                    imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
                    clicked = true;

                } else {
                    clientController.getUser().setFirstName(editText.getText().toString());
                    clientController.getUser().setLastName(editText2.getText().toString());
                    clientController.getClient().updateUserObject(clientController.getUser());
                    textViewFirstName.setText(clientController.getUser().getFirstName());
                    textViewLastName.setText(clientController.getUser().getLastName());
                    textViewFirstName.setVisibility(View.VISIBLE);
                    textViewLastName.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    editText2.setVisibility(View.INVISIBLE);
                    clicked = false;
                    button.setText("EDIT");
                    blackLine2.setVisibility(View.VISIBLE);
                    blackLine3.setVisibility(View.VISIBLE);
                    button.setBackground(getActivity().getDrawable(R.drawable.colorblack));

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
                }
            }
        });

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
