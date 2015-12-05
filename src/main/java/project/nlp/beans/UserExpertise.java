package project.nlp.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserExpertise {
	
	public UserExpertise() {
		// TODO Auto-generated constructor stub
	}
	public UserExpertise(String skill) {
		super();
		this.skill = skill;
	}
	String skill;
	double weight;
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserExpertise [skill=" + skill + ", weight=" + weight + "]";
	}
	
	

}
