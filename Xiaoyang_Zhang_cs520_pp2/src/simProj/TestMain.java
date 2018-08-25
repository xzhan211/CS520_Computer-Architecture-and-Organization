package simProj;
import java.io.*; // File class
import java.util.Formatter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File; 

public class TestMain {	
	
	
	public static String strBuffer;
	public static String strBuffer2;
	public static String[] strArrBuffer;
	public static boolean dataReadyRegister;
	public static boolean blockFetch;
	public static boolean DRF_MUL_Flag; 
	public static boolean DRF_DIV_Flag; 
	public static String pathBuffer;
	public static boolean pathReady;
	public static String fileNameBuffer;
	public static boolean changeTestFile;
	

	
	static StageStatusValue Fetch = new StageStatusValue();
	static StageStatusValue DRF = new StageStatusValue();
	static StageStatusValue EX = new StageStatusValue(); // integer FU
	static StageStatusValue EX_MUL = new StageStatusValue(); // 1st MUL
	static StageStatusValue EX_MUL2 = new StageStatusValue();// 2ed MUL
	static StageStatusValue EX_DU1 = new StageStatusValue();
	static StageStatusValue EX_DU2 = new StageStatusValue();
	static StageStatusValue EX_DU3 = new StageStatusValue();
	static StageStatusValue EX_DU4 = new StageStatusValue();
	static StageStatusValue MEM = new StageStatusValue();
	static StageStatusValue WB = new StageStatusValue();
	static Formatter formatterUI = new Formatter(System.out);
	
	/*forwarding bus*/
	static ForwardingBus IntForwardBus = new ForwardingBus();
	static ForwardingBus MulForwardBus = new ForwardingBus();
	static ForwardingBus DuForwardBus = new ForwardingBus();
	static ForwardingBus ForwardingPSW = new ForwardingBus();
	/*forwarding bus end*/	
	
	public static boolean nonum = true;
	public static int inputCycleNum = 0;
	public static int MAX_RUN_CYCLE = 100000;
	public static boolean NoRelateInstAhead;
	public static boolean Output_Dependence;
	public static boolean DRF_src1_ready;
	public static boolean DRF_src2_ready;
	
	public static RegisterFile[] R = new RegisterFile[21];

	public static void main(String[] args) throws IOException{
		
		/*---initialization---*/
		dataReadyRegister = true;
		blockFetch = false;
		DRF_MUL_Flag = false; // MUL = true; Integer = False
		DRF_DIV_Flag = false; // DIV = true; Integer = False
		strArrBuffer = new String[10];
		inputCycleNum = 0;
		nonum = true;
		NoRelateInstAhead = false;
		Output_Dependence = false;
		DRF_src1_ready = false;
		DRF_src2_ready = false;
		pathReady = false;
		pathBuffer = null;
		fileNameBuffer = null;
		changeTestFile = true;
		
		/*Pipeline status*/
		new CorePipeline();
		/*Memory + register*/
		new CodeMemory();
		new DataMemory();
		/*Register*/	
		for(int i = 0; i<21; i++) {
			R[i] = new RegisterFile(i);
			R[i].cleanRegister();
		}
		/*ReadTxt*/
		new ReadTxt();
		/*cycle*/
		new Stats();
		
		Fetch.cleanStageValue();
		DRF.cleanStageValue();
		EX.cleanStageValue();
		EX_MUL.cleanStageValue();
		EX_MUL2.cleanStageValue();
		EX_DU1.cleanStageValue();
		EX_DU2.cleanStageValue();
		EX_DU3.cleanStageValue();
		EX_DU4.cleanStageValue();
		MEM.cleanStageValue();
		WB.cleanStageValue();
		
		IntForwardBus.cleanForwardingBus();
		MulForwardBus.cleanForwardingBus();
		DuForwardBus.cleanForwardingBus();
		ForwardingPSW.cleanForwardingBus();
		
			
		/*---END initialization---*/
		
		/*read in instruct file*/
		//File file = new File("/Users/xiaoyangzhang/eclipse-workspace/ComputerA_Q_Proj_1/src/simProj/testf.txt");
		//ReadTxt.txt2String(file);
		String path = "/Users/xiaoyangzhang/eclipse-workspace/ComputerA_Q_Proj_1/src/simProj/test_file";
		File file = new File(path);  
		File[] fileArray = file.listFiles(); 
		
		String[] Buffer = new String[10];
		System.out.println("\n");
		System.out.println("***************************************");
		System.out.println("**  Xiaoyang Zhang                   **");
		System.out.println("**  xzhan211@binghamton.edu          **");
		System.out.println("**  Pipeline Simulator   Version 2.0 **");
		System.out.println("***************************************");
		System.out.println("\n");
		
		
		while(true) {
			while(changeTestFile == true) {
				System.out.println("File list >>");
				System.out.print("\n");
				for(int i=0;i<fileArray.length;i++){   
					if(fileArray[i].isFile()){   
						// only take file name   
						System.out.println(fileArray[i].getName());   
					}        
				}  
				System.out.print("\n");
				pathBuffer = null;
				pathReady = false;
				formatterUI.format("%-15s %-2s %-50s\n", "Input file name", ":", "example: input.txt");
				System.out.print(">>>> ");
				BufferedReader fileName = new BufferedReader(new InputStreamReader(System.in));
				fileNameBuffer = fileName.readLine();
				for(int i=0;i<fileArray.length;i++){
					if(fileArray[i].getName().equals(fileNameBuffer)) {
						pathBuffer = fileArray[i].getPath();
						pathReady = true;
						changeTestFile = false;
						break;
					}
				}  
				if(pathReady == true) {
					pathReady = false;
					File fileBuffer = new File(pathBuffer);
					ReadTxt.txt2String(fileBuffer);
					break;	
				}else {
					System.out.println("Input File name error.");
					System.out.println("\n");
					
				}			
			}	
			System.out.println("\n");
			System.out.println("Simulator Commands:");
			formatterUI.format("%-15s %-2s %-50s\n", "<initialize>", ":", "Initializes the simulator state.");
			formatterUI.format("%-15s %-2s %-70s\n", "<simulate n>", ":", "Enter [simulate]+[n], n means quantity of cycles.");
			formatterUI.format("%-15s %-2s %-50s\n", "<display>", ":", "Display data/address about register and memory.");
			formatterUI.format("%-15s %-2s %-50s\n", "<run>", ":", "Run all instructions.");
			formatterUI.format("%-15s %-2s %-50s\n", "<change>", ":", "Change test file.");
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
				else if(Buffer[0].equals("change") ) 
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
				DRF_MUL_Flag = false; // MUL = true; Integer = False
				DRF_DIV_Flag = false; // DIV = true; Integer = False
				strArrBuffer = new String[10];
				inputCycleNum = 0;
				nonum = true;
				NoRelateInstAhead = false;
				Output_Dependence = false;
				DRF_src1_ready = false;
				DRF_src2_ready = false;
				//changeTestFile = false;
				/*Pipeline status*/
				new CorePipeline();
				/*Memory + register*/
				new CodeMemory();
				new DataMemory();
				/*Register*/
				for(int i = 0; i<16; i++) {
					R[i].cleanRegister();
				}
				/*cycle*/
				new Stats();
				/*---END initialization---*/
				Fetch.cleanStageValue();
				DRF.cleanStageValue();
				EX.cleanStageValue();
				EX_MUL.cleanStageValue();
				EX_MUL2.cleanStageValue();
				EX_DU1.cleanStageValue();
				EX_DU2.cleanStageValue();
				EX_DU3.cleanStageValue();
				EX_DU4.cleanStageValue();
				MEM.cleanStageValue();
				WB.cleanStageValue();	
		
				IntForwardBus.cleanForwardingBus();
				MulForwardBus.cleanForwardingBus();
				DuForwardBus.cleanForwardingBus();
				ForwardingPSW.cleanForwardingBus();
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
				inputCycleNum = MAX_RUN_CYCLE;
				break;
			case "change":
				changeTestFile = true;
				break;
			case "exit":
				return;
			default:
				break;
			}
		
		for(int n = 0; n < inputCycleNum; n++) {	
			if(changeTestFile == true) {
				break;
			}
			
			IntForwardBus.cleanForwardingBus();
			MulForwardBus.cleanForwardingBus();
			DuForwardBus.cleanForwardingBus();
			ForwardingPSW.cleanForwardingBus();
			
/*------------------------------WB start---------------------------------------*/				
		if(WB.c_instruction != null) {
				/*send the current value to WB function*/
				CorePipeline.WB_current_instr = WB.c_instruction;
				CorePipeline.WB_current_instrNo = WB.c_instr_number;
				CorePipeline.WB_current_dest_addr = WB.c_dest_addr;
				CorePipeline.WB_current_memory_result = WB.c_MEM_result;
				CorePipeline.WB_current_alu_result = WB.c_ALU_result;
				CorePipeline.WB_current_alu_result2 = WB.c_ALU_result2;
				CorePipeline.WB_current_literal = WB.c_literal;
				CorePipeline.WB_current_flag_zero = WB.c_FLAG_zero;
				CorePipeline.writeback();
				strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];	
				switch(strBuffer) {
				case "MOVC":		
					WB.c_dest_data = R[WB.c_dest_addr].value;
					R[WB.c_dest_addr].valueReady = true;	
					break;
							
				case "LOAD":
					WB.c_dest_data = R[WB.c_dest_addr].value;
					R[WB.c_dest_addr].valueReady = true;
					break;
				
				case "JUMP":
				case "BZ":	
				case "BNZ":
					break;
					
				case "JAL":
					WB.c_dest_data = R[WB.c_dest_addr].value;
					R[WB.c_dest_addr].valueReady = true;
					break;
							
				case "MUL":
				case "DIV":
				case "EXOR":
				case "AND":
				case "OR":
				case "ADD":			
				case "SUB":
					WB.c_dest_data = R[WB.c_dest_addr].value;
					R[WB.c_dest_addr].valueReady = true;
					break;	
					
				case "HALT":
				case "STORE":
				default:
					break;
				}	
		}
		
/*------------------------------WB end-----------------------------------------*/				
/*------------------------------MEM start--------------------------------------*/			
		if(MEM.c_instruction != null) {
			while(MEM.c_free){	
					/*send the current value to MEM function*/
					CorePipeline.MEM_current_instr = MEM.c_instruction; 
					CorePipeline.MEM_current_target_address = MEM.c_target_memory_addr;
					CorePipeline.MEM_current_sour_register_data = MEM.c_src1_data;
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
/*------------------------------MEM end----------------------------------------*/	
		
/*------------------------------EX DU4 start---------------------------------------*/
		if(EX_DU4.c_instruction != null) {	
			while(EX_DU4.c_free){
					EX_DU4.c_free = false; // when finish this stage's operation, set c_free "false"		
					//DuForwardBus.value = EX_DU4.c_ALU_result;
					//DuForwardBus.address = EX_DU4.c_dest_addr;
					//DuForwardBus.busDataReady = true;
					//ForwardingPSW.PSWflag = EX_DU4.c_FLAG_zero;
					//ForwardingPSW.flagReady = true;
					//ForwardingPSW.instrNo = EX_DU4.c_instr_number;
			}	
			DuForwardBus.value = EX_DU4.c_ALU_result;
			DuForwardBus.address = EX_DU4.c_dest_addr;
			DuForwardBus.busDataReady = true;
			ForwardingPSW.PSWflag = EX_DU4.c_FLAG_zero;
			ForwardingPSW.flagReady = true;
			ForwardingPSW.instrNo = EX_DU4.c_instr_number;
		}	
/*------------------------------EX DU4 end---------------------------------------*/
		
/*------------------------------EX DU3 start---------------------------------------*/
		if(EX_DU3.c_instruction != null) {	
			while(EX_DU3.c_free){
					EX_DU3.c_free = false; // when finish this stage's operation, set c_free "false"		
			}	
		}	
/*------------------------------EX DU3 end---------------------------------------*/
		
/*------------------------------EX DU2 start---------------------------------------*/
		if(EX_DU2.c_instruction != null) {	
			while(EX_DU2.c_free){
					EX_DU2.c_free = false; // when finish this stage's operation, set c_free "false"		
			}	
		}		
/*------------------------------EX DU2 end---------------------------------------*/	
		
/*------------------------------EX DU1 start---------------------------------------*/
		if(EX_DU1.c_instruction != null) {	
			while(EX_DU1.c_free){					
					CorePipeline.EX_current_instr = EX_DU1.c_instruction;
					CorePipeline.EX_literal = EX_DU1.c_literal;  
					CorePipeline.EX_destBuffer = EX_DU1.c_dest_addr;
					CorePipeline.EX_sourBufferA = EX_DU1.c_src1_data;
					CorePipeline.EX_sourBufferB = EX_DU1.c_src2_data;
					CorePipeline.execute();
					EX_DU1.c_ALU_result = CorePipeline.ALU_result_DIV;
					EX_DU1.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
					EX_DU1.c_free = false;
			}	
		}				
/*------------------------------EX DU1 end---------------------------------------*/
		
/*------------------------------EX MUL2 start---------------------------------------*/	
		if(EX_MUL2.c_instruction != null) {	
			while(EX_MUL2.c_free){
					EX_MUL2.c_free = false; // when finish this stage's operation, set c_free "false"		
					//MulForwardBus.value = EX_MUL2.c_ALU_result;
					//MulForwardBus.address = EX_MUL2.c_dest_addr;
					//MulForwardBus.busDataReady = true;
					//ForwardingPSW.PSWflag = EX_MUL2.c_FLAG_zero;
					//ForwardingPSW.flagReady = true;
					//ForwardingPSW.instrNo = EX_MUL2.c_instr_number;
			}
			MulForwardBus.value = EX_MUL2.c_ALU_result;
			MulForwardBus.address = EX_MUL2.c_dest_addr;
			MulForwardBus.busDataReady = true;
			ForwardingPSW.PSWflag = EX_MUL2.c_FLAG_zero;
			ForwardingPSW.flagReady = true;
			ForwardingPSW.instrNo = EX_MUL2.c_instr_number;
		}	
/*------------------------------EX MUL2 end-----------------------------------------*/	


/*------------------------------EX MUL start---------------------------------------*/		
		if(EX_MUL.c_instruction != null) {	
			while(EX_MUL.c_free){					
					CorePipeline.EX_current_instr = EX_MUL.c_instruction;
					CorePipeline.EX_literal = EX_MUL.c_literal;  
					CorePipeline.EX_destBuffer = EX_MUL.c_dest_addr;
					CorePipeline.EX_sourBufferA = EX_MUL.c_src1_data;
					CorePipeline.EX_sourBufferB = EX_MUL.c_src2_data;
					CorePipeline.execute();
					EX_MUL.c_ALU_result = CorePipeline.ALU_result_MUL;
					EX_MUL.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
					EX_MUL.c_free = false;
			}	
		}	
		
/*------------------------------EX MUL end-----------------------------------------*/			
		
	
		
/*------------------------------EX start---------------------------------------*/					
		if(EX.c_instruction != null) {	
			while(EX.c_free){				
					/*send the current value to EX function to calculate*/
					CorePipeline.EX_current_instr = EX.c_instruction;
					CorePipeline.EX_current_instr_num = EX.c_instr_number;
					CorePipeline.EX_literal = EX.c_literal;  
					CorePipeline.EX_destBuffer = EX.c_dest_addr;
					CorePipeline.EX_sourBufferA = EX.c_src1_data;
					CorePipeline.EX_sourBufferB = EX.c_src2_data;
					CorePipeline.EX_zeroFlagForward = EX.zeroFlagForwardBack;
					/*end*/
					CorePipeline.execute();
					
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];

					switch(strBuffer) {
					case "MOVC":
						EX.c_ALU_result= CorePipeline.ALU_result_MOVC;
						IntForwardBus.value = EX.c_ALU_result;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.busDataReady = true;
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
						IntForwardBus.value = EX.c_ALU_result;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.busDataReady = true;
						ForwardingPSW.PSWflag = EX.c_FLAG_zero;	
						ForwardingPSW.flagReady = true;
						ForwardingPSW.instrNo = EX.c_instr_number;
						break;
						
						
					case "SUB":
						EX.c_ALU_result = CorePipeline.ALU_result_SUB;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						IntForwardBus.value = EX.c_ALU_result;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.busDataReady = true;
						ForwardingPSW.PSWflag = EX.c_FLAG_zero;
						ForwardingPSW.flagReady = true;	
						ForwardingPSW.instrNo = EX.c_instr_number;
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
						
					case "JAL":
						EX.c_ALU_result = CorePipeline.ALU_result_JAL;
						Fetch.cleanStageValue();
						DRF.cleanStageValue();
						Fetch.c_free = false;
						EX.c_ALU_result2 = CorePipeline.ALU_result_JAL2;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.value = EX.c_ALU_result2;
						IntForwardBus.busDataReady = true;
						break;
					
					case "EXOR":	
						EX.c_ALU_result = CorePipeline.ALU_result_EXOR;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						IntForwardBus.value = EX.c_ALU_result;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.busDataReady = true;
						break;
						
					case "OR":
						EX.c_ALU_result = CorePipeline.ALU_result_OR;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						IntForwardBus.value = EX.c_ALU_result;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.busDataReady = true;
						break;
						
					case "AND":
						EX.c_ALU_result = CorePipeline.ALU_result_AND;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						IntForwardBus.value = EX.c_ALU_result;
						IntForwardBus.address = EX.c_dest_addr;
						IntForwardBus.busDataReady = true;
						break;
							
					default:
						break;			
					}					
					EX.c_free = false; // when finish this stage's operation, set c_free "false"		
					}
			
			/*keep on passing value and flag on forwarding bus when this instruction is stalled in DRF */
			strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
			switch(strBuffer) {
			case "MOVC":
				IntForwardBus.value = EX.c_ALU_result;
				IntForwardBus.address = EX.c_dest_addr;
				IntForwardBus.busDataReady = true;
				break;
				
			case "STORE":
			case "LOAD":
				break;

			case "ADD":
			case "SUB":
				IntForwardBus.value = EX.c_ALU_result;
				IntForwardBus.address = EX.c_dest_addr;
				IntForwardBus.busDataReady = true;
				ForwardingPSW.PSWflag = EX.c_FLAG_zero;
				ForwardingPSW.flagReady = true;	
				ForwardingPSW.instrNo = EX.c_instr_number;
				break;
				
			case "BZ":
			case "BNZ":
			case "JUMP":
				break;
				
			case "JAL":
				IntForwardBus.address = EX.c_dest_addr;
				IntForwardBus.value = EX.c_ALU_result2;
				IntForwardBus.busDataReady = true;
				break;
			
			case "EXOR":	
			case "OR":
			case "AND":
				IntForwardBus.value = EX.c_ALU_result;
				IntForwardBus.address = EX.c_dest_addr;
				IntForwardBus.busDataReady = true;
				break;
					
			default:
				break;			
			}
			
			/*==================================================================*/
		}
/*------------------------------EX end-----------------------------------------*/				

			
			
/*------------------------------DRF start---------------------------------------*/		
		if(DRF.c_instruction != null) {	
			while(DRF.c_free){
					DRF.c_free = false; // when finish this stage's operation, set c_free "false"					
					/*send the current value to DRF function*/
					CorePipeline.DRF_current_instr = DRF.c_instruction; 
					CorePipeline.DRFinstrNo = DRF.c_instr_number;
					/*end*/
					CorePipeline.decode();
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
					DRF.op = strBuffer;
					switch(strBuffer) {
					case "MOVC":		
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;	
						R[DRF.c_dest_addr].valueReady = false;
						break;
									
					case "LOAD":	
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;
						R[DRF.c_dest_addr].valueReady = false;
						break;
			
					case "STORE":
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_src2_addr = CorePipeline.DRF_sourBufferB_addr;
						break;
											
					case "MUL":
					case "DIV":	
					case "ADD":
					case "SUB":
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_src2_addr = CorePipeline.DRF_sourBufferB_addr;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;
						R[DRF.c_dest_addr].valueReady = false;
						break;
					case "OR":
					case "EXOR":
					case "AND":
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_src2_addr = CorePipeline.DRF_sourBufferB_addr;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;
						R[DRF.c_dest_addr].valueReady = false;	
						break;
					
					case"JUMP":
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_literal = CorePipeline.DRF_literal;
						break;
						
					case"JAL":
						DRF.c_literal = CorePipeline.DRF_literal;
						DRF.c_src1_addr = CorePipeline.DRF_sourBufferA_addr;
						DRF.c_dest_addr = CorePipeline.DRF_destBuffer_addr;
						R[DRF.c_dest_addr].valueReady = false;
						
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
						DRF_DIV_Flag = false;
						break;
					case "DIV":
					case "HALT":
						DRF_MUL_Flag = false;
						DRF_DIV_Flag = true;
						break;
					default:
						DRF_MUL_Flag = false;
						DRF_DIV_Flag = false;
						break;
					
					}
					
					
					
		/*---------------------------------------------------------------bubble start-------------------------------------------------------------*/
					/*figure out which instruction has right to set destination register ready bit*/
					
					if(DRF.c_dest_addr != 20) {
						if(DRF.c_dest_addr == EX.c_dest_addr) {
								Output_Dependence = true;
								
						}else if(DRF.c_dest_addr == EX_MUL.c_dest_addr) {
								Output_Dependence = true;
								
						}else if(DRF.c_dest_addr == EX_MUL2.c_dest_addr) {
								Output_Dependence = true;
			
						}else if(DRF.c_dest_addr == EX_DU1.c_dest_addr) {
								Output_Dependence = true;
	
						}else if(DRF.c_dest_addr == EX_DU2.c_dest_addr) {
								Output_Dependence = true;
	
						}else if(DRF.c_dest_addr == EX_DU3.c_dest_addr) {
								Output_Dependence = true;
	
						}else if(DRF.c_dest_addr == EX_DU4.c_dest_addr) {
								Output_Dependence = true;
	
						}else if(DRF.c_dest_addr == MEM.c_dest_addr) {
								Output_Dependence = true;
						}
					
							//NoRelateInstAhead = true;
						// It is no sense to set updateReg bit in WB stage. When instruction in WB, they just use
						 // the updateReg bit which passed by MEM stage.
					
					
					else if(DRF.c_dest_addr == DRF.c_src1_addr || DRF.c_dest_addr == DRF.c_src2_addr){
							NoRelateInstAhead = true;
							//System.out.println("in none");
						// no other dest_addr in other stages is same as the dest_addr in DRF.
					    // for example,in DRF, ADD R1,R1,R2, 
					    // Dest R1 can set the register.valueready bit false.( this bit is used by other instruction which after current instruction)
						}
					}
					
					/*when to block ( destination register not ready)*/
					if(R[DRF.c_src1_addr].valueReady == false || R[DRF.c_src2_addr].valueReady == false)	{
						if(R[DRF.c_src1_addr].valueReady == true) {
							DRF_src1_ready = true;
						}
						if(R[DRF.c_src2_addr].valueReady == true) {
							DRF_src2_ready = true;
						}
				
						if(R[DRF.c_src1_addr].valueReady == false) {
							if(DuForwardBus.busDataReady == true && DuForwardBus.address == DRF.c_src1_addr) {
								DRF.c_src1_data = DuForwardBus.value; 
								DRF_src1_ready = true;
								
							}else if(MulForwardBus.busDataReady == true && MulForwardBus.address == DRF.c_src1_addr) {
								DRF.c_src1_data = MulForwardBus.value;
								DRF_src1_ready = true;
								
							}else if(IntForwardBus.busDataReady == true && IntForwardBus.address == DRF.c_src1_addr) {
								DRF.c_src1_data = IntForwardBus.value;
								DRF_src1_ready = true;			
							}
						}
						
						if(R[DRF.c_src2_addr].valueReady == false) {
							if(DuForwardBus.busDataReady == true && DuForwardBus.address == DRF.c_src2_addr) {
								DRF.c_src2_data = DuForwardBus.value; 
								DRF_src2_ready = true;
								
							}else if(MulForwardBus.busDataReady == true && MulForwardBus.address == DRF.c_src2_addr) {
								DRF.c_src2_data = MulForwardBus.value;
								DRF_src2_ready = true;
								
							}else if(IntForwardBus.busDataReady == true && IntForwardBus.address == DRF.c_src2_addr) {
								DRF.c_src2_data = IntForwardBus.value;
								DRF_src2_ready = true;	
							}
						}

						if(NoRelateInstAhead == false && (DRF_src1_ready == false || DRF_src2_ready == false)) {
							dataReadyRegister = false;
							blockFetch = true;
							if(Output_Dependence == true) {
								Output_Dependence = false;
							}
							break;
						}else if(NoRelateInstAhead == true) {
							NoRelateInstAhead = false;
						}	
					}	

					if(R[DRF.c_dest_addr].valueReady == false && Output_Dependence == true) {
							dataReadyRegister = false;
							blockFetch = true;
							Output_Dependence = false;
							break;
					}
					

					strArrBuffer = DRF.c_instruction.split(",");
					if(strArrBuffer[0].equals("BNZ") && CorePipeline.ZERO_FLAG.bitStatusBusy == true) {
						if(ForwardingPSW.instrNo != CorePipeline.ZERO_FLAG.lastInstrNo) {
							dataReadyRegister = false;
							blockFetch = true;
							break;
						}else
							DRF.zeroFlagForwardBack = ForwardingPSW.PSWflag;
						
					}								
						
					if(strArrBuffer[0].equals("BZ") && CorePipeline.ZERO_FLAG.bitStatusBusy == true) {
						if(ForwardingPSW.instrNo != CorePipeline.ZERO_FLAG.lastInstrNo) {
							dataReadyRegister = false;
							blockFetch = true;
							break;
						}else
							DRF.zeroFlagForwardBack = ForwardingPSW.PSWflag;
					
					}
					
					dataReadyRegister = true;
					blockFetch = false;
					DRF_src1_ready = false;
					DRF_src2_ready = false;
					
					
		/*---------------------------------------------------------------bubble end-------------------------------------------------------------*/

					
					switch(strBuffer) {
					case "MOVC":		
						DRF.c_dest_data = R[DRF.c_dest_addr].value;
						break;
									
					case "LOAD":	
						if(R[DRF.c_src1_addr].valueReady == true) {
							DRF.c_src1_data = R[DRF.c_src1_addr].value;
						}
							DRF.c_dest_data = R[DRF.c_dest_addr].value;
						break;
			
					case "STORE":
						if(R[DRF.c_src1_addr].valueReady == true) {
							DRF.c_src1_data = R[DRF.c_src1_addr].value;
						}
						if(R[DRF.c_src2_addr].valueReady == true) {
							DRF.c_src2_data = R[DRF.c_src2_addr].value;
						}
						break;
											
					case "MUL":
					case "DIV":
					case "ADD":
					case "SUB":
					case "OR":
					case "EXOR":
					case "AND":
						if(R[DRF.c_src1_addr].valueReady == true) {
							DRF.c_src1_data = R[DRF.c_src1_addr].value;
						}
						if(R[DRF.c_src2_addr].valueReady == true) {
							DRF.c_src2_data = R[DRF.c_src2_addr].value;	
						}
						DRF.c_dest_data = R[DRF.c_dest_addr].value;
						break;
						
					case"JUMP":
						if(R[DRF.c_src1_addr].valueReady == true) {
							DRF.c_src1_data = R[DRF.c_src1_addr].value;
						}
						break;
						
					case"JAL":
						if(R[DRF.c_src1_addr].valueReady == true) {
							DRF.c_src1_data = R[DRF.c_src1_addr].value;
						}
						DRF.c_dest_data = R[DRF.c_dest_addr].value;
						break;
						
					case"BZ":						
					case "BNZ":
					case"HALT":
					default:
						break;
					}
					if(DRF.op.equals("ADD") || DRF.op.equals("SUB") || DRF.op.equals("MUL") || DRF.op.equals("DIV")) {
						DRF.math_op_flag = true;
					}else
						DRF.math_op_flag = false;
					
					
				
			}
		}			
/*------------------------------DRF end----------------------------------------*/			
		
		
/*------------------------------Fetch start------------------------------------*/	 
			while(Fetch.c_free){  // when first time enter F stage, F_current_free is true.
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
						Fetch.c_free = false; // when fetch a new instruction, F is not free	
						Fetch.c_ALU_result = 0;
					} else {
						Fetch.c_free = false; //
					}						
			}
/*------------------------------Fetch end---------------------------------------*/			
		
			
/*------------------------------Display----------------------------------------*/		
			FormatDisplay.runDisplay();	
			//System.out.println("\n");
			//FormatDisplay.registerDisplay();
/*-----------------------------Display End-------------------------------------*/			
			
/*-------------------------Pass Start------------------------------------------*/
/*-------------------------MEM to WB-------------------------------------------*/

				MEM.passStageValue(WB);
				MEM.cleanStageValue();
			
			

/*-------------------------EX to MEM------------------------------------------*/
			
			if(MEM.c_free == true) { 
				if(EX_DU4.c_free == false) {
					EX_DU4.passStageValue(MEM);
					EX_DU4.cleanStageValue();
			    }else if(EX_MUL2.c_free == false && EX_DU4.c_free == true) {
					EX_MUL2.passStageValue(MEM);
					EX_MUL2.cleanStageValue();
				}else if(EX_MUL2.c_free == true && EX_DU4.c_free == true && EX.c_free == false) {
					EX.passStageValue(MEM);
					EX.cleanStageValue();
				}	
			}	

/*----------------------EX DU3 to EX DU4-------------------------------------*/
			if(EX_DU4.c_free == true && EX_DU3.c_free == false) {
				EX_DU3.passStageValue(EX_DU4);
				EX_DU3.cleanStageValue();
			}
			
/*----------------------EX DU2 to EX DU3-------------------------------------*/	
			if(EX_DU3.c_free == true && EX_DU2.c_free == false) {
				EX_DU2.passStageValue(EX_DU3);
				EX_DU2.cleanStageValue();
			}
						
/*----------------------EX DU1 to EX DU2-------------------------------------*/
			if(EX_DU2.c_free == true && EX_DU1.c_free == false) {
				EX_DU1.passStageValue(EX_DU2);
				EX_DU1.cleanStageValue();
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
				if(DRF_MUL_Flag == true && DRF_DIV_Flag == false && EX_MUL.c_free == true) { 
					DRF.passStageValue(EX_MUL);
					DRF.cleanStageValue();
				}
				else if(DRF_MUL_Flag == false && DRF_DIV_Flag == true && EX_DU1.c_free == true){
					DRF.passStageValue(EX_DU1);
					DRF.cleanStageValue();
				}
				else if(DRF_MUL_Flag == false && DRF_DIV_Flag == false && EX.c_free == true) {
					DRF.passStageValue(EX);
					DRF.cleanStageValue();
				}
			}				
/*---------------------Fetch to DRF--------------------------------------------*/			
			if(CorePipeline.HALT_RUN == true) {
				Fetch.cleanStageValue();
				Fetch.c_free = false;
				
			}
			if(CorePipeline.HALT_RUN == false) {
				if(Fetch.c_free == false && DRF.c_free == true && blockFetch == false) {
					if(CorePipeline.BZ_RUN == false && CorePipeline.BNZ_RUN == false && CorePipeline.JUMP_RUN == false && CorePipeline.JAL_RUN == false) {
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
					}else if(CorePipeline.JAL_RUN == true) {
						Fetch.cleanStageValue();
						CorePipeline.instructionCounter = CorePipeline.instCounterBuffer - 1;
						CorePipeline.JAL_RUN = false;	
					}
				}
			}
			
			
			
/*----------------------Pass END----------------------------------------------*/						
			Stats.increaseCycle();			
			if(Fetch.c_instruction == null && DRF.c_instruction == null && MEM.c_instruction == null && WB.c_instruction == null
					&& EX.c_instruction == null && EX_MUL.c_instruction == null && EX_MUL2.c_instruction == null
					&& EX_DU1.c_instruction == null && EX_DU2.c_instruction == null && EX_DU3.c_instruction == null && EX_DU4.c_instruction == null) {
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
		System.out.println("Cycle " + Stats.cycle + ":");
		
		
		if(TestMain.Fetch.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "Fetch ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "Fetch ", "     :", "(I", TestMain.Fetch.c_instr_number - 1, ")" , "   ", TestMain.Fetch.c_instruction);
		}
        
		if(TestMain.DRF.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DRF ", "     :", " Empty");
		}else if(TestMain.DRF.c_instr_number - 1 >= 0 && TestMain.blockFetch == true){
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s %-10s\n", "DRF ", "     :", "(I", TestMain.DRF.c_instr_number - 1, ")" , "   ", TestMain.DRF.c_instruction, " Stalled");
		}else if(TestMain.DRF.c_instr_number - 1 >= 0){
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DRF ", "     :", "(I", TestMain.DRF.c_instr_number - 1, ")" , "   ", TestMain.DRF.c_instruction);
		}
        
		if(TestMain.EX.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "INTFU ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "INTFU ", "     :", "(I", TestMain.EX.c_instr_number - 1, ")" , "   ", TestMain.EX.c_instruction);
		}
		
		if(TestMain.EX_MUL.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "MUL1 ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "MUL1 ", "     :", "(I", TestMain.EX_MUL.c_instr_number - 1, ")" , "   ", TestMain.EX_MUL.c_instruction);
		}
		
		if(TestMain.EX_MUL2.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "MUL2 ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "MUL2 ", "     :", "(I", TestMain.EX_MUL2.c_instr_number - 1, ")" , "   ", TestMain.EX_MUL2.c_instruction);
		}
		
		if(TestMain.EX_DU1.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV1 ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV1 ", "     :", "(I", TestMain.EX_DU1.c_instr_number - 1, ")" , "   ", TestMain.EX_DU1.c_instruction);
		}
		
		if(TestMain.EX_DU2.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV2 ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV2 ", "     :", "(I", TestMain.EX_DU2.c_instr_number - 1, ")" , "   ", TestMain.EX_DU2.c_instruction);
		}
		
		if(TestMain.EX_DU3.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV3 ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV3 ", "     :", "(I", TestMain.EX_DU3.c_instr_number - 1, ")" , "   ", TestMain.EX_DU3.c_instruction);
		}
			
		if(TestMain.EX_DU4.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV4 ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV4 ", "     :", "(I", TestMain.EX_DU4.c_instr_number - 1, ")" , "   ", TestMain.EX_DU4.c_instruction); 
		}
		
		if(TestMain.MEM.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "MEM ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "MEM ", "     :", "(I", TestMain.MEM.c_instr_number - 1, ")" , "   ", TestMain.MEM.c_instruction);
		}
		
		if(TestMain.WB.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "WB ", "     :", " Empty");
		}else {
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "WB ", "     :", "(I", TestMain.WB.c_instr_number - 1, ")" , "   ", TestMain.WB.c_instruction);
		}

		formatter.format("\n");
		formatter.format("\n");
	}
	public static void registerDisplay() {
		formatter.format("Register Information:\n");
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R0", "R1", "R2", "R3");
		formatter.format("%-10d %-10d %-10d %-10d\n", TestMain.R[0].value, TestMain.R[1].value, TestMain.R[2].value, TestMain.R[3].value);
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R4", "R5", "R6", "R7");
		formatter.format("%-10d %-10d %-10d %-10d\n", TestMain.R[4].value, TestMain.R[5].value, TestMain.R[6].value, TestMain.R[7].value);
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R8", "R9", "R10", "R11");
		formatter.format("%-10d %-10d %-10d %-10d\n", TestMain.R[8].value, TestMain.R[9].value, TestMain.R[10].value, TestMain.R[11].value);
		formatter.format("------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R12", "R13", "R14", "R15");
		formatter.format("%-10d %-10d %-10d %-10d\n", TestMain.R[12].value, TestMain.R[13].value, TestMain.R[14].value, TestMain.R[15].value);
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
