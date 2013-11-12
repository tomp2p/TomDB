package uzh.tomdb.db.operations;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uzh.tomdb.api.ResultSet;
import uzh.tomdb.db.TableColumns;
import uzh.tomdb.db.TableIndexes;
import uzh.tomdb.db.TableRows;
import uzh.tomdb.db.operations.engines.QueryExecuter;
import uzh.tomdb.db.operations.helpers.Row;
import uzh.tomdb.db.operations.helpers.WhereCondition;
import uzh.tomdb.p2p.DBPeer;
import uzh.tomdb.parser.MalformedSQLQuery;

/**
 * 
 * @author Francesco Luminati
 */
public class Select extends Operation implements Operations {
	private final Logger logger = LoggerFactory.getLogger(Select.class);
	private List<String> columns;
	private List<WhereCondition> whereConditions;
	private boolean allColumns = false;
	private ResultSet resultSet;
	private String scanType;

	public Select(boolean allColumns, String tabName, List<String> columns, List<WhereCondition> conditions, String scanType) {
		super();
		super.tabName = tabName;
		super.tabKey = Number160.createHash(tabName);
		this.allColumns = allColumns;
		this.columns = columns;
		this.whereConditions = conditions;
		this.scanType = scanType;
	}

	@Override
	public void init() {
		Map<Number160, Data> tabColumns = DBPeer.getTabColumns();
		Map<Number160, Data> tabRows = DBPeer.getTabRows();
		Map<Number160, Data> tabIndexes = DBPeer.getTabIndexes();

		try {
			tc = (TableColumns) tabColumns.get(tabKey).getObject();
			tr = (TableRows) tabRows.get(tabKey).getObject();
			ti =  (TableIndexes) tabIndexes.get(tabKey).getObject();

			resultSet = new ResultSet();
			resultSet.setColumns(tc.getColumns());

			new QueryExecuter(this);

		} catch (ClassNotFoundException | IOException e) {
			logger.error("Data error", e);
		} catch (MalformedSQLQuery e) {
			logger.error("SQL error", e);
		}
	}

	
	public void addToResultSet(Row row) {
		resultSet.addRow(row);
	}

	public ResultSet getResultSet() {
		new Thread(resultSet).start();
		return resultSet;
	}

	public List<String> getColumns() {
		return columns;
	}

	public List<WhereCondition> getWhereConditions() {
		return whereConditions;
	}
	
	public String getScanType() {
		return scanType;
	}
	
	public boolean isAllColumns() {
		return allColumns;
	}

	@Override
	public String toString() {
		return super.toString()+" Select{" + "tabName=" + tabName + ", columns=" + columns
				+ ", whereOperations=" + whereConditions + ", allColumns="
				+ allColumns + '}';
	}

}