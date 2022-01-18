
public enum HackCommandComp {
	 NULL(0b0101010, "0"),
	 ONE(0b0111111, "1"),
	 NEG_ONE(0b0111010, "-1"),
	 D(0b0001100, "D"),
	 A(0b0110000, "A"), M(0b1110000, "M"),
	 NOT_D(0b0001101, "!D"),
	 NOT_A(0b0110001, "!A"), NOT_M(0b1110001, "!M"),
	 NEG_D(0b0001111, "-D"),
	 NEG_A(0b0110011, "-A"), NEG_M(0b1110011, "-M"),
	 D_INC(0b0011111, "D+1"),
	 A_INC(0b0110111, "A+1"), M_INC(0b1110111, "M+1"),
	 D_DEC(0b0001110, "D-1"),
	 A_DEC(0b0110010, "A-1"), M_DEC(0b1110010, "M-1"),
	 D_PLUS_A(0b0000010, "D+A"), D_PLUS_M(0b1000010, "D+M"),
	 D_MINUS_A(0b0010011, "D-A"), D_MINUS_M(0b1010011, "D-M"),
	 A_MINUS_D(0b0000111, "A-D"), M_MINUS_D(0b1000111, "M-D"),
	 D_AND_A(0b0000000, "D&A"), D_AND_M(0b1000000, "D&M"),
	 D_OR_A(0b0010101, "D|A"), D_OR_M(0b1010101, "D|M");
	 
	 final int bval;
	 final String mnemonic;
	
	HackCommandComp (int i, String s) {
		this.bval = i;
		this.mnemonic = s;
	}
	
	public int getBValue() {
		return this.bval;
	}
	
	public String getMnemonic() {
		return this.mnemonic;
	}
	
	public String toDebugString() {
		return this.mnemonic;
	}

	public static HackCommandComp fromString(String s) throws IllegalArgumentException {
		String str = s.replaceAll("\\s+","");
		for (HackCommandComp c : HackCommandComp.values()) {
			if (str.equals(c.getMnemonic()))
				return c;
		}
		throw new IllegalArgumentException("Invalid compute command");
	}
	
	public String toBinaryString() {
		return Integer.toBinaryString(0b10000000 | this.bval).substring(1);
	}	
}
