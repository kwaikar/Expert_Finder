package project.nlp.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AnswerUserList {
	
	boolean bestAnswer;
	int noOfUpvotes;
	int noOfDownvotes;
	String[] tags = null;
	int score; 
	int threadUpvotesMaxCount=1;
	List<OntologyNode> ontologyNode = new ArrayList<OntologyNode>();
	
	public AnswerUserList(Answer answer,List<OntologyNode> ontologyNode){
		 this.bestAnswer = answer.isIs_accepted();
		 this.noOfUpvotes = answer.getUpVotes();
		 this.noOfDownvotes = answer.getDownVotes();
		 this.tags = answer.getTags();
		 this.score = answer.getScore();
		 this.ontologyNode = ontologyNode;
		this.threadUpvotesMaxCount = answer.getThreadUpvoteCount();
		this.threadmaxDownvoteCount = answer.getThreadmaxDownvoteCount();
	}
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
	 * 
	 * @return the ontologyNode
	 */
	public List<OntologyNode> getOntologyNode() {
		return ontologyNode;
	}
	/**
	 * @param ontologyNode the ontologyNode to set
	 */
	public void setOntologyNode(List<OntologyNode> ontologyNode) {
		this.ontologyNode = ontologyNode;
	}
		
	public boolean isBestAnswer() {
		return bestAnswer;
	}
	public void setBestAnswer(boolean bestAnswer) {
		this.bestAnswer = bestAnswer;
	}
	public int getNoOfUpvotes() {
		return noOfUpvotes;
	}
	public void setNoOfUpvotes(int noOfUpvotes) {
		this.noOfUpvotes = noOfUpvotes;
	}
	public int getNoOfDownvotes() {
		return noOfDownvotes;
	}
	public void setNoOfDownvotes(int noOfDownvotes) {
		this.noOfDownvotes = noOfDownvotes;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * @return the threadUpvotesMaxCount
	 */
	public int getThreadUpvotesMaxCount() {
		return threadUpvotesMaxCount;
	}
	/**
	 * @param threadUpvotesMaxCount the threadUpvotesMaxCount to set
	 */
	public void setThreadUpvotesMaxCount(int threadUpvotesMaxCount) {
		this.threadUpvotesMaxCount = threadUpvotesMaxCount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnswerUserList [bestAnswer=" + bestAnswer + ", noOfUpvotes=" + noOfUpvotes + ", noOfDownvotes="
				+ noOfDownvotes + ", tags=" + Arrays.toString(tags) + ", score=" + score + ", threadUpvotesMaxCount="
				+ threadUpvotesMaxCount + ", ontologyNode=" + ontologyNode  + "]";
	}
 

}
