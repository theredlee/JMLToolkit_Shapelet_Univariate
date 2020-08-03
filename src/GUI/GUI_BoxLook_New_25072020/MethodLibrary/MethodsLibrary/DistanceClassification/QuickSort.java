package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification;

// Java program for implementation of QuickSort
public class QuickSort
{
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    int partition(double arr[][], int low, int high)
    {
        double pivot = arr[high][0];
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than the pivot
            if (arr[j][0] < pivot)
            {
                i++;

                // swap arr[i] and arr[j]
//                int temp = arr[i];
                double[] temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
//        int temp = arr[i+1];
        double[] temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    void sort(double arr[][], int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }

    /* A utility function to print array of size n */
    static void printArray(double arr[][])
    {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.println(arr[i][0] + " " + arr[i][1] + " " + arr[i][2]);
        System.out.println("\n" + "--------------------");
    }

    // Driver program
    public static void main(String args[])
    {
//        int arr[] = {10, 7, 8, 9, 1, 5};
        double arr[][] = {{10, 100, 0}, {7, -100, 1}, {8, 100, 2}, {9, -100, 3}, {1, 100, 4}, {5, -100, 5}};
        int n = arr.length;


        QuickSort ob = new QuickSort();
        ob.sort(arr, 0, n-1);

        System.out.println("sorted array");
        printArray(arr);
    }
}
/*This code is contributed by Rajat Mishra */
