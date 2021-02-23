import java.io.File;
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
	 * Constructs a dataset by loading a CSV file
	 * 
	 * @param strFilename is the name of the file
	 */
	public CSVReader(String filePath) throws Exception {
		// WRITE YOUR CODE HERE!
	}


	public String[] getAttributeNames() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	public String[][] getData() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	public String getSourceId() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	public int getNumberOfColumns() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return -1;
	}

	public int getNumberOfDataRows() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return -1;
	}
}