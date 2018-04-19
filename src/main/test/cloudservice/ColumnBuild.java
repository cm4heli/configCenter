package cloudservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ColumnBuild {
	private static String sqlFile = "res/column.txt";
	private static String jb = "G:";

	public static void main(String[] args) throws Exception {
		
		index();
	}
	
	public static void index()throws Exception {
		FileReader fr = new FileReader(sqlFile);
		BufferedReader br = new BufferedReader(fr);
		String content = null;
		StringBuffer sb = new StringBuffer();
		while ((content = br.readLine()) != null) {
			if (content != null && content.trim() != "") {
	            
				String as[] =content.split(",");
				for(String aa: as){
					sb.append("|").append(aa+":"+"50");
				}
			}

		}
		System.out.println(sb.toString());
		br.close();
		fr.close();
	}
	
	public static void hbase() throws IOException{
		FileReader fr = new FileReader(sqlFile);
		BufferedReader br = new BufferedReader(fr);
		String content = null;
		StringBuffer sb = new StringBuffer();
		while ((content = br.readLine()) != null) {
			if (content != null && content.trim() != "") {
				if (content.contains("_GZ")) {
					jb = "GZ:";
				} else if (content.endsWith("_CZ")) {
					jb = "CZ:";
				} else if (content.endsWith("_XX")) {
					jb = "XX:";
				}else if (content.endsWith("_YE")) {
					jb = "YE:";
				}
				if (sb.length() == 0) {
					sb.append(jb + content.split(" ")[0]);
				} else {
					sb.append("," + jb + content.split(" ")[0]);
				}
			}

		}
		System.out.println(sb.toString());
		br.close();
		fr.close();
	}

}
