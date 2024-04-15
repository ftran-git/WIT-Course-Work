
public class BubbleSortAlgorithm {

	public static void BubbleSort(int[] A) {
		for(int i = 0; i <= A.length - 2; i++) {
			
			for(int j = 0; j <= A.length - 2 - i; j++) {
				
				if(A[j+1]<A[j]) {
					int temp = A[j] ;
					A[j] = A[j+1] ;
					A[j+1] = temp ;
				} // end if
				
			} // end for
			
		} // end for
		
	} // end BubbleSort()
	
	
	public static void main(String[] args) {
		// creating array
		int[] array = {89, 45, 68, 90, 29, 34, 17};
		
		// before sort
		for(int i = 0; i < array.length;i++) {
			System.out.print(array[i]);
			System.out.print(", ");
		} // end for
		
		// sorting
		System.out.println();
		BubbleSort(array);
		
		// after sort
		for(int i = 0; i < array.length;i++) {
			System.out.print(array[i]);
			System.out.print(", ");
		} // end for

	} // end main()

}
