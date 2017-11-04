/**
 * GENERAL DIRECTIONS -
 *
 * 1. You may add private data fields and private methods as you see fit.
 * 2. Implement ALL the methods defined in the PriorityQueueADT interface.
 * 3. DO NOT change the name of the methods defined in the PriorityQueueADT interface.
 * 4. DO NOT edit the PriorityQueueADT interface.
 * 5. DO NOT implement a shadow array.
 */

public class MaxPQ<E extends Comparable<E>> implements PriorityQueueADT<E>
{
    private E[] items;
    private static final int INITIAL_SIZE = 10; 
    private int numItems;
   
    public MaxPQ()
    {
        this.items = (E[]) new Comparable[INITIAL_SIZE];
        numItems = 0;
    }
    
    private void expandItemsArray() {
		E[] expandedArray = (E[]) new Comparable[items.length * 2]; //Double the size of original array. 
		
		for (int i = 1; i < numItems+1; i++) { 	  //Copy all elements of items array into expandedArray.	
			expandedArray[i] = items[i];
		}
		items = expandedArray;				    //Reassign items to point to bigger and filled-in array.
	}
    
    /**
     * Adds a data item to the priority queue.
     * Reorders all the other data items in the
     * queue accordingly.
     *
     * If the size is equal to the capacity of the
     * priority queue, double the capacity and then
     * add the new item.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if item is null
     */
    @Override
	public void insert(E item) {
   		if (item == null) {
			throw new IllegalArgumentException();
		}
   		
		boolean order = false;		//Returns true when swapping reaches root or the order property holds. 
		E childVal = null; 			//The pointer to the item we are adding in.
		int childIdx = numItems+1;  //Index of where we always insert the new item (as a child in the heap).
		
		if (isEmpty()) {
			items[1] = item; 		//If array is void of nodes, then make this new item the first node/root. 
			numItems++;
			return;
		}
		
		if (numItems == items.length - 1) { //Expand the array if it's full.
			expandItemsArray();
		}
		
		items[childIdx] = item;  	   //Putting item in next available position of heap. Ensures shape.
		childVal = items[childIdx]; 
		E parentVal = null; 
		
		//Moving up the heap, starting from where we added item, to compare child and parent values.
		int parentIdx = childIdx;   
		while (childIdx > 1 && order == false) {
			parentIdx = childIdx/2;   //Parent of item we added is at childIdx/2. 	
			parentVal = items[parentIdx]; 	
			
			int compareParentChild = childVal.compareTo(parentVal); 
						
			if (compareParentChild > 0) { //If child > parent, then swap element in parentIdx w/element in childIdx. 
				E temp = items[childIdx];
				items[childIdx] = items[parentIdx];
				items[parentIdx] = temp;			
				childIdx = parentIdx; 	 //New item now takes the index/position of its prior parent it swapped with.  
			}
			
			else { 						 //compareParentChild returns 0 or -value, then parent is <= child, and order holds.
				order = true;
			}
		}
		numItems++;
	}
    
    //TODO DELETE BEFORE SUBMISSION!!!
    public void print() {
    	System.out.println("From print()");
    	for (int i = 1; i < numItems+1; i++) {
    		System.out.println(items[i]);
    	}
    	System.out.println("End of print()");
    }
    
    /**
     * 
     */
	@Override
	public boolean isEmpty() {
		return numItems == 0;
	}
	
	@Override
	public E getMax() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		return items[1]; //The root contains the max value.
	}
	
	/**
	 *
	 */
	@Override
	public E removeMax() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		
		E saveRm = null;		//The value returned from the root we just removed.
		boolean order = false;	//Indicates whether or not the heap order has been established. 
		int leftChildIdx = 0; 	//Index of the child that is to the left of the parent.
		int rightChildIdx = 0;	//Index of the child that is to the right of the parent.
		int compLeftToParent = 0;  	//Value used to compare the left child and the parent.
		int compRightToParent = 0; 	//Value used to compare the right Child and the parent.
		int compLeftToRight = 0;   	//Compares the leftChild to the rightChild to see which has greater value.
		
		saveRm = items[1];		    //Have to save and then return the root we are removing.
		items[1] = items[numItems]; //Replacing root with value at end of array.
		numItems--; 			    //Removing item we swapped into the root
				
		int rootIdx = 1; 	  	//Moving down heap from rootIdx, to restore order after removing the max value.
		while (order == false) {  
			leftChildIdx = rootIdx * 2;         //The leftwards child of the parent is always at an even index.
			rightChildIdx =  leftChildIdx + 1;  //The rightwards child of the parent is always at an odd index.

			if (leftChildIdx > numItems || rightChildIdx > numItems){
				break;
			} 
			
			compLeftToParent = items[leftChildIdx].compareTo(items[rootIdx]);
		  	compRightToParent = items[rightChildIdx].compareTo(items[rootIdx]);
		  			  	
		  	//If either (or both) of the children are greater than parent, start comparing them to do a swap.
		  	if (compLeftToParent > 0 || compRightToParent > 0) { 
			  	compLeftToRight = items[leftChildIdx].compareTo(items[rightChildIdx]);
			  	
			  	//Depending on which child is greater, swap with the parent.
			  	if (compLeftToRight > 0) {  //If rightvLeft returns +value, that means left > right, swap parent w/left.
		  			E swapL = items[leftChildIdx];
		  			items[leftChildIdx] = items[rootIdx];
		  			items[rootIdx] = swapL;
		  			rootIdx = leftChildIdx; //rootIdx = 2. leftIdx = 4.
			  	}
			  	else if (compLeftToRight < 0) { //If rightvLeft returns -value, that means right > left, swap parent w/right.
		  			E swapR = items[rightChildIdx];
		  			items[rightChildIdx] = items[rootIdx];
		  			items[rootIdx] = swapR;
		  			rootIdx = rightChildIdx; //rootIdx = rightIdx: items[1] now corresponds to 20, items[3] is 18.
			  	}
		  	}
		  	else { //If order between the children and the parent is established, break out of the loop.
		  		order = true;
		  	}
		}
		return saveRm;
	}
	
	/**
	 * 
	 */
	@Override
	public int size() {
		return numItems;
	}
}
