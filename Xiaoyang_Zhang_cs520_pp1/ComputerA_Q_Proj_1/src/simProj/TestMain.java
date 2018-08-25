package simProj;
import java.io.*; // File class
import java.util.Formatter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TestMain {	
	
	
	public static String strBuffer;
	public static String strBuffer2;
	public static String[] strArrBuffer;
	public static boolean dataReadyRegister;
	public static boolean blockFetch;
	public static boolean blockDecodeMUL;
	public static boolean blockDecodeInteger;
	public static boolean DRF_MUL_Flag;
	public static boolean EX_MUL_Flag;
	public static boolean EX_Integer_Flag;
	public static boolean EX_Flush;
	public static boolean EX_BNZ_RUN;
	public static boolean EX_BZ_RUN;
	public static boolean EX_JUMP_RUN;
	public static boolean EX_HALT_RUN;
	public static int instructionCounterBuffer;
	
	
	
	static StageStatusValue Fetch = new StageStatusValue();
	static StageStatusValue DRF = new StageStatusValue();
	static StageStatusValue EX = new StageStatusValue(); // integer FU
	static StageStatusValue EX_MUL = new StageStatusValue(); // 1st MUL
	static StageStatusValue EX_MUL2 = new StageStatusValue();// 2ed MUL
	static StageStatusValue MEM = new StageStatusValue();
	static StageStatusValue WB = new StageStatusValue();
	static Formatter formatterUI = new Formatter(System.out);
	public static boolean TerminateRun;
	public static boolean Terminate;
	
	public static boolean nonum = true;
	public static int inputCycleNum = 0;
	
	
	
	
	public static void main(String[] args) throws IOException{
		
		/*---initialization---*/
		dataReadyRegister = true;
		blockFetch = false;
		blockDecodeMUL = false;
		blockDecodeInteger = false;
		DRF_MUL_Flag = false; // MUL = true; Integer = False
		EX_MUL_Flag = false; // MUL = true
		EX_Integer_Flag = false; // Integer = true
		EX_Flush = false;
		strArrBuffer = new String[10];
		EX_BNZ_RUN = false;
		EX_BZ_RUN = false;
		EX_JUMP_RUN = false;
		EX_HALT_RUN = false;
		inputCycleNum = 0;
		nonum = true;
		
		
		/*Pipeline status*/
		new CorePipeline();
		/*Memory + register*/
		new CodeMemory();
		new DataMemory();
		/*Register*/
		new RegisterFile();
		/*ReadTxt*/
		new ReadTxt();
		/*cycle*/
		new Stats();
		
		Fetch.cleanStageValue();
		DRF.cleanStageValue();
		EX.cleanStageValue();
		EX_MUL.cleanStageValue();
		EX_MUL2.cleanStageValue();
		MEM.cleanStageValue();
		WB.cleanStageValue();
		
		/*---END initialization---*/
		
		
		//Formatter formatterUI = new Formatter(System.out);
		/*read in instruct file*/
		File file = new File("/Users/xiaoyangzhang/eclipse-workspace/ComputerA_Q_Proj_1/src/simProj/input.txt");
		ReadTxt.txt2String(file);
		String[] Buffer = new String[10];
		System.out.println("\n");
		System.out.println("***************************************");
		System.out.println("**  Xiaoyang Zhang                   **");
		System.out.println("**  xzhan211@binghamton.edu          **");
		System.out.println("**  Pipeline Simulator   Version 1.0 **");
		System.out.println("***************************************");
		System.out.println("\n");
		
		while(true) {
			System.out.println("\n");
			System.out.println("Simulator Commands:");
			formatterUI.format("%-15s %-2s %-50s\n", "<initialize>", ":", "Initializes the simulator state.");
			formatterUI.format("%-15s %-2s %-70s\n", "<simulate n>", ":", "Enter [simulate]+[n], n means quantity of cycles.");
			formatterUI.format("%-15s %-2s %-50s\n", "<display>", ":", "Display data/address about register and memory.");
			formatterUI.format("%-15s %-2s %-50s\n", "<run>", ":", "Run all instructions.");
			formatterUI.format("%-15s %-2s %-50s\n", "<exit>", ":", "Exit this program.");
			
			while(true) {
				System.out.print("Enter command \n>> ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				Buffer = br.readLine().split(" ");
				if(Buffer[0].equals("initialize") )
					break;
				else if(Buffer[0].equals("display") )
					break;
				else if(Buffer[0].equals("simulate") ) {
					if(Buffer.length == 2) {
						for (int i = 0; i < Buffer[1].length(); i++){
							   if (!Character.isDigit(Buffer[1].charAt(i)))
								   nonum = false;
						}
						if(nonum == true)
							break;
						else
							Buffer[1] = null;
					}
				}
				else if(Buffer[0].equals("run") )
					break;
				else if(Buffer[0].equals("exit") )
					break;
				
				Buffer[0] = null;
				System.out.println("Input Error");
				nonum = true;
			}
	
			switch (Buffer[0]){
			case "initialize":
				/*---initialization---*/
				dataReadyRegister = true;
				blockFetch = false;
				blockDecodeMUL = false;
				blockDecodeInteger = false;
				DRF_MUL_Flag = false; // MUL = true; Integer = False
				EX_MUL_Flag = false; // MUL = true
				EX_Integer_Flag = false; // Integer = true
				EX_Flush = false;
				strArrBuffer = new String[10];
				EX_BNZ_RUN = false;
				EX_BZ_RUN = false;
				EX_JUMP_RUN = false;
				EX_HALT_RUN = false;
				inputCycleNum = 0;
				//DataMemory.cleanDataMemory();
				//RegisterFile.cleanRegisterFile();
				nonum = true;
				/*Pipeline status*/
				new CorePipeline();
				/*Memory + register*/
				new CodeMemory();
				new DataMemory();
				/*Register*/
				new RegisterFile();
				/*cycle*/
				new Stats();
				/*---END initialization---*/
				//new CorePipeline();
				Fetch.cleanStageValue();
				DRF.cleanStageValue();
				EX.cleanStageValue();
				EX_MUL.cleanStageValue();
				EX_MUL2.cleanStageValue();
				MEM.cleanStageValue();
				WB.cleanStageValue();	
				/*END establish*/	
				
				
				System.out.println("Total number of instructions >> " + ReadTxt.arrlength);	
				break;
				
			case "simulate":
				inputCycleNum = Integer.parseInt(Buffer[1]);
				break;
				
			case "display":
				System.out.println("\n");
				FormatDisplay.registerDisplay();
				System.out.println("\n");
				FormatDisplay.memoryDisplay();
				inputCycleNum = 0;
				break;
			case "run":
				inputCycleNum = 100000;
				break;
			case "exit":
				return;
			default:
				break;
			}
		
		for(int n = 0; n < inputCycleNum; n++) {		
/*------------------------------WB start---------------------------------------*/				
		if(WB.c_instruction != null) {
			if(WB.c_stalled == true) {
				return;
			}
			else {
				/*send the current value to WB function*/
				CorePipeline.WB_current_instr = WB.c_instruction;
				CorePipeline.WB_current_dest_addr = WB.c_dest_addr;
				CorePipeline.WB_current_memory_result = WB.c_MEM_result;
				CorePipeline.WB_current_alu_result = WB.c_ALU_result;
				CorePipeline.WB_current_literal = WB.c_literal;
				CorePipeline.WB_current_flag_zero = WB.c_FLAG_zero;
				CorePipeline.writeback();
				strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];	
				switch(strBuffer) {
				case "MOVC":		
					WB.c_dest_data = RegisterFile.registerArr[WB.c_dest_addr];
					break;
							
				case "LOAD":
					WB.c_dest_data = RegisterFile.registerArr[WB.c_dest_addr];
					break;
				
				case "JUMP":
				case "BZ":	
				case "BNZ":
					break;
							
				case "MUL":
				case "EXOR":
				case "AND":
				case "OR":
				case "ADD":			
				case "SUB":
					WB.c_dest_data = RegisterFile.registerArr[WB.c_dest_addr];
					break;	
				case "HALT":
				case "STORE":
				default:
					break;
				}	
			}
		}

			
/*------------------------------WB end-----------------------------------------*/				
	
		
/*------------------------------MEM start--------------------------------------*/			
		if(MEM.c_instruction != null) {
			while(MEM.c_free){
				if(MEM.c_stalled == true) {
					return;
				}
				else {	
					/*send the current value to MEM function*/
					CorePipeline.MEM_current_instr = MEM.c_instruction; 
					CorePipeline.MEM_current_target_address = MEM.c_target_memory_addr;
					CorePipeline.MEM_current_sour_register_address = MEM.c_src1_addr;// < MEM.c_src2_addr) ? MEM.c_src1_addr : MEM.c_src2_addr);
					/*end*/
						
					CorePipeline.memory();
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
						
					switch(strBuffer) {
					case "STORE":
						MEM.c_target_memory_data = DataMemory.Data_Instruction_Segment[MEM.c_target_memory_addr];
						break;
					case "LOAD":
						MEM.c_MEM_result = CorePipeline.MEM_current_load_value;
						break;	
					}
					MEM.c_free = false; // when finish this stage's operation, set c_free "false"	
				}
			}
		}			 
/*------------------------------MEM end----------------------------------------*/	
		
/*------------------------------EX MUL2 start---------------------------------------*/	
		if(EX_MUL2.c_instruction != null) {	
			while(EX_MUL2.c_free){
				if(EX_MUL2.c_stalled == true) {
					return;
				}
				else {
					EX_MUL2.c_free = false; // when finish this stage's operation, set c_free "false"		
				}
			}	
		}	
	
/*------------------------------EX MUL2 end-----------------------------------------*/	


/*------------------------------EX MUL start---------------------------------------*/		
		if(EX_MUL.c_instruction != null) {	
			while(EX_MUL.c_free){
				if(EX_MUL.c_stalled == true) {
					return;
				}
				else {					
					CorePipeline.EX_current_instr = EX_MUL.c_instruction;
					CorePipeline.EX_literal = EX_MUL.c_literal;  
					CorePipeline.EX_destBuffer = EX_MUL.c_dest_addr;
					CorePipeline.EX_sourBufferA = EX_MUL.c_src1_addr;
					CorePipeline.EX_sourBufferB = EX_MUL.c_src2_addr;
					CorePipeline.execute();
					EX_MUL.c_ALU_result = CorePipeline.ALU_result_MUL;
					EX_MUL.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
					EX_MUL.c_free = false;
				}
			}	
		}	
		
/*------------------------------EX MUL end-----------------------------------------*/			
		
	
		
/*------------------------------EX start---------------------------------------*/					
		if(EX.c_instruction != null) {	
			while(EX.c_free){
				if(EX.c_stalled == true) {
					return;
				}
				else {					
					/*send the current value to EX function to calculate*/
					CorePipeline.EX_current_instr = EX.c_instruction;
					CorePipeline.EX_current_instr_num = EX.c_instr_number;
					CorePipeline.EX_literal = EX.c_literal;  
					CorePipeline.EX_destBuffer = EX.c_dest_addr;
					CorePipeline.EX_sourBufferA = EX.c_src1_addr;
					CorePipeline.EX_sourBufferB = EX.c_src2_addr;
					/*end*/
						
					CorePipeline.execute();
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];

					switch(strBuffer) {
					case "MOVC":
						EX.c_ALU_result= CorePipeline.ALU_result_MOVC;
						break;
					case "STORE":
						EX.c_target_memory_addr = CorePipeline.ALU_result_STORE;
						EX.c_target_memory_data = DataMemory.Data_Instruction_Segment[EX.c_target_memory_addr];	
						EX.c_ALU_result = CorePipeline.ALU_result_STORE;
						break;
						
					case "LOAD":
						EX.c_target_memory_addr = CorePipeline.ALU_result_LOAD;
						EX.c_target_memory_data = DataMemory.Data_Instruction_Segment[EX.c_target_memory_addr];
						EX.c_ALU_result = CorePipeline.ALU_result_LOAD;
						break;

					case "ADD":
						EX.c_ALU_result = CorePipeline.ALU_result_ADD;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						break;
							
					case "SUB":
						EX.c_ALU_result = CorePipeline.ALU_result_SUB;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						break;
					case "BZ":
						if(CorePipeline.BZ_RUN == true) {
							EX.c_ALU_result = CorePipeline.ALU_result_BZ;
							Fetch.cleanStageValue();
							DRF.cleanStageValue();
							Fetch.c_free = false;
						}
						break;
						
					case "BNZ":
						if(CorePipeline.BNZ_RUN == true) {
							EX.c_ALU_result = CorePipeline.ALU_result_BNZ;
							Fetch.cleanStageValue();
							DRF.cleanStageValue();
							Fetch.c_free = false;
						}
						break;
					
					case "JUMP":
						EX.c_ALU_result = CorePipeline.ALU_result_JUMP;
						Fetch.cleanStageValue();
						DRF.cleanStageValue();
						Fetch.c_free = false;
						break;
					
					case "EXOR":	
						EX.c_ALU_result = CorePipeline.ALU_result_EXOR;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						break;
						
					case "OR":
						EX.c_ALU_result = CorePipeline.ALU_result_OR;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						break;
						
					case "AND":
						EX.c_ALU_result = CorePipeline.ALU_result_AND;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						break;
							
					default:
						break;			
					}					
					EX.c_free = false; // when finish this stage's operation, set c_free "false"		
					}
				}	
		}
			
/*------------------------------EX end-----------------------------------------*/				

			
			
/*------------------------------DRF start---------------------------------------*/		
		if(DRF.c_instruction != null) {	
			while(DRF.c_free){
				if (DRF.c_stalled == true){
					return;
				}
				else{
					DRF.c_free = false; // when finish this stage's operation, set c_free "false"					
					/*send the current value to DRF function*/
					CorePipeline.DRF_current_instr = DRF.c_instruction; 
					/*end*/
					CorePipeline.decode();
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
					DRF.op = strBuffer;
					switch(strBuffer) {
					case "MOVC":		
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;	
						break;
									
					case "LOAD":	
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;
						break;
			
					case "STORE":
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_src2_addr = CorePipeline.DRF_sourBufferB_addr;
						break;
											
					case "MUL":
					case "ADD":
					case "SUB":
					case "OR":
					case "EXOR":
					case "AND":
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_src2_addr = CorePipeline.DRF_sourBufferB_addr;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;
						break;
					
					case"JUMP":
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_literal = CorePipeline.DRF_literal;
						break;
						
					case"BZ":						
					case "BNZ":
						DRF.c_literal = CorePipeline.DRF_literal;
						break;	
					case"HALT":
						Fetch.cleanStageValue();
						break;
					default:
						break;
					}		
					
					switch(strBuffer) {
					case "MUL":
						DRF_MUL_Flag = true;
						break;
					default:
						DRF_MUL_Flag = false;
						break;
					
					}
					
		/*---------------------------------------------------------------bubble start-------------------------------------------------------------*/
					if(MEM.c_dest_addr != 20 && (DRF.c_src1_addr == MEM.c_dest_addr || DRF.c_src2_addr == MEM.c_dest_addr)){ 
						dataReadyRegister = false;
						blockFetch = true;
						break;
					}		
					if(EX.c_dest_addr != 20 && (DRF.c_src1_addr == EX.c_dest_addr || DRF.c_src2_addr == EX.c_dest_addr)){ 
						dataReadyRegister = false;
						blockFetch = true;
						break;	
					}		
					if(EX_MUL.c_dest_addr != 20 && (DRF.c_src1_addr == EX_MUL.c_dest_addr || DRF.c_src2_addr == EX_MUL.c_dest_addr)){ 
						dataReadyRegister = false;
						blockFetch = true;
						break;	
					}		
					if(EX_MUL2.c_dest_addr != 20 && (DRF.c_src1_addr == EX_MUL2.c_dest_addr || DRF.c_src2_addr == EX_MUL2.c_dest_addr)){ 
						dataReadyRegister = false;
						blockFetch = true;
						break;	
					}
					
					strArrBuffer = DRF.c_instruction.split(" ");
					if(strArrBuffer[0].equals("BNZ") && CorePipeline.ZERO_FLAG.bitStatusBusy == true) {
						dataReadyRegister = false;
						blockFetch = true;
						break;
					}
					
						
					if(strArrBuffer[0].equals("BZ") && CorePipeline.ZERO_FLAG.bitStatusBusy == true) {
						dataReadyRegister = false;
						blockFetch = true;
						break;
					}
		/*---------------------------------------------------------------bubble end-------------------------------------------------------------*/
					
					dataReadyRegister = true;
					blockFetch = false;
					
					switch(strBuffer) {
					case "MOVC":		
						DRF.c_dest_data = RegisterFile.registerArr[DRF.c_dest_addr];
						break;
									
					case "LOAD":	
						DRF.c_src1_data = RegisterFile.registerArr[DRF.c_src1_addr];
						DRF.c_dest_data = RegisterFile.registerArr[DRF.c_dest_addr];
						break;
			
					case "STORE":
						DRF.c_src1_data = RegisterFile.registerArr[DRF.c_src1_addr];
						DRF.c_src2_data = RegisterFile.registerArr[DRF.c_src2_addr];
						break;
											
					case "MUL":
					case "ADD":
					case "SUB":
					case "OR":
					case "EXOR":
					case "AND":
						DRF.c_src1_data = RegisterFile.registerArr[DRF.c_src1_addr];
						DRF.c_src2_data = RegisterFile.registerArr[DRF.c_src2_addr];
						DRF.c_dest_data = RegisterFile.registerArr[DRF.c_dest_addr];
						break;
						
					case"JUMP":
						DRF.c_src1_data = RegisterFile.registerArr[DRF.c_src1_addr];
						break;
						
					case"BZ":						
					case "BNZ":
					case"HALT":
					default:
						break;
					}
					if(DRF.op.equals("ADD") || DRF.op.equals("SUB") || DRF.op.equals("MUL") || DRF.op.equals("OR") || DRF.op.equals("EXOR") || DRF.op.equals("AND")) {
						DRF.math_op_flag = true;
					}else
						DRF.math_op_flag = false;
					
					
				}
			}
		}			
/*------------------------------DRF end----------------------------------------*/			
		
		
/*------------------------------Fetch start------------------------------------*/	 
			while(Fetch.c_free){  // when first time enter F stage, F_current_free is true.
				if (Fetch.c_stalled == true){
				}
				else{
					if(CorePipeline.instructionCounter < ReadTxt.arrlength)	
					{	
						CorePipeline.fetch();
						Fetch.c_instruction = CodeMemory.CurrentLine_string;
						Fetch.c_instr_number = CorePipeline.instructionCounter;
						Fetch.c_PC = CodeMemory.Address[Fetch.c_instr_number];
						Fetch.c_src1_data = 0;
						Fetch.c_src2_data = 0;
						Fetch.c_dest_data = 0;
						Fetch.c_target_memory_data = 0;
						Fetch.c_literal = 0;
						Fetch.c_src1_addr = 20;
						Fetch.c_src2_addr = 20;
						Fetch.c_dest_addr = 20;
						Fetch.c_target_memory_addr = 5000;
						Fetch.c_stalled = false;// open or close while loop
						Fetch.c_free = false; // when fetch a new instruction, F is not free	
						Fetch.c_ALU_result = 0;
					} else {
						Fetch.c_stalled = true; // finally, when instruction counter larger than total number of input instruction, stall Fetch part.
						Fetch.c_free = false; //
					}				
				}
				
			}
/*------------------------------Fetch end---------------------------------------*/			
		
			
/*------------------------------Display----------------------------------------*/		
			FormatDisplay.runDisplay();			
/*-----------------------------Display End-------------------------------------*/			
			
/*-------------------------Pass Start------------------------------------------*/
/*-------------------------MEM to WB-------------------------------------------*/

				MEM.passStageValue(WB);
				MEM.cleanStageValue();
			
			

/*-------------------------EX to MEM------------------------------------------*/
			
			if(MEM.c_free == true) { 
				if(EX_MUL2.c_free == false) {
					EX_MUL2.passStageValue(MEM);
					EX_MUL2.cleanStageValue();
				}else if(EX_MUL2.c_free == true && EX.c_free == false) {
					EX.passStageValue(MEM);
					EX.cleanStageValue();
				}	
			}		
			
/*----------------------EX MUL1 to EX MUL2-------------------------------------*/		
			if(EX_MUL2.c_free == true && EX_MUL.c_free == false) {
				EX_MUL.passStageValue(EX_MUL2);
				EX_MUL.cleanStageValue();
			}
		
/*----------------------DRF to EX----------------------------------------------*/			
			if(DRF.c_free == false && dataReadyRegister == false) {
				DRF.c_free = true;	
			}
			if(DRF.c_free == false && dataReadyRegister == true) {
				if(DRF_MUL_Flag == true && EX_MUL.c_free == true) { 
					DRF.passStageValue(EX_MUL);
					DRF.cleanStageValue();
				}	
				else if(DRF_MUL_Flag == false && EX.c_free == true) {
					DRF.passStageValue(EX);
					DRF.cleanStageValue();
				}
			}				
/*---------------------Fetch to DRF--------------------------------------------*/			
			if(CorePipeline.HALT_RUN == true) {
				Fetch.cleanStageValue();
				Fetch.c_stalled = true;
				Fetch.c_free = false;
				
			}
			if(CorePipeline.HALT_RUN == false) {
				if(Fetch.c_free == false && DRF.c_free == true && blockFetch == false) {
					if(CorePipeline.BZ_RUN == false && CorePipeline.BNZ_RUN == false && CorePipeline.JUMP_RUN == false) {
						Fetch.passStageValue(DRF);
						Fetch.cleanStageValue();
					}else if(CorePipeline.BZ_RUN == true) {
						Fetch.cleanStageValue();
						CorePipeline.instructionCounter = CorePipeline.instCounterBuffer - 1;
						CorePipeline.BZ_RUN = false;
					}else if(CorePipeline.BNZ_RUN == true) {
						Fetch.cleanStageValue();
						CorePipeline.instructionCounter = CorePipeline.instCounterBuffer - 1;
						CorePipeline.BNZ_RUN = false;
					}else if(CorePipeline.JUMP_RUN == true) {
						Fetch.cleanStageValue();
						CorePipeline.instructionCounter = CorePipeline.instCounterBuffer - 1;
						CorePipeline.JUMP_RUN = false;
					}
				}
			}
			
			
			
/*----------------------Pass END----------------------------------------------*/						
			Stats.increaseCycle();			
			if(Fetch.c_instruction == null && DRF.c_instruction == null && EX.c_instruction == null && MEM.c_instruction == null && WB.c_instruction == null) {
				//FormatDisplay.registerDisplay();
				//System.out.println("\n");
				//FormatDisplay.memoryDisplay();
				//System.out.println("\n");
				System.out.println("---END---");
				break;
			}
		}// for	
	}// while	
}// main
}// class

/*----------------------------------------------------------------------------------------*/
class FormatDisplay{
	static Formatter formatter = new Formatter(System.out);
	public static void runDisplay() {
		System.out.println("Current cycle >>>>>> " + Stats.cycle);
		formatter.format("*********************************************************************\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "FETCH ", ">>>>>", "No.", TestMain.Fetch.c_instr_number, ":  Address ", TestMain.Fetch.c_PC, "   ", TestMain.Fetch.c_instruction);
        formatter.format("---------------------------------------------------------------------\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "DRF ", ">>>>>", "No.", TestMain.DRF.c_instr_number, ":  Address ", TestMain.DRF.c_PC, "   ", TestMain.DRF.c_instruction);
        formatter.format("---------------------------------------------------------------------\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "EX_Integer ", ">>>>>", "No.", TestMain.EX.c_instr_number, ":  Address ", TestMain.EX.c_PC, "   ", TestMain.EX.c_instruction);
        formatter.format("---------------------------------------------------------------------\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "EX_MUL1 ", ">>>>>", "No.", TestMain.EX_MUL.c_instr_number, ":  Address ", TestMain.EX_MUL.c_PC, "   ", TestMain.EX_MUL.c_instruction);
        formatter.format("---------------------------------------------------------------------\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "EX_MUL2 ", ">>>>>", "No.", TestMain.EX_MUL2.c_instr_number, ":  Address ", TestMain.EX_MUL2.c_PC, "   ", TestMain.EX_MUL2.c_instruction);
        formatter.format("---------------------------------------------------------------------\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "MEMORY ", ">>>>>", "No.", TestMain.MEM.c_instr_number, ":  Address ", TestMain.MEM.c_PC, "   ", TestMain.MEM.c_instruction);
        formatter.format("---------------------------------------------------------------------\n");
        formatter.format("%-11s %-5s %3s %-3d %-12s %4d %-4s %-25s\n", "WB ", ">>>>>", "No.", TestMain.WB.c_instr_number, ":  Address ", TestMain.WB.c_PC, "   ", TestMain.WB.c_instruction);
        formatter.format("*********************************************************************\n");
        formatter.format("\n");
	}
	public static void registerDisplay() {
		formatter.format("Register Information:\n");
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R0", "R1", "R2", "R3");
		formatter.format("%-10d %-10d %-10d %-10d\n", RegisterFile.registerArr[0], RegisterFile.registerArr[1], RegisterFile.registerArr[2], RegisterFile.registerArr[3]);
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R4", "R5", "R6", "R7");
		formatter.format("%-10d %-10d %-10d %-10d\n", RegisterFile.registerArr[4], RegisterFile.registerArr[5], RegisterFile.registerArr[6], RegisterFile.registerArr[7]);
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R8", "R9", "R10", "R11");
		formatter.format("%-10d %-10d %-10d %-10d\n", RegisterFile.registerArr[8], RegisterFile.registerArr[9], RegisterFile.registerArr[10], RegisterFile.registerArr[11]);
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R12", "R13", "R14", "R15");
		formatter.format("%-10d %-10d %-10d %-10d\n", RegisterFile.registerArr[12], RegisterFile.registerArr[13], RegisterFile.registerArr[14], RegisterFile.registerArr[15]);
		formatter.format("------------------------------------------\n");
	}
	
	public static void memoryDisplay() {
		formatter.format("Memory Information:\n");
		for(int i = 0; i < 100; i=i+5) {
			formatter.format("========================================================================\n");
			formatter.format("%-10s %-6s %-10d %-10d %-10d %-10d %-10d\n", "Value", "  >>>", DataMemory.Data_Instruction_Segment[i], DataMemory.Data_Instruction_Segment[i+1], DataMemory.Data_Instruction_Segment[i+2], DataMemory.Data_Instruction_Segment[i+3], DataMemory.Data_Instruction_Segment[i+4]);
			formatter.format("%-10s %-6s %-10d %-10d %-10d %-10d %-10d\n", "Address", "  >>>", i, i+1, i+2, i+3, i+4);
		}
		
	}
	
	
}
