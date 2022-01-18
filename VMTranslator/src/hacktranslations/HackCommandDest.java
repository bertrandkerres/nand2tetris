package hacktranslations;

public enum HackCommandDest {
	NULL, M, D, MD, A, AM, AD, AMD;
	
	public static HackCommandDest fromString(String s) throws IllegalArgumentException {
		switch (s) {
		case "": return HackCommandDest.NULL;
		case "M": return HackCommandDest.M;
		case "D": return HackCommandDest.D;
		case "MD": return HackCommandDest.MD;
		case "A": return HackCommandDest.A;
		case "AM": return HackCommandDest.AM;
		case "AD": return HackCommandDest.AD;
		case "AMD": return HackCommandDest.AMD;
		default: throw new IllegalArgumentException("Invalid destination command");
		}
	}
	
	public String toDebugString() {
		if (this == HackCommandDest.NULL)
			return "";
		else
			return this.toString();
	}
	
	public String toBinaryString() {
		return Integer.toBinaryString(0b1000 | this.ordinal()).substring(1);
	}	
}
