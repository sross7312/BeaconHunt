package org.norc.beaconhunt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.norc.beaconhunt.util.SystemUiHider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.RangingListener;
import com.estimote.sdk.Region;
import com.example.beaconhunt.R;
import com.google.gson.Gson;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends FragmentActivity implements RangingListener{
	Fragment currentFragment;
	Gson gson = new Gson();
	Player player;
	BeaconObject currentBeacon;
	
	 public static final String EXTRAS_BEACON = "extrasBeacon";
	 private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	 private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId",
	   ESTIMOTE_PROXIMITY_UUID, null, null);
	 private static final String TAG = "MainActivity";
	 private BeaconManager beaconManager;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setup a new beacon manager
		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(this);
		
		//check to see if a player_id is saved in shared_preferences
		SharedPreferences pref = getSharedPreferences(ApplicationConstants.GAME_PREF,MODE_PRIVATE);
		//if not a player_id saved, show login fragment
		if(!pref.contains(ApplicationConstants.PLAYER_ID)){
			//Create a login fragment 
            LoginFragment loginFrag = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFrag).commit();
		}else{
			//else start game
			playerLoggedIn();
		}
		//Still set the layout of the main activity which holds the fragment container
		setContentView(R.layout.activity_main);    
	}
	
	//If the player already has a player_id saved, then start the game
	public void playerLoggedIn(){
		currentFragment = new GameFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, currentFragment,ApplicationConstants.GAME_FRAG);
		transaction.commit();
		reload();
	}
	
	//Check the data_store and reload the player.
	//should call this if anything is changed in the player object
	public void reload(){
		try
        {
            FileInputStream fis = openFileInput(ApplicationConstants.DATA_STORE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null, input="";
            while ((line = reader.readLine()) != null)
                input += line;
            reader.close();
            fis.close();
//            alert("File successfully loaded.");
//            Log.i("Tag",input);
            player = gson.fromJson(input, Player.class);
            currentBeacon = player.getNextBeacon();
        }
        catch (Exception ex)
        {
           ex.printStackTrace();
        }
		showNextClue();
		startRanging();
	}
	

	@Override
	protected void onStop() {
		stopRanging();
		
		super.onStop();
	}

	//Tell the beacon manager to start listening for beacons
	private void startRanging(){
		 beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
	    @Override public void onServiceReady() {
	      try {
	        beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
	      } catch (RemoteException e) {
	        Log.e(TAG, "Cannot start ranging", e);
	      }
	    }
	  });
	}
	
	//Tells the beacon manager to stop listening for beacons
	private void stopRanging(){
		  try {
	    beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
	  } catch (RemoteException e) {
	    Log.e(TAG, "Cannot stop but it does not matter now", e);
	  }

	  // When no longer needed. Should be invoked in #onDestroy.
	  beaconManager.disconnect();
	}
	
	 @Override
	  protected void onResume() {
	    super.onResume();
	  }
	 
	 //Beacon manager callback.  
	 @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
	      Log.d(TAG, "Ranged beacons: " + beacons);
	      if(null != currentBeacon){
		      for(Beacon beacon : beacons){
		    	  String beaconId = String.valueOf(beacon.getMajor());
		    	  String currBeaconId =  currentBeacon.getId();
		    	  if(beaconId.equals(currBeaconId) && beacon.getRssi() < 70){
		    		  Log.i("Tag","Found Beacon "+ beacon.getMajor());
		    		  currentBeacon.setFound(true);
		    		  savePlayer(); //Updates beacon to found in the data_store
		    		  currentBeacon = player.getNextBeacon();
		    		  showNextClue();
		    	  }
		      }
	      }
	      
	    }
	 
	 //Looks for the current beacon and shows the clue inside the game fragment
	 public void showNextClue(){
		 Fragment fragment = getSupportFragmentManager().findFragmentByTag(ApplicationConstants.GAME_FRAG);
		 if(fragment instanceof GameFragment ){
			 if(currentBeacon != null){
				 ((GameFragment)fragment).changeBeacon(currentBeacon);
			 }else{
				 ((GameFragment)fragment).showEndGame();
				 stopRanging();
			 }
		 }
		 
	 }
	 
	 private void savePlayer(){
		 Gson gson = new Gson();
			String jsonPlayer = gson.toJson(player);
			
			//Write the json player to file
			FileOutputStream outputStream;
			try {
			  outputStream = openFileOutput(ApplicationConstants.DATA_STORE, Activity.MODE_PRIVATE);
			  outputStream.write(jsonPlayer.getBytes());
			  outputStream.close();
			} catch (Exception e) {
			  e.printStackTrace();
			}
	 }
	
	
	
	
	
	
	

}
