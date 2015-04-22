package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import projectc4.c4.R;
import projectc4.c4.client.ClientController;
import projectc4.c4.client.MainActivity;


/**
 * Created by Emil on 2015-04-17.
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
        textViewFirstName.setTypeface(type,Typeface.BOLD);
        textViewLastName.setTypeface(type,Typeface.BOLD);

        TextView tvHeader = (TextView)view.findViewById(R.id.tvHeader);
        tvHeader.setTypeface(type,Typeface.BOLD);

        TextView tvContent = (TextView)view.findViewById(R.id.tvContent);
        tvContent.setText(clientController.getPlayerStats(false));
        tvContent.setTypeface(type, Typeface.BOLD);

        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        imageViewProfile.setEnabled(false);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select profile-picture"), 1);
            }
        });

        if (clientController.getUser().getProfileImage() != null) {
        imageViewProfile.setImageURI(clientController.getUser().getProfileImage().getData());
        }

        final Button button = (Button)view.findViewById(R.id.button);
        button.setTypeface(type, Typeface.BOLD);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    textViewFirstName.setVisibility(View.INVISIBLE);
                    textViewLastName.setVisibility(View.INVISIBLE);
                    editText2.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    button.setText("SAVE");
                    imageViewProfile.setEnabled(true);
                    imageViewProfile.setImageResource(R.drawable.changepictureicon );
                    clicked = true;
                } else {
                    
                }
            }
        });

        return view;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
       if (data == null) {
           getActivity().getFragmentManager().popBackStackImmediate("Profilefragment" , 0);
       } else if (reqCode == 1) {
           clientController.getUser().setProfileImage(data);
           imageViewProfile.setImageURI(clientController.getUser().getProfileImage().getData());
       }
    }
}
