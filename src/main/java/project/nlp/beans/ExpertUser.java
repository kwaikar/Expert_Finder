package project.nlp.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpertUser {
	Owner owner = null;
	HashMap<String,UserExpertise>  userExpertise = new HashMap<String,UserExpertise>();
	List<AnswerUserList> answerUserList = new ArrayList<AnswerUserList>();
	/**
	 * @return the owner
	 */
	public Owner getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	/**
	 * @return the userExpertise
	 */
	public HashMap<String, UserExpertise> getUserExpertise() {
		return userExpertise;
	}
	/**
	 * @param userExpertise the userExpertise to set
	 */
	public void setUserExpertise(HashMap<String, UserExpertise> userExpertise) {
		this.userExpertise = userExpertise;
	}
	/**
	 * @return the answerUserList
	 */
	public List<AnswerUserList> getAnswerUserList() {
		return answerUserList;
	}
	/**
	 * @param answerUserList the answerUserList to set
	 */
	public void setAnswerUserList(List<AnswerUserList> answerUserList) {
		this.answerUserList = answerUserList;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExpertUser [owner=" + owner + ", userExpertise=" + userExpertise + ", answerUserList=" + answerUserList
				+ "]";
	}
	

}
