package main;

public class KeyParamPair {
	private String keyword;
	private String param;
	
	KeyParamPair(String commandKeyword, String commandParam) {
		keyword = commandKeyword;
		param = commandParam;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other.getClass() == this.getClass()) {
			String xKeyword = this.getKeyword();
			String yKeyword = ((KeyParamPair) other).getKeyword();
			String xParam = this.getParam();
			String yParam = ((KeyParamPair) other).getParam();
			return xKeyword.equals(yKeyword) && xParam.equals(yParam);
		} else {
			return false;
		}
	}
}
