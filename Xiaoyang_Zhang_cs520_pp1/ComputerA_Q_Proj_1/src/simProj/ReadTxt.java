package simProj;

import java.io.*;

public class ReadTxt {
    /**
     * Read txt file
     * @param file path/name
     * @return file content
     */
	public static String[] instArr = new String[200];
	public static int arrlength;
	
	
	public ReadTxt() {
		arrlength = 0;
		for(int i = 0; i<200; i++) {
			instArr[i] = null;
		}
	}
	
	
    public static void txt2String(File file){
        StringBuilder result = new StringBuilder();
        String combineLine = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            /*readLine，once per line */
            while((combineLine = br.readLine())!=null){
            	/*remove the " " from the beginning and end of per string element.*/
            		combineLine = combineLine.trim();
            	/*combined the string, use " " as a interval*/
                result.append(combineLine+"$"); 
            }
            /*close io stream*/
            br.close();                       
        }catch(Exception e){
            e.printStackTrace();
        }
        /* remove the  "," from string. */
        String str = result.toString();
        //String newstr = result.toString().replaceAll(",","");
        //newstr = newstr.replaceAll("R", "");
        //newstr = newstr.replaceAll("#", "");
        /*split the string by "&", store in stArr[], each element in stArr is stored a instruction.*/
        /*if use "$" as split sign, we should use "\\$"*/
        //instArr = newstr.split("\\$"); 
        instArr = str.split("\\$");
        /*Debug information*/
        
        arrlength = instArr.length;
        /*
        System.out.println(arrlength);
        for(int i = 0; i < arrlength; i++){
            System.out.println(instArr[i]);
        }
        
        */         
    } 
}
