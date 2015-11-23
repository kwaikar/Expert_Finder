package project.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.nlp.beans.Answer;
import project.nlp.beans.Items;
import project.nlp.beans.QuestionDetails;

public class ReadUsers {

	private static String url = "https://api.stackexchange.com/2.2/search?page=8&pagesize=100&order=desc&sort=activity&tagged=java&nottagged=android&site=stackoverflow";
	private static String answers ="https://api.stackexchange.com/2.2/answers/33452399/?order=desc&sort=activity&site=stackoverflow&filter=withbody";
		  
	public void readJson() throws Exception
	{	
		//System.out.println("############");
		ObjectMapper mapper = new ObjectMapper();
		
		//System.out.println("############");
		File unigramsFile = new File( this.getClass().getResource("/resources/questions_subset.json").getFile());
		//System.out.println("############");
		Items items = mapper.readValue(unigramsFile, Items.class); 
		//System.out.println("############");
		HashMap<String,String> user = new HashMap<String,String>(); 
	//	List<String> answers = new ArrayList<String>();
		List<Answer> answers = new ArrayList<Answer>();
		for (QuestionDetails owner : items.getItems()) {
			String userId = owner.getOwner().getUserId();
			answers = owner.getAnswers();
			//String username = owner.getOwner().getDisplayName();
			//user.put(userId, username);
			
		}
		System.out.println("##########");
		int size = answers.size();
		System.out.println(size);
		for(int i=0;i<size;i++){
			System.out.println(answers.get(i));
		}
		
		/*  for(Entry<String, String> entry : user.entrySet()){
				String k = entry.getKey();
				String value = entry.getValue();
				System.out.println(k + " : " + value);
				
				
		}*/
		
		
	}
	public static void main(String[] args) throws Exception{
		    // Create an instance of HttpClient.
		 
				ReadUsers ir = new ReadUsers();
				ir.readJson();
		  }
}
