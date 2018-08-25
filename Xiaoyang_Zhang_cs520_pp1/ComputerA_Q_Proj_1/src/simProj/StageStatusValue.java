package simProj;

public class StageStatusValue {
	
	public String c_instruction;
	public int c_instr_number;
	public int c_PC;
	public int c_src1_data;
	public int c_src2_data;
	public int c_dest_data;
	public int c_target_memory_data;
	public int c_literal;
	public int c_src1_addr;
	public int c_src2_addr;
	public int c_dest_addr;
	public int c_target_memory_addr;
	public boolean c_stalled;
	public boolean c_free;
	public int c_ALU_result;
	public int c_MEM_result;
	public boolean c_FLAG_zero;
	public String op;
	public boolean math_op_flag;
	
	
	/*initialization*/
	StageStatusValue() {
		c_instruction = null;
		c_instr_number = 0;
		c_PC = 0;
		c_src1_data = 20;
		c_src2_data = 20;;
		c_dest_data = 20;
		c_target_memory_data = 0;
		c_literal = 0;
		c_src1_addr = 0;
		c_src2_addr = 0;
		c_dest_addr = 0;
		c_target_memory_addr = 5000;
		c_stalled = false;
		c_free = true;
		c_ALU_result = 0;
		c_MEM_result = 0;
		c_FLAG_zero = false;
		op = null;
		math_op_flag = false;
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
		next.c_MEM_result = c_MEM_result;
		next.c_FLAG_zero = c_FLAG_zero;
		next.op = op;
		next.math_op_flag = math_op_flag;
	}

	public void displayStageValue(String stagename) {

		System.out.println("********************************************************");
		System.out.println("Stage >> "+ stagename);
		System.out.println("Current_instruction >> "+ c_instruction );
		System.out.println("Current_instr_number >> "+ c_instr_number);
		System.out.println("Current_PC >> "+ c_PC);
		System.out.println("Current_src1_data >> "+ c_src1_data);
		System.out.println("Current_src2_data >> "+ c_src2_data);
		System.out.println("Current_dest_data >> "+ c_dest_data);
		System.out.println("Current_target_memory_data >> "+ c_target_memory_data);
		System.out.println("Current_literal >> "+ c_literal);
		if(c_src1_addr >= 18) 
			System.out.println("Current_src1_addr >> null");
		else
			System.out.println("Current_src1_addr >> R"+ c_src1_addr);
		if(c_src2_addr >= 18) 
			System.out.println("Current_src2_addr >> null");
		else
			System.out.println("Current_src2_addr >> R"+ c_src2_addr);
		if(c_dest_addr >= 18) 
			System.out.println("Current_dest_addr >> null");
		else
			System.out.println("Current_dest_addr >> R"+ c_dest_addr);
		if(c_target_memory_addr >= 4000) 
			System.out.println("Current_target_memory_addr >> null");
		else
			System.out.println("Current_target_memory_addr >> "+ c_target_memory_addr); 
		System.out.println("Current_stalled >> "+ c_stalled);
		System.out.println("Current_free >> "+ c_free);
		System.out.println("Current_ALU_result >> "+ c_ALU_result);
		System.out.println("Current_MEM_result >> "+ c_MEM_result);
		System.out.println("Current_Flag_zero >> "+ c_FLAG_zero);
		System.out.println("********************************************************");	
	}
	
	
	
	public void cleanStageValue() {
		c_instruction = null;
		c_instr_number = 0;
		c_PC = 0;
		c_src1_data = 0;
		c_src2_data = 0;
		c_dest_data = 0;
		c_target_memory_data = 0;
		c_literal = 0;
		c_src1_addr = 20;
		c_src2_addr = 20;
		c_dest_addr = 20;
		c_target_memory_addr = 5000;
		c_stalled = false;
		c_free = true;
		c_ALU_result = 0;
		c_MEM_result = 0;
		c_FLAG_zero = false;
		op = null;
		math_op_flag = false;
	}
	
	public void nullStageValue() {
		
		c_instruction = "Null,now";
		c_instr_number = 0;
		c_PC = 0;
		c_src1_data = 0;
		c_src2_data = 0;
		c_dest_data = 0;
		c_target_memory_data = 0;
		c_literal = 0;
		c_src1_addr = 20;
		c_src2_addr = 20;
		c_dest_addr = 20;
		c_target_memory_addr = 5000;
		c_stalled = false;
		c_free = true;
		c_ALU_result = 0;
		c_MEM_result = 0;
		c_FLAG_zero = false;
		op = null;
		math_op_flag = false;
	}
	
}
