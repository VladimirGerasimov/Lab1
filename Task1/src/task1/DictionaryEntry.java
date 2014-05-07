package task1;

import javax.swing.ImageIcon;

class DictionaryEntry extends DictionaryElem
{
	protected String theName;
	protected String theSurname;
	protected String theDate;
	protected String theNumber;
	protected String thePic;
	protected ImageIcon icon;
	public DictionaryEntry (String name, String surname, String date, String number){
		theName = name;
		theSurname = surname;
		theNumber = number;
		theDate = date;
		thePic = "";
	}
	
	public DictionaryEntry (String completeStr){
			theName = completeStr.substring(0, completeStr.indexOf(" "));
			completeStr = completeStr.substring(completeStr.indexOf(" ")+1);
			theSurname = completeStr.substring(0, completeStr.indexOf(" "));
			completeStr = completeStr.substring(completeStr.indexOf(" ")+1);
			theDate = completeStr.substring(0, completeStr.indexOf(" "));
			completeStr = completeStr.substring(completeStr.indexOf(" ")+1);
			if(completeStr.indexOf(" ") != -1){
				theNumber = completeStr.substring(0, completeStr.indexOf(" "));
				completeStr = completeStr.substring(completeStr.indexOf(" ")+1);
				if(completeStr.length() > 1){
					thePic = completeStr;
					ImageProcessor ip = new ImageProcessor(completeStr);
					icon = ip.icon;
				}
			} else {
				theNumber = completeStr;
			}
	}	
	
	public void setImageIcon(ImageProcessor ip){
		icon = ip.returnImageIcon();
		thePic = ip.makeString();
	}
	
	public String getType(){
		return "Entry";
	}
	
	public  String [] getValue(){
		String a[] = {theName, theSurname, theDate, theNumber, thePic};
		return a;		
	}
	
	public  String toString(){
		if(theName.indexOf(" ") > 0){
			theName = theName.replaceAll(" ", "_");
		}
		if(theSurname.indexOf(" ") > 0){
			theSurname = theSurname.replaceAll(" ", "_");
		}
		return theName + " " + theSurname + " " + theDate +" " + theNumber;		
	}
	
	public String getName(){
		return theName;
	}
	public String getSurname(){
		return theSurname;
	}
	public String getDate(){
		return theDate;
	}
	public String getNumber(){
		return theNumber;
	}
	public String getPicString(){
		return thePic;
	}		
	public ImageIcon getPic(){
		return icon;
	}
}
