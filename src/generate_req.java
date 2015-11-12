import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.io.*;



public class generate_req {
	
	public static void main(String[] args) throws IOException{
		PrintWriter writer = new PrintWriter("testcase.txt", "UTF-8");
		for(int i=0;i<10000;i++)
		writer.println(generate_reqs());
		
		writer.close();
	}
	
	
	
	
	public static String generate_reqs(){
		
		Random core = new Random();
		int coreNum = core.nextInt(2);
		
		Random rand = new Random();
		int randomNum = rand.nextInt(32768);
		
		Random bul = new Random();
		int bulNum = bul.nextInt(2);
		
		String rw ;
		if(bulNum==1){
			rw = "W";
		}else{
			rw = "R";
		}
		
		return "P"+coreNum+" "+randomNum+" "+rw;
	}
	
}
