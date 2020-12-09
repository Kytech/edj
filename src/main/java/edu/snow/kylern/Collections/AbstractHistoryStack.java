package edu.snow.kylern.Collections;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 * Abstract base class for common code used to build an implementation of a
 * HistoryStack that uses a Deque for it's internal management
 * of elements. Provides an implementation for the trivial pass-through
 * operations, as well as the simpler methods that should typically be
 * the same across most implementations.
 *
 * A concrete implementation minimally needs to override push, pop, and
 * unpop. It also needs to initialize the stack and historyStack in it's
 * constructor. See the HistoryStack interface for implementation notes and
 * expected behaviors of these methods. Additional methods can be overridden
 * if more complex behaviors are wanted.
 *
 * @param <E>
 * @author Kyler N
 */
public abstract class AbstractHistoryStack<E> implements HistoryStack<E> {

    protected Deque<E> stack;
    protected Deque<E> historyStack;

    @Override
    public E peek() {
        return stack.peek();
    }

    @Override
    public E popHistory() {
        return historyStack.pop();
    }

    @Override
    public E popNoHistory() {
        return stack.pop();
    }

    @Override
    public E peekHistory() {
        return historyStack.peek();
    }

    @Override
    public int historySize() {
        return historyStack.size();
    }

    @Override
    public boolean isHistoryEmpty() {
        return historyStack.isEmpty();
    }

    @Override
    public void clear() {
        stack.clear();
        historyStack.clear();
    }

    @Override
    public void clearHistory() {
        historyStack.clear();
    }

    @Override
    public void clearRetainHistory() {
        stack.clear();
    }

    @Override
    public Object[] historyToArray() {
        return historyStack.toArray();
    }

    @Override
    public <T> T[] historyToArray(T[] ts) {
        return historyStack.toArray(ts);
    }

    @Override
    public Iterator<E> historyIterator() {
        return historyStack.iterator();
    }

    @Override
    public Iterator<E> historyDescendingIterator() {
        return historyStack.descendingIterator();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return stack.containsAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return stack.contains(o);
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return stack.iterator();
    }

    @Override
    public Object[] toArray() {
        return stack.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return stack.toArray(ts);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return stack.descendingIterator();
    }
}
