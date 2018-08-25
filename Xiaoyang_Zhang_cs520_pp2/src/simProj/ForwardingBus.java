package simProj;

public class ForwardingBus {
	
	public int value;
	public int address;
	public boolean PSWflag;
	public boolean busDataReady;
	public boolean flagReady;
	public int instrNo;
	
	public ForwardingBus() {
		value = 0;
		address = 20;
		PSWflag = false;
		busDataReady = false;
		flagReady = false;
		instrNo = 0;
	}
	
	public void cleanForwardingBus() {
		value = 0;
		address = 20;
		PSWflag = false;
		busDataReady = false;
		flagReady = false;
		instrNo = 0;
	}

}
