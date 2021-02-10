package edu.snow.kylern.Collections;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Interface for stacks containing a pop history.
 *
 * This interface provides an abstraction for implementing a type of complimentary double stack where
 * popping from this stack pushes the popped element onto an internal, secondary history record stack. This can be
 * useful in handling cases such as edit/navigation histories or for a sort of logged stack that tracks
 * all items that have ever been queued into this stack and subsequently completed (popped), maintaining the
 * order that the items were processed in.
 *
 * Two most common behaviors of implementations on a push to this type of stack will be to either clear the history,
 * such as might be the case of an edit or navigation history where previously popped elements no longer become
 * relevant as the new history is created and diverges from the old history. The second behavior will not clear
 * the history on a push. This is most useful in the case of a logged stack where one wants to maintain a record of
 * all items that have been popped from the stack.
 *
 * Since this is intended to be strictly used as a stack, any add/remove/peek methods from the parent Deque
 * interface should be redirected to the stack-style methods internally for most implementations, effectively
 * creating a strict LIFO-only view of a Deque. This interface provides defaults for these redirections which can
 * be overridden if different behavior is desired.
 *
 * Depending on the desired behavior, an implementation may also want to return an iterator that does not permit
 * the use of the iterator's remove() method
 *
 * @param <E>
 * @author Kyler N
 */
public interface HistoryStack<E> extends Deque<E> {
    /** Restores last popped item to the stack by popping from the history. */
    E unpop();

    /** Pops from the history without re-applying to the stack. */
    E popHistory();

    /** Pop off the stack, but do not add item to the history. */
    E popNoHistory();

    /** Retrieves, but does not remove, the first element in the history */
    E peekHistory();

    int historySize();
    boolean isHistoryEmpty();

    /** Clears only the pop history of the stack. Is used by the default implementation of clear() */
    void clearHistory();

    /** Clears the stack while leaving the history intact */
    void clearRetainHistory();

    /** Clear the stack, including history. */
    void clear();

    Object[] historyToArray();
    <T> T[] historyToArray(T[] ts);

    /** Returns an iterator over all items in the history */
    Iterator<E> historyIterator();

    /** Returns an iterator over all items in the history in reverse sequential order */
    Iterator<E> historyDescendingIterator();

    // -------

    // Default internal redirection of Deque methods to the stack methods. Allows for using this
    // wherever a Deque is asked for to be used as a stack, or when a Deque or Queue is asked for to be
    // used as a regular queue when you would like to supply a LIFO-style queue with this interface.
    // Also helps to remove some redundant code in implementing classes.

    default boolean add(E e) { this.push(e); return true; }
    default void addFirst(E e) { this.push(e); }
    default void addLast(E e) {this.push(e); }
    default boolean addAll(Collection<? extends E> collection) {
        for (E element : collection) {
            this.push(element);
        }

        return true;
    }

    default E element() {
        E elem = this.pop();
        if (elem == null)
            throw new NoSuchElementException();
        else
            return elem;
    }
    default E getFirst() { return this.element(); }
    default E getLast() { return this.element(); }

    default boolean offer(E e) {
        try {
            this.push(e);
            return true;
        } catch (IllegalStateException except) {
            return false;
        }
    }
    default boolean offerFirst(E e) { return this.offer(e); }
    default boolean offerLast(E e) { return this.offer(e); }

    default E peekFirst() { return this.peek(); }
    default E peekLast() { return this.peek(); }

    default E poll() {
        try {
            return this.pop();
        } catch (NoSuchElementException except) {
            return null;
        }
    }
    default E pollFirst() { return this.poll(); }
    default E pollLast() { return this.poll(); }

    default E remove() { return this.pop(); }
    default E removeFirst() { return this.pop(); }
    default E removeLast() { return this.pop(); }

    // Not permitting removal of elements in the middle of the stack
    default boolean remove(Object o) { throw new UnsupportedOperationException(); }
    default boolean retainAll(Collection<?> collection) { throw new UnsupportedOperationException(); }
    default boolean removeAll(Collection<?> collection) { throw new UnsupportedOperationException(); }
    default boolean removeFirstOccurrence(Object o) { return false; }
    default boolean removeLastOccurrence(Object o) { return false; }

}
