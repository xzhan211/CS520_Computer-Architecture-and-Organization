package simProj;

public class registerFlag {
	public boolean bitValue;
	public boolean bitStatusBusy;
	public int lastInstrNo;
	registerFlag(){
		bitValue = false; // true means "equal 0", false means "is not 0", 
		bitStatusBusy = true; // busy true, free false
		lastInstrNo = 0;
	}
    public void cleanFlag() {
    		bitValue = false;
		bitStatusBusy = true;
		lastInstrNo = 0;
    }
}
