package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import projectc4.c4.R;

/**
 * Created by Emil on 2015-04-29.
 */
public class CreateAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createaccount, container, false);

        return view;
    }
}
