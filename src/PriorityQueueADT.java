// DO NOT EDIT THIS INTERFACE

public interface PriorityQueueADT<E extends Comparable<E>>
{

    /**
     * Checks if the priority queue has any
     * elements and returns true if no elements,
     * false otherwise.
     *
     * @return true if no elements in queue, false otherwise.
     */
    boolean isEmpty();

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
     void insert(E item);

    /**
     * Returns the highest priority item in the priority queue.
     *
     * MinPriorityQueue => it will return the smallest valued element.
     * MaxPriorityQueue => it will return the largest valued element.
     *
     * @return the highest priority item in the priority queue.
     * @throws EmptyQueueException if priority queue is empty.
     */
    E getMax() throws EmptyQueueException;

    /**
     * Returns and removes the highest priority item in the priority queue.
     * Reorders all the other data items in the
     * queue accordingly.
     *
     * MinPriorityQueue => it will return and remove the smallest valued element.
     * MaxPriorityQueue => it will return and remove the largest valued element.
     *
     * @return the highest priority item in the priority queue.
     * @throws EmptyQueueException if priority queue is empty.
     */
    E removeMax() throws EmptyQueueException;

    /**
     * Returns the number of elements in the priority queue.
     *
     * @return number of elements in the queue.
     */
    int size();
}
