package simProj;

public class DataMemory {
	
	public DataMemory() {
		baseAddress = 0;
		for(int i = 0; i< 6000 ; i++) {
			Data_Instruction_Segment[i] = 0;
		}
		
	}
	public static void cleanDataMemory() {
		baseAddress = 0;
		for(int i = 0; i< 6000 ; i++) {
			Data_Instruction_Segment[i] = 0;
		}
	}
	
	
	/*Data base address*/
	public static int baseAddress = 0;
	/*0~3999 are Data segment, 4000~4999 are Instruction segment*/
	public static int[] Data_Instruction_Segment = new int[6000];

}
