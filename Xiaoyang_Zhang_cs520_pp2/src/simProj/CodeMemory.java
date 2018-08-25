package simProj;

public class CodeMemory {
	
	public CodeMemory() {
		file_line_number = 0;
		CODE_ADDRESS_BASE = 4000;
		for(int i =0; i<2000; i++) {
			Address[i] = 4000 + i*4;
		}
		CurrentLine_string = null;
	}
	
	
	
	/*Code line number*/
	public static int file_line_number;  
	/*simulate the code running address*/
	public static int[] Address = new int[2000];
	/*current operating instruction, assembly instruction, string type*/
	public static String CurrentLine_string;
	public static int CODE_ADDRESS_BASE;
	
	public static void loadInstruction(int numofinstruction){
		
		if(numofinstruction <= ReadTxt.arrlength-1) {
			CurrentLine_string = ReadTxt.instArr[numofinstruction];
			file_line_number = numofinstruction;
			
			
		}
	}
}

