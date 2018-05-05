package us.sparknetwork.util;

public class ArrayUtils {

    public static <T> T[] subArrayFromIndex(T[] array, int startIndex, int endIndex) {
        int index = endIndex - startIndex;
        T[] returnArray = (T[]) new Object[index];

        int x = 0;
        for (int i = startIndex; i < endIndex; i++) {
            returnArray[x] = array[i];
            x++;
        }
        return returnArray;
    }

    public static <T> T[] subArrayFromIndex(T[] array, int startIndex) {
        return subArrayFromIndex(array, startIndex, array.length);
    }

    public static <T> T[] subArrayFromEndIndex(T[] array, int endIndex) {
        return subArrayFromIndex(array, 0, endIndex);
    }
}
