package edu.snow.kylern.Collections;

import java.util.ArrayDeque;
import java.util.Collection;

/**
 * Concrete implementation of a HistoryStack to be used for a forward/backward
 * edit or navigation history.
 *
 * Implements the standard history behavior where popped elements are pushed
 * on to the history stack. Implements the push behavior that clears the history
 * since edit histories or navigation histories diverge when the user undoes
 * something and then keeps editing. At that point, there is nothing to redo.
 *
 * @param <E>
 * @author Kyler N
 */
public class EditHistoryStack<E> extends AbstractHistoryStack<E> {

    public EditHistoryStack() {
        this.stack = new ArrayDeque<>();
        this.historyStack = new ArrayDeque<>();
    }

    public EditHistoryStack(int numElements) {
        this.stack = new ArrayDeque<>(numElements);
        this.historyStack = new ArrayDeque<>();
    }

    public EditHistoryStack(Collection<? extends E> c) {
        this.stack = new ArrayDeque<>(c.size());
        this.historyStack = new ArrayDeque<>();

        for (E elem : c) {
            this.stack.push(elem);
        }
    }

    @Override
    public E unpop() {
        E element = historyStack.pop();
        stack.push(element);
        return element;
    }

    @Override
    public void push(E e) {
        stack.push(e);
        clearHistory();
    }

    @Override
    public E pop() {
        E element = stack.pop();
        historyStack.push(element);
        return element;
    }
}
