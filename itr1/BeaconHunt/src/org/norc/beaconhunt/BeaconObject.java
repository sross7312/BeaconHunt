package org.norc.beaconhunt;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * This class represents an iBeacon object.  It is created inside the BeaconFactory
 * @author steven ross
 *
 */
public class BeaconObject {
	private String location;
	private String id;
	private String clue;
	private Boolean isFound = false;
	private Timestamp foundTime;
	
	public BeaconObject (String id, String location, String clue){
		this.id = id;
		this.location = location;
		this.clue = clue;
	}

	public String getLocation(){
		return location;
	}
	
	public String getId() {
		return id;
	}
	
	public String getClue() {
		return clue;
	}
	
	/**
	 * Sets this beacon's status to found
	 * @param bool
	 */
	public void setFound(Boolean bool) {
		Calendar calendar = Calendar.getInstance();
		Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
		this.isFound = bool;
		this.foundTime = currentTimestamp;
	}
	
	/**
	 * Returns beacons status.  
	 * @return Boolean (Found or notFound)
	 */
	public Boolean getFound(){
		return isFound;
	}
	
	public Timestamp getFoundTime(){
		return foundTime;
	}
	
}
