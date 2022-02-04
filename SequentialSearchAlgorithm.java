
public class SequentialSearchAlgorithm {

	public static int SequentialSearch(int[] A, int K) {
		// creating a bigger temporary array to hold K at the end
		int tempArray[] = new int [A.length + 1] ;
		for(int i = 0; i<A.length;i++) {
			tempArray[i] = A[i];
		} // end for
		
		tempArray[A.length] = K;
		int i = 0 ;
		
		while(tempArray[i] != K) {
			i = i + 1 ;
		} // end while
		
		if( i < A.length) {
			return i ;
		} // end if
		else {
			return -1 ;
		} // end else
		
	} // end SequentialSearch
	
	public static void main(String[] args) {
		// creating array
		int[] array = {89, 45, 68, 90, 29, 34, 17};
		
		// searching tests
		System.out.println(SequentialSearch(array, 900));
		System.out.println(SequentialSearch(array, 29));
		System.out.println(SequentialSearch(array, 89));
		
		} // end for

	} // end main()


