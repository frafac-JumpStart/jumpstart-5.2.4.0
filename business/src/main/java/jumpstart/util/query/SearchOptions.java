package jumpstart.util.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SearchOptions implements Serializable {

	private static final int DEFAULT_MAX_ROWS = 200;
	private int _maxRows = DEFAULT_MAX_ROWS;
	private List<String> _sortColumnNames;
	private List<Boolean> _sortAscendings;

	public SearchOptions() {
		_sortColumnNames = new ArrayList<String>();
		_sortAscendings = new ArrayList<Boolean>();
	}

	public SearchOptions(int maxRows) {
		_maxRows = maxRows;
		_sortColumnNames = new ArrayList<String>();
		_sortAscendings = new ArrayList<Boolean>();
	}

	public SearchOptions(int maxRows, String sortColumnName, boolean sortAscending) {
		_maxRows = maxRows;
		_sortColumnNames = new ArrayList<String>();
		_sortAscendings = new ArrayList<Boolean>();
		_sortColumnNames.add(sortColumnName);
		_sortAscendings.add(sortAscending);
	}

	public SearchOptions(String sortColumnName, boolean sortAscending) {
		_sortColumnNames = new ArrayList<String>();
		_sortAscendings = new ArrayList<Boolean>();
		_sortColumnNames.add(sortColumnName);
		_sortAscendings.add(sortAscending);
	}

	public SearchOptions(List<String> sortColumnNames, List<Boolean> sortAscendings) {
		this._sortAscendings = sortAscendings;
		this._sortColumnNames = sortColumnNames;
	}

	public SearchOptions(int maxRows, List<String> sortColumnNames, List<Boolean> sortAscendings) {
		_maxRows = maxRows;
		this._sortAscendings = sortAscendings;
		this._sortColumnNames = sortColumnNames;
	}

	public SearchOptions(List<String> sortColumnNames) {
		this._sortColumnNames = sortColumnNames;

		this._sortAscendings = new ArrayList<Boolean>();

		for (int i = 0; i < sortColumnNames.size(); i++) {
			this._sortAscendings.add(new Boolean(true));
		}
	}

	public int getMaxRows() {
		return _maxRows;
	}

	public void setMaxRows(int maxRows) {
		this._maxRows = maxRows;
	}

	public boolean isSortAscending() {
		return isSortAscending(0);
	}

	public boolean isSortAscending(int index) {
		boolean result = true;
		if (index < _sortAscendings.size())
			result = _sortAscendings.get(index).booleanValue();
		return result;
	}

	public void addSearchOptions(String sortColumnName, boolean sortAscending) {
		addSortAscending(sortAscending);
		addSortColumnName(sortColumnName);
	}

	public void addSortAscending(boolean sortAscending) {
		_sortAscendings.add(new Boolean(sortAscending));
	}

	public void setSortAscending(boolean sortAscending) {
		setSortAscending(0, sortAscending);
	}

	public void setSortAscending(int index, boolean sortAscending) {
		if (index < _sortAscendings.size())
			_sortAscendings.set(index, new Boolean(sortAscending));
		else
			addSortAscending(sortAscending);
	}

	public String getSortColumnName() {
		return getSortColumnName(0);
	}

	public String getSortColumnName(int index) {
		String result = "";
		if (index < _sortColumnNames.size())
			result = _sortColumnNames.get(index);
		return result;
	}

	private void addSortColumnName(String sortColumnName) {
		_sortColumnNames.add(sortColumnName);
	}

	public void setSortColumnNames(List<String> sortColumnNames) {
		this._sortColumnNames = sortColumnNames;

		// Default the direction to ascending if the directions are out of synch
		if (this._sortColumnNames.size() != this._sortAscendings.size()) {

			this._sortAscendings = new ArrayList<Boolean>();

			for (int i = 0; i < sortColumnNames.size(); i++) {
				this._sortAscendings.add(new Boolean(true));
			}
		}
	}

	public List<Boolean> getSortAscendings() {
		return _sortAscendings;
	}

	public List<String> getSortColumnNames() {
		return _sortColumnNames;
	}

}
