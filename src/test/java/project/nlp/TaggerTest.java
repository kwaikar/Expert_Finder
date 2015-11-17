package project.nlp;

import java.util.ArrayList;
import java.util.List;

import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import junit.framework.TestCase;

public class TaggerTest extends TestCase {

	/**
	 * Basic Test case for validation of the Tagger ouput
	 */
	public void testTagger()
	{
		TaggerUtil util = new TaggerUtil();
		List<String> list = new ArrayList<String>();
		list.add("kanchan");
		list.add("This");
		list.add("Symantec family safety");
		List<String> output = util.extractNounEntities("Hello kanchan. This is Symantec family safety");
		ReflectionAssert.assertReflectionEquals(output,list, ReflectionComparatorMode.LENIENT_ORDER);

	}
}
