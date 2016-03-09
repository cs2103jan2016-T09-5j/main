package main;
import java.util.ArrayList;

public class ParsedInput {
	
	private KEYWORDS type;
	private ArrayList<KeyParamPair> keyParamPairList;
    
    public ParsedInput(KEYWORDS cType, ArrayList<KeyParamPair> pairList) {
    	type = cType;
    	keyParamPairList= pairList;
	}
    
    public KEYWORDS getType() {
		return type;
		}

	public ArrayList<KeyParamPair> getParamPairList() {
		return keyParamPairList;
	}
	
	/**
	 * Iterates through the keyParamPair ArrayList and checks if any parameter is an empty string.
	 * 
	 * @return boolean If there is at least one empty string parameter, return true. Else, return false.
	 */
	public boolean containsEmptyParams() {
		for(KeyParamPair pair : keyParamPairList){
			if(pair.getParam().equals("")){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass().equals(this.getClass())) {
			return this.getType().equals(((ParsedInput) o).getType()) &&
					this.getParamPairList().equals(((ParsedInput) o).getParamPairList());
		}
		return false;
	}
}
