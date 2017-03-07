package javaTemp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PhantojsEcharts {
	 public static void getAjaxCotnent(String url) throws IOException {   
	        Runtime rt = Runtime.getRuntime();   
	        Process p = rt.exec("phantomjs d:/workSpace/code.js "+url);
	        InputStream is = p.getInputStream();   
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));   
	        StringBuffer sbf = new StringBuffer();   
	        String tmp = "";   
	        while((tmp = br.readLine())!=null){   
	            sbf.append(tmp);   
	        }   
	        System.out.println(sbf.toString());   
	        //return sbf.toString();   
	    }   
	   
	    public static void main(String[] args) throws IOException {   
	       //getAjaxCotnent("http://www.oicqzone.com");// 
	       Runtime rt = Runtime.getRuntime();   
	        Process p = rt.exec("phantomjs ../test.js -infile ../option -datafile ../data -outfile d:/t.png");
	        InputStream is = p.getInputStream();   
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));   
	        StringBuffer sbf = new StringBuffer();   
	        String tmp = "";   
	        while((tmp = br.readLine())!=null){   
	            sbf.append(tmp);   
	        }   
	        System.out.println(sbf.toString()); 
	    }   

}
