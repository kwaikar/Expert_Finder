package project.nlp.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDetails  implements Question{


	 private List<Answer> answers= new ArrayList<Answer>();
	 
	 private Owner owner= null;
	 
	@JsonProperty(value = "question_id")
	private String questionId;
	private String body;
	private String title;
	private String link;
	private Integer score;
	@JsonProperty(value = "user_id")
	private String userId;
	@JsonProperty(value = "answer_count")
	private Integer answerCount;
	@JsonProperty(value = "up_vote_count")
	private Integer upVoteCount;
	@JsonProperty(value = "down_vote_count")
	private Integer downVoteCount;
	@JsonProperty(value = "view_count")
	private Integer viewCount;
	@JsonProperty(value = "is_answered")
	private Boolean isAnswered;
	
	private int threadmaxUpvoteCount=1;
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

	private List<String> tags = new ArrayList<String>();

	/**
	 * @return the answers
	 */
	public List<Answer> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

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
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * @return the answerCount
	 */
	public Integer getAnswerCount() {
		return answerCount;
	}

	/**
	 * @param answerCount
	 *            the answerCount to set
	 */
	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}

	/**
	 * @return the upVoteCount
	 */
	public Integer getUpVoteCount() {
		return upVoteCount;
	}

	/**
	 * @param upVoteCount
	 *            the upVoteCount to set
	 */
	public void setUpVoteCount(Integer upVoteCount) {
		this.upVoteCount = upVoteCount;
	}

	/**
	 * @return the downVoteCount
	 */
	public Integer getDownVoteCount() {
		return downVoteCount;
	}

	/**
	 * @param downVoteCount
	 *            the downVoteCount to set
	 */
	public void setDownVoteCount(Integer downVoteCount) {
		this.downVoteCount = downVoteCount;
	}

	/**
	 * @return the viewCount
	 */
	public Integer getViewCount() {
		return viewCount;
	}

	/**
	 * @param viewCount
	 *            the viewCount to set
	 */
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * @return the isAnswered
	 */
	public Boolean getIsAnswered() {
		return isAnswered;
	}

	/**
	 * @param isAnswered
	 *            the isAnswered to set
	 */
	public void setIsAnswered(Boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

	/**
	 * @return the questionId
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId
	 *            the questionId to set
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the threadmaxCount
	 */
	public int getThreadmaxCount() {
		return threadmaxUpvoteCount;
	}

	/**
	 * @param threadmaxCount the threadmaxCount to set
	 */
	public void setThreadmaxCount(int threadmaxCount) {
		this.threadmaxUpvoteCount = threadmaxCount;
	}

 

}
