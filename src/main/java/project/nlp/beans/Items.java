/**
 * 
 */
package project.nlp.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author kanchan
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Items {

	 private List<QuestionDetails> items= new ArrayList<QuestionDetails>();

	/**
	 * @return the items
	 */
	public List<QuestionDetails> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<QuestionDetails> items) {
		this.items = items;
	}

}
