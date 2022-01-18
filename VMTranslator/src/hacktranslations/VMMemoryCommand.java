package hacktranslations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VMMemoryCommand extends VMCommand {
	String hackSegName;
	String vmSegName;
	String baseFileName;
	
	final static Map<String,String> MEMORY_SEGMENTS = Map.of("local", "LCL",
			"argument", "ARG",
			"this", "THIS",
			"that", "THAT",
			"temp", "TEMP",
			"pointer", "POINTER",
			"static", "STATIC",
			"constant", "CONSTANT");
	
	final static int TMP_BASE = 5;
	
		
	public VMMemoryCommand(VMCommandType t, String vmSegName, String filename, int arg) {
		this.type = t;
		this.hackSegName = VMMemoryCommand.MEMORY_SEGMENTS.get(vmSegName);
		this.vmSegName = vmSegName;
		this.baseFileName = filename;
		this.arg = arg;
	}
	
	@Override
	public String toString() {
		String cmd = type.toString().substring(0,3).toUpperCase().equals("POP") ? "pop" : "push";
		return cmd + " " + vmSegName + " " + String.valueOf(arg);
	}

	@Override
	public List<HackCommand> toHackCommands() throws Exception {
		switch (this.type) {
		case PUSH_SEG:
			return Arrays.asList(VMMemoryCommand.pushSeg(hackSegName, arg));
		case POP_SEG:
			return Arrays.asList(VMMemoryCommand.popSeg(hackSegName, arg));
		case PUSH_CONST:
			return Arrays.asList(VMMemoryCommand.pushConst(arg));
		case PUSH_TEMP:
			return Arrays.asList(VMMemoryCommand.pushTemp(arg));
		case POP_TEMP:
			return Arrays.asList(VMMemoryCommand.popTemp(arg));
		case PUSH_POINTER:
			return Arrays.asList(VMMemoryCommand.pushPointer(arg));
		case POP_POINTER:
			return Arrays.asList(VMMemoryCommand.popPointer(arg));
		case PUSH_STATIC:
			return Arrays.asList(VMMemoryCommand.pushStatic(baseFileName, arg));
		case POP_STATIC:
			return Arrays.asList(VMMemoryCommand.popStatic(baseFileName, arg));
		default:
			throw new Exception("Unhandled VMMemoryCommand: " + this.toString());	
		}

	}
	
	
	private static HackCommand[] popStatic(String filename, int i) {
		HackCommand[] retVal = new HackCommand[VMMemoryCommand.SPdec.length+3];
		int j = 0;
		// SP--
		System.arraycopy(VMMemoryCommand.SPdec, 0, retVal, 0, VMMemoryCommand.SPdec.length);
		j += VMMemoryCommand.SPdec.length;
		// D = SP*
		retVal[j++] = HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL);
		// filename.i* = D
		retVal[j++] = HackCommand.newACommand(filename + "." + String.valueOf(i)); 
		retVal[j++] = HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL);
		return retVal;
	}
	
	private static HackCommand[] pushStatic(String filename, int i) {
		HackCommand[] retVal = new HackCommand[VMMemoryCommand.SPinc.length+VMMemoryCommand.SP_ass_D.length+2];
		int j = 0;
		// D = filename.i*
		retVal[j++] = HackCommand.newACommand(filename + "." + String.valueOf(i));
		retVal[j++] = HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL);
		// SP* = D
		System.arraycopy(VMMemoryCommand.SP_ass_D, 0, retVal, j, VMMemoryCommand.SP_ass_D.length);
		j += VMMemoryCommand.SP_ass_D.length;
		// SP++
		System.arraycopy(VMMemoryCommand.SPinc, 0, retVal, j, VMMemoryCommand.SPinc.length);
		// j += VMMemoryCommands.SPinc.length;
		
		return retVal;
	}
	
	private static HackCommand[] popSeg(String seg, int i) {
		HackCommand[] retVal = {
			// R13 = seg+i
			HackCommand.newACommand(String.valueOf(i)),
			HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.A, HackCommandJump.NULL),
			HackCommand.newACommand(seg),
			HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.D_PLUS_M, HackCommandJump.NULL),
			HackCommand.newACommand("R13"),
			HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL),
			// SP--
			HackCommand.newACommand("SP"),
			HackCommand.newCCommand(HackCommandDest.AM, HackCommandComp.M_DEC, HackCommandJump.NULL),
			HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL),
			// addr* = SP*
			HackCommand.newACommand("R13"),
			HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M, HackCommandJump.NULL),
			HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL)			
		};
		return retVal;
	}
	
	private static HackCommand[] pushSeg(String seg, int i) {
		HackCommand[] retVal = {
			// D = addr*
			HackCommand.newACommand(String.valueOf(i)),
			HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.A, HackCommandJump.NULL),
			HackCommand.newACommand(seg),
			HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.D_PLUS_M, HackCommandJump.NULL),
			HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL),
			// SP* = D
			HackCommand.newACommand("SP"),
			HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M, HackCommandJump.NULL),
			HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL),
			// SP++
			HackCommand.newACommand("SP"),
			HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.M_INC, HackCommandJump.NULL)
		};
		return retVal;
	}
	
	private static HackCommand[] pushConst(int i) {
		HackCommand[] retVal = {
				// D = const
				HackCommand.newACommand(String.valueOf(i)),
				HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.A, HackCommandJump.NULL),
				// SP* = D
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M, HackCommandJump.NULL),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL),
				// SP++
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.M_INC, HackCommandJump.NULL)
			};
			return retVal;
	}
	
	private static HackCommand[] pushTemp(int i) {
		HackCommand[] retVal = {
				// D = addr*
				HackCommand.newACommand(String.valueOf(TMP_BASE+i)),
				HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL),				
				// SP* = D
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M, HackCommandJump.NULL),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL),
				// SP++
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.M_INC, HackCommandJump.NULL)
			};
			return retVal;
		}
	
	private static HackCommand[] popTemp(int i) {
		HackCommand[] retVal = {
				// SP--, D = SP*
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.AM, HackCommandComp.M_DEC, HackCommandJump.NULL),
				HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL),
				// addr* = D
				HackCommand.newACommand(String.valueOf(TMP_BASE+i)),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL)			
			};
			return retVal;
		
	}
	
	private static HackCommand[] pushPointer(int i) {
		String seg;
		if (i == 0) seg = "THIS";
		else if (i == 1) seg = "THAT";
		else throw new IllegalArgumentException();

		HackCommand[] retVal = {
				// D = THIS/THAT
				HackCommand.newACommand(seg),
				HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL),
				// SP* = D
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M, HackCommandJump.NULL),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL),
				// SP++
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.M_INC, HackCommandJump.NULL)
		};
		
		return retVal;
	}
	
	private static HackCommand[] popPointer(int i) {
		String seg;
		if (i == 0) seg = "THIS";
		else if (i == 1) seg = "THAT";
		else throw new IllegalArgumentException();
		
		HackCommand[] retVal = {
				// D = SP--
				HackCommand.newACommand("SP"),
				HackCommand.newCCommand(HackCommandDest.AM, HackCommandComp.M_DEC, HackCommandJump.NULL),
				HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL),
				// M = D
				HackCommand.newACommand(seg),
				HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL)
		};
		return retVal;
	}
}
