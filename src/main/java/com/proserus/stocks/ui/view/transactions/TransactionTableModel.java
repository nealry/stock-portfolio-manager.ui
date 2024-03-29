package com.proserus.stocks.ui.view.transactions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.StringUtils;

import com.proserus.stocks.bo.symbols.Symbol;
import com.proserus.stocks.bo.transactions.Label;
import com.proserus.stocks.bo.transactions.Transaction;
import com.proserus.stocks.bo.transactions.TransactionType;
import com.proserus.stocks.bo.utils.BigDecimalUtils;
import com.proserus.stocks.ui.controller.PortfolioController;
import com.proserus.stocks.ui.controller.ViewControllers;

public class TransactionTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 20080113L;

	private PortfolioController transactionController = ViewControllers.getController();

	public static final String[] COLUMN_NAMES = { "Date", "Symbol", "type", "Quantity", "Price", "Commission", "total", "Tags" };

	// Arrays of Contractors (kept as Object)
	private Object[] data = null;

	/**
	 * Default constructor to create the Table Model for the contractor table.
	 */
	public TransactionTableModel() {

	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col < (getColumnCount() - 2)) {
			return true;
		}
		return false;
	}

	/**
	 * Replace the list of contractors to be displayed in the table.
	 * 
	 * @param data
	 *            Array of contractors (as Object).
	 * @see Contractor
	 */
	public void setData(final Object[] data) {
		this.data = data;
		fireTableDataChanged();
	}

	/**
	 * Retrieves the number of column in the table.
	 */
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	/**
	 * Retrieves the number of row in the table.
	 */
	public int getRowCount() {
		if (data != null) {
			return data.length;
		} else {
			return 0;
		}
	}

	/**
	 * Retrieves the name of a specific column in the table.
	 * 
	 * @param columnIndex
	 *            The column number requested.
	 * @return The name to be displayed for that column.
	 */
	@Override
	public String getColumnName(final int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}

	public Transaction getTransaction(final int rowIndex) {
		return (Transaction) data[rowIndex];
	}

	/**
	 * Retrieves the value of a specific cell in the table.
	 * 
	 * @param rowIndex
	 *            The row number of the field requested.
	 * @param columnIndex
	 *            The column number of the field requested.
	 * @return The coressponding field of the contracotr at the specified row
	 *         and column.
	 * @see Contractor
	 */
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return (getColValue((Transaction) data[rowIndex], columnIndex));
	}

	@Override
	public void setValueAt(Object obj, int row, int col) {
		// TODO NullPointer when no data...
		setColValue((Transaction) data[row], obj, col);
		fireTableCellUpdated(row, col);
	}

	/**
	 * Invoked when the table is displayed, to identify the value type
	 */
	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return Calendar.class;
		case 1:
			return Symbol.class;
		case 2:
			return TransactionType.class;
		case 3:
		case 4:
		case 5:
		case 6:
			return BigDecimal.class;
		case 7:
			return String.class;
		}
		throw new AssertionError();
	}

	private Object getColValue(final Transaction transaction, final int column) {
		switch (column) {
		case 0:
			return transaction.getCalendar();
		case 1:
			return transaction.getSymbol();
		case 2:
			return transaction.getType();
		case 3:
			return transaction.getQuantity();
		case 4:
			return transaction.getPrice();
		case 5:
			return BigDecimalUtils.setDecimalWithScale(transaction.getCommission());
		case 6:
			return transaction.getPrice().multiply(transaction.getQuantity()).add(transaction.getCommission());
		case 7:
			List<Label> sortedLabels = new ArrayList<Label>(transaction.getLabelsValues());
			Collections.sort(sortedLabels);
			StringBuilder labelString = new StringBuilder();
			for (Label l : sortedLabels) {
				labelString.append(l.getName() + ", ");
			}

			return StringUtils.removeEnd(String.valueOf(labelString), ", ");
		}

		throw new AssertionError();
	}

	@SuppressWarnings("unchecked")
	private void setColValue(final Transaction t, Object value, int column) {
		int i = 0;
		if (column == i++) {
			t.setCalendar((Calendar) value);
		} else if (column == i++) {
			t.setSymbol((Symbol) value);
		} else if (column == i++) {
			t.setType((TransactionType) value);
		} else if (column == i++) {
			t.setQuantity((BigDecimal) value);
		} else if (column == i++) {
			t.setPrice((BigDecimal) value);
		} else if (column == i++) {
			t.setCommission((BigDecimal) value);
		} else if (column == i++) {

		} else if (column == i++) {
			t.setLabels((Collection<Label>) value);
		}
		transactionController.updateTransaction(t);
	}
}
