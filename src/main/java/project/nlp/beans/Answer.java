package project.nlp.beans;

import java.util.Arrays;

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
	private String[] tags = null;
	private int score;
	private int threadUpvoteCount=1;
	private int threadmaxDownvoteCount=1;

	/**
	 * @return the threadmaxDownvoteCount
	 */
	public int getThreadmaxDownvoteCount() {
		return threadmaxDownvoteCount;
	}

	/**
	 * @param threadmaxDownvoteCount the threadmaxDownvoteCount to set
	 */
	public void setThreadmaxDownvoteCount(int threadmaxDownvoteCount) {
		this.threadmaxDownvoteCount = threadmaxDownvoteCount;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}
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
	/**
	 * @return the threadUpvoteCount
	 */
	public int getThreadUpvoteCount() {
		return threadUpvoteCount;
	}
	/**
	 * @param threadUpvoteCount the threadUpvoteCount to set
	 */
	public void setThreadUpvoteCount(int threadUpvoteCount) {
		this.threadUpvoteCount = threadUpvoteCount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Answer [owner=" + owner + ", downVotes=" + downVotes + ", upVotes=" + upVotes + ", is_accepted="
				+ is_accepted + ", tags=" + Arrays.toString(tags) + ", score=" + score + ", threadUpvoteCount="
				+ threadUpvoteCount + ", answer_id=" + answer_id + ", body=" + body + ", title=" + title + "]";
	}	


	
	

}
