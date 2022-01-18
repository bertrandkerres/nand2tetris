package hacktranslations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HackCommand {
	static final Pattern validSymbolPattern = Pattern.compile("^[a-zA-Z_.$:]{1}[a-zA-Z_.$:0-9]*");
	HackCommandType ctype;
	String symbol;
	
	HackCommandDest dest;
	HackCommandComp comp;
	HackCommandJump jump;
	
	public HackCommandType getCType() {
		return ctype;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(int i) throws IllegalArgumentException {
		if (i >= 0x8000 || i < 0)
			throw new IllegalArgumentException("Invalid symbol integer value " + String.valueOf(i));
		symbol = String.valueOf(i);
	}

	public HackCommandDest getDest() {
		return dest;
	}

	public HackCommandComp getComp() {
		return comp;
	}

	public HackCommandJump getJump() {
		return jump;
	}
	
	public boolean isSymbolic() {
		Matcher m = HackCommand.validSymbolPattern.matcher(symbol);
		return m.matches();
	}

	public HackCommand(String s) throws IllegalArgumentException {
		s = s.trim();
		if (s.length()==0) {
			this.ctype = HackCommandType.NULL_COMMAND;
			return;
		}
		
		switch (s.charAt(0)) {
		case '@':
			this.ctype = HackCommandType.A_COMMAND;
			this.symbol = s.substring(1);
			break;
		case '(':
			this.ctype = HackCommandType.L_COMMAND;
			this.symbol = s.substring(1, s.indexOf(')'));
			break;
		default:
			this.ctype = HackCommandType.C_COMMAND;
			String[] c_j = s.split(";");
			String[] d_c = c_j[0].split("=");
			
			if (c_j.length == 1) {
				this.jump = HackCommandJump.NULL;
			} 
			else {
				this.jump = HackCommandJump.fromString(c_j[1]);
			}
			
			if (d_c.length == 1) { 
				this.dest = HackCommandDest.NULL;
				this.comp = HackCommandComp.fromString(d_c[0]);
			}
			else {
				this.dest = HackCommandDest.fromString(d_c[0]);
				this.comp = HackCommandComp.fromString(d_c[1]);
			}
			
		}
	}
	
	public HackCommand(HackCommandType t) {
		ctype = t;
	}
	
	@Override
	public String toString() {
		return this.toDebugString();
	}

	public String toDebugString() {
		switch (this.ctype) {
		case A_COMMAND:
			return "@" + this.symbol;
		case L_COMMAND:
			return "(" + this.symbol + ")";
		case C_COMMAND:
			String ret = this.dest.toDebugString();
			if (!ret.equals(""))
				ret = ret + "=";
			ret = ret + this.comp.toDebugString();
			
			if (this.jump != HackCommandJump.NULL) {
				ret = ret + ";" + this.jump.toDebugString();
			}
			return ret;
		default:
			return "";	
		}
	}
	
	public String toBinaryString() {
		switch (this.ctype) {
		case A_COMMAND:
			return "0" + Integer.toBinaryString(0b1000000000000000 | Integer.valueOf(symbol).intValue()).substring(1);
		case C_COMMAND:
			return "111" + comp.toBinaryString() + dest.toBinaryString() + jump.toBinaryString();
		default:
			return "";
		}
	}
	
	public int toInt() {
		switch (ctype) {
		case A_COMMAND:
			return Integer.valueOf(symbol).intValue();
		case C_COMMAND:
			return (0b111 << 13) | (comp.getBValue() << 7) | (dest.ordinal() << 3) | (jump.ordinal());
		default:
			return -1;	
		}
	}
	
	public static HackCommand newACommand(String sym) {
		HackCommand hc = new HackCommand(HackCommandType.A_COMMAND);
		hc.symbol = sym;
		return hc;
	}
	
	public static HackCommand newLCommand(String sym) {
		HackCommand hc = new HackCommand(HackCommandType.L_COMMAND);
		hc.symbol = sym;
		return hc;
	}

	
	public static HackCommand newCCommand(HackCommandDest d, HackCommandComp c, HackCommandJump j) {
		HackCommand hc = new HackCommand(HackCommandType.C_COMMAND);
		hc.dest = d;
		hc.comp = c;
		hc.jump = j;
		return hc;
	}
}
