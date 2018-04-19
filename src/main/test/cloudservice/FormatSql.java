package cloudservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;

public class FormatSql {
	public static void main(String[] args) throws IOException {
		Configuration configuration = HBaseConfiguration.create();
		HConnection connection = HConnectionManager
				.createConnection(configuration);
		System.out.println(connection);

	}

	private static String formatSQL(String sql) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sql.length(); i++) {
			String c = String.valueOf(sql.charAt(i));
			if (sb.length() == 0 && !" ".equals(c)) {
				sb.append(c);
			} else {
				if (" ".equals(c) && sb.toString().endsWith(" ")) {
					 
				} 
				 else {
					sb.append(c);
				}

			}

		}
		return sb.toString();
	}

	private static String replaceNull(String sql) {
		if (sql.contains("  ")) {
			sql = sql.replace("  ", " ");
			replaceNull(sql);
		}
		return sql;
	}

	private static String readFromFile() throws FileNotFoundException,
			IOException {
		StringBuffer sb = new StringBuffer();
		FileReader reader = new FileReader("G://a.sql");
		BufferedReader br = new BufferedReader(reader);
		String str = null;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		br.close();
		reader.close();
		return sb.toString();
	}

}
