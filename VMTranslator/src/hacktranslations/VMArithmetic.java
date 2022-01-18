package hacktranslations;

import java.util.ArrayList;
import java.util.List;

public class VMArithmetic extends VMCommand {
	boolean use2Arg;
	HackCommandComp mainOP;
	
	public boolean has2Arg() { return use2Arg; }
	
	public VMArithmetic (VMCommandType t) {
		this.type = t;
		this.arg = 0;
		switch (t) {
		case ADD:
			use2Arg = true;
			mainOP = HackCommandComp.D_PLUS_M;
			break;
		case SUB:
			use2Arg = true;
			mainOP = HackCommandComp.M_MINUS_D;
			break;
		case AND:
			use2Arg = true;
			mainOP = HackCommandComp.D_AND_M;
			break;
		case OR:
			use2Arg = true;
			mainOP = HackCommandComp.D_OR_M;
			break;
		case NEG:
			use2Arg = false;
			mainOP = HackCommandComp.NEG_M;
			break;
		case NOT:
			use2Arg = false;
			mainOP = HackCommandComp.NOT_M;
			break;
		default:
			throw new IllegalArgumentException();
			
		}
	}
	
	@Override
	public String toString() {
		return this.type.toString().toLowerCase();
	}
	
	@Override
	public List<HackCommand> toHackCommands() {
		List<HackCommand> ops = new ArrayList<HackCommand>();
		// SP--
		ops.add(HackCommand.newACommand("SP"));
		if (use2Arg) {
			ops.add(HackCommand.newCCommand(HackCommandDest.AM, HackCommandComp.M_DEC, HackCommandJump.NULL));
			ops.add(HackCommand.newCCommand(HackCommandDest.D, HackCommandComp.M, HackCommandJump.NULL));
			ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.A_DEC, HackCommandJump.NULL));
		} else {
			ops.add(HackCommand.newCCommand(HackCommandDest.A, HackCommandComp.M_DEC, HackCommandJump.NULL));
		}
		ops.add(HackCommand.newCCommand(HackCommandDest.M, this.mainOP, HackCommandJump.NULL));

		return ops;
	}
}
