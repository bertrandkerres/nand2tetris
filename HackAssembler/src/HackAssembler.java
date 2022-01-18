import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

/*import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;*/

// @Command(description = "Assembles HACK symbolic code to (strings of) binary code",
// name = "HackAssembler", mixinStandardHelpOptions = true, version = "HackAssembler 1.0")
public class HackAssembler implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(HackAssembler.class.getName());
	
// 	@Parameters(index = "0", description = "The file to assemble.")
	private String inFileName;
	
	private String outFileName;
	private String logFileName;
	private String symbolTableFileName;
	
	
	public static void main (String[] args) {
		(new HackAssembler(args[0])).run();
	}
	
	public HackAssembler (String ifn) {
		inFileName = ifn;
		outFileName = FilenameUtils.removeExtension(ifn) + ".hack";
		logFileName = FilenameUtils.removeExtension(ifn) + ".log";
		symbolTableFileName = FilenameUtils.removeExtension(ifn) + ".symb";
	}

	@Override
	public void run() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFileName)))) {
			HackParser hp = new HackParser(inFileName);
			
			FileHandler logFH = new FileHandler(logFileName);
			HackDebugFormatter formatter = new HackDebugFormatter();
			logFH.setFormatter(formatter);
			HackAssembler.LOGGER.addHandler(logFH);
			HackAssembler.LOGGER.setLevel(Level.FINE);
			
			hp.printSymbolTable(symbolTableFileName);
			
			for (HackCommand c : hp.getOps()) {
				bw.write(c.toBinaryString() + System.lineSeparator());
				HackAssembler.LOGGER.fine(c.toDebugString() + "\t\t" + c.toBinaryString() + System.lineSeparator());
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			// nothing here
		}
		
	}
}
