package simProj;

public class DataMemory {
	
	public DataMemory() {
		baseAddress = 0;
		for(int i = 0; i< 6000 ; i++) {
			Data_Instruction_Segment[i] = -1;
			Data_Segment_Vaild[i] = true;
		}
	}
	public static void cleanDataMemory() {
		baseAddress = 0;
		for(int i = 0; i< 6000 ; i++) {
			Data_Instruction_Segment[i] = -1;
			Data_Segment_Vaild[i] = true;
		}
	}
	
	
	/*Data base address*/
	public static int baseAddress = 0;
	/*0~3999 are Data segment, 4000~4999 are Instruction segment*/
	public static int[] Data_Instruction_Segment = new int[6000];
	/*0~3999 are Data segment vaild bit, others are useless*/
	public static boolean[] Data_Segment_Vaild = new boolean[6000];

}
