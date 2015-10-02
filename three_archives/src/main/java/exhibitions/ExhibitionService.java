package exhibitions;

import java.util.List;

import common.Service;
import common.model.Exhibition;

public class ExhibitionService extends Service {
	ManageExhibition manager=new ManageExhibition();
	public ExhibitionService() {
		super();
	}
	
	/*public Exhibition[] listAllExhibitions()
	{
		Exhibition[] allExhibitions= new Exhibition[5];
		allExhibitions[0]= new Exhibition("Queens",0);
		allExhibitions[1]= new Exhibition("Media coverage",1);
		allExhibitions[2]= new Exhibition("In the eyes of a queen",2);
		allExhibitions[3]= new Exhibition("Remembering removals",3);
		allExhibitions[4]= new Exhibition("Then and now",4);
		
		return allExhibitions;
	}*/
	
	public List<Exhibition> listExhibitions()
	{
		@SuppressWarnings("unchecked")
		List<Exhibition> listExhibitions= manager.listAllExhibitions();
		return listExhibitions;
	}
	
	public Integer saveExhibition(Exhibition exhibition)
	{
		return(manager.addExhibition(exhibition));//add exhibition to the db
	}
	

	
	public Exhibition getExhibition(int exhibitionID)
	{
		return (manager.getExhibition(exhibitionID));//get exhibition with the specific ID
		
	}
	
	public String[] processTemplate(String input)
	{
		String [] exhibition= new String[12];
		String temp="";
		int position=0;
		boolean remove=false;
		int flag=0;//0 if it is a template box, 1 if it is an image title
		
		for (int i=0;i<input.length();i++){
			while(input.charAt(i)!=' '){
				temp+=input.charAt(i);
				if(i<input.length()-1){
					i++;
				}
			}
			if (temp.equals("REMOVE")){
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
			System.out.println(temp);
			temp="";	
		}
		return exhibition;
	}

}
