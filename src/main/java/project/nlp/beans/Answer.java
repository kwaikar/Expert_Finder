package project.nlp.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer {

 
	Owner owner = null;
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
	@JsonProperty(value = "down_vote_count")
	private int downVotes  ; 
	@JsonProperty(value = "up_vote_count")
	private int upVotes  ; 
	private boolean is_accepted  ;
	private String answer_id;
	private String body;
	private String title;
	/**
	 * @return the downVotes
	 */
	public int getDownVotes() {
		return downVotes;
	}
	/**
	 * @param downVotes the downVotes to set
	 */
	public void setDownVotes(int downVotes) {
		this.downVotes = downVotes;
	}
	/**
	 * @return the upVotes
	 */
	public int getUpVotes() {
		return upVotes;
	}
	/**
	 * @param upVotes the upVotes to set
	 */
	public void setUpVotes(int upVotes) {
		this.upVotes = upVotes;
	}
	/**
	 * @return the is_accepted
	 */
	public boolean isIs_accepted() {
		return is_accepted;
	}
	/**
	 * @param is_accepted the is_accepted to set
	 */
	public void setIs_accepted(boolean is_accepted) {
		this.is_accepted = is_accepted;
	}
	/**
	 * @return the answer_id
	 */
	public String getAnswer_id() {
		return answer_id;
	}
	/**
	 * @param answer_id the answer_id to set
	 */
	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}	


	
	

}
