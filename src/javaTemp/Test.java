package javaTemp;

import java.io.IOException;

public class Test {
	public static void stringSplit(){
		String a="aaa,sss,fff,ggg,kkk";
		String [] b=a.split(",");
		System.out.println(b.length);
	}
	public static void main(String[] args) throws IOException{
		stringSplit();
		
	}

}
