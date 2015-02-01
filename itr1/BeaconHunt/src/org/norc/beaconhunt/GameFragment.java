package org.norc.beaconhunt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beaconhunt.R;

public class GameFragment extends Fragment {


	TextView txtClue;
	TextView txtStatus;
	BeaconObject currentBeacon;
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.game_layout, container, false);
	    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		txtStatus = (TextView) view.findViewById(R.id.game_status_label);
		txtClue= (TextView) view.findViewById(R.id.game_clue_label);
		
		txtStatus.setText("This is some random Status");
		
		txtClue.setText("This is the clue to your next beacon");
		super.onViewCreated(view, savedInstanceState);
	}
	

	public void changeBeacon(BeaconObject currentBeacon) {
		this.currentBeacon = currentBeacon;
		reload();
	}
	
	public void reload(){
		txtClue.setText(currentBeacon.getClue());
	}

	public void showEndGame(){
		txtClue.setText("Game Over!  \nThanks for playing!");
	}
	
	
}
