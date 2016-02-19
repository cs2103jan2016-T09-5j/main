package main;

import java.util.ArrayList;

public abstract class Command {
	ParsedInput input;
	Memory memory;
	ArrayList<KeyParamPair> keyParamPairList;
	
	public Command(ParsedInput input, Memory memory) {
		keyParamPairList = input.getParamPairList();
		this.input = input;
		this.memory = memory;
	}

	public abstract Signal execute();

}
