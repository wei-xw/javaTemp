package javaTemp;

import java.io.File;
import java.io.IOException;

import other.Filemain;

public class Test {
	
	public static void stringSplit(){
		String a="aaa,sss,fff,ggg,kkk";
		String [] b=a.split(",");
		System.out.println(b.length);
	}
	public static void main(String[] args) throws IOException{
		stringSplit();
		File file=new File("echartsPhantoms/a.txt");
				System.out.println(file.getCanonicalPath());
				Filemain.readFile1(file);
				
		
		
	}

}
