package com.ma.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * @author marlboro.chu@gmail.com
 **/
public class DBUtil {

	private static DBUtil instance;

	public synchronized static DBUtil getInstance() {
		if (instance == null)
			instance = new DBUtil();
		return instance;
	}

	public ArrayList<Map> convertResult(ResultSet rs) throws Exception {

		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		ArrayList<Map> result = new ArrayList<Map>();
		while (rs.next()) {
			
			HashMap cursor = new HashMap();
			
			for (int x = 1; x <= columns; x++) {
				String colName = rsmd.getColumnName(x);
				int colType = rsmd.getColumnType(x);
				switch (colType) {
				case java.sql.Types.VARCHAR:
					cursor.put(colName.toUpperCase(), rs.getString(colName));
					break;
				case java.sql.Types.LONGNVARCHAR:
					cursor.put(colName.toUpperCase(), rs.getString(colName));
					break;
				case java.sql.Types.DATE:
					cursor.put(colName.toUpperCase(), rs.getTimestamp(colName));
					break;
				case java.sql.Types.TIMESTAMP:
					cursor.put(colName.toUpperCase(), rs.getTimestamp(colName));
					break;
				case java.sql.Types.NUMERIC:
					cursor.put(colName.toUpperCase(), rs.getBigDecimal(colName));
					break;
				case java.sql.Types.INTEGER:
					cursor.put(colName.toUpperCase(), rs.getBigDecimal(colName));
					break;	
				default:
					cursor.put(colName.toUpperCase(), rs.getObject(colName));
					break;
				}
			}
			result.add(cursor);
			
		}
		return result;
	}

}
