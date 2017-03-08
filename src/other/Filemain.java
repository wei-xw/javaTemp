package other;


import java.io.*;

public class Filemain {
	public static void readFile1(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	 
		br.close();
	}
	 public static void write(String path, String content, String encoding)  
	            throws IOException {  
	        File file = new File(path);  
	       
	      //  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(  
	          //      new FileOutputStream(file), encoding));  
	        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(
	        		new FileOutputStream(file), encoding));
	        writer.write(content);  
	        writer.close();  
	    }  
	  
	    public static String read(String path, String encoding) throws IOException {  
	        String content = "";  
	        File file = new File(path);  
	        BufferedReader reader = new BufferedReader(new InputStreamReader(  
	                new FileInputStream(file), encoding));  
	        String line = null;  
	        while ((line = reader.readLine()) != null) {  
	            content += line + "\n";  
	        }  
	        reader.close();  
	        return content;  
	    }  
	  
	    
	        
	public static void main(String[] args) throws IOException{
		File file =new File("../option");
		readFile1(file);
		System.out.println(file.getCanonicalPath());
		String content = "ÖÐÎÄÄÚÈÝfsdvsvsff";  
        String path = "d:/test.txt";  
       
        write(path, content,"utf-8" );  
        System.out.println(read(path, "utf-8"));  
   
}

}
