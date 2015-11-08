package project.nlp.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {

 
	 
	@JsonProperty(value = "user_id")
	private String userId  ; 
	@JsonProperty(value = "display_name")
	private String displayName  ; 
	private int reputation  ;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the reputation
	 */
	public int getReputation() {
		return reputation;
	}
	/**
	 * @param reputation the reputation to set
	 */
	public void setReputation(int reputation) {
		this.reputation = reputation;
	} 
	
	

}
