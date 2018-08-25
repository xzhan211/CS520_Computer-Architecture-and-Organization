package simProj;

public class StageStatusValue {
	
	public String c_instruction; // share with IQ, ROB
	public int c_instr_number; // share with IQ, ROB
	public int c_PC;
	public int c_src1_data;//architectural register data
	public int c_src2_data;//architectural register data
	public int c_dest_data;//architectural register data
	public int c_target_memory_data;
	public int c_literal;
	public int c_src1_addr; //architectural register address
	public int c_src2_addr; //architectural register address
	public int c_dest_addr; //architectural register address
	public int c_target_memory_addr;
	public boolean c_target_memory_addr_ready;
	public boolean c_free;
	public int c_ALU_result;
	public int c_ALU_result2;
	public int c_MEM_result;
	public boolean c_FLAG_zero;
	public String op;
	public boolean math_op_flag;
	public boolean updateReg;
	public boolean ALU_done;
	public boolean zeroFlagForwardBack;  // true == 0; false != 0;
	public boolean stalledFlag;
	
	public String FU_type_needed;
	/*physical register part*/
	public boolean IQ_status_allocated;//indicates if this IQ entry is allocated or free, true is allocated, false is free
	public boolean IQ_src1_ready;  
	public int IQ_src1_addr;//physical register address
	public int IQ_src1_data;//physical register data
	public boolean IQ_src2_ready; 
	public int IQ_src2_addr;
	public int IQ_src2_data;
	public boolean IQ_dest_ready;
	public int IQ_dest_addr;
	public int IQ_dest_data;
	public int dispatchTime;
	/*physical register part end*/
	
	/*IQ part slot*/
	public boolean slotFree;
	public int slotNumber;
	public boolean wakeUp;
	/*IQ part end*/
	
	
	/*ROB part slot*/
	// entry type/operation/PC value are shared with above data
	public int ROB_phy_dest_addr;
	public int ROB_ach_dest_addr;
	public boolean ROB_phy_dest_ready;
	public boolean ROB_excode_flag;
	public int ROB_pre_dest_addr;
	/*ROB part end*/
	
	/*LSQ part slot*/
	public int LSQ_memory_addr;
	public int LSQ_slot_num;
	public String LSQ_slot_op;
	public boolean LSQ_ready;
	/*LSQ part end*/
	
	
	
	
	/*initialization*/
	StageStatusValue() {
		c_instruction = null;
		c_instr_number = 0;
		c_PC = 0;
		c_src1_data = -1;
		c_src2_data = -1;
		c_dest_data = -1;
		c_target_memory_data = 0;
		c_literal = 0;
		c_src1_addr = 20;
		c_src2_addr = 20;
		c_dest_addr = 20;
		c_target_memory_addr = 5000;
		c_target_memory_addr_ready = false;
		c_free = true;
		c_ALU_result = 0;
		c_ALU_result2 =0;
		c_MEM_result = 0;
		c_FLAG_zero = false;
		op = "null";
		math_op_flag = false;
		updateReg = true;
		ALU_done = false;
		zeroFlagForwardBack = false;
		stalledFlag = false;
		
		/*physical register part*/
		IQ_status_allocated = false;
		FU_type_needed = null;
		IQ_src1_ready = false;  
		IQ_src1_addr = 40;
		IQ_src1_data = -1;
		IQ_src2_ready = false; 
		IQ_src2_addr = 40;
		IQ_src2_data = -1;
		IQ_dest_ready = false;
		IQ_dest_addr = 40;
		IQ_dest_data = -1;
		dispatchTime = 0;
		/*physical register part end*/
		
		/*IQ part slot*/
		slotFree = true;
		slotNumber = 40;
		wakeUp = false;
		/*IQ part end*/
		
		/*ROB*/
		ROB_phy_dest_addr =  40;
		ROB_ach_dest_addr = 40;
		ROB_phy_dest_ready =  false;
		ROB_excode_flag = false;
		ROB_pre_dest_addr = 40;
		
		/*LSQ part slot*/
		LSQ_memory_addr = 40;
		LSQ_slot_num = 40;
		LSQ_slot_op = null;
		LSQ_ready = false;
		/*LSQ part end*/
		
		
		
	}
	
	public void passStageValue(StageStatusValue next) {
		
		next.c_instruction = c_instruction;
		next.c_instr_number = c_instr_number;
		next.c_PC = c_PC;
		next.c_src1_data = c_src1_data;
		next.c_src2_data = c_src2_data;
		next.c_dest_data = c_dest_data;
		next.c_target_memory_data = c_target_memory_data;
		next.c_literal = c_literal;
		next.c_src1_addr = c_src1_addr;
		next.c_src2_addr = c_src2_addr;
		next.c_dest_addr = c_dest_addr;
		next.c_target_memory_addr = c_target_memory_addr;
		next.c_ALU_result = c_ALU_result;
		next.c_ALU_result2 = c_ALU_result2;
		next.c_MEM_result = c_MEM_result;
		next.c_FLAG_zero = c_FLAG_zero;
		next.op = op;
		next.math_op_flag = math_op_flag;
		next.updateReg = updateReg;
		next.ALU_done = ALU_done;
		next.zeroFlagForwardBack = zeroFlagForwardBack;
		//stalledFlag no need to pass
		
		/*physical register part*/
		next.IQ_status_allocated = IQ_status_allocated;
		next.FU_type_needed = FU_type_needed;
		next.IQ_src1_ready = IQ_src1_ready;  
		next.IQ_src1_addr = IQ_src1_addr;
		next.IQ_src1_data = IQ_src1_data;
		next.IQ_src2_ready = IQ_src2_ready; 
		next.IQ_src2_addr = IQ_src2_addr;
		next.IQ_src2_data = IQ_src2_data;
		next.IQ_dest_ready = IQ_dest_ready;
		next.IQ_dest_addr = IQ_dest_addr;
		next.IQ_dest_data = IQ_dest_data;
		next.dispatchTime = dispatchTime;
		/*physical register part end*/
		
		/*IQ part slot*/
		next.slotFree = slotFree;
		next.slotNumber = slotNumber;
		next.wakeUp = wakeUp;
		/*IQ part end*/
	}

	
	
	public void cleanStageValue() {
		c_instruction = null;
		c_instr_number = 0;
		c_PC = 0;
		c_src1_data = -1;
		c_src2_data = -1;
		c_dest_data = -1;
		c_target_memory_data = 0;
		c_literal = 0;
		c_src1_addr = 20;
		c_src2_addr = 20;
		c_dest_addr = 20;
		c_target_memory_addr = 5000;
		c_target_memory_addr_ready = false;
		c_free = true;
		c_ALU_result = 0;
		c_ALU_result2 =0;
		c_MEM_result = 0;
		c_FLAG_zero = false;
		op = "null";
		math_op_flag = false;
		updateReg = true;
		ALU_done = false;
		zeroFlagForwardBack = false;
		stalledFlag = false;
		
		/*physical register part*/
		IQ_status_allocated = false;//indicates if this IQ entry is allocated or free, true is allocated, false is free
		FU_type_needed = null;
		IQ_src1_ready = false;  
		IQ_src1_addr = 40;//physical register address
		IQ_src1_data = -1;//physical register data
		IQ_src2_ready = false; 
		IQ_src2_addr = 40;
		IQ_src2_data = -1;
		IQ_dest_ready = false;
		IQ_dest_addr = 40;
		IQ_dest_data = -1;
		dispatchTime = 0;
		/*physical register part end*/	
		
		/*IQ part slot*/
		slotFree = true;
		slotNumber = 40;
		wakeUp = false;
		/*IQ part end*/
		
		/*ROB*/
		ROB_phy_dest_addr =  40;
		ROB_ach_dest_addr = 40;
		ROB_phy_dest_ready =  false;
		ROB_excode_flag = false;
		ROB_pre_dest_addr = 40;
		
		/*LSQ part slot*/
		LSQ_memory_addr = 40;
		LSQ_slot_num = 40;
		LSQ_slot_op = null;
		LSQ_ready = false;
		/*LSQ part end*/
	}
}
