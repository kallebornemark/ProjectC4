package projectc4.c4.client.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import projectc4.c4.R;

/**
 * Created by Emil on 2015-04-14.
 */
public class SettingsFragment extends Fragment {
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        spinner = (Spinner)view.findViewById(R.id.spinner);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.theme, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPopupBackgroundDrawable(getActivity().getDrawable(R.drawable.colorlightgray));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Switch switchVolume = (Switch)view.findViewById(R.id.switchVolume);
        //Lyssnare till switchen
        switchVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/msyi.ttf");
        TextView textViewVolume = (TextView)view.findViewById(R.id.textViewVolume);
        TextView textViewTheme = (TextView)view.findViewById(R.id.textViewTheme);

        textViewVolume.setTypeface(type,Typeface.BOLD);
        textViewTheme.setTypeface(type,Typeface.BOLD);


        return view;
    }
}
