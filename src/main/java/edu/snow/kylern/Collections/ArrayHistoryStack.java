package edu.snow.kylern.Collections;

import java.util.*;

/**
 * Another concrete implementation of the HistoryStack Interface.
 * This implementation does not use any dequeues as the backing
 * structure for it's data. Instead, this implementation relies
 * on a single ArrayList to back both the history and the regular
 * data. It tracks what is history and what is data using the
 * stackHead member, which stores the index of the stack head.
 * Lower indices are also data, while higher indices are all history
 * items.
 *
 * This implementation implements the standard history behavior by primarily
 * modifying the stack head to indicate where the demarcation point is between
 * standard data and history data. It also implements a push behavior that is
 * slightly different than EditHistoryStack in that a push does not affect the
 * history (does not clear history, though it could be changed to do so). If an
 * object is pushed on to this stack, then an unpop is called, the item in the
 * history will be restored to be above the newly pushed item in the stack.
 *
 * @param <E>
 * @author Kyler N
 */
public class ArrayHistoryStack<E> implements HistoryStack<E> {

    private List<E> data;
    private int stackHead;

    public ArrayHistoryStack() {
        data = new ArrayList<E>();
        stackHead = -1;
    }

    public ArrayHistoryStack(int numOfElements) {
        data = new ArrayList<E>(numOfElements);
        stackHead = -1;
    }

    public ArrayHistoryStack(Collection<? extends E> c) {
        data = new ArrayList<E>(c);
        stackHead = data.size() - 1;
    }

    protected class ArrayHistoryStackIterator implements Iterator<E> {

        boolean isReverse;
        int endIdx;
        int curIdx;

        protected ArrayHistoryStackIterator(int startIdx, int endIdx) {
            this(startIdx, endIdx, false);
        }

        protected ArrayHistoryStackIterator(int startIdx, int endIdx, boolean isReverse) {
            this.endIdx = endIdx;
            this.isReverse = isReverse;
            this.curIdx = isReverse ? startIdx + 1 : startIdx - 1;
        }

        @Override
        public boolean hasNext() {
            if (!isReverse) {
                return curIdx + 1 <= endIdx;
            } else {
                return curIdx - 1 >= endIdx;
            }
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            if (!isReverse)
                curIdx++;
            else
                curIdx--;

            return data.get(curIdx);
        }
    }

    @Override
    public E unpop() {
        if (!isHistoryEmpty()) {
            stackHead++;
            return data.get(stackHead);
        } else {
            return null;
        }
    }

    @Override
    public E popHistory() {
        if (!isHistoryEmpty()) {
            return data.remove(stackHead + 1);
        } else {
            return null;
        }
    }

    @Override
    public E popNoHistory() {
        if (!isEmpty()) {
            stackHead--;
            return data.remove(stackHead + 1);
        } else {
            return null;
        }
    }

    @Override
    public E peekHistory() {
        if (!isHistoryEmpty())
            return data.get(stackHead + 1);
        else
            return null;
    }

    @Override
    public int historySize() {
        return data.size() - (stackHead + 1);
    }

    @Override
    public boolean isHistoryEmpty() {
        return stackHead + 1 >= data.size();
    }

    @Override
    public void clearHistory() {
        if (!isHistoryEmpty()) {
            clearDataRange(stackHead + 1, data.size() - 1);
        }
    }

    @Override
    public void clearRetainHistory() {
        if (!isEmpty()) {
            clearDataRange(0, stackHead);
            stackHead = -1;
        }
    }

    private void clearDataRange(int startIdx, int endIdx) {
        for (int i = endIdx; i >= startIdx; i--) {
            data.remove(startIdx);
        }
    }

    @Override
    public void clear() {
        data.clear();
        stackHead = -1;
    }

    @Override
    public Object[] historyToArray() {
        if (isHistoryEmpty())
            return new Object[0];

        List<E> historyList = data.subList(stackHead + 1, data.size() - 1);
        return historyList.toArray();
    }

    @Override
    public <T> T[] historyToArray(T[] ts) {
        if (isHistoryEmpty())
            return (T[]) new Object[0];

        List<E> historyList = data.subList(stackHead + 1, data.size() - 1);
        return historyList.toArray(ts);
    }

    @Override
    public Iterator<E> historyIterator() {
        return new ArrayHistoryStackIterator(stackHead + 1, data.size() - 1);
    }

    @Override
    public Iterator<E> historyDescendingIterator() {
        return new ArrayHistoryStackIterator(data.size()-1, stackHead + 1, true);
    }

    @Override
    public E peek() {
        if (stackHead > -1)
            return data.get(stackHead);
        else
            return null;
    }

    @Override
    public void push(E e) {
        stackHead++;
        data.add(stackHead, e);
    }

    @Override
    public E pop() {
        if (!isEmpty()) {
            E poppedItem = data.get(stackHead);
            stackHead--;
            return poppedItem;
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(Object o) {
        if (isEmpty())
            return false;

        List<E> stackList = data.subList(0, stackHead);
        return stackList.contains(o);
    }

    @Override
    public int size() {
        return stackHead + 1;
    }

    @Override
    public boolean isEmpty() {
        if (stackHead > -1)
            return false;
        else
            return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayHistoryStackIterator(0, stackHead);
    }

    @Override
    public Object[] toArray() {
        if (isEmpty())
            return new Object[0];

        List<E> stackList = data.subList(0, stackHead);
        return stackList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        if (isEmpty())
            return (T[]) new Object[0];

        List<E> stackList = data.subList(0, stackHead);
        return stackList.toArray(ts);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (isEmpty())
            return false;

        List<E> stackList = data.subList(0, stackHead);
        return stackList.containsAll(collection);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new ArrayHistoryStackIterator(stackHead, 0, true);
    }
}
