package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
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
    private String firstName;
    private String lastName;
    private ImageView imageViewProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        clientController = ((MainActivity)getActivity()).getClientController();

        TextView textViewProfileName = (TextView)view.findViewById(R.id.textViewProfileName);
        /*
            The 2 if-else checks if the first and last name is null or not.
         */
        if (clientController.getUser().getFirstName() == null) {
            firstName = " - ";
        } else {
            firstName = clientController.getUser().getFirstName();
        }

        if (clientController.getUser().getLastName() == null) {
            lastName = " - ";
        } else {
            lastName = clientController.getUser().getLastName();
        }

        textViewProfileName.setText(clientController.getUser().getUsername() + "\n"
        + firstName + "\n"
        + lastName);
        textViewProfileName.setTypeface(type, Typeface.BOLD);

        TextView tvHeader = (TextView)view.findViewById(R.id.tvHeader);
        tvHeader.setTypeface(type,Typeface.BOLD);

        TextView tvContent = (TextView)view.findViewById(R.id.tvContent);
        tvContent.setText(clientController.getPlayerStats(false));
        tvContent.setTypeface(type, Typeface.BOLD);

        imageViewProfile = (ImageView)view.findViewById(R.id.imageViewProfile);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(Intent.createChooser(intent, "Select profile-picture"),1);
            }
        });

        return view;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == 1) {
            imageViewProfile.setImageURI(data.getData());
        }
    }


}
