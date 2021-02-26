// You are allowed to use LinkedList or other Collection classes in A2 and A3
import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class is used for representing a virtual dataset, that is, a dataset
 * that is a view over an actual dataset. A virtual dataset has no data matrix
 * of its own.
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class VirtualDataSet extends DataSet {

	/**
	 * reference to the source dataset (instance of ActualDataSet)
	 */
	private ActualDataSet source;

	/**
	 * array of integers mapping the rows of this virtual dataset to the rows of its
	 * source (actual) dataset
	 */
	private int[] map;

	/**
	 * Constructor for VirtualDataSet. There are two important considerations here:
	 * (1) Make sure that you keep COPIES of the "rows" and "attributes" passed as
	 * formal parameters. Do not, for example, say this.map = rows. Instead, create
	 * a copy of rows before assigning that copy to this.map. (2) Prune the value
	 * sets of the attributes. Since a virtual dataset is only a subset of an actual
	 * dataset, it is likely that some or all of its attributes may have smaller
	 * value sets.
	 * 
	 * @param source     is the source dataset (always an instance of ActualDataSet)
	 * @param rows       is the set of rows from the source dataset that belong to
	 *                   this virtual dataset
	 * @param attributes is the set of attributes belonging to this virtual dataset.
	 *                   IMPORTANT: you need to recalculate the unique value sets
	 *                   for these attributes according to the rows. Why? Because
	 *                   this virtual set is only a subset of the source dataset and
	 *                   its attributes potentially have fewer unique values.
	 */
	public VirtualDataSet(ActualDataSet source, int[] rows, Attribute[] attributes) {
		// WRITE YOUR CODE HERE!

		// For the map, the access index 0,1,2,... is the row in the virtualDataSet
		// and the number at the index is the row in the source actualDataSet

		this.source = source;

		int[] tmp = new int[rows.length];
		System.arraycopy(rows, 0, tmp, 0, rows.length);
		this.map = tmp;
		this.attributes = new Attribute[attributes.length];




		// TODO review this efficiency
		int index = 0;
		for (Attribute attr : attributes) {
			String[] remaining = new String[attr.getValues().length];
			int remainingIndex = 0;
			for (String value : attr.getValues()) {
				for (int i = 0; i < rows.length; i++) {
					if (value.equals(source.getValueAt(rows[i], attr.getAbsoluteIndex()))) {
						remaining[remainingIndex] = value;
						remainingIndex++;
						break;
					}
				}
			}
			String[] temp = remaining;
			remaining = new String[remainingIndex];
			for (int i = 0; i < remainingIndex; i++) {
				remaining[i] = temp[i];
			}

			attr.replaceValues(remaining);
			this.attributes[index] = attr;
			index++;
		}

		this.numAttributes = this.attributes.length;
	}

	/**
	 * String representation of the virtual dataset.
	 */
	public String toString() {
		// WRITE YOUR CODE HERE!
		String out = "Virtual dataset with ";
		out = out.concat(String.valueOf(attributes.length));
		out = out.concat(" attribute(s) and ");
		out = out.concat(String.valueOf(map.length));
		out = out.concat(" row(s)\n");
		out = out.concat(" - Dataset is a view over ");
		out = out.concat(String.valueOf(source.getSourceId()));
		out = out.concat("\n - Row indices in the dataset (w.r.t its source dataset) ");
		out = out.concat(Arrays.toString(map));
		out = out.concat("\n");
		out = out.concat(source.toString());


		return out;
	}

	/**
	 * Implementation of DataSet's getValueAt abstract method for virtual datasets.
	 * Hint: You need to call source.getValueAt(...). What you need to figure out is
	 * with what parameter values that method needs to be called.
	 */
	public String getValueAt(int row, int attributeIndex) {
		// WRITE YOUR CODE HERE!
		// TODO - Review this logic
		return source.getValueAt(map[row], source.getAttributeIndex(attributes[attributeIndex].getName()));
	}

	/**
	 * @return reference to source dataset
	 */
	public ActualDataSet getSourceDataSet() {
		// WRITE YOUR CODE HERE!
		// TODO - Should just be this no?
		return source;
	}


	/**
	 * This method splits the virtual dataset over a nominal attribute. This process
	 * has been discussed and exemplified in detail in the assignment description.
	 * 
	 * @param attributeIndex is the index of the nominal attribute over which we
	 *                       want to split.
	 * @return a set (array) of partitions resulting from the split. The partitions
	 *         will no longer contain the attribute over which we performed the
	 *         split.
	 */
	public VirtualDataSet[] partitionByNominallAttribute(int attributeIndex) {
		// WRITE YOUR CODE HERE!
		Attribute attribute = source.getAttribute(attributeIndex);
		VirtualDataSet[] out = new VirtualDataSet[attribute.getValues().length];
		String[] values = attribute.getValues();

		int datasetIndex = 0;
		for (String value : values) {
			int rowIndex = 0;
			int[] rows = new int[source.getNumberOfDatapoints()];
			for (int i = 0; i < map.length; i++) {
				if (source.getValueAt(i, attributeIndex).matches(value)){
					rows[rowIndex] = i;
					rowIndex++;
				}
			}
			int[] temp = rows;
			boolean pastFirst = false;
			int index = 1;
			for (int x : temp) {
				if (!pastFirst) {
					pastFirst = true;
					continue;
				}
				if (x != 0) index++;
				else break;
			}
			rows = new int[index];
			for (int i = 0; i < index; i++) {
				rows[i] = temp[i];
			}

			out[datasetIndex] = new VirtualDataSet(this.source, rows, this.attributes);
			datasetIndex++;
			return out;
		}
		return out;
	}

	/**
	 * This method splits the virtual dataset over a given numeric attribute at a
	 * specific value from the value set of that attribute. This process has been
	 * discussed and exemplified in detail in the assignment description.
	 * 
	 * @param attributeIndex is the index of the numeric attribute over which we
	 *                       want to split.
	 * @param valueIndex     is the index of the value (in the value set of the
	 *                       attribute of interest) to use for splitting
	 * @return a pair of partitions (VirtualDataSet array of length two) resulting
	 *         from the two-way split. Note that the partitions will retain the
	 *         attribute over which we perform the split. This is in contrast to
	 *         splitting over a nominal, where the split attribute disappears from
	 *         the partitions.
	 */
	public VirtualDataSet[] partitionByNumericAttribute(int attributeIndex, int valueIndex) {
		// WRITE YOUR CODE HERE!
		int[] columnMaps = map;

		boolean sorted = false;
		int temp;
		while(!sorted) {
			
			for (int i = 0; i < columnMaps.length - 1; i++) {
				if (Integer.parseInt(this.source.getValueAt(columnMaps[i], attributeIndex)) > Integer.parseInt(this.source.getValueAt(columnMaps[i+1], attributeIndex))) {
					temp = columnMaps[i];
					columnMaps[i] = columnMaps[i+1];
					columnMaps[i+1] = temp;
					sorted = false;
				}
			}
			sorted = true;
		}
		int[] partitionA = new int[(columnMaps.length % 2) > 0 ? (columnMaps.length/2)+1 : (columnMaps.length/2)];
		int[] partitionB = new int[(columnMaps.length/2)];

		System.arraycopy(columnMaps, 0, partitionA, 0, partitionA.length);
		System.arraycopy(columnMaps, partitionA.length, partitionB, 0, partitionB.length);

		return new VirtualDataSet[]{
				new VirtualDataSet(this.source, partitionA, this.attributes),
				new VirtualDataSet(this.source, partitionB, this.attributes),
		};

	}

	public static void main(String[] args) throws Exception {

		StudentInfo.display();

		System.out.println("============================================");
		System.out.println("THE WEATHER-NOMINAL DATASET:");
		System.out.println();

		ActualDataSet figure5Actual = new ActualDataSet(new CSVReader("./datasets/weather-nominal.csv"));

		System.out.println(figure5Actual);

		VirtualDataSet figure5Virtual = figure5Actual.toVirtual();

		System.out.println("JAVA IMPLEMENTATION OF THE SPLIT IN FIGURE 5:");
		System.out.println();

		VirtualDataSet[] figure5Partitions = figure5Virtual.partitionByNominallAttribute(figure5Virtual.getAttributeIndex("outlook"));

		for (int i = 0; i < figure5Partitions.length; i++)
			System.out.println("Partition " + i + ": " + figure5Partitions[i]);

		System.out.println("============================================");
		System.out.println("THE WEATHER-NUMERIC DATASET:");
		System.out.println();

		ActualDataSet figure9Actual = new ActualDataSet(new CSVReader("./datasets/weather-numeric.csv"));

		System.out.println(figure9Actual);

		VirtualDataSet figure9Virtual = figure9Actual.toVirtual();

		// Now let's figure out what is the index for humidity in figure9Virtual and
		// what is the index for "80" in the value set of humidity!

		int indexForHumidity = figure9Virtual.getAttributeIndex("humidity");

		Attribute humidity = figure9Virtual.getAttribute(indexForHumidity);

		String[] values = humidity.getValues();

		int indexFor80 = -1;

		for (int i = 0; i < values.length; i++) {
			if (values[i].equals("80")) {
				indexFor80 = i;
				break;
			}
		}

		if (indexFor80 == -1) {
			System.out.println("Houston, we have a problem!");
			return;
		}

		VirtualDataSet[] figure9Partitions = figure9Virtual.partitionByNumericAttribute(indexForHumidity, indexFor80);

		System.out.println("JAVA IMPLEMENTATION OF THE SPLIT IN FIGURE 9:");
		System.out.println();

		for (int i = 0; i < figure9Partitions.length; i++)
			System.out.println("Partition " + i + ": " + figure9Partitions[i]);

	}
}