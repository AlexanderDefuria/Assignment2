import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class provides an implementation of the DataReader interface for CSV
 * files
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class CSVReader implements DataReader {
	// WRITE YOUR CODE HERE!
	/**
	 * The delimiter that separates attribute names and attribute values
	 */
	private static final char DELIMITER = ',';

	private String filePath;

	/**
	 * Character allowing escape sequences containing the delimiter
	 */
	private static final char QUOTE_MARK = '\'';

	/**
	 * Instance variable for storing the number of attributes (columns)
	 */
	private int numColumns;

	/**
	 * Instance variable for storing the number of datapoints (data rows)
	 */
	private int numRows;

	/**
	 * Instance variable for storing attribute names
	 */
	private String[] attributeNames;

	/**
	 * Instance variable for storing datapoints
	 */
	private String[][] matrix;
	
	/**
	 * Constructs a dataset by loading a CSV file
	 * 
	 * @param filePath is the name of the file
	 */
	public CSVReader(String filePath) throws Exception {
		this.filePath = filePath;
		this.matrix = this.getData();
		this.attributeNames = this.getAttributeNames();
	}


	public String[] getAttributeNames() {
		return this.attributeNames;
	}

	public String[][] getData() {
		Scanner scanner = null;
		Scanner lineCounter = null;
		try {
			scanner = new Scanner(new File(filePath));
			lineCounter = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		boolean firstLine = true;

		int rowNum = 0;

		while (scanner.hasNext()) {
			String str = scanner.nextLine();


			if (!str.trim().isEmpty()) {

				if (firstLine) {
					firstLine = false;

					populateAttributeNames(str);

					int numLines = 0;
					while (true) {
						assert lineCounter != null;
						if (!lineCounter.hasNextLine()) break;
						lineCounter.nextLine();
						numLines++;
					} lineCounter.close();


					this.matrix = new String[numLines][attributeNames.length];

				} else {
					populateRow(str, rowNum);
					rowNum++;
				}
			}
		}

		scanner.close();
		numRows = rowNum;
		return matrix;
	}

	private void populateAttributeNames(String str) {

		if (str == null || str.isEmpty()) {
			return;
		}

		StringBuffer buffer = new StringBuffer();

		boolean isInQuote = false;

		int position = 0;

		char[] chars = str.toCharArray();
		char ch;

		int len = 0;

		for (int i = 0; i < chars.length; i++) {
			ch = chars[i];

			if (isInQuote) {
				if (ch == QUOTE_MARK) {
					isInQuote = false;
				} else {
					buffer.append(ch);
				}

			} else if (ch == QUOTE_MARK) {
				isInQuote = true;
			} else if (ch == DELIMITER) {
				len++;
			}
		}

		this.attributeNames = new String[len+1];

		for (int i = 0; i < chars.length; i++) {

			ch = chars[i];

			if (isInQuote) {
				if (ch == QUOTE_MARK) {
					isInQuote = false;
				} else {
					buffer.append(ch);
				}

			} else if (ch == QUOTE_MARK) {
				isInQuote = true;
			} else if (ch == DELIMITER) {
				attributeNames[position++] = buffer.toString().trim();
				buffer.delete(0, buffer.length());
			} else {
				buffer.append(ch);
			}
		}

		if (buffer.toString().trim().length() > 0) { // deal with last attribute name
			attributeNames[position++] = buffer.toString().trim();
		}
		numColumns = buffer.length();
	}

	private void populateRow(String str, int currentRow) {

		if (str == null || str.isEmpty()) {
			return;
		}

		StringBuffer buffer = new StringBuffer();

		boolean isInQuote = false;

		int position = 0;

		char[] chars = str.toCharArray();
		char ch;

		for (int i = 0; i < chars.length; i++) {

			ch = chars[i];

			if (isInQuote) {
				if (ch == QUOTE_MARK) {
					isInQuote = false;
				} else {
					buffer.append(ch);
				}

			} else if (ch == QUOTE_MARK) {
				isInQuote = true;
			} else if (ch == DELIMITER) {
				matrix[currentRow][position++] = buffer.toString().trim();
				buffer.delete(0, buffer.length());
			} else {
				buffer.append(ch);
			}
		}

		if (buffer.toString().trim().length() > 0) { // deal with last attribute value
			matrix[currentRow][position++] = buffer.toString().trim();
		} else if (chars[chars.length - 1] == ',') {// deal with potentially missing last attribute value
			matrix[currentRow][position++] = "";
		}

	}

	public String getSourceId() {
		return filePath;
	}

	public int getNumberOfColumns() {
		return numColumns;
	}

	public int getNumberOfDataRows() {
		return numRows;

	}
}