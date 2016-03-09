package main;

public class ExitCommand extends Command {
	public ExitCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
    public Signal execute() {
        String param = keyParamPairList.get(0).getParam();
        if (!param.isEmpty()) {
            return new Signal(Signal.EXIT_INVALLID_PARAMS, false);
        }

        return new Signal(Signal.EXIT_SUCCESS, true);
    }
}
