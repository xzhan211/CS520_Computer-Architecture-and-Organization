package simProj;

public class RegisterFile {
	public RegisterFile(int i) {
		address = i;
		value = -1;
		valueReady = false;  // P110, use valueReady bit as a bit vector,Status[].
		allocated = false;
		renamed = false;
		lastDestPhysicalRegisterAddr = 40;
		for(int k = 0; k<20; k++) {
			w[k] = false;   // false means no slots need it. vice versa. In IQ each slots related to one w[] bit.
		}
		zeroFlag = false;
		instructionNum = 0;
		zeroFlagReady = false;
	}
	
	public void cleanRegister() {
		value = -1;
		valueReady = false;
		allocated = false;
		renamed = false;
		lastDestPhysicalRegisterAddr = 40;
		for(int k = 0; k<20; k++) {
			w[k] = false;  // false means no slots need it. vice versa
		}
		zeroFlag = false;
		instructionNum = 0;
		zeroFlagReady = false;
	}
	
	public boolean valueReady;   // status bit 
	public int value;
	public int address; //0~15
	
	public boolean allocated;
	public boolean renamed;
	public boolean[] w = new boolean[20];  
	public boolean zeroFlag; // true means "equal 0", false means "is not 0"
	public int instructionNum;
	public boolean zeroFlagReady;
	
	public int lastDestPhysicalRegisterAddr;
}

