package hacktranslations;

public enum VMCommandType {
	GT, LT, EQ,
	ADD, SUB, AND, OR, 
	NEG, NOT,
	PUSH_SEG, POP_SEG, 
	PUSH_CONST,
	PUSH_STATIC, POP_STATIC, 
	PUSH_TEMP, POP_TEMP, 
	PUSH_POINTER, POP_POINTER;
	
	public static VMCommandType commandType(String s) {
		String[] parts = s.split(" ");
		if ("".equals(parts[0])) throw new IllegalArgumentException();
		
		if (parts[0].toLowerCase().equals("push")) {
			if (parts.length != 3) throw new IllegalArgumentException();
			switch(parts[1]) {
			case "local":
			case "argument":
			case "this":
			case "that":
				return PUSH_SEG;
			case "constant":
				return PUSH_CONST;
			case "static":
				return PUSH_STATIC;
			case "temp":
				return PUSH_TEMP;
			case "pointer":
				return PUSH_POINTER;
			default:
				throw new IllegalArgumentException();
			}
		} else if (parts[0].toLowerCase().equals("pop")) {
			if (parts.length != 3) throw new IllegalArgumentException();
			
			switch(parts[1]) {
			case "local":
			case "argument":
			case "this":
			case "that":
				return POP_SEG;
			case "static":
				return POP_STATIC;
			case "temp":
				return POP_TEMP;
			case "pointer":
				return POP_POINTER;
			default:
				throw new IllegalArgumentException();
			}
			
		} else {
			switch(parts[0].toLowerCase()) {
			case "gt": return GT;
			case "lt": return LT;
			case "eq": return EQ;
			case "add": return ADD;
			case "sub": return SUB;
			case "and": return AND;
			case "or": return OR;
			case "neg": return NEG;
			case "not": return NOT;
			default: throw new IllegalArgumentException();
			}
		}
	}
	
}
