package simProj;

public class RegisterFile {
	public RegisterFile(int i) {
		address = i;
		value = 0;
		valueReady = true;
	}
	
	public void cleanRegister() {
		value = 0;
		valueReady = true;
	}
	
	public boolean valueReady; 
	public int value;
	public int address; //0~15
}

