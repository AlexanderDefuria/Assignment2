// You are allowed to use LinkedList or other Collection classes in A2 and A3
import java.util.ArrayList;
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
		this.map = rows;
		this.attributes = new Attribute[attributes.length];
		this.numAttributes = this.attributes.length;


		for (int i = 0; i < attributes.length; i++ ) {
			this.attributes[i] = new Attribute(attributes[i].getName(), attributes[i].getAbsoluteIndex(), attributes[i].getType(), new String[0]);
			String[] values = new String[rows.length];

			int count = 0;

			for (int j = 0; j < map.length - 1; j++) {
				String newValue = source.getValueAt(map[j], attributes[i].getAbsoluteIndex());

				boolean found = false;
				for (String x : values) {
					if (x == null) {
						found = false;
						break;
					}
					if (x.equals(newValue)) {
						found = true;
						break;
					}
				}

				if (!found && newValue != null	) {
					count++;
					values[j] = newValue;
				}


			}

			int nullCount = 0;
			for (int x = 0; x < values.length; x++) {
				if (values[x] != null) {
					nullCount++;
				}
			}

			String[] cleanValues = new String[nullCount];
			int cleanCount = 0;
			for (int x = 0; x < values.length; x++) {
				if (values[x] != null) {
					cleanValues[cleanCount] = values[x];
					cleanCount++;
				}
			}


			this.attributes[i].replaceValues(cleanValues);


		}
		System.out.println();

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
		out = out.concat(super.toString());


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
		Attribute tempAttribute = source.getAttribute(attributeIndex);
		VirtualDataSet[] out = new VirtualDataSet[tempAttribute.getValues().length];
		String[] values = tempAttribute.getValues();

		int datasetIndex = 0;
		for (String value : values) {
			Attribute attribute = new Attribute(tempAttribute.getName(), tempAttribute.getAbsoluteIndex(), tempAttribute.getType(), new String[]{value});
			Attribute[] attributes = this.attributes.clone();
			//attribute = source.getAttribute(attributeIndex);
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


			//attributes[0].replaceValues(new String[]{"sunny"});
			out[datasetIndex] = new VirtualDataSet(this.source, rows, attributes);
			datasetIndex++;
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
		int value = Integer.parseInt(source.getValueAt(valueIndex, attributeIndex));

		ArrayList<Integer> rowsA = new ArrayList<Integer>();
		ArrayList<Integer> rowsB = new ArrayList<Integer>();

		for (int i = 0; i < this.source.numRows; i++) {
			if (Integer.parseInt(this.source.getValueAt(i, attributeIndex)) <= value){
				rowsA.add(i);
			} else {
				rowsB.add(i);
			}
		}



		return new VirtualDataSet[] {
				new VirtualDataSet(this.getSourceDataSet(), rowsA.stream().mapToInt(i -> i).toArray(), attributes),

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