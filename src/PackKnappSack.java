import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

class PackKnappSack {

    private List<Item> itemsList;
    private int capacity;

    PackKnappSack(String fileName)
    {
        loadDataSet(fileName);
        chooseItems();
    }

    private void loadDataSet(String fName)
    {
        int min = 1;
        int max = 15;

        int selectDataSetNumber = (int)(Math.random() * (max - min + 1)) + min;
        System.out.println("Looking for " + selectDataSetNumber + " data set");
        itemsList = new ArrayList<>();

        try {
            Scanner scan = new Scanner(new FileReader(fName));
            int length;
            String firstLine = scan.nextLine();
            String[] firstLineData = firstLine.split("\\D+");
            length = Integer.parseInt(firstLineData[1]);
            capacity = Integer.parseInt(firstLineData[2]);

            int counter = 1;

            while (scan.hasNext() && counter <= length)
            {
                String line = scan.nextLine();
                if (line.contains("dataset " + selectDataSetNumber))
                {
                    line = scan.nextLine();
                    String[] sizes = line.split("\\D+");
                    line = scan.nextLine();
                    String[] values = line.split("\\D+");
                    for (;counter <= sizes.length && counter <= values.length && counter <= length;counter++)
                        itemsList.add(new Item(counter, Integer.parseInt(sizes[counter]), Integer.parseInt(values[counter])));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void chooseItems() {
        long startTime = System.currentTimeMillis();
        List<List<Item>> packedItems = new ArrayList<>();

        int[][] m = new int[itemsList.size() + 1][capacity + 1];
        for (int j = 0; j <= capacity; j++) {
            List<Item> empty = new ArrayList<>();
            packedItems.add(empty);
        }
        for (int i = 1; i <= itemsList.size(); i++) {
            List<Item> empty = new ArrayList<>();
            packedItems.add(empty);
            for (int j = 1; j <= capacity; j++) {
                if (itemsList.get(i - 1).size > j) {
                    m[i][j] = m[i - 1][j];
                    packedItems.add(packedItems.get((i-1)*(capacity + 1) + j));
                } else {
                    if (m[i-1][j] > m[i-1][j-itemsList.get(i-1).size] + itemsList.get(i -1).value){
                        m[i][j] = m[i-1][j];
                        packedItems.add(packedItems.get((i-1)*(capacity + 1) + j));
                    }else{
                        m[i][j] = m[i - 1][j - itemsList.get(i - 1).size] + itemsList.get(i - 1).value;
                        List<Item> temp = new ArrayList<>(packedItems.get((i - 1) * (capacity + 1) + j - itemsList.get(i - 1).size));
                        if (!temp.contains(itemsList.get(i-1)))
                            temp.add(itemsList.get(i-1));
                        packedItems.add(temp);
                    }
                }
            }
        }
        int knappSackCapacity = 0;
        for (Item i : packedItems.get(packedItems.size()-1)) {
            System.out.println(i);
            knappSackCapacity += i.size;
        }
        printResult(startTime, m[itemsList.size()], knappSackCapacity);
    }

    private void printResult(long startTime, int[] ints, int knapSackCapacity) {
        System.out.println("KnappSack value: " + ints[capacity]);
        System.out.println("KnappSack capacity used: " + knapSackCapacity);
        System.out.println("Execution time: " + (System.currentTimeMillis() - startTime)/1000.0 + "s");
    }


    private static class Item{
        int index;
        int size;
        int value;

        Item(int index, int size, int value)
        {
            this.index = index;
            this.size = size;
            this.value = value;
        }

        @Override
        public String toString() {
            return index + " " + size + " " + value;
        }
    }
}
