package echa;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Larik {

	public static void cetak(int[] array) {
		String index = "Index :";
		String value = "Value :";
		for (int i = 0; i < array.length; i++) {
			index += String.format(" %4d", i);
			value += String.format(" %4d", array[i]);
		}
		System.out.println(index);
		System.out.println(value);
	}

	public static int sequentialSearch(int[] array, int key) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == key) {
				return i;
			}
		}
		return -1;
	}


	public static int binarySearch(int[] array, int key) {
		int high = array.length - 1;
		return binaryRecursive(array, key, 0, high);
	}

	private static int binaryRecursive(int[] array, int key, int low, int high) {
		if (low > high) {
			return -1;
		} else {
			int mid = (low + high) / 2;
			if (key == array[mid]) {
				return mid;
			} else if (key > array[mid]) {
				return binaryRecursive(array, key, mid + 1, high);
			} else return binaryRecursive(array, key, low, mid - 1);
		}
	}

	public static int interpolationSearch(int[] array, int key) {
		int high = array.length - 1;
		return interpolationRecursive(array, key, 0, high);
	}

	private static int interpolationRecursive(int[] array, int key, int low, int high) {
		if (low > high) {
			return -1;
		} else {
			int mid = low + (((key - array[low]) * (high - low)) / (array[high] - array[low]));
			if (key == array[mid]) {
				return mid;
			} else if (key > array[mid]) {
				return interpolationRecursive(array, key, mid + 1, high);
			} else return interpolationRecursive(array, key, low, mid - 1);
		}
	}

	public static int[] generateArrayUniform(int length, int min, int space) {
		int[] array = new int[length];
		for (int i = 0; i < length; i++) {
			array[i] = min;
			min += space;
		}
		return array;
	}

	public static int[] generateArrayRandom(int length, int min, int max) {
		int[] array = new int[length];
		Random random = new Random();

		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt(max - min) + min;
		}
		return array;
	}

	public static int[] randomizeArray(int[] array) {
		Random random = new Random();
		int[] arrayTemp = Arrays.copyOf(array, array.length);

		for (int i = arrayTemp.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			int temp = arrayTemp[index];
			arrayTemp[index] = arrayTemp[i];
			arrayTemp[i] = temp;
		}
		return arrayTemp;
	}

	public static boolean checkSorted(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if ((array[i] > array[i + 1])) {
				return false;
			}
		}
		return true;
	}

	public static void selectionSort(int[] array) {



		for (int i = 0; i < array.length - 1; i++) {
			int mIndex = i;
			for (int j = i + 1; j < array.length; j++) {
				if ((array[j] ) < (array[mIndex] )) {
					mIndex = j;
				}
			}
			swap(array, mIndex, i);
		}
	}

	public static void bubbleSort(int[] array) {

		for (int i = 1; i < array.length; i++) {
			for (int j = 0; j < array.length - i; j++) {
				if (array[j] > array[j + 1]) {
					swap(array, j, j + 1);
				}
			}
		}
	}

	public static void swap(int[] array, int firstIndex, int secondIndex) {
		int temp = array[firstIndex];
		array[firstIndex] = array[secondIndex];
		array[secondIndex] = temp;
	}

	public static void bubbleComplex(int[] array) {
		int tukar = 0;
		int compare = 0;
		System.out.println("-----------------------------------------------");
		for (int i = 1; i < array.length; i++) {
			System.out.println("Iterasi " + i);
			System.out.printf("%5s | %5s | %6s | %6s%n", "Count", "Key", "Compare", "Tukar");
			for (int j = 0; j < array.length - i; j++) {
				System.out.printf("%5s | %5d | %6d ", j + 1, array[j], array[j + 1]);
				compare++;
				if (array[j] > array[j + 1]) {
					swap(array, j, j + 1);
					tukar++;
					System.out.printf(" | %6s%n", "Ya");
				} else {
					System.out.printf(" | %6s%n", "Tidak");
				}
			}

			System.out.print("Array di akhir iterasi : ");
			for (int each : array) {
				System.out.print(each + " ");
			}
			System.out.println("\n");
		}
		System.out.println("Tukar total = " + tukar);
		System.out.println("Banding total = " + compare);
		System.out.println("-----------------------------------------------");
	}

	public static void selectionComplex(int[] array) {
		int compare = 0;
		int switching = 0;

		System.out.println("-----------------------------------------------");

		for (int i = 0; i < array.length - 1; i++) {
			int mIndex = i;
			int count = 1;

//			System.out.println("Iterasi " + (i + 1));
//			System.out.printf("%5s | %5s | %6s | %6s%n", "Count", "Key", "Compare", m);
			for (int j = i + 1; j < array.length; j++) {
//				System.out.printf("%5s | %5d | %6d ", count++, array[mIndex], array[j]);
				compare++;
//				System.out.printf(" | %6d%n", mIndex);
				if ((array[j] ) < (array[mIndex])) {
					mIndex = j;
				}
			}
//			System.out.println("Index " + i + " ditukar dengan index " + mIndex);
			swap(array, mIndex, i);
			switching++;
//			System.out.print("Array di akhir iterasi : ");
//			for (int each : array) {
//				System.out.print(each + " ");
//			}
//			System.out.println("\n");
		}
		System.out.println("Banding total\t: " + compare);
		System.out.println("Tukar total\t\t: " + switching);

		System.out.println("-----------------------------------------------");
	}

	public static void selectionComplexBig(int[] array, boolean isAscending) {
		int factor = 1;

		if (!isAscending) {
			factor = -1;
		}
		System.out.println("-----------------------------------------------");

		for (int i = 0; i < array.length - 1; i++) {
			int mIndex = i;
			int count = 1;

			System.out.println("\tIterasi " + (i + 1));
			for (int j = i + 1; j < array.length; j++) {
				if ((array[j] * factor) < (array[mIndex] * factor)) {
					mIndex = j;
				}
			}
			swap(array, mIndex, i);
		}
	}

	public static void insertionSort(int[] array, boolean isAscending) {
		int factor = 1;
		if (!isAscending) {
			factor = -1;
		}
		for (int i = 1; i < array.length; i++) {
			int key = array[i];
			int j = i - 1;

			while (j >= 0 && ((key) < (array[j]))) {
				array[j + 1] = array[j];
				j--;
			}

			array[j + 1] = key;
		}
	}

	public static void insertionSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int key = array[i];
			int j = i - 1;
			while (j >= 0 && ((key) < (array[j]))) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = key;
		}
	}

	public static long insertionComplex(int[] array) {
		long swaps = 0;
		for (int i = 1; i < array.length; i++) {
			int key = array[i];
			int j = i - 1;
			while (j >= 0 && (key) < (array[j])) {
				array[j + 1] = array[j];
				j--;
				swaps++;
			}
			if(j != (i - 1)) {
				array[j + 1] = key;
				swaps++;
			}
		}
		return swaps;
	}

	public static long insertionPerIteration(int[] array) {
		long swaps = 0;
		for (int i = 1; i < array.length; i++) {
			int key = array[i];
			int j = i - 1;
			while (j >= 0 && (key) < (array[j])) {
				array[j + 1] = array[j];
				j--;
				swaps++;
			}
			if(j != (i - 1)) {
				array[j + 1] = key;
				swaps++;
			}
			System.out.println("Condition at end of iteration");
		}
		return swaps;
	}

	public static void quickSort(int[] array) {
		AtomicLong swaps = new AtomicLong(0);
		quickSort(array, 0, array.length - 1, swaps);
	}

	private static void quickSort(int[] array, int lowIndex, int highIndex, AtomicLong swaps) {
		if (lowIndex < highIndex) {
			int i = lowIndex + 1;
			int j = highIndex;

			while (i <= highIndex && array[i] <= array[lowIndex]) {
				i++;
			}
			while (j > lowIndex && array[j] > array[lowIndex]) {
				j--;
			}
			while (i < j) {
				swap(array, i, j);
				swaps.incrementAndGet();
				while (i <= highIndex && array[i] <= array[lowIndex]) {
					i++;
				}
				while (j > lowIndex && array[j] > array[lowIndex]) {
					j--;
				}
			}
			if(lowIndex != j){
				swap(array, lowIndex, j);
				swaps.incrementAndGet();
			}
			quickSort(array, lowIndex, j - 1, swaps);
			quickSort(array, j + 1, highIndex,swaps);
		}
	}

	public static long quickSortComplex(int[] array) {
		AtomicLong swaps = new AtomicLong(0);
		quickSort(array, 0, array.length-1, swaps);
		return swaps.get();
	}

	public static void quickSortDesc(int[] array) {
		quickSortDesc(array, 0, array.length - 1);
	}

	private static void quickSortDesc(int[] array, int lowIndex, int highIndex) {
		if (lowIndex < highIndex) {
			int i = lowIndex + 1;
			int j = highIndex;

			while (i <= highIndex && array[i] > array[lowIndex]) {
				i++;
			}
			while (j > lowIndex && array[j] <= array[lowIndex]) {
				j--;
			}
			while (i < j) {
				swap(array, i, j);
				while (i <= highIndex && array[i] > array[lowIndex]) {
					i++;
				}
				while (j > lowIndex && array[j] <= array[lowIndex]) {
					j--;
				}
			}
			if(lowIndex != j) {
				swap(array, lowIndex, j);
			}
			quickSortDesc(array, lowIndex, j - 1);
			quickSortDesc(array, j + 1, highIndex);
		}
	}

	public static void mergeSort(int[] array) {
		mergeSort(array, array.clone(), 0, array.length - 1);
	}

	private static void mergeSort(int[] array, int[] destArray, int lowIndex, int highIndex) {
		if (highIndex <= lowIndex) {
//			insertionSort(array, lowIndex, highIndex);
			return;
		}
		int mid =   (highIndex + lowIndex) / 2;

		mergeSort(destArray, array, lowIndex, mid);
		mergeSort(destArray, array, mid + 1, highIndex);
		if (array[mid] <= array[mid + 1]) {
			System.arraycopy(destArray, lowIndex, array, lowIndex, highIndex - lowIndex + 1);
		}
		mergeArrays(destArray, array, lowIndex, mid, highIndex);
	}



	private static void mergeArrays(int[] array, int[] destArray, int lowIndex, int mid, int highIndex) {

		int i = lowIndex;
		int j = mid + 1;
		for (int x = lowIndex; x <= highIndex; x++) {
			if (i > mid) {
				destArray[x] = array[j++];
			} else if (j > highIndex) {
				destArray[x] = array[i++];
			} else if (array[j] < array[i]) {
				destArray[x] = array[j++];
			} else {
				destArray[x] = array[i++];
			}
		}
	}

	public static long mergeComplex(int[] array) {
		AtomicLong swaps = new AtomicLong(0);
		mergeComplex(array, array.clone(), 0, array.length - 1, swaps);
		return swaps.get();
	}

	private static void mergeComplex(int[] array, int[] destArray, int lowIndex, int highIndex, AtomicLong swaps) {
		if (highIndex <= lowIndex) {
//			insertionSort(array, lowIndex, highIndex);
			return;
		}
		int mid = (highIndex + lowIndex) / 2;

		mergeComplex(destArray, array, lowIndex, mid, swaps);
		mergeComplex(destArray, array, mid + 1, highIndex, swaps);
		if (array[mid] <= array[mid + 1]) {
			System.arraycopy(destArray, lowIndex, array, lowIndex, highIndex - lowIndex + 1);
		}
		mergeArraysComplex(destArray, array, lowIndex, mid, highIndex,swaps);
	}

	private static void mergeArraysComplex(int[] array, int[] destArray, int lowIndex, int mid, int highIndex, AtomicLong swaps) {
		int i = lowIndex;
		int j = mid + 1;
		for (int x = lowIndex; x <= highIndex; x++) {
			if (i > mid) {
				destArray[x] = array[j++];
			} else if (j > highIndex) {
				destArray[x] = array[i++];
			} else if (array[j] < array[i]) {
				destArray[x] = array[j++];
			} else {
				destArray[x] = array[i++];
			}
			swaps.incrementAndGet();
		}
	}

	private static void insertionSort(int[] array, int lowIndex, int highIndex) {
		for (int i = lowIndex + 1; i <= highIndex; i++) {
			int key = array[i];
			int j = i - 1;
			while (j >= 0 && ((key) < (array[j]))) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = key;
		}
	}

	public static void swap(Object[] objects, int i1, int i2) {
		Object temp = objects[i1];
		objects[i1] = objects[i2];
		objects[i2] = temp;
	}


	public static void cetak(Object[] objects) {
		for (int i = 0; i < objects.length; i++) {
			System.out.println(objects[i].toString());
		}
	}

	public static int sequentialSearch(Object[] objects, Object key) throws ClassCastException {
		for (int i = 0; i < objects.length; i++) {
			if (((Comparable) objects[i]).compareTo(key) == 0) {
				return i;
			}
		}
		return -1;
	}

	public static int binarySearch(Object[] objects, Object key) throws ClassCastException {
		return binaryRecursive(objects, key, 0, objects.length - 1);
	}

	private static int binaryRecursive(Object[] array, Object key, int low, int high) {
		if (low > high) return -1;
		int mid = (low + high) / 2;
		if (((Comparable) key).compareTo(array[mid]) == 0) {
			return mid;
		} else if (((Comparable) key).compareTo(array[mid]) == 1) {
			return binaryRecursive(array, key, mid + 1, high);
		} else return binaryRecursive(array, key, low, mid - 1);
	}

	public static void selectionSort(Object[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			int mIndex = i;
			for (int j = i+1; j < array.length; j++) {
				if(((Comparable) array[j]).compareTo(array[mIndex]) == -1) {
					mIndex = j;
				}
			}
			swap(array, mIndex, i);
		}
	}

	public static void bubbleSort(Object[] objects) {
		for (int i = 1; i < objects.length; i++) {
			for (int j = 0; j < objects.length - i; j++) {
				if (((Comparable) objects[j]).compareTo(objects[j + 1]) == 1) {
					swap(objects, j, j + 1);
				}
			}
		}
	}

	public static void insertionSort(Object[] array) {
		for (int i = 1; i < array.length; i++) {
			Comparable key = (Comparable) array[i];
			int j = i - 1;
			while(j >= 0 && (key.compareTo(array[j]) == -1)) {
				array[j + 1] = array[j];
				j--;
			}
			array[j + 1] = key;
		}
	}

	public static void quickSort(Object[] array) {
		quickSortRecursive((Comparable[]) array, 0, array.length - 1);
	}

	private static void quickSortRecursive(Comparable[] array, int low, int high) {
		if(low < high) {
			int i = low + 1;
			int j = high;

			while(i <= high && array[i].compareTo(array[low]) <= 0) {
				i++;
			}
			while(j > low && array[j].compareTo(array[low]) == 1) {
				j--;
			}
			while(i < j) {
				swap(array, i, j);
				while(i <= high && array[i].compareTo(array[low]) <= 0) {
					i++;
				}
				while(j > low && array[j].compareTo(array[low]) == 1) {
					j--;
				}
			}
			swap(array, low, j);
			quickSortRecursive(array, low, j -1);
			quickSortRecursive(array, j+1, high);
		}
	}

	public static void mergeSort(Object[] array) {
		mergeSortRecursive((Comparable[]) array, ((Comparable[]) array).clone(), 0, array.length - 1);
	}

	private static void mergeSortRecursive(Comparable[] array, Comparable[] destArray, int low, int high) {
		if (high <= low) {
			return;
		}
		int mid = (high + low) / 2;

		mergeSortRecursive(destArray, array, low, mid);
		mergeSortRecursive(destArray, array, mid + 1, high);
		if (array[mid].compareTo(array[mid + 1]) == -1) {
			System.arraycopy(destArray, low, array, low, high - low + 1);
		}
		mergeArrays(destArray, array, low, mid, high);
	}

	private static void mergeArrays(Comparable[] array, Comparable[] destArray, int low, int mid, int high) {

		int i = low;
		int j = mid + 1;
		for (int x = low; x <= high; x++) {
			if (i > mid) {
				destArray[x] = array[j++];
			} else if (j > high) {
				destArray[x] = array[i++];
			} else if (array[j].compareTo(array[i]) == -1) {
				destArray[x] = array[j++];
			} else {
				destArray[x] = array[i++];
			}
		}
	}

}