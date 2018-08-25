package simProj;
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
	//public static boolean physicalRegister;
	public static boolean blockFetch;
	public static boolean DRF_MUL_Flag; 
	public static boolean DRF_DIV_Flag; 
	public static String pathBuffer;
	public static boolean pathReady;
	public static String fileNameBuffer;
	public static boolean changeTestFile;
	public static String physrtingbuffer;

	
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
	
	/*Architectural register*/
	public static RegisterFile[] R = new RegisterFile[21]; //architectural register file (array)
	/*ISSUE QUEUE*/
	public static StageStatusValue[] IQ = new StageStatusValue[16]; // Issue Queue
	/*Physical register*/
	public static RegisterFile[] PR = new RegisterFile[41]; //physical register file (array)
	/*renaming table*/
	public static int[] renameTable = new int[16]; // eg: renameTable[2] = 3 means architectural register is 2, physical register is 3. 
	/*ROB*/
	public static StageStatusValue[] ROB = new StageStatusValue[41];
	public static int headPointer;
	public static int tailPointer;
	//public static boolean ROB_first_solt_done;
	//public static boolean ROB_second_solt_done;
	/*LSQ*/
	public static StageStatusValue[] LSQ = new StageStatusValue[41]; 
	public static boolean newInstructionInDrfReady;
	public static boolean IQhasFreeSlot;
	public static boolean ROBhasFreeSlot;
	public static boolean LSQhasFreeSlot;
	
	public static StageStatusValue[] commitBuffer = new StageStatusValue[2];
	
	
	//
	
	/*FU status*/
	public static boolean MEMStatusBusy;
	public static boolean DUStatusBusy;
	public static boolean MULStatusBusy;
	public static boolean IFUStatusBusy;
	
	public static boolean IQ_src1_done;
	public static boolean IQ_src2_done;
	public static boolean IQ_dest_done;
	
	public static String physicalDisplay;
	
	
	public static void main(String[] args) throws IOException{
		/*---initialization---*/
		dataReadyRegister = true;
		//physicalRegister = true;
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
		physicalDisplay = null;
		
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
		
		/*Issue Queue*/
		for(int i = 0; i<16; i++) {
			IQ[i] = new StageStatusValue();
			IQ[i].cleanStageValue();
		}
		
		/*ROB*/
		for(int i = 0; i<41; i++) {
			ROB[i] = new StageStatusValue();
			ROB[i].cleanStageValue();
		}
		
		/*LSQ*/
		for(int i = 0; i<41; i++) {
			LSQ[i] = new StageStatusValue();
			LSQ[i].cleanStageValue();
		}
		
		/*Commitment*/
		for(int i = 0; i<2; i++) {
			commitBuffer[i] = new StageStatusValue();
			commitBuffer[i].cleanStageValue();
		}
		
		/*Physical Register file*/
		for(int i = 0; i<41; i++) {
			PR[i] = new RegisterFile(i);
			PR[i].cleanRegister();
		}
		
		/*renameTable information*/
		for(int i = 0; i<16; i++) {
			renameTable[i] = 20;
		}
		
		
		/*FU parts status*/
		MEMStatusBusy = false;
		DUStatusBusy = false;
		MULStatusBusy = false;
		IFUStatusBusy = false;
		
		IQ_src1_done = false;
		IQ_src2_done = false;
		IQ_dest_done = false;
		newInstructionInDrfReady = false;
		IQhasFreeSlot = false;
		ROBhasFreeSlot = false;
		LSQhasFreeSlot = false;
		headPointer = 0;
		tailPointer = 0;
		//firstCommitBufferNo = 1000;
		//secondCommitBufferNo = 1000;
		//firstCommitBufferInstr = null;
		//secondCommitBufferInstr = null;
		//ROB_first_solt_done = false;
		//ROB_second_solt_done = false;
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
		System.out.println("**  Pipeline Simulator   Version 3.0 **");
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
				//physicalRegister = true;
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
				/*Issue Queue*/
				for(int i = 0; i<16; i++) {
					IQ[i].cleanStageValue();
				}
				/*ROB*/
				for(int i = 0; i<32; i++) {
					ROB[i].cleanStageValue();
				}
				/*LSQ*/
				for(int i = 0; i<32; i++) {
					LSQ[i].cleanStageValue();
				}
				
				/*Commit*/
				for(int i = 0; i<2; i++) {
					commitBuffer[i].cleanStageValue();
				}
				
				/*Physical Register file*/
				for(int i = 0; i<32; i++) {
					PR[i].cleanRegister();
				}
				/*other renamed information*/
				for(int i = 0; i<16; i++) {
					renameTable[i] = 20;
				}
				
				/*renamed*/
				/*
				for(int i = 0; i<32; i++) {
					renamed[i] = false;  //architectural register is not renamed 
				}
				*/
				
				/*FU parts status*/
				MEMStatusBusy = false;
				DUStatusBusy = false;
				MULStatusBusy = false;
				IFUStatusBusy = false;
				
				IQ_src1_done = false;
				IQ_src2_done = false;
				IQ_dest_done = false;

				newInstructionInDrfReady = false;
				IQhasFreeSlot = false;
				ROBhasFreeSlot = false;
				LSQhasFreeSlot = false;
				headPointer = 0;
				tailPointer = 0;
				physicalDisplay = null;
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
				FormatDisplay.displayArchRegister();
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
						
/*------------------------------MEM start--------------------------------------*/			
		/*
			if(MEM.c_instruction != null) {
			while(MEM.c_free){	
					//send the current value to MEM function
					CorePipeline.MEM_current_instr = MEM.c_instruction; 
					CorePipeline.MEM_current_target_address = MEM.LSQ_memory_addr;
					CorePipeline.MEM_current_sour_register_data = MEM.c_src1_data;
					//
						
					CorePipeline.memory();
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
						
					switch(strBuffer) {
					case "STORE":
						MEM.c_target_memory_data = DataMemory.Data_Instruction_Segment[MEM.LSQ_memory_addr];
						break;
					case "LOAD":
						MEM.c_MEM_result = CorePipeline.MEM_current_load_value;
						PR[MEM.IQ_dest_addr].value= MEM.c_MEM_result;
						PR[MEM.IQ_dest_addr].valueReady = true; 
						break;	
					}
					MEM.c_free = false; // when finish this stage's operation, set c_free "false"	
				}
		}	
		*/		 
/*------------------------------MEM end----------------------------------------*/	
		
/*------------------------------EX DU4 start---------------------------------------*/
		if(EX_DU4.c_instruction != null) {	
			while(EX_DU4.c_free){
					EX_DU4.c_free = false; // when finish this stage's operation, set c_free "false"		
			}	
			//DuForwardBus.value = EX_DU4.c_ALU_result;
			//DuForwardBus.address = EX_DU4.IQ_dest_addr;
			//DuForwardBus.busDataReady = true;
			PR[EX_DU4.IQ_dest_addr].value = EX_DU4.c_ALU_result;
			PR[EX_DU4.IQ_dest_addr].valueReady = true;
			//ForwardingPSW.PSWflag = EX_DU4.c_FLAG_zero;
			//ForwardingPSW.flagReady = true;
			//ForwardingPSW.instrNo = EX_DU4.c_instr_number;
			PR[EX_DU4.IQ_dest_addr].zeroFlag = EX_DU4.c_FLAG_zero;
			PR[EX_DU4.IQ_dest_addr].zeroFlagReady = true;
			PR[EX_DU4.IQ_dest_addr].instructionNum = EX_DU4.c_instr_number; 	
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
					//CorePipeline.EX_destBuffer = EX_DU1.IQ_dest_addr;
					CorePipeline.EX_sourBufferA = EX_DU1.IQ_src1_data;
					CorePipeline.EX_sourBufferB = EX_DU1.IQ_src2_data;
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
			}
			//MulForwardBus.value = EX_MUL2.c_ALU_result;
			//MulForwardBus.address = EX_MUL2.IQ_dest_addr;
			//MulForwardBus.busDataReady = true;
			PR[EX_MUL2.IQ_dest_addr].value = EX_MUL2.c_ALU_result;
			PR[EX_MUL2.IQ_dest_addr].valueReady = true;
			//ForwardingPSW.PSWflag = EX_MUL2.c_FLAG_zero;
			//ForwardingPSW.flagReady = true;
			//ForwardingPSW.instrNo = EX_MUL2.c_instr_number;
			PR[EX_MUL2.IQ_dest_addr].zeroFlag = EX_MUL2.c_FLAG_zero;
			PR[EX_MUL2.IQ_dest_addr].zeroFlagReady = true;
			PR[EX_MUL2.IQ_dest_addr].instructionNum = EX_MUL2.c_instr_number; 	
		}	
/*------------------------------EX MUL2 end-----------------------------------------*/	


/*------------------------------EX MUL start---------------------------------------*/		
		if(EX_MUL.c_instruction != null) {	
			while(EX_MUL.c_free){					
					CorePipeline.EX_current_instr = EX_MUL.c_instruction;
					CorePipeline.EX_literal = EX_MUL.c_literal;  
					//CorePipeline.EX_destBuffer = EX_MUL.IQ_dest_addr;
					CorePipeline.EX_sourBufferA = EX_MUL.IQ_src1_data;
					CorePipeline.EX_sourBufferB = EX_MUL.IQ_src2_data;
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
					//send the current value to EX function to calculate
					CorePipeline.EX_current_instr = EX.c_instruction;
					CorePipeline.EX_current_instr_num = EX.c_instr_number;
					CorePipeline.EX_literal = EX.c_literal;  
					//CorePipeline.EX_destBuffer = EX.IQ_dest_addr;
					CorePipeline.EX_sourBufferA = EX.IQ_src1_data;
					CorePipeline.EX_sourBufferB = EX.IQ_src2_data;
					CorePipeline.EX_zeroFlagForward = EX.zeroFlagForwardBack; //use zero flag from forwarding bus
					
					CorePipeline.execute();
					
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];

					switch(strBuffer) {
					case "MOVC":
						EX.c_ALU_result= CorePipeline.ALU_result_MOVC;
						//IntForwardBus.value = EX.c_ALU_result;
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result;
						PR[EX.IQ_dest_addr].valueReady = true;
							
						break;
					case "STORE": // In LSQ, this part should be improved.
						EX.c_target_memory_addr = CorePipeline.ALU_result_STORE;
						EX.c_target_memory_data = DataMemory.Data_Instruction_Segment[EX.c_target_memory_addr];	
						EX.c_ALU_result = EX.c_target_memory_addr;
						EX.c_target_memory_addr_ready = true;
						break;
						
					case "LOAD": // In LSQ, this part should be improved.
						EX.c_target_memory_addr = CorePipeline.ALU_result_LOAD;
						EX.c_target_memory_data = DataMemory.Data_Instruction_Segment[EX.c_target_memory_addr];
						EX.c_ALU_result = EX.c_target_memory_addr;
						EX.c_target_memory_addr_ready = true;
						break;
						
					case "ADD":
						EX.c_ALU_result = CorePipeline.ALU_result_ADD;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						//IntForwardBus.value = EX.c_ALU_result;
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result;
						PR[EX.IQ_dest_addr].valueReady = true;
						//ForwardingPSW.PSWflag = EX.c_FLAG_zero;	
						//ForwardingPSW.flagReady = true;
						//ForwardingPSW.instrNo = EX.c_instr_number;
						PR[EX.IQ_dest_addr].zeroFlag = EX.c_FLAG_zero;
						PR[EX.IQ_dest_addr].zeroFlagReady = true;
						PR[EX.IQ_dest_addr].instructionNum = EX.c_instr_number; 
						break;
						
					case "SUB":
						EX.c_ALU_result = CorePipeline.ALU_result_SUB;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						//IntForwardBus.value = EX.c_ALU_result;
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result;
						PR[EX.IQ_dest_addr].valueReady = true;
						//ForwardingPSW.PSWflag = EX.c_FLAG_zero;
						//ForwardingPSW.flagReady = true;	
						//ForwardingPSW.instrNo = EX.c_instr_number;
						PR[EX.IQ_dest_addr].zeroFlag = EX.c_FLAG_zero;
						PR[EX.IQ_dest_addr].zeroFlagReady = true;
						PR[EX.IQ_dest_addr].instructionNum = EX.c_instr_number;
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
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.value = EX.c_ALU_result2;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result2;
						PR[EX.IQ_dest_addr].valueReady = true;
						break;
					
					case "EXOR":	
						EX.c_ALU_result = CorePipeline.ALU_result_EXOR;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						//IntForwardBus.value = EX.c_ALU_result;
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result;
						PR[EX.IQ_dest_addr].valueReady = true;
						break;
						
					case "OR":
						EX.c_ALU_result = CorePipeline.ALU_result_OR;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						//IntForwardBus.value = EX.c_ALU_result;
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result;
						PR[EX.IQ_dest_addr].valueReady = true;
						break;
						
					case "AND":
						EX.c_ALU_result = CorePipeline.ALU_result_AND;
						EX.c_FLAG_zero = CorePipeline.ALU_result_ZeroFlag;
						//IntForwardBus.value = EX.c_ALU_result;
						//IntForwardBus.address = EX.IQ_dest_addr;
						//IntForwardBus.busDataReady = true;
						PR[EX.IQ_dest_addr].value = EX.c_ALU_result;
						PR[EX.IQ_dest_addr].valueReady = true;
						break;	
					default:
						break;			
					}					
					EX.c_free = false; // when finish this stage's operation, set c_free "false"		
					}
			
			/*keep on passing value and flag on forwarding bus when this instruction is stalled in DRF */
			/*
			strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
			switch(strBuffer) {
			case "MOVC":
				IntForwardBus.value = EX.c_ALU_result;
				IntForwardBus.address = EX.IQ_dest_addr;
				IntForwardBus.busDataReady = true;
				break;
				
			case "STORE":
			case "LOAD":
				break;

			case "ADD":
			case "SUB":
				IntForwardBus.value = EX.c_ALU_result;
				IntForwardBus.address = EX.IQ_dest_addr;
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
				IntForwardBus.address = EX.IQ_dest_addr;
				IntForwardBus.value = EX.c_ALU_result2;
				IntForwardBus.busDataReady = true;
				break;
			
			case "EXOR":	
			case "OR":
			case "AND":
				IntForwardBus.value = EX.c_ALU_result;
				IntForwardBus.address = EX.IQ_dest_addr;
				IntForwardBus.busDataReady = true;
				break;
					
			default:
				break;			
			}
			*/
			//==================================================================
		}
/*------------------------------EX end-----------------------------------------*/		

/*------------------------------ROB--------------------------------------------*/

		
			//check if there is available slot in ROB
			if((headPointer - tailPointer) == 1 || (tailPointer - headPointer) == 31) {
				ROBhasFreeSlot = false;
			}else
				ROBhasFreeSlot = true;
			
			
			if(PR[ROB[headPointer].ROB_phy_dest_addr].valueReady == true && ROB[headPointer].ROB_excode_flag == false) {
				//ROB_first_solt_done = true;
				ROB[headPointer].ROB_phy_dest_ready = true;
			}
			if(headPointer != 31) {
				if(PR[ROB[headPointer+1].ROB_phy_dest_addr].valueReady == true && ROB[headPointer+1].ROB_excode_flag == false) {
					//ROB_second_solt_done = true;
					ROB[headPointer+1].ROB_phy_dest_ready = true;
				}
			}else {
				if(PR[ROB[headPointer-31].ROB_phy_dest_addr].valueReady == true && ROB[headPointer-31].ROB_excode_flag == false) {
					//ROB_second_solt_done = true;
					ROB[headPointer-31].ROB_phy_dest_ready = true;
				}
				
			}
			
			
			
			
			
/*------------------------------ROB end----------------------------------------*/	
	
/*------------------------------LSQ--------------------------------------------*/
			/*
			for(int i=0;i<32;i++) {
				if(LSQ[i].slotFree == true) {
					LSQhasFreeSlot = true;
					break;
				}
			}
	
			for(int i = 0; i<32; i++) {
				if((LSQ[i].c_instr_number == EX.c_instr_number) && (EX.c_target_memory_addr_ready == true) && (LSQ[i].op.equals("null") == false)) {
					LSQ[i].LSQ_memory_addr = EX.c_target_memory_addr;
					EX.c_target_memory_addr_ready = false;
					LSQ[i].LSQ_ready = true;
				}
			}
*/
/*------------------------------LSQ end----------------------------------------*/		
		
		
		
		
/*------------------------------Issue Q----------------------------------------*/
						
		
		
			//check free slot
			for(int i=0;i<16;i++) {
				if(IQ[i].slotFree == true) {
					IQhasFreeSlot = true;
					break;
				}
			}			
				
			
			/*IQ slot get src1/src2 value and wake up*/
			for(int i = 0; i<16; i++) {
				if(IQ[i].op.equals("null") == false) {
					switch(IQ[i].op) {
					case "JAL":
					case "LOAD":
					case "JUMP":
						if(PR[IQ[i].IQ_src1_addr].valueReady == true) {
							IQ[i].IQ_src1_data = PR[IQ[i].IQ_src1_addr].value;
							IQ[i].IQ_src1_addr = PR[IQ[i].IQ_src1_addr].address;
							IQ[i].IQ_src1_ready = true;	
							IQ[i].wakeUp = true;
						}
						break;
					case "STORE":
					case "MUL":
					case "DIV":
					case "ADD":
					case "SUB":
					case "OR":
					case "EXOR":
					case "AND":
						if(PR[IQ[i].IQ_src1_addr].valueReady == true) {
							IQ[i].IQ_src1_data = PR[IQ[i].IQ_src1_addr].value;
							IQ[i].IQ_src1_addr = PR[IQ[i].IQ_src1_addr].address;
							IQ[i].IQ_src1_ready = true;	
						}
						
						if(PR[IQ[i].IQ_src2_addr].valueReady == true) {
							//System.out.println("IQ[i] >> "+ i);
							IQ[i].IQ_src2_data = PR[IQ[i].IQ_src2_addr].value;
							IQ[i].IQ_src2_addr = PR[IQ[i].IQ_src2_addr].address;
							IQ[i].IQ_src2_ready = true;
						}
						
						if(IQ[i].IQ_src1_ready == true && IQ[i].IQ_src2_ready == true) {
							IQ[i].wakeUp = true;
						}
						break;
					case "MOVC":
					case "HALT":
					case "BZ":
					case "BNZ":
						IQ[i].wakeUp = true;
						break;
					default:
						break;
					}	
				}
			}

/*------------------------------Issue Q END-------------------------------------*/			
			
/*------------------------------DRF start---------------------------------------*/		
		/* step1: decode
		 * step2: rename
		 * step3: fetch value
		 * step4: dispatch
		 */
			
		if(DRF.c_instruction != null) {	
			while(DRF.c_free){
				//System.out.println("In DRF while");
					DRF.c_free = false; // when finish this stage's operation, set c_free "false"					
					/*send the current value to DRF function*/
					CorePipeline.DRF_current_instr = DRF.c_instruction; 
					CorePipeline.DRFinstrNo = DRF.c_instr_number;
					/*end*/
					CorePipeline.decode();
					strBuffer = CorePipeline.ReturnAssemblyInstructionArr[0];
					DRF.op = strBuffer;
					/*decode, get the architecture registers and literal*/
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
		
		/*---------------renaming part--------------*/
					
					/*assign the appropriate FU type*/
					switch(strBuffer) {									
					case "MUL":
						DRF.FU_type_needed = "FU_MUL";
						break;
						
					case "DIV":
					case"HALT":
						DRF.FU_type_needed = "FU_DU";
						break;
						
					case "LOAD":	
					case "STORE":
					case "MOVC":	
					case "ADD":
					case "SUB":
					case "OR":
					case "EXOR":
					case "AND":
					case"JUMP":
					case"JAL":
					case"BZ":						
					case "BNZ":	
						DRF.FU_type_needed = "FU_INT";
						break;	
					default:
						break;
					}
					
					/*find the available physical register*/
					/*if there are not enough physical registers, stalled and wait until the p registers resource available*/
					
					
					switch(strBuffer) {	
					/* need 2 register */
					case "STORE":
						//1st test: lastDestPhysicalRegisterAddr this should be released when instruction go out of EX
						/*if the previous instruction dest has the same physical register*/
						
						if(renameTable[DRF.c_src1_addr] != 20 && IQ_src1_done == false){
							DRF.IQ_src1_addr = renameTable[DRF.c_src1_addr];
							IQ_src1_done = true;
						}
						if(renameTable[DRF.c_src2_addr] != 20 && IQ_src2_done == false){
							DRF.IQ_src2_addr = renameTable[DRF.c_src2_addr];
							IQ_src2_done = true;
						}
						/*fetch the valid physical register for src1 */
						fetchPhysicalRegisterSrc1 ();
						fetchPhysicalRegisterSrc2 ();
						
						/*make sure the block can be release*/
						if(IQ_src1_done == true && IQ_src2_done == true){
							newInstructionInDrfReady = true;
							IQ_src1_done = false;
							IQ_src2_done = false;
						}
						break;
				
					case "LOAD":	
					case"JAL":
						if(renameTable[DRF.c_src1_addr] != 20 && IQ_src1_done == false){
							DRF.IQ_src1_addr = renameTable[DRF.c_src1_addr];
							IQ_src1_done = true;
						}
						/*fetch the valid physical register for src1 */
						fetchPhysicalRegisterSrc1();
						fetchPhysicalRegisterDest();
						
						/*make sure the block can be release*/
						if(IQ_src1_done == true && IQ_dest_done == true){
							newInstructionInDrfReady = true;
							IQ_src1_done = false;
							IQ_dest_done = false;
						}
						break;
						
					/* need 1 register */	
					case "MOVC":
						/*fetch the valid physical register for dest */
						fetchPhysicalRegisterDest();
						if(IQ_dest_done == true) {
							newInstructionInDrfReady = true;
							IQ_dest_done = false;
						}
						break;
						
					case "JUMP":
						/*if the previous instruction dest has the same physical register*/
						if(renameTable[DRF.c_src1_addr] != 20 && IQ_src1_done == false){
							DRF.IQ_src1_addr = renameTable[DRF.c_src1_addr];
							IQ_src1_done = true;
						}					
						/*fetch the valid physical register for src1 */
						fetchPhysicalRegisterSrc1();
						
						if(IQ_src1_done == true){
							newInstructionInDrfReady = true;
							IQ_src1_done = false;
						}
						break;
						
					/* need 3 register */	
					case "MUL":
					case "DIV":	
					case "ADD":
					case "SUB":
					case "OR":
					case "EXOR":
					case "AND":
						//1st test: lastDestPhysicalRegisterAddr this should be released when instruction go out of EX
						/*if the previous instruction dest has the same physical register*/
						if(renameTable[DRF.c_src1_addr] != 20 && IQ_src1_done == false){
							DRF.IQ_src1_addr = renameTable[DRF.c_src1_addr];
							IQ_src1_done = true;
						}
						if(renameTable[DRF.c_src2_addr] != 20 && IQ_src2_done == false){
							DRF.IQ_src2_addr = renameTable[DRF.c_src2_addr];
							IQ_src2_done = true;
						}
						
						/*fetch the valid physical register for src1 */
						fetchPhysicalRegisterDest();
						fetchPhysicalRegisterSrc1();
						fetchPhysicalRegisterSrc2();
						
						if(IQ_src1_done == true && IQ_src2_done == true && IQ_dest_done == true ){
							newInstructionInDrfReady = true;
							IQ_src1_done = false;
							IQ_src2_done = false;
							IQ_dest_done = false;
						}
						/*fetch the valid physical register for src2 */
						break;
						
					/* need 0 register */	
					case"HALT":	
					case"BZ":						
					case "BNZ":	
						newInstructionInDrfReady = true;
					default:
						break;
					}

		/*---------------renaming part end----------*/
					
		/*---------------get value part----------*/
					if(R[DRF.c_src1_addr].valueReady == true) {
						DRF.IQ_src1_data = R[DRF.c_src1_addr].value;
						DRF.IQ_src1_ready = true;
					}else if(PR[DRF.IQ_src1_addr].valueReady == true) {
						DRF.IQ_src1_data = PR[DRF.IQ_src1_addr].value;
						DRF.IQ_src1_ready = true;
					}
					
					if(R[DRF.c_src2_addr].valueReady == true) {
						DRF.IQ_src2_data = R[DRF.c_src2_addr].value;
						DRF.IQ_src2_ready = true;
					}else if(PR[DRF.IQ_src2_addr].valueReady == true) {
						DRF.IQ_src2_data = PR[DRF.IQ_src2_addr].value;
						DRF.IQ_src2_ready = true;
					}			
					
		/*---------------get value part end----------*/		
					
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
				
			//System.out.println("\n");
			//FormatDisplay.registerDisplay();
			/*
			System.out.println("=============================================================");
			System.out.println("==========START=====START=====START==========================");
			System.out.println("=============================================================");
			System.out.println("Cycle >> "+ Stats.cycle);
			System.out.println("-------------------------------------------------------------");
			FormatDisplay.runDisplay();
			System.out.println("-------------------------------------------------------------");
			FormatDisplay.displayArchRegister();
			System.out.println("-------------------------------------------------------------");
			FormatDisplay.displayPhysicalRegister();
			System.out.println("-------------------------------------------------------------");
			for(int i = 0; i<5;i++) {
				FormatDisplay.simpleIQDisplay(i);
			}
			System.out.println("-------------------------------------------------------------");
			for(int i = 0; i<5;i++) {
				FormatDisplay.ROBDisplay(i);
			}
			System.out.println("==========END=====END=======END==============================");
			*/
			FormatDisplay.runDisplay();
			commitBuffer[0].cleanStageValue();
			commitBuffer[1].cleanStageValue();
			
			
			
			
/*-----------------------------Display End-------------------------------------*/			
			
/*-------------------------Pass Start------------------------------------------*/
/*-------------------------MEM to WB-------------------------------------------*/

				MEM.passStageValue(WB);
				MEM.cleanStageValue();
			
			

/*-------------------------EX to MEM------------------------------------------*/
			/*
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
			*/
			EX_DU4.cleanStageValue();
			EX_MUL2.cleanStageValue();
			EX.cleanStageValue();
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
/*----------------------IQ to EX----------------------------------------------*/	
			
			if(EX_MUL.c_free == true || EX.c_free == true || EX_DU1.c_free == true) {
				int countMUL = 100000;
				int bufferMUL = 20;
				int countDU = 100000;
				int bufferDU = 20;
				int countINT = 100000;
				int bufferINT = 20;
				for(int i = 0; i< 16; i++) {
					if(IQ[i].wakeUp == true) {
						switch(IQ[i].FU_type_needed) {
						case "FU_MUL":
							if(IQ[i].dispatchTime < countMUL) {
								countMUL = IQ[i].dispatchTime;
								bufferMUL = i;
							}
							break;
						case "FU_DU":
							if(IQ[i].dispatchTime < countDU) {
								countDU = IQ[i].dispatchTime;
								bufferDU = i;
							}
							break;
						case "FU_INT":
							if(IQ[i].dispatchTime < countINT) {
								countINT = IQ[i].dispatchTime;
								bufferINT = i;
							}
							break;
						}
					}
				}
				
				if(bufferMUL != 20 && EX_MUL.c_free == true) {
					IQ[bufferMUL].passStageValue(EX_MUL);
					IQ[bufferMUL].cleanStageValue();
				}
				
				if(bufferDU != 20 && EX_DU1.c_free == true) {
					IQ[bufferDU].passStageValue(EX_DU1);
					IQ[bufferDU].cleanStageValue();
				}
				
				if(bufferINT != 20 && EX.c_free == true) {
					IQ[bufferINT].passStageValue(EX);
					IQ[bufferINT].cleanStageValue();
				}	
			}
/*----------------------ROB to ARF--------------------------------------------*/			
			// In ROB, read physical register and committing cannot happen in one cycle.
			// committing
			int k = 0;
			int m = 0;
			boolean firstIsCommit = false;
			boolean secondIsCommit = false;
			if(ROB[headPointer].ROB_phy_dest_ready == true) {
				while(k<16) {
					if(renameTable[k] == ROB[headPointer].ROB_phy_dest_addr) { //check if this physical register exist in rename table
						R[ROB[headPointer].ROB_ach_dest_addr].value = PR[ROB[headPointer].ROB_phy_dest_addr].value;
						R[ROB[headPointer].ROB_ach_dest_addr].valueReady = true;
						ROB[headPointer].passStageValue(commitBuffer[0]);
						firstIsCommit = true;
						break;
					}	
					k++;
				}
				
				for(int i=0;i<32;i++) {
					if((ROB[headPointer].ROB_phy_dest_addr == ROB[i].IQ_src1_addr || ROB[headPointer].ROB_phy_dest_addr == ROB[i].IQ_src2_addr) && headPointer != i) {
						m++;
					}
				}
				
				if(DRF.IQ_src1_addr == ROB[headPointer].ROB_phy_dest_addr ||DRF.IQ_src2_addr == ROB[headPointer].ROB_phy_dest_addr) {
					m++;
				}
				
				if(m==0 && firstIsCommit == true) {
					renameTable[ROB[headPointer].ROB_ach_dest_addr] = 20;
					PR[ROB[headPointer].ROB_phy_dest_addr].cleanRegister();
				}
				ROB[headPointer].cleanStageValue();
				if(headPointer == 31) {
					headPointer = headPointer - 31;
				}else {
					headPointer++;
				}	
			}

			k = 0;
			m = 0;
			
			if(ROB[headPointer].ROB_phy_dest_ready == true) {
				while(k<16) {
					if(renameTable[k] == ROB[headPointer].ROB_phy_dest_addr) {
						R[ROB[headPointer].ROB_ach_dest_addr].value = PR[ROB[headPointer].ROB_phy_dest_addr].value;
						R[ROB[headPointer].ROB_ach_dest_addr].valueReady = true;
						ROB[headPointer].passStageValue(commitBuffer[1]);
						secondIsCommit = true;
						break;
						
					}
					k++;	
				}
				
				for(int i=0;i<32;i++) {
					if((ROB[headPointer].ROB_phy_dest_addr == ROB[i].IQ_src1_addr || ROB[headPointer].ROB_phy_dest_addr == ROB[i].IQ_src2_addr) && headPointer != i){
						m++;
					}
				}
				
				if(DRF.IQ_src1_addr == ROB[headPointer].ROB_phy_dest_addr ||DRF.IQ_src2_addr == ROB[headPointer].ROB_phy_dest_addr) {
					m++;
				}
				
				if(m==0 && secondIsCommit == true) {
					renameTable[ROB[headPointer].ROB_ach_dest_addr] = 20;
					PR[ROB[headPointer].ROB_phy_dest_addr].cleanRegister();
				}
				ROB[headPointer].cleanStageValue();
				if(headPointer == 31) {
					headPointer = headPointer - 31;
				}else {
					headPointer++;
				}	
			}	
			firstIsCommit = false;
			secondIsCommit = false;
			k = 0;
			m = 0;
							
/*----------------------DRF to IQ/LSQ/ROB--------------------------------------*/		
			//no stall mechanism yet when IQ/LSQ/ROB have not available slots.
			if(DRF.c_free == false && newInstructionInDrfReady == false) {
				DRF.c_free = true;	
			}
			if(DRF.c_free == false && newInstructionInDrfReady == true && IQhasFreeSlot == true && ROBhasFreeSlot == true) {
				for(int i = 0;i<16;i++) {
					if(IQ[i].slotFree == true) {
						DRF.passStageValue(IQ[i]);
						IQ[i].slotNumber = i;
						IQ[i].slotFree = false;
						IQ[i].dispatchTime = Stats.cycle;
						break;
					}
				}	
				
				DRF.passStageValue(ROB[tailPointer]);
				ROB[tailPointer].ROB_ach_dest_addr = DRF.c_dest_addr;
				ROB[tailPointer].ROB_phy_dest_addr = DRF.IQ_dest_addr;
				ROB[tailPointer].ROB_phy_dest_ready =  false;
				ROB[tailPointer].ROB_excode_flag = false;
				//ROB[tailPointer].ROB_pre_dest_addr = 40; // use for predict
				if(tailPointer == 31) {
					tailPointer = tailPointer - 31;
				}else {
					tailPointer++;
				}
				/*	
				for(int i = 0;i<32;i++) {
					if(LSQ[i].slotFree == true && (DRF.op.equals("LOAD") || DRF.op.equals("STORE"))) {
						DRF.passStageValue(LSQ[i]);
						LSQ[i].slotNumber = i;
						LSQ[i].slotFree = false;
						LSQ[i].dispatchTime = Stats.cycle;
						//LSQ[i].LSQ_memory_addr = DRF.c_target_memory_addr;
						LSQ[i].LSQ_slot_num = i;
						LSQ[i].LSQ_slot_op = DRF.op;
						break;
					}
				}
				*/	
				DRF.cleanStageValue();	
				newInstructionInDrfReady = false;	
			}
						
/*---------------------Fetch to DRF--------------------------------------------*/		
			CorePipeline.HALT_RUN =false;
			if(CorePipeline.HALT_RUN == true) {
				Fetch.cleanStageValue();
				Fetch.c_free = false;
				
			}
		
			if(CorePipeline.HALT_RUN == false) {

				if(Fetch.c_free == false && DRF.c_free == true && blockFetch == false) {
					if(CorePipeline.BZ_RUN == false && CorePipeline.BNZ_RUN == false && CorePipeline.JUMP_RUN == false && CorePipeline.JAL_RUN == false) {
						//System.out.println("I should be here");
						Fetch.passStageValue(DRF);//ok in proj3
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
	
static void fetchPhysicalRegisterSrc1() {
	int physicalRegisterCounter = 0;
	if(IQ_src1_done == false) {
		while(physicalRegisterCounter < 32) {
			if(PR[physicalRegisterCounter].allocated == false) {
				DRF.IQ_src1_addr = physicalRegisterCounter;
				renameTable[DRF.c_src1_addr] = physicalRegisterCounter;
				PR[DRF.IQ_src1_addr].allocated = true;
				PR[DRF.IQ_src1_addr].renamed = true;
				PR[DRF.IQ_src1_addr].address = physicalRegisterCounter;
				IQ_src1_done = true;	
				if(R[DRF.c_src1_addr].valueReady == true) {	// fetch value from architectural register
					PR[DRF.IQ_src1_addr].value = R[DRF.c_src1_addr].value;
					PR[DRF.IQ_src1_addr].valueReady = true;
				}
				break;
			}
			physicalRegisterCounter++;	
		}
		if(physicalRegisterCounter == 32) {
			//dataReadyRegister = false;
			//physicalRegister = false;
			blockFetch = true;
		}
	}	
}//fetchPhysicalRegister

static void fetchPhysicalRegisterSrc2() {
	int physicalRegisterCounter = 0;
	if(IQ_src2_done == false) {
		while(physicalRegisterCounter < 32) {
			if(PR[physicalRegisterCounter].allocated == false) {
				DRF.IQ_src2_addr = physicalRegisterCounter;
				renameTable[DRF.c_src2_addr] = physicalRegisterCounter;
				PR[DRF.IQ_src2_addr].allocated = true;
				PR[DRF.IQ_src2_addr].renamed = true;
				PR[DRF.IQ_src2_addr].address = physicalRegisterCounter;
				IQ_src2_done = true;
				if(R[DRF.c_src1_addr].valueReady == true) {    // fetch value from architectural register
					PR[DRF.IQ_src2_addr].value = R[DRF.c_src1_addr].value;
					PR[DRF.IQ_src2_addr].valueReady = true;
				}
				break;
			}
			physicalRegisterCounter++;	
		}
		if(physicalRegisterCounter == 32) {
			//dataReadyRegister = false;
			//physicalRegister = false;
			blockFetch = true;
		}
	}	
}//fetchPhysicalRegister

static void fetchPhysicalRegisterDest() {
	int physicalRegisterCounter = 0;
	if(IQ_dest_done == false) {
		while(physicalRegisterCounter < 32) {
			if(PR[physicalRegisterCounter].allocated == false) {
				DRF.IQ_dest_addr = physicalRegisterCounter;
				renameTable[DRF.c_dest_addr] = physicalRegisterCounter;
				PR[DRF.IQ_dest_addr].allocated = true;
				PR[DRF.IQ_dest_addr].renamed = true;
				PR[DRF.IQ_dest_addr].address = physicalRegisterCounter;
				PR[DRF.IQ_dest_addr].valueReady = false;
				IQ_dest_done = true;	
				//R[DRF.c_dest_addr].valueReady = false; // when instruction in commitment, architectural register value ready bit can set ready. 
				
				break;
			}
			physicalRegisterCounter++;	
		}
		if(physicalRegisterCounter == 32) {
			blockFetch = true;
		}
	}	
}//fetchPhysicalRegister


static void archToPhy(StageStatusValue input) {
	
	switch(input.op) {									
	case "LOAD":
	case"JAL":
		physrtingbuffer = input.op + ",P"+ String.valueOf(input.IQ_dest_addr)+",P"+String.valueOf(input.IQ_src1_addr)+",#"+ String.valueOf(input.c_literal);
		break;
		
	case "STORE":
		physrtingbuffer = input.op + ",P"+ String.valueOf(input.IQ_src1_addr)+",P"+String.valueOf(input.IQ_src2_addr)+",#"+ String.valueOf(input.c_literal);
		break;	
		
	case"JUMP":
		physrtingbuffer = input.op + ",P"+ String.valueOf(input.IQ_src1_addr)+",#"+ String.valueOf(input.c_literal);
		break;
	
	case "MOVC":	
		physrtingbuffer = input.op + ",P"+ String.valueOf(input.IQ_dest_addr)+",#"+ String.valueOf(input.c_literal);
		break;
		
	case "ADD":
	case "SUB":
	case "MUL":
	case "DIV":
	case "OR":
	case "EXOR":
	case "AND":
		physrtingbuffer = input.op + ",P"+ String.valueOf(input.IQ_dest_addr)+",P"+String.valueOf(input.IQ_src1_addr)+",P"+ String.valueOf(input.IQ_src2_addr);
		break;

	case"BZ":						
	case "BNZ":	
	case"HALT":
		physrtingbuffer = input.c_instruction;
		break;	
	default:
		break;
	}
}
	
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
		
		System.out.println("");
		System.out.println("<RENAME TABLE>:");
		for(int i = 0; i<16; i++) {
			if(TestMain.renameTable[i] != 20) {
				formatter.format("%-1c  %-1c%-2d  %-1c  %-1c%-2d\n", '*', 'R', i,':', 'P', TestMain.renameTable[i]);
			}
		}
		System.out.println("");
		
		System.out.println("<IQ>:");
		for(int i = 0; i<16; i++) {
			if(TestMain.IQ[i].c_instruction != null) {
				TestMain.archToPhy(TestMain.IQ[i]);
				formatter.format("%-1c  %-1c%-1c%-2d%-1c  %-20s\n", '*', '(', 'I', TestMain.IQ[i].c_instr_number-1,')', TestMain.physrtingbuffer );
			}
		}
		System.out.println("");
		
		System.out.println("<ROB>:");
		for(int i = 0; i<32; i++) {
			if(TestMain.ROB[i].c_instruction != null) {
				TestMain.archToPhy(TestMain.ROB[i]);
				formatter.format("%-1c  %-1c%-1c%-2d%-1c  %-20s\n", '*', '(', 'I', TestMain.ROB[i].c_instr_number-1,')', TestMain.physrtingbuffer);
			}
		}
		System.out.println("");
		
		System.out.println("Commit:");
		if(TestMain.commitBuffer[0].c_instruction != null) {
			TestMain.archToPhy(TestMain.commitBuffer[0]);
			formatter.format("%-1c  %-1c%-1c%-2d%-1c  %-20s\n", '*', '(', 'I', TestMain.commitBuffer[0].c_instr_number - 1,')', TestMain.physrtingbuffer);
		}
		if(TestMain.commitBuffer[1].c_instruction != null) {
			TestMain.archToPhy(TestMain.commitBuffer[1]);
			formatter.format("%-1c  %-1c%-1c%-2d%-1c  %-20s\n", '*', '(', 'I', TestMain.commitBuffer[1].c_instr_number - 1,')', TestMain.physrtingbuffer);
		}
		System.out.println("");
		
		/*
		System.out.println("<LSQ>:");
		for(int i = 0; i<32; i++) {
			if(TestMain.LSQ[i].c_instruction != null) {
				TestMain.archToPhy(TestMain.LSQ[i]);
				formatter.format("%-1c  %-1c%-1c%-2d%-1c  %-20s\n", '*', '(', 'I', TestMain.LSQ[i].c_instr_number-1,')', TestMain.physrtingbuffer);
			}
		}
		System.out.println("");
		*/
		
		if(TestMain.EX.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "INTFU ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "INTFU ", "     :", "(I", TestMain.EX.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
		}
		
		if(TestMain.EX_MUL.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "MUL1 ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX_MUL);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "MUL1 ", "     :", "(I", TestMain.EX_MUL.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
		}
		
		if(TestMain.EX_MUL2.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "MUL2 ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX_MUL2);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "MUL2 ", "     :", "(I", TestMain.EX_MUL2.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
		}
		
		if(TestMain.EX_DU1.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV1 ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX_DU1);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV1 ", "     :", "(I", TestMain.EX_DU1.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
		}
		
		if(TestMain.EX_DU2.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV2 ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX_DU2);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV2 ", "     :", "(I", TestMain.EX_DU2.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
		}
		
		if(TestMain.EX_DU3.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV3 ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX_DU3);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV3 ", "     :", "(I", TestMain.EX_DU3.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
		}
			
		if(TestMain.EX_DU4.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "DIV4 ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.EX_DU4);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "DIV4 ", "     :", "(I", TestMain.EX_DU4.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer); 
		}
		
		if(TestMain.MEM.c_instr_number - 1 < 0) {
			formatter.format("%-8s %-5s %-20s\n", "MEM ", "     :", " Empty");
		}else {
			TestMain.archToPhy(TestMain.MEM);
			formatter.format("%-8s %-5s %3s%-2d%1s %-4s %-20s\n", "MEM ", "     :", "(I", TestMain.MEM.c_instr_number - 1, ")" , "   ", TestMain.physrtingbuffer);
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
	
	public static void IQdisplay(int i) {
		System.out.println("");
		System.out.println(" >> " + TestMain.IQ[0].FU_type_needed);
		System.out.println("Instruction >> " + TestMain.IQ[i].c_instruction);
		System.out.println("================================================================================");
		System.out.println("slotNumber        slotFree          allocated");
		System.out.println("--------------------------------------------------------------------------------");
		formatter.format("%-3d               %b              %b\n", TestMain.IQ[i].slotNumber, TestMain.IQ[i].slotFree, TestMain.IQ[i].IQ_status_allocated);
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("IQ_src1_addr      IQ_src1_data      IQ_src1_ready");
		System.out.println("--------------------------------------------------------------------------------");
		formatter.format("%-3d               %-5d             %b\n", TestMain.IQ[i].IQ_src1_addr, TestMain.IQ[i].IQ_src1_data, TestMain.IQ[i].IQ_src1_ready);
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("IQ_src2_addr      IQ_src2_data      IQ_src2_ready");
		System.out.println("--------------------------------------------------------------------------------");
		formatter.format("%-3d               %-5d             %b\n", TestMain.IQ[i].IQ_src2_addr, TestMain.IQ[i].IQ_src2_data, TestMain.IQ[i].IQ_src2_ready);
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("IQ_dest_addr      IQ_dest_data      IQ_dest_ready");
		System.out.println("--------------------------------------------------------------------------------");
		formatter.format("%-3d               %-5d             %b\n", TestMain.IQ[i].IQ_dest_addr, TestMain.IQ[i].IQ_dest_data, TestMain.IQ[i].IQ_dest_ready);
		System.out.println("================================================================================");
	}
	
	public static void simpleIQDisplay(int i) {
		System.out.println("");
		System.out.println("Instruction >> " + TestMain.IQ[i].c_instruction);
		System.out.println("----- IQ slot >> " + i + " ----------------------------------------------------------------------");
		System.out.println("dest_addr   dest_data   src1_addr   src1_data   src1_ready   src2_addr   src2_data   src2_ready");
		formatter.format("%-3d         %-5d        %-3d         %-5d       %b         %-3d         %-5d       %b\n", TestMain.IQ[i].IQ_dest_addr, TestMain.IQ[i].IQ_dest_data, TestMain.IQ[i].IQ_src1_addr, TestMain.IQ[i].IQ_src1_data, TestMain.IQ[i].IQ_src1_ready,TestMain.IQ[i].IQ_src2_addr, TestMain.IQ[i].IQ_src2_data, TestMain.IQ[i].IQ_src2_ready);
	}
	
	public static void ROBDisplay(int i) {
		System.out.println("");
		System.out.println("Instruction >> " + TestMain.ROB[i].c_instruction);
		System.out.println("----- ROB slot >> " + i + " ----------------------------------------------------------------------");
		System.out.println("inst_munb   arch_addr   phys_addr   phys_ready");
		formatter.format("%-3d         %-5d        %-3d         %b\n", TestMain.ROB[i].c_instr_number-1, TestMain.ROB[i].ROB_ach_dest_addr, TestMain.ROB[i].ROB_phy_dest_addr, TestMain.ROB[i].ROB_phy_dest_ready);
	}
	
	
	
	public static void displayPhysicalRegister() {
		System.out.println("");
		formatter.format("Physical Register Information:\n");
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "PR0", "PR1", "PR2", "PR3", "PR4", "PR5");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.PR[0].value, TestMain.PR[1].value, TestMain.PR[2].value, TestMain.PR[3].value, TestMain.PR[4].value, TestMain.PR[5].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "PR6", "PR7", "PR8", "PR9", "PR10", "PR11");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.PR[6].value, TestMain.PR[7].value, TestMain.PR[8].value, TestMain.PR[9].value, TestMain.PR[10].value, TestMain.PR[11].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "PR12", "PR13", "PR14", "PR15", "PR16", "PR17");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.PR[12].value, TestMain.PR[13].value, TestMain.PR[14].value, TestMain.PR[15].value, TestMain.PR[16].value, TestMain.PR[17].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "PR18", "PR19", "PR20", "PR21", "PR22", "PR23");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.PR[18].value, TestMain.PR[19].value, TestMain.PR[20].value, TestMain.PR[21].value, TestMain.PR[22].value, TestMain.PR[23].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "PR24", "PR25", "PR26", "PR27", "PR28", "PR29");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.PR[24].value, TestMain.PR[25].value, TestMain.PR[26].value, TestMain.PR[27].value, TestMain.PR[28].value, TestMain.PR[29].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s\n", "PR30", "PR31");
		formatter.format("%-10d %-10d\n", TestMain.PR[30].value, TestMain.PR[31].value);
		formatter.format("-----------------------------------------------------------\n");
	}
	
	public static void displayArchRegister() {
		System.out.println("");
		formatter.format("Architectural Register Information:\n");
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "R0", "R1", "R2", "R3", "R4", "R5");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.R[0].value, TestMain.R[1].value, TestMain.R[2].value, TestMain.R[3].value, TestMain.R[4].value, TestMain.R[5].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s %-10s %-10s\n", "R6", "R7", "R8", "R9", "R10", "R11");
		formatter.format("%-10d %-10d %-10d %-10d %-10d %-10d\n", TestMain.R[6].value, TestMain.R[7].value, TestMain.R[8].value, TestMain.R[9].value, TestMain.R[10].value, TestMain.R[11].value);
		formatter.format("-----------------------------------------------------------\n");
		formatter.format("%-10s %-10s %-10s %-10s\n", "R12", "R13", "R14", "R15");
		formatter.format("%-10d %-10d %-10d %-10d\n", TestMain.R[12].value, TestMain.R[13].value, TestMain.R[14].value, TestMain.R[15].value);
		formatter.format("-----------------------------------------------------------\n");
	}
	
}
