package parser;

//@@author A0126219J
/**
 * This class stores a Keyword, its string and its parameter. 
 */
public class KeyParamPair {
	private Keywords keyword;
	private String keyString;
	private String param;
	
	/**
	 * Constructs a KeyParamPair object with fields equal to the respective parameters.
	 * @param inputKeyword
	 * @param inputKeyString
	 * @param inputParam
	 */
	public KeyParamPair(Keywords inputKeyword, String inputKeyString, String inputParam) {
		keyword = inputKeyword;
		keyString = inputKeyString;
		param = inputParam;
	}

	/**
	 * Returns the keyword as a Keywords enum type
	 * @return keyword of the pair
	 */
	public Keywords getKeyword() {
		return keyword;
	}

	/**
	 * Returns the parameters of the pair
	 * @return parameters
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Returns the keyword as its original String
	 * @return keyword of the pair as String
	 */
	public String getKeyString() {
		return keyString;
	}

	/**
	 * Sets the parameters of the pair as param
	 * @param param
	 */
	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public boolean equals(Object other) {
		if (other.getClass() == this.getClass()) {
			Keywords xKeyword = this.getKeyword();
			Keywords yKeyword = ((KeyParamPair) other).getKeyword();
			String xKeyString = this.getKeyString();
			String yKeyString = ((KeyParamPair) other).getKeyString();
			String xParam = this.getParam();
			String yParam = ((KeyParamPair) other).getParam();
			return xKeyword.equals(yKeyword) && xParam.equals(yParam)
					&& xKeyString.equals(yKeyString);
		} else {
			return false;
		}
	}
}
