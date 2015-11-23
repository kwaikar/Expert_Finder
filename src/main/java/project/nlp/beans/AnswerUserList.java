package project.nlp.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AnswerUserList {
	
	boolean bestAnswer;
	int noOfUpvotes;
	int noOfDownvotes;
	String[] tags;
	int score;
	
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
	
	

}
