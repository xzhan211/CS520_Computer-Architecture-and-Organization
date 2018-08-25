package simProj;

public class CorePipeline {
	/*initialization*/
	public CorePipeline() {
		/*------member variable in five stage------*/
		
		/*--Fetch--*/
		instructionCounter = 0;
		/*--Fetch End--*/

		/*--D/RF--*/
		DRF_literal = 0;
		DRF_destBuffer_addr = 20;
		DRF_sourBufferA_addr = 20;
		DRF_sourBufferB_addr = 20;
		DRF_current_instr = null;
		DRFinstrNo = 0;
		/*--EX--*/
		ALU_result_MOVC = 0;
		ALU_result_STORE = 0;
		ALU_result_LOAD = 0;
		ALU_result_MUL = 0;
		ALU_result_DIV = 0;
		ALU_result_ADD = 0;
		ALU_result_SUB = 0;
		ALU_result_BNZ = 0;
		ALU_result_BZ = 0;
		ALU_result_JUMP = 0;
		ALU_result_JAL = 0;
		ALU_result_JAL2 = 0;
		ALU_result_OR = 0;
		ALU_result_EXOR = 0;
		ALU_result_AND = 0;
		ALU_result_ZeroFlag = false;
		EX_current_instr = null;
		EX_literal = 0;
		EX_sourBufferA = 0;
		EX_sourBufferB = 0;
		//EX_destBuffer = 0;
		BZ_RUN = false;
		BNZ_RUN = false;
		JUMP_RUN = false;
		JAL_RUN = false;
		HALT_RUN = false;
		instCounterBuffer = 0;
		EX_current_instr_num = 0;
		EX_zeroFlagForward = false;
		/*--EX End--*/
		
		/*--MEM--*/
		MEM_current_instr = null;
		MEM_current_target_address = 0;
		MEM_current_sour_register_address = 0;
		MEM_current_sour_register_data = 0;
		MEM_current_dest_register_address = 0;
		MEM_current_load_value = 0;
		/*--MEM End--*/
		
		/*--WB--*/
		WB_current_instr = null;
		WB_current_memory_result = 0;
		WB_current_dest_addr = 0;
		WB_current_literal = 0;
		WB_current_alu_result = 0;
		WB_current_alu_result2 = 0;
		WB_current_flag_zero = false;
		WB_current_instrNo = 0;
		/*--WB End--*/	
		/*------member variable in five stage END------*/	
		
	}
	
	/*------member variable in five stage------*/
	/*--Fetch--*/
	public static String[] assemblyInstructionArr = new String[10];
	public static String[] ReturnAssemblyInstructionArr = new String[10];
	public static int instructionCounter;	
	/*--Fetch End--*/

	/*--D/RF--*/
	public static int DRF_literal;
	public static int DRF_destBuffer_addr;
	public static int DRF_sourBufferA_addr;
	public static int DRF_sourBufferB_addr;
	public static String DRF_current_instr;
	public static int DRFinstrNo;
	
	
	/*--D/RF End--*/
	
	/*--EX--*/		
	public static int ALU_result_MOVC;
	public static int ALU_result_STORE;
	public static int ALU_result_LOAD;
	public static int ALU_result_MUL;
	public static int ALU_result_DIV;
	public static int ALU_result_ADD;
	public static int ALU_result_SUB;
	public static int ALU_result_BNZ;
	public static int ALU_result_BZ;
	public static int ALU_result_JUMP;
	public static int ALU_result_JAL;
	public static int ALU_result_JAL2;
	public static int ALU_result_OR;
	public static int ALU_result_EXOR;
	public static int ALU_result_AND;
	public static boolean ALU_result_ZeroFlag;
	public static String EX_current_instr;
	public static int EX_literal;
	public static int EX_sourBufferA;
	public static int EX_sourBufferB;
	//public static int EX_destBuffer;
	public static boolean BZ_RUN;
	public static boolean BNZ_RUN;
	public static boolean JUMP_RUN;
	public static boolean JAL_RUN;
	public static boolean HALT_RUN;
	public static int instCounterBuffer;
	public static int EX_current_instr_num;
	public static boolean EX_zeroFlagForward;
	
	/*--EX End--*/
	
	/*--MEM--*/
	public static String MEM_current_instr;
	public static int MEM_current_target_address;
	public static int MEM_current_sour_register_address;
	public static int MEM_current_dest_register_address;
	public static int MEM_current_load_value;
	/*--MEM End--*/
	
	/*--WB--*/
	public static String WB_current_instr;
	public static int WB_current_memory_result;
	public static int WB_current_dest;
	public static int WB_current_literal;
	public static int WB_current_dest_addr;
	public static int MEM_current_sour_register_data;
	public static int WB_current_alu_result;
	public static int WB_current_alu_result2;
	public static boolean WB_current_flag_zero;
	public static int WB_current_instrNo;
	/*--WB End--*/	
	/*------member variable in five stage END------*/
	
	static registerFlag ZERO_FLAG = new registerFlag();
	
		
	
	public static void fetch() {
		instructionCounter++;
		/*public static String CurrentLine_string */
			CodeMemory.loadInstruction(instructionCounter-1);
			CodeMemory.Address[instructionCounter] = CodeMemory.CODE_ADDRESS_BASE + 4*(instructionCounter-1);
		/*current information in Fetch stage*/ 	
	}
	
	public static void decode() {
		//String newstr = DRF_current_instr.replaceAll(",","");
		String newstr = DRF_current_instr;
        newstr = newstr.replaceAll("R", ""); 
        newstr = newstr.replaceAll("#", "");       
		//assemblyInstructionArr = newstr.split(" ");
		assemblyInstructionArr = newstr.split(",");
		//ReturnAssemblyInstructionArr = DRF_current_instr.split(" ");
		ReturnAssemblyInstructionArr = DRF_current_instr.split(",");
		switch(assemblyInstructionArr[0]) {
		case "MOVC":		
			DRF_destBuffer_addr = Integer.parseInt(assemblyInstructionArr[1]); 
			DRF_literal = Integer.parseInt(assemblyInstructionArr[2]);
			break;
					
		case "STOE":
			DRF_sourBufferA_addr = Integer.parseInt(assemblyInstructionArr[1]);
			DRF_sourBufferB_addr = Integer.parseInt(assemblyInstructionArr[2]); 
			DRF_literal = Integer.parseInt(assemblyInstructionArr[3]);
			break;
					
		case "LOAD":
			DRF_destBuffer_addr = Integer.parseInt(assemblyInstructionArr[1]);
			DRF_sourBufferA_addr= Integer.parseInt(assemblyInstructionArr[2]); 
			DRF_literal = Integer.parseInt(assemblyInstructionArr[3]);
			break;
		
		case "AND":	
		case "EXO":	
		case "O":	
			DRF_destBuffer_addr = Integer.parseInt(assemblyInstructionArr[1]);
			DRF_sourBufferA_addr = Integer.parseInt(assemblyInstructionArr[2]); 
			DRF_sourBufferB_addr = Integer.parseInt(assemblyInstructionArr[3]);
			break;
		case "MUL":
		case "DIV":	
		case "ADD":
		case "SUB":
			DRF_destBuffer_addr = Integer.parseInt(assemblyInstructionArr[1]);
			DRF_sourBufferA_addr = Integer.parseInt(assemblyInstructionArr[2]); 
			DRF_sourBufferB_addr = Integer.parseInt(assemblyInstructionArr[3]);
			ZERO_FLAG.bitStatusBusy = true;
			ZERO_FLAG.lastInstrNo = DRFinstrNo;
			break;	
			
		case "BZ":	
		case "BNZ":
			DRF_literal = Integer.parseInt(assemblyInstructionArr[1]);
			break;
			
		case "JUMP":	
			DRF_sourBufferA_addr = Integer.parseInt(assemblyInstructionArr[1]); 
			DRF_literal = Integer.parseInt(assemblyInstructionArr[2]);
			break;
				
		case "HALT":	
			HALT_RUN = true;
			break;
			
		case "JAL":
			DRF_destBuffer_addr = Integer.parseInt(assemblyInstructionArr[1]);
			DRF_sourBufferA_addr= Integer.parseInt(assemblyInstructionArr[2]); 
			DRF_literal = Integer.parseInt(assemblyInstructionArr[3]);
			break;
			
		default:
			break;
		}			
	}
	
	public static void execute() {
		//ReturnAssemblyInstructionArr = EX_current_instr.split(" ");
		ReturnAssemblyInstructionArr = EX_current_instr.split(",");
		switch(ReturnAssemblyInstructionArr[0]) {
		case "MOVC":	
			ALU_result_MOVC = EX_literal + 0;
			break;
					
		case "STORE":
			ALU_result_STORE = EX_sourBufferB + EX_literal;
			break;
					
		case "LOAD":
			ALU_result_LOAD = EX_sourBufferA + EX_literal;
			break;

		case "MUL":
			ALU_result_MUL = EX_sourBufferA * EX_sourBufferB;
			if(ALU_result_MUL == 0) {
				ALU_result_ZeroFlag = true;
			}else
				ALU_result_ZeroFlag = false;	
			break;	
			
		case "DIV":
			ALU_result_DIV = EX_sourBufferA / EX_sourBufferB;
			if(ALU_result_DIV == 0) {
				ALU_result_ZeroFlag = true;
			}else
				ALU_result_ZeroFlag = false;	
			break;	
			
		case "ADD":
			ALU_result_ADD = EX_sourBufferA + EX_sourBufferB;
			if(ALU_result_ADD == 0) {
				ALU_result_ZeroFlag = true;
			}else
				ALU_result_ZeroFlag = false;
			break;
					
		case "SUB":
			ALU_result_SUB = EX_sourBufferA - EX_sourBufferB;
			if(ALU_result_SUB == 0) {
				ALU_result_ZeroFlag = true;	
			}else
				ALU_result_ZeroFlag = false;
			break;
			
		case "OR":	
			ALU_result_OR = EX_sourBufferA | EX_sourBufferB;
			/*
			if(ALU_result_OR == 0) {
				ALU_result_ZeroFlag = true;	
			}else
				ALU_result_ZeroFlag = false;
			*/ //no need to update the PSW in proj2
			break;
			
		case "EXOR":	 
			ALU_result_EXOR = EX_sourBufferA ^ EX_sourBufferB;
			/*
			if(ALU_result_EXOR == 0) {
				ALU_result_ZeroFlag = true;	
			}else
				ALU_result_ZeroFlag = false;
			*/ //no need to update the PSW in proj2
			break;
		
		case "AND":	
			ALU_result_AND = EX_sourBufferA & EX_sourBufferB;
			/*
			if(ALU_result_AND == 0) {
				ALU_result_ZeroFlag = true;	
			}else
				ALU_result_ZeroFlag = false;
			*/ //no need to update the PSW in proj2
			break;
			
		case "JUMP":	
			ALU_result_JUMP = EX_sourBufferA + EX_literal;
			instCounterBuffer = (ALU_result_JUMP - 3996)/4; // code memory from 4000, the 1st instruction in 4000
 			JUMP_RUN = true;
			break;
				
		case "HALT":	
			break;
			
		case "BZ":
			if(ZERO_FLAG.bitValue == true && ZERO_FLAG.bitStatusBusy == false) {
				ALU_result_BZ = EX_current_instr_num + EX_literal/4;
				instCounterBuffer = ALU_result_BZ;
				BZ_RUN = true;
			}else if(EX_zeroFlagForward == true && ZERO_FLAG.bitStatusBusy == true){	
				ALU_result_BZ = EX_current_instr_num + EX_literal/4;
				instCounterBuffer = ALU_result_BZ;
				BZ_RUN = true;
				EX_zeroFlagForward = false;
			}else {
				BZ_RUN = false;
			}
			break;
			
		case "BNZ":
			if(ZERO_FLAG.bitValue == false && ZERO_FLAG.bitStatusBusy == false) {
				ALU_result_BNZ = EX_current_instr_num + EX_literal/4;
				instCounterBuffer = ALU_result_BNZ;
				BNZ_RUN = true;
			}else if(EX_zeroFlagForward == false && ZERO_FLAG.bitStatusBusy == true){  //BNZ
				ALU_result_BNZ = EX_current_instr_num + EX_literal/4;
				instCounterBuffer = ALU_result_BNZ;
				BNZ_RUN = true;	
				EX_zeroFlagForward = false;
			}else {
				BNZ_RUN = false;
			}
			break;
			
		
		case"JAL":
			ALU_result_JAL = EX_sourBufferA + EX_literal;
			ALU_result_JAL2 = (EX_current_instr_num - 1 + 1) * 4 + CodeMemory.CODE_ADDRESS_BASE; //code memory from 4000, the 1st instruction in 4000
			instCounterBuffer = (ALU_result_JAL - 3996)/4; // code memory from 4000, the 1st instruction in 4000
 			JAL_RUN = true;
			
			
			
			break;
		
		
		default:
			break;
		}
	}
	
	public static void memory() {
		//ReturnAssemblyInstructionArr = MEM_current_instr.split(" ");
		ReturnAssemblyInstructionArr = MEM_current_instr.split(",");
		switch(ReturnAssemblyInstructionArr[0]) {			
		case "STORE":
			DataMemory.Data_Instruction_Segment[MEM_current_target_address] = MEM_current_sour_register_data;
			break;			
		case "LOAD":
			MEM_current_load_value = DataMemory.Data_Instruction_Segment[MEM_current_target_address];
			break;
				
		case "MOVC":	
		case "MUL":
		case "DIV":	
		case "ADD":	
		case "SUB":
		case "BNZ":
		case "BZ":
		case "JUMP":
		case "HALT":
		case "EXOR":
		case "AND":
		case "OR":
		case "JAL":
		default:
			break;
		}	
		
	}
	
	public static void writeback() {
			//ReturnAssemblyInstructionArr = WB_current_instr.split(" ");
			ReturnAssemblyInstructionArr = WB_current_instr.split(",");
			switch(ReturnAssemblyInstructionArr[0]) {
			case "MOVC":	
				TestMain.R[WB_current_dest_addr].value = WB_current_alu_result;
				break;
									
			case "LOAD":
				TestMain.R[WB_current_dest_addr].value = WB_current_memory_result;
				break;
	
			case "MUL":
			case "DIV":	
			case "ADD":
			case "SUB":
			case "OR":
			case "EXOR":	
			case "AND":
				TestMain.R[WB_current_dest_addr].value = WB_current_alu_result;
				/*
				if((TestMain.DRF.math_op_flag || TestMain.EX.math_op_flag 
					|| TestMain.EX_MUL.math_op_flag || TestMain.EX_MUL2.math_op_flag || TestMain.MEM.math_op_flag
					|| TestMain.EX_DU1.math_op_flag || TestMain.EX_DU2.math_op_flag || TestMain.EX_DU3.math_op_flag || TestMain.EX_DU4.math_op_flag) == false) {
				*/
				
				if(TestMain.WB.c_instr_number == ZERO_FLAG.lastInstrNo) {
					ZERO_FLAG.bitValue = WB_current_flag_zero;
					ZERO_FLAG.bitStatusBusy = false;
				} // math_op_flag use for old version. change to ready bit version.
				break;
			case "JAL":
				TestMain.R[WB_current_dest_addr].value = WB_current_alu_result2;
				break;
				
				
			case "STORE":	
			case "JUMP":
			case "BZ":
			case "BNZ":
			case "HALT":
			default:
				break;
			}
		}			
}
