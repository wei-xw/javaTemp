package javaTemp;
import java.io.BufferedReader;
import java.io.File;
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
	   
	    public static void main(String[] args) throws IOException, InterruptedException {   
	       //getAjaxCotnent("http://www.oicqzone.com");// 
	    	
	       Runtime rt = Runtime.getRuntime();   
	        Process p = rt.exec("phantomjs "+new File("echartsPhantoms/test.js").getCanonicalPath()+
		            " -infile "+new File("echartsPhantoms/option").getCanonicalPath()+
		            " -datafile "+new File("echartsPhantoms/data").getCanonicalPath()+
		            " -outfile d:/t.png");
	        InputStream is = p.getInputStream();   
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));   
	        StringBuffer sbf = new StringBuffer();   
	        String tmp = "";   
	        while((tmp = br.readLine())!=null){   
	            sbf.append(tmp);   
	        }   
	        System.out.println(sbf.toString()); 
	       int i= p.waitFor();//p执行是否正常，正常返回0
	       System.out.println(i);
	        p = rt.exec("ping www.baidu.com");
	        is = p.getInputStream();   
	       br = new BufferedReader(new InputStreamReader(is));   
	        sbf = new StringBuffer();   
	       tmp = "";   
	        while((tmp = br.readLine())!=null){   
	        	 System.out.println(tmp);
	        }   
	        i= p.waitFor();
		       System.out.println(i);
	        
	    }   

}
