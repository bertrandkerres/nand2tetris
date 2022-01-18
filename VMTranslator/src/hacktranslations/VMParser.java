package hacktranslations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class VMParser {
	String baseFileName;
	BufferedReader br;
	int curLblNum;
	
	List<VMCommand> ops = new ArrayList<VMCommand>();

	public VMParser(String inFileName) throws IOException {
		br = new BufferedReader(new FileReader(inFileName));
		baseFileName = FilenameUtils.getBaseName(inFileName);
		
		while (this.hasMoreCommands())
			this.advance();
	}

	public List<VMCommand> getCommands() {
		return ops;
	}
	
	public boolean hasMoreCommands() throws IOException {
		return br.ready();
	}
	
	public void advance() throws IOException {
		String nextMne = VMParser.removeComments(br.readLine()).trim();
		while ("".equals(nextMne)) {
			nextMne = VMParser.removeComments(br.readLine()).trim();
		}
		
		VMCommandType curT = VMCommandType.commandType(nextMne);
		VMCommand curCom = null;
		switch(curT) {
		case ADD:
		case SUB:
		case AND:
		case OR:
		case NEG:
		case NOT:
			curCom = new VMArithmetic(curT);
			break;
		case GT:
		case LT:
		case EQ:
			curCom = new VMComparison(curT, curLblNum++);
			break;
		case PUSH_SEG:
		case POP_SEG:
		case PUSH_CONST:
		case PUSH_TEMP:
		case POP_TEMP:
		case PUSH_STATIC:
		case POP_STATIC:
		case PUSH_POINTER:
		case POP_POINTER:
			String[] parts = nextMne.split(" ");
			curCom = new VMMemoryCommand(curT, parts[1], baseFileName, Integer.valueOf(parts[2]).intValue());
			break;
		default:
			// do nothing
		}
		if (curCom != null)
			ops.add(curCom);
	}
	
	public static String removeComments (String s) {
		int i = s.indexOf("//");
		if (i == -1)
			return s;
		else if (i == 0)
			return "";
		else
			return s.substring(0, i-1);
	}
}
