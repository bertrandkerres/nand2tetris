package hacktranslations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.apache.commons.io.FilenameUtils;

public class VMTranslator implements Runnable {
	private String inFileName;
	private String outFileName;
	
	public static void main(String[] args) {
		(new VMTranslator((args[0]))).run();
	}
	
	public VMTranslator(String inFileName) {
		this.inFileName = inFileName;
		this.outFileName = FilenameUtils.removeExtension(inFileName) + ".asm";
	}

	@Override
	public void run() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFileName)))) {
			VMParser vmp = new VMParser(inFileName);
			
			for (VMCommand v : vmp.getCommands()) {
				
				bw.write("// " + v.toString() + System.lineSeparator());
				for (HackCommand hc : v.toHackCommands()) {
					bw.write(hc.toString() + System.lineSeparator());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
