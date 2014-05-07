package task1;

class DictionaryTopic extends DictionaryElem
{
	private String theTopic;
		
	public DictionaryTopic(String topic)
	{
		theTopic = topic;
	}
	
	public  String getType()
	{
		return "Topic";
	}
	
	public String[] getValue()
	{
		String [] a = {theTopic};
		return a;		
	}	

	public String toString(){
		return theTopic;		
	}

	@Override
	public String getPicString() {
		return null;
	}	

}