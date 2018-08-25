package simProj;

public class RegisterFile {
	
	public RegisterFile() {
		CODE_ADDRESS_BASE = 4000;
		//registerValue = 0;
		//registerStatus = false;
		//flag_zero = false;
		//flag_carry = false;
		//flag_negative = false;
		for(int i = 0; i<25; i++) {
			registerArr[i] = 0; // real one
		}
	}
	
	public static void cleanRegisterFile() {
		for(int i = 0; i<25; i++) {
			registerArr[i] = 0; // real one
		}
	}
	
	public static int CODE_ADDRESS_BASE;
	//public int registerValue;
	/*Valid = true, Invalid false*/
	//public boolean registerStatus;
	public static int[] registerArr = new int[25];
	//public static boolean flag_zero;
	//public static boolean flag_carry;
	//public static boolean flag_negative;
}
