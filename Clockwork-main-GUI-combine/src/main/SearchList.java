package main;

/**
* Serves as a library for searching through any arbitrary list. Flexible to being used as an object
* for arbitrary texts and delimeters, to just being used statically, where the user must provide
* the text to be searched, the target pattern, as well as the delimeter. The delimeter may be
* any standard regex pattern.
**/

public class SearchList {
	private String listedInput;
	private String delimeter;
	
	public SearchList(String listedInput, String delimeter) {
		this.listedInput = listedInput;
	}
	
	public SearchList(String listedInput) {
		this(listedInput, "\n");
	}
	
	public SearchList() {
		this(null, "\n");
	}
	
	public String getListedInput() {
		return listedInput;
	}
	
	public void setListedInput(String listedInput) {
		this.listedInput = listedInput;
	}
	
	public String getDelimeter() {
		return delimeter;
	}
	
	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}
	
	public String[] searchCurrent(String targetWord) {
		String[] itemsContainingWord = null;
		
		if(listedInput != null) {
			String[] tokensAfterDelimeter = listedInput.split(delimeter);
			int targetWordHit = 0;
			for(String s: tokensAfterDelimeter) {
				if(s.contains(targetWord)) {
					targetWordHit++;
				}
			}
			
			if(targetWordHit > 0) {
				itemsContainingWord = new String[targetWordHit];
				targetWordHit = 0;
				for(String s: tokensAfterDelimeter) {
					if(s.contains(targetWord)) {
						itemsContainingWord[targetWordHit] = s;
					}
				}
			}
		}
		return itemsContainingWord;
	}
	
	public static String[] searchList(String targetWord, String inputList, String delimeter) {
		SearchList searcher = new SearchList(inputList, delimeter);
		return searcher.searchCurrent(targetWord);
	}
}

