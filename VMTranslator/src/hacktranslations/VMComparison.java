package hacktranslations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VMComparison extends VMCommand {
	
	public VMComparison(VMCommandType t, int arg) {
		this.type = t;
		this.arg = arg;
	}
	
	@Override
	public String toString() {
		return this.type.toString().toLowerCase();
	}

	@Override
	public List<HackCommand> toHackCommands() {
		List<HackCommand> ops = new ArrayList<HackCommand>();
		// @SP, AM=M-1
		ops.addAll(Arrays.asList(VMCommand.SPdec));
//		ops.add(HackCommand.newACommand("SP"));
//		ops.add(HackCommand.newCCommand(HackCommandDest.AM, HackCommandComp.M_DEC, HackCommandJump.NULL));

		// POP y: D=M, A=A-1
		ops.add(HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL));
		ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.A_DEC, HackCommandJump.NULL));
		// x-y: D=M-D
		ops.add(HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M_MINUS_D, HackCommandJump.NULL));
		ops.add(HackCommand.newACommand("PUSH_FALSE" + String.valueOf(arg)));
		if (this.type == VMCommandType.GT)
			ops.add(HackCommand.newCCommand(HackCommandDest.NULL, HackCommandComp.D, HackCommandJump.JLE));
		else if (this.type == VMCommandType.LT)
			ops.add(HackCommand.newCCommand(HackCommandDest.NULL, HackCommandComp.D, HackCommandJump.JGE));
		else
			ops.add(HackCommand.newCCommand(HackCommandDest.NULL, HackCommandComp.D, HackCommandJump.JNE));


		// (PUSH_TRUE)
		ops.add(HackCommand.newLCommand("PUSH_TRUE" + String.valueOf(arg)));
		// Push -1: @SP, A=M-1, M=-1, A=A+1
		ops.add(HackCommand.newACommand("SP"));
		ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M_DEC, HackCommandJump.NULL));
		ops.add(HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.NEG_ONE, HackCommandJump.NULL));
		// ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.A_INC, HackCommandJump.NULL));
		// @END, JMP
		ops.add(HackCommand.newACommand("END" + String.valueOf(arg)));
		ops.add(HackCommand.newCCommand(HackCommandDest.NULL, HackCommandComp.NULL, HackCommandJump.JMP));
		
		// (PUSH_FALSE)
		ops.add(HackCommand.newLCommand("PUSH_FALSE" + String.valueOf(arg)));
		// Push 0: @SP, M=0, A=A+1
		ops.add(HackCommand.newACommand("SP"));
		ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M_DEC, HackCommandJump.NULL));
		ops.add(HackCommand.newCCommand(HackCommandDest.M, HackCommandComp.NULL, HackCommandJump.NULL));
		// ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.A_INC, HackCommandJump.NULL));

		// (END)
		ops.add(HackCommand.newLCommand("END" + String.valueOf(arg)));
		
		return ops;
	}
	
}
