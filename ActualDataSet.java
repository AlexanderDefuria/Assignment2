/**
 * This class is used for representing an actual dataset, that is, a dataset
 * that holds a data matrix
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class ActualDataSet extends DataSet {
	/**
	 * The data matrix
	 */
	private String[][] matrix;

	/**
	 * The source identifier for the data. When the data source is a file, sourceId
	 * will be the name and location of the source file
	 */
	private String dataSourceId;

	/**
	 * Constructor for ActualDataSet. In addition to initializing dataSourceId,
	 * numAttributes, numRows and matrix, the constructor needs to create an array of
	 * attributes (instance of the Attribute class) and initialize the "attributes"
	 * instance variable of DataSet.
	 * 
	 * 
	 * @param reader is the DataReader instance to read data from.
	 */
	public ActualDataSet(DataReader reader) {
		// WRITE YOUR CODE HERE!
		this.dataSourceId = reader.getSourceId();
		this.matrix = new String[reader.getNumberOfColumns()][reader.getNumberOfDataRows()];
		this.matrix = reader.getData();
		this.numAttributes = reader.getAttributeNames().length;

		String[] attributeNameList = reader.getAttributeNames();
		this.attributes = new Attribute[this.numAttributes];
		for (int i = 0; i < attributeNameList.length; i++) {
			this.attributes[i] = new Attribute(attributeNameList[i], i, AttributeType.NOMINAL, reader.getData()[i]);
		}


	}

	/**
	 * Implementation of DataSet's abstract getValueAt method for an actual dataset
	 */
	public String getValueAt(int row, int attributeIndex) {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return matrix[row][attributeIndex];
	}

	/**
	 * @return the sourceId of the dataset.
	 */
	public String getSourceId() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return this.dataSourceId;
	}

	/**
	 * Returns a virtual dataset over this (actual) dataset
	 * 
	 * @return a virtual dataset spanning the entire data in this (actual) dataset
	 */
	public VirtualDataSet toVirtual() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	/**
	 * Override of toString() in DataSet
	 * 
	 * @return a string representation of this (actual) dataset.
	 */
	public String toString() {
		// WRITE YOUR CODE HERE!

		return super.toString();
	}
}