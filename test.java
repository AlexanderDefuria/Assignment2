

public class test {

    public static void main(String[] args) throws Exception {
        ActualDataSet actual = new ActualDataSet(new CSVReader("./datasets/weather-nominal.csv"));
        System.out.println(actual);
        VirtualDataSet virtual = new VirtualDataSet(actual, new int[]{1,2,3}, new Attribute[]{new Attribute("outlook", 0, AttributeType.NOMINAL, new String[]{"sunny", "overcast", "rainy"})});
        System.out.println(virtual.partitionByNominallAttribute(0));

    }
}
