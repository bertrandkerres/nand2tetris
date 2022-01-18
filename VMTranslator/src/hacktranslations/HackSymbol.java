package hacktranslations;

public enum HackSymbol {
	SP(0), LCL(1), ARG(2),
	THIS(3), THAT(4),
	SCREEN(0x4000), KBD(0x6000);
	
	final int val;
	
	HackSymbol(int i) {
		val = i;
	}
	
	public int getVal() {
		return val;
	}
}
