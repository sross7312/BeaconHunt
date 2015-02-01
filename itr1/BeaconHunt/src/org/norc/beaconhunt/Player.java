package org.norc.beaconhunt;

/**
 * This class represents a player.
 * @author stevenross
 *
 */
public class Player {
	
	private String id;
	private String name;
	private BeaconObject[] beacons;
	private Boolean gameOver = false;
	
	public Player(String name, String id){
		this.name = name;
		this.id = id;
//		this.beacons = BeaconFactory.getNewBeaconList();
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setBeacons(BeaconObject[] beacons){
		this.beacons = beacons;
	}
	
	/**
	 * Gets the beacon the user is currently assigned to find
	 * @return Beacon 
	 */
	public BeaconObject getNextBeacon(){
		BeaconObject beacon;
		for(int i=0; i<=beacons.length; i++){
			if(i ==  beacons.length){
				setGameOver(true);
				return null;
			}
			beacon = beacons[i];
			if(!beacon.getFound()){
				return beacon;
			}
		}
		return null;
	}

	public Boolean getGameOver() {
		return gameOver;
	}

	public void setGameOver(Boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	/**
	 * Sets a beacon's status to found. 
	 * @param bID
	 * @return Beacon (the next iBeacon to find)
	 */
	public BeaconObject setBeaconFound(String bID){
		BeaconObject b = getNextBeacon();
		if(b.getId().equals(bID)){
			b.setFound(true);
		}	
		return getNextBeacon();
	}

}
