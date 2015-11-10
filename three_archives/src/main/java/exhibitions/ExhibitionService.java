/*Author: Nicole Petersen
Description: Service class for the Exhibition Service. 
			Contains methods to talk to interact with the data and to perform functions on Exhibition content*/
package exhibitions;

import java.util.List;
import common.Service;
import common.model.Exhibition;

public class ExhibitionService extends Service {
	ManageExhibition manager=new ManageExhibition(); // Variable to interact with the database
	public ExhibitionService() {
		super();
	}
	
	public List<Exhibition> listExhibitions() //Get all exhibitions from the database
	{
		@SuppressWarnings("unchecked")
		List<Exhibition> listExhibitions= manager.listAllExhibitions();
		return listExhibitions;
	}
	
	public Integer saveExhibition(Exhibition exhibition) // Save an exhibition
	{
		return(manager.addExhibition(exhibition));//add exhibition to the db
	}
	

	
	public Exhibition getExhibition(int exhibitionID) // Get a specific exhibition from the database
	{
		return (manager.getExhibition(exhibitionID));//get exhibition with the specific ID
		
	}
	
	public String[] processTemplate(String input) // Order the exhibition images. "Input" is an unordered string of Image PIDs
	{
		String [] exhibition= new String[12];
		String temp="";
		int position=0;
		boolean remove=false;
		int flag=0;//0 if it is a text box, 1 if it is an image title
		
		for (int i=0;i<input.length();i++){
			while(input.charAt(i)!=' '){
				temp+=input.charAt(i);
				if(i<input.length()-1){
					i++;
				}
			}
			if (temp.equals("REMOVE")){ // if the user dragged the image out of the respective block
				remove=true;
			}
			else if(remove==true){
				for(int j=0;j<12;j++){
					if (exhibition[j]!=null){
						if (exhibition[j].equals(temp)){
							exhibition[j]=null;
							remove=false;
							break;
						}
					}
				}
			}
		
			else if (flag==0){
				position=Integer.valueOf(temp);
				flag=1;
			}
			else{
				flag=0;
				exhibition[position]=temp;
			}
			System.out.println(temp);// logs: Print out the ordered Image PIDs
			temp="";	
		}
		return exhibition;
	}

}
