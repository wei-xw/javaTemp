package javaTemp;
import java.io.File;
import java.io.IOException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
public class ReadExcel {
	public String readCell(int line,String str,String path){
		Workbook rwb = null;
		String str1 = null;
		try {
			rwb = Workbook.getWorkbook(new File(path));
			Sheet rs=rwb.getSheet(0);
		    int row=rs.getCell(str).getRow();
		    str1=rs.getCell(line, row).getContents();
		    
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rwb.close();
		return str1;
		
	}
	public static void readLine(){  //读取一列并去重，表格已排序
		String path ="D:\\back\\电警卡口信息.xls";
		int rows = 0;
		try {
			Workbook rwb=Workbook.getWorkbook(new File(path));
			Sheet rs=rwb.getSheet(0);
		    rows=rs.getRows();
		    String str1=rs.getCell(2, 1).getContents();	
		    int j=0;
		    for (int i = 2; i < rows; i++) {
		    	String str2=rs.getCell(2, i).getContents();
		    	if(str2.equals(str1))
		    		continue;    		
		    	else{
		    		System.out.println("贵阳市"+str1+","+rs.getCell(11, i-1).getContents()+","+rs.getCell(17, i-1).getContents());
		    		str1=str2;
		    		j++;
		    	}	
		    	
		}
		    System.out.println(j);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(rows);
	}
	public static void main(String[] args) throws IOException{
		
		readLine();
		
	}

}
