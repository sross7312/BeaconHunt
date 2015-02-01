package org.norc.beaconhunt;

import java.io.FileOutputStream;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beaconhunt.R;
import com.google.gson.Gson;

public class LoginFragment extends Fragment {


	EditText txtName;
	Button btnDone;
	MainActivity activity;
	static Map<String,Map<String,String>>beacons;
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.login_layout, container, false);
	    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		txtName = (EditText) view.findViewById(R.id.input_player_name);
		btnDone = (Button) view.findViewById(R.id.login_done);
		btnDone.setOnClickListener(new OnClickListener(){
			
			//ON-Click,check to see if the text box is empty.
			//if not empty, login.
			@Override
			public void onClick(View v) {
				CharSequence name = txtName.getText();
				if(!name.equals("") && name.length()>0){
					//TODO register here
					
					//Added a mock players in lieu of logging in 
					addMockPlayer();
					Toast.makeText(v.getContext(), "You have successfully registered", Toast.LENGTH_SHORT).show();
					if(true){
						activity.playerLoggedIn();
						activity.reload();
					}
				}
				
			}
			
		});
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (MainActivity)activity;
		super.onAttach(activity);
	}

	
	private void addMockPlayer(){
		//Create new list of beacons		
		BeaconObject[] beaconList ={ new BeaconObject("43523", "1407F", "Its in Steve's Cube!"),
		 new BeaconObject("63225", "1407D", "Psstt..You may want to Check by Tina"),
		 new BeaconObject("16653", "1407A", "Dude, Really?  You can't find it?"),
		 new BeaconObject("18918","1405D", "This clue is really useless"),
		 new BeaconObject("16653", "1407A", "I'm thinking Jorge's area for this one.  Just a gut feeling!")
		};
		
		//Create new player and add the beacons
		String pName = "Bugsy Mobile";
		String pId =  "bm12345";
		Player player = new Player(pName,pId);
		player.setBeacons(beaconList);
		Gson gson = new Gson();
		String jsonPlayer = gson.toJson(player);
		
		//Write the json player object to file
		FileOutputStream outputStream;
		try {
		  outputStream = activity.openFileOutput(ApplicationConstants.DATA_STORE, Activity.MODE_PRIVATE);
		  outputStream.write(jsonPlayer.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		
		//Save the player name and id inside SharedPreferences
		SharedPreferences.Editor pref = (Editor) activity.getSharedPreferences(ApplicationConstants.GAME_PREF, Activity.MODE_PRIVATE).edit();
		pref.putString(ApplicationConstants.PLAYER_ID, pId);
		pref.commit();
		
	}
	
	
	
	
}
