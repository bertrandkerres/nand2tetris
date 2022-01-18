
public enum HackCommandJump {
	NULL, JGT, JEQ, JGE, JLT, JNE, JLE, JMP;
	
	public static HackCommandJump fromString(String s) throws IllegalArgumentException {
		switch (s) {
		case "": return HackCommandJump.NULL;
		case "JGT": return HackCommandJump.JGT;
		case "JEQ": return HackCommandJump.JEQ;
		case "JGE": return HackCommandJump.JGE;
		case "JLT": return HackCommandJump.JLT;
		case "JNE": return HackCommandJump.JNE;
		case "JLE": return HackCommandJump.JLE;
		case "JMP": return HackCommandJump.JMP;
		default: throw new IllegalArgumentException("Invalid jump command");
		}
	}
	
	public String toDebugString() {
		if (this == HackCommandJump.NULL)
			return "";
		else
			return this.toString();
	}	
	
	public String toBinaryString() {
		return Integer.toBinaryString(0b1000 | this.ordinal()).substring(1);
	}
}
