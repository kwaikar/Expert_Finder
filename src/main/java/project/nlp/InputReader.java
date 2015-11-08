package project.nlp;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.nlp.beans.Answer;
import project.nlp.beans.Items;
import project.nlp.beans.QuestionDetails;
 
/**
 * Hello world!
 *
 */
public class InputReader 
{
	private static String url = "https://api.stackexchange.com/2.2/search?page=8&pagesize=100&order=desc&sort=activity&tagged=java&nottagged=android&site=stackoverflow";
private static String answers ="https://api.stackexchange.com/2.2/answers/33452399/?order=desc&sort=activity&site=stackoverflow&filter=withbody";
	  
public void readJson(String fileName) throws Exception
{
	ObjectMapper mapper = new ObjectMapper();

	File unigramsFile = new File( fileName );
	Items items = mapper.readValue(unigramsFile, Items.class); 
	for (QuestionDetails question : items.getItems()) {
		System.out.println(question.getQuestionId());
		for (Answer answer : question.getAnswers()){
			System.out.println(answer.getAnswer_id());
		}
	}
	
	/*new TypeReference<List<Gram>>() {
	}*/
}
public static void main(String[] args) throws Exception{
	    // Create an instance of HttpClient.
	 
			InputReader ir = new InputReader();
			ir.readJson("S:\\NLP\\Source_Code\\expertfinder\\src\\main\\java\\resources\\questions.json");
	  }
}
