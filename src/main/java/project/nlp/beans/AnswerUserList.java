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
	List<OntologyNode> ontologyNode = new ArrayList<OntologyNode>();
	
	public AnswerUserList(Answer answer,List<OntologyNode> ontologyNode){
		 this.bestAnswer = answer.isIs_accepted();
		 this.noOfUpvotes = answer.getUpVotes();
		 this.noOfDownvotes = answer.getDownVotes();
		 this.tags = answer.getTags();
		 this.score = answer.getScore();
		 this.ontologyNode = ontologyNode;
		
	}
	/**
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
		
	List<OntologyNode> ontology = new ArrayList<OntologyNode>();
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
	public List<OntologyNode> getOntology() {
		return ontology;
	}
	public void setOntology(List<OntologyNode> ontology) {
		this.ontology = ontology;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnswerUserList [bestAnswer=" + bestAnswer + ", noOfUpvotes=" + noOfUpvotes + ", noOfDownvotes="
				+ noOfDownvotes + ", tags=" + Arrays.toString(tags) + ", score=" + score + ", ontologyNode=" + ontologyNode + ", ontology=" + ontology + "]";
	}
	
	

}
