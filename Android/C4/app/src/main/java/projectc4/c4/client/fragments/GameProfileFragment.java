package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import projectc4.c4.R;
import projectc4.c4.client.*;

import static projectc4.c4.util.C4Constants.*;


/**
 * Created by Emil on 2015-04-09.
 */
 public class GameProfileFragment extends Fragment {
    private ClientController clientController;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientController = ((MainActivity)getActivity()).getClientController();
        final View view = inflater.inflate(R.layout.fragment_gameprofile, container, false);
        this.view = view;

        initGraphics(view);
        initListeners();

        return view;
    }

    public void initGraphics(View view) {

    }

    public void initListeners() {
        // In-game profile button
        /*buttonGameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(android.R.id.content, GameProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

    }
}
