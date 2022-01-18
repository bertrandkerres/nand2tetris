// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

(START)
	// Init scrpos = SCREEN[0]
	@SCREEN
	D=A;
	
	@scrpos
	M=D
	
	// If keyboard==0, goto WHITE; else goto BLACK
	@KBD
	D=M;
	
	@BLACK
	D; JNE
	
(WHITE)
	// R0 = 0x0000; goto UPDATESCREEN
	@R0
	M=0
	
	@UPDATESCREEN
	0; JMP

(BLACK)
	// R0 = 0xFFFF; (goto UPDATESCREEN)
	@R0
	M=-1

(UPDATESCREEN)
	// if (scrpos >= 0x6000) goto START
	@scrpos
	D=M;

	@KBD
	D=D-A;
	
	@START
	D; JGE
	
	@R0
	D=M
	
	// RAM[scrpos] = R0
	@scrpos
	A=M;
	M=D;
	
	// scrpos++
	@scrpos
	M=M+1
	
	@UPDATESCREEN
	0; JMP
	