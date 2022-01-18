import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HackParser {
	HackCommand curCommand;
	List<HackCommand> ops = new ArrayList<HackCommand>();
	
	HashMap<String,Integer> symbolTable = new HashMap<String,Integer>();
	int curMemAddr = 16;
	
	BufferedReader br;
	
	public List<HackCommand> getOps() {
		return this.ops;
	}
	
	public HackParser (String path) throws IOException {
		this.br = new BufferedReader(new FileReader(path));
		this.symbolTable.put("SP", 0);
		this.symbolTable.put("LCL", 1);
		this.symbolTable.put("ARG", 2);
		this.symbolTable.put("THIS", 3);
		this.symbolTable.put("THAT", 4);
		this.symbolTable.put("SCREEN", 0x4000);
		this.symbolTable.put("KBD", 0x6000);
		for (int i = 0; i <= 15; i++)
			this.symbolTable.put("R"+String.valueOf(i), i); 

		while (this.hasMoreCommands()) {
			this.advance();
		}
		
		for (HackCommand hc : this.ops) {
			if (hc.getCType() == HackCommandType.A_COMMAND && hc.isSymbolic()) {
				String s = hc.getSymbol();
				if (!symbolTable.containsKey(s))
					symbolTable.put(s, curMemAddr++);
				hc.setSymbol(symbolTable.get(s).intValue());
			}
		}
	}
	
	public boolean hasMoreCommands() throws IOException {
		return br.ready();
	}
	
	public void printSymbolTable (String filename) {
		StringBuilder s = new StringBuilder();
		symbolTable.forEach((k, v) -> s.append(k + "\t\t" + String.valueOf(v) + System.lineSeparator()));
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)))) {
			bw.write(s.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void advance () throws IOException {
		String nextMne;
		do {
			nextMne = HackParser.removeComments(br.readLine());
			curCommand = new HackCommand(nextMne);
		} while (curCommand.getCType() == HackCommandType.NULL_COMMAND && this.hasMoreCommands());

		switch(curCommand.getCType()) {
		case A_COMMAND:
		case C_COMMAND:
			this.ops.add(curCommand);
			break;
		case L_COMMAND:
			if (curCommand.isSymbolic())
				this.symbolTable.put(curCommand.getSymbol(), this.ops.size());
			break;
		default:
			// do nothing
		}

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
