package Exhibition;

import static org.junit.Assert.*;

import org.junit.Test;

import exhibitions.ExhibitionService;

public class exhibitionServiceTest {
	@Test
	public void testProcessTemplate() {
		ExhibitionService service= new ExhibitionService();
		String [] expectedOutput=new String[12];
		String [] actualOutput=new String[12];
		expectedOutput[0]= "sq:1";
		expectedOutput[1]= null;
		expectedOutput[2]= null;
		expectedOutput[3]= null;
		expectedOutput[4]="sq:3";expectedOutput[5]= null;expectedOutput[6]= null;expectedOutput[7]= null;
		expectedOutput[8]= null;expectedOutput[9]= null;expectedOutput[10]= null;expectedOutput[11]= null;
		actualOutput=service.processTemplate("0 sq:1 3 sq:3 REMOVE sq:3 4 sq:3 ");
		assertArrayEquals(expectedOutput,actualOutput);
	}
	
}
