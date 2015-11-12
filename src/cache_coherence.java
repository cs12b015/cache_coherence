import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class cache_coherence {
	public static int modnumb;
	public static int shrnumb;
	public static int invnumb;
	public static int busnumb;
	
	public static ArrayList<Integer> cache1 = new ArrayList<Integer>();
	public static ArrayList<Integer> cache2 = new ArrayList<Integer>();
	public static ArrayList<Integer> memory = new ArrayList<Integer>();
	public static ArrayList<Integer> state_cache1 = new ArrayList<Integer>();
	public static ArrayList<Integer> state_cache2 = new ArrayList<Integer>();
	
	public static HashMap <Integer,ArrayList<Integer>> cache = new HashMap <Integer,ArrayList<Integer>>();
	public static HashMap <Integer,ArrayList<Integer>> state_cache = new HashMap <Integer,ArrayList<Integer>>();
	
	public static void Init(){
		for(int i=0;i<32;i++ ){
			state_cache1.add(2);
			state_cache2.add(2);
			cache2.add(-1);
			cache1.add(-1);
		}		
		for(int i=0;i<1024;i++ ){
			memory.add(-1);
		}
		cache.put(0, cache1);
		cache.put(1, cache2);
		state_cache.put(0, state_cache1);
		state_cache.put(1, state_cache2);
		modnumb=0;
		shrnumb=0;
		invnumb=0;
		busnumb=0;
	}
	
	public static void printresults(){
		System.out.println("Modified transactions          "+modnumb);
		System.out.println(" Shared  transactions          "+shrnumb);
		System.out.println("invalid  transactions          "+invnumb);
		System.out.println(" no of times we placed on bus  "+busnumb);
	}
	
	public static void main(String[] args) throws IOException{
		Init();
		List<String> lines = Files.readAllLines(Paths.get("testcase.txt"), StandardCharsets.UTF_8);
		for(int i=0;i<lines.size();i++){
			domsi(lines.get(i));
		}
		System.out.println("**************** Results  for MSI Protocol *****************");
		printresults();
		Init();
		for(int i=0;i<lines.size();i++){
			domesi(lines.get(i));
		}
		System.out.println("**************** Results  for MESI Protocol *****************");
		printresults();
	}

	
	public static void checkbus(int cachenum,int cacheindex,int memindex,int rw){		
		if(cache.get(cachenum).get(cacheindex).equals(memindex)){
			if(state_cache.get(cachenum).get(cacheindex).equals(1) && rw ==1 ){
				state_cache.get(cachenum).set(cacheindex,2);
				invnumb++;
			}else if(state_cache.get(cachenum).get(cacheindex).equals(0) && rw ==1 ){
				state_cache.get(cachenum).set(cacheindex,2);
				invnumb++;
			}else if(state_cache.get(cachenum).get(cacheindex).equals(0) && rw ==0 ){
				state_cache.get(cachenum).set(cacheindex,1);
				shrnumb++;
			}
		}	
	}
	
	public static void domsi (String instruction){
		String[] array = instruction.split(" ");
		int cachenum = Integer.parseInt(array[0].substring(1));
		int invcachenum;
		if(cachenum == 0){
			invcachenum=1;
		}else{
			invcachenum=0;
		}
		int address = Integer.parseInt(array[1]);
		int rw;
		if(array[2].equals("R")){
			rw= 0;
		}else{
			rw = 1;
		}
		
		int cacheindex = address/1024;
		int memindex = address/32;
		int cachehit;
		if(cache.get(cachenum).get(cacheindex).equals(memindex)){
			cachehit = 1;
		}else{
			cachehit = 0;
		}
				
		if(cachehit==0){//cache miss
			busnumb++;
			cache.get(cachenum).set(cacheindex, memindex);
			if(state_cache.get(cachenum).get(cacheindex).equals(2)){//invalid
				if(rw==0){//read
					state_cache.get(cachenum).set(cacheindex,1);
					shrnumb++;
				}else {//write
					state_cache.get(cachenum).set(cacheindex,0);
					modnumb++;
				}	
			}else if(state_cache.get(cachenum).get(cacheindex).equals(1)){//shared
				if(rw==0){//read
					state_cache.get(cachenum).set(cacheindex,1);			
				}else {//write
					state_cache.get(cachenum).set(cacheindex,0);
					modnumb++;
				}
			}else if(state_cache.get(cachenum).get(cacheindex).equals(0)){//modify
				if(rw==0){//read
					state_cache.get(cachenum).set(cacheindex,1);	
					shrnumb++;
				}else {//write
					state_cache.get(cachenum).set(cacheindex,0);
				}
			}
			
			checkbus(invcachenum,cacheindex,memindex,rw);
		}
		else{//cache hit			
			if(state_cache.get(cachenum).get(cacheindex).equals(1)){//shared
				if(rw==0){//read
					state_cache.get(cachenum).set(cacheindex,1);			
				}else {//write
					state_cache.get(cachenum).set(cacheindex,0);
					modnumb++;
					//invalidate bus
					if(cache.get(invcachenum).get(cacheindex).equals(memindex)){
						state_cache.get(invcachenum).set(cacheindex,2);
						invnumb++;
					}			
					busnumb++;
				}
			}	
		}		
	}	
	
	public static void domesi (String instruction){
		String[] array = instruction.split(" ");
		int cachenum = Integer.parseInt(array[0].substring(1));
		int invcachenum;
		if(cachenum == 0){
			invcachenum=1;
		}else{
			invcachenum=0;
		}
		int address = Integer.parseInt(array[1]);
		int rw;
		if(array[2].equals("R")){
			rw= 0;
		}else{
			rw = 1;
		}
		
		int cacheindex = address/1024;
		int memindex = address/32;
		int cachehit;
		if(cache.get(cachenum).get(cacheindex).equals(memindex)){
			cachehit = 1;
		}else{
			cachehit = 0;
		}
		
		if(cachehit == 0){//cache miss
			if(state_cache.get(cachenum).get(cacheindex).equals(0)){
				
			}else if(state_cache.get(cachenum).get(cacheindex).equals(1)){
				
			}else if(state_cache.get(cachenum).get(cacheindex).equals(2)){
				
			}else if(state_cache.get(cachenum).get(cacheindex).equals(3)){
				
			}
			
		}else{//cache hit
			if(state_cache.get(cachenum).get(cacheindex).equals(0)){
				
			}else if(state_cache.get(cachenum).get(cacheindex).equals(1)){
				
			}else if(state_cache.get(cachenum).get(cacheindex).equals(2)){
				
			}else if(state_cache.get(cachenum).get(cacheindex).equals(3)){
				
			}
		}
					
	}
}
