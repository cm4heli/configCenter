package cloudservice;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HbalseClentTest {

	public static void main(String  args[]) throws IOException {
		Configuration configuration = HBaseConfiguration.create();
		HConnection connection = HConnectionManager
				.createConnection(configuration);

		// HTableInterface table = connection.getTable("RK_GONGAJ_CZRK");
		 //getBykey(connection, table);
		 //scan(table);
		 
		 

	}

	private static void scan(HTableInterface table) throws IOException {
		Scan scan = new Scan();
		// scan.setFilter(new PageFilter(2L));
		ResultScanner rs = table.getScanner(scan);
		System.out.println("==============="+rs);
		for (Result r : rs) {
			System.out.println("-----------------------"+r);
		}
	}

	private static void getBykey(HConnection connection, HTableInterface table)
			throws IOException {
		Get get = new Get(Bytes.toBytes("320325196804088315"));
		Result result = table.get(get);
		for (Cell cell : result.rawCells()) {
			System.out.println("rowkey:" + new String(CellUtil.cloneRow(cell))
					+ " ");
			System.out.println("Timetamp:" + cell.getTimestamp() + " ");
			System.out.println("Family:"
					+ new String(CellUtil.cloneFamily(cell)) + " ");
			System.out.println("列名:"
					+ new String(CellUtil.cloneQualifier(cell)) + " ");
			System.out.println("值:" + new String(CellUtil.cloneValue(cell))
					+ " ");
			System.out.println("-----------------------------");
		}
		table.close();
		System.out
				.println("-----------==========================------------------");
		table = connection.getTable("RK_GONGAJ_ZZRK");

		get = new Get(Bytes.toBytes("110106197801244840"));
		result = table.get(get);
		for (Cell cell : result.rawCells()) {
			System.out.println("rowkey:" + new String(CellUtil.cloneRow(cell))
					+ " ");
			System.out.println("Timetamp:" + cell.getTimestamp() + " ");
			System.out.println("Family:"
					+ new String(CellUtil.cloneFamily(cell)) + " ");
			System.out.println("列名:"
					+ new String(CellUtil.cloneQualifier(cell)) + " ");
			System.out.println("值:" + new String(CellUtil.cloneValue(cell))
					+ " ");
			System.out.println("-----------------------------");
		}
		// HConnection connection =
		// HConnectionManager.createConnection(configuration);
	}

}
