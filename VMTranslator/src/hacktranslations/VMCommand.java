package hacktranslations;

import java.util.List;

public abstract class VMCommand {
	int arg;
	VMCommandType type;
	
	public abstract List<HackCommand> toHackCommands() throws Exception;
	
	public int getIntArg() { return arg; }

	public VMCommandType getType() { return type; }
	
	final static HackCommand[] SPinc = {
			// SP++
			HackCommand.newACommand("SP"),
			HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.M_INC, HackCommandJump.NULL)
		};
		
		final static HackCommand[] SPdec = {
			// SP--
			HackCommand.newACommand("SP"),
			HackCommand.newCCommand(HackCommandDest.AM, HackCommandComp.M_DEC, HackCommandJump.NULL),
		};
		
		final static HackCommand[] SP_ass_D = {
			// SP* = D
			HackCommand.newACommand("SP"),
			HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M, HackCommandJump.NULL),
			HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.D, HackCommandJump.NULL),
		};	
}
