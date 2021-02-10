package edu.snow.kylern.Collections;

import java.util.ArrayDeque;
import java.util.Collection;

// TODO: This implementation should override the default iterators to prevent iterator.remove()

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
        if (!historyStack.isEmpty()) {
            E element = historyStack.pop();
            stack.push(element);
            return element;
        } else {
            return null;
        }
    }

    @Override
    public void push(E e) {
        stack.push(e);
        clearHistory();
    }

    @Override
    public E pop() {
        if (!stack.isEmpty()) {
            E element = stack.pop();
            historyStack.push(element);
            return element;
        } else {
            return null;
        }
    }

    private static void editHistoryStackDemo() {
        HistoryStack<Integer> stack = new EditHistoryStack<>();

        System.out.println("Demo of Edit History Stack.\n");
        System.out.println("Creating edit history stack and pushing values '1', '2', and '3'...");

        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println("Contents of stack:");
        for (Object item : stack.toArray())
            System.out.println(item.toString());

        System.out.println("Now popping the top two items off stack and DISCARDING the popped value!...");

        stack.pop();
        stack.pop();

        System.out.println("Contents of stack:");

        for (Object item : stack.toArray())
            System.out.println(item.toString());

        System.out.println("Invoking the history stack's unpop method twice to restore popped elements back to stack one by one...");

        stack.unpop();

        System.out.println("Values of the stack after unpopping once:");

        for (Object item : stack.toArray())
            System.out.println(item.toString());

        stack.unpop();

        System.out.println("Values of the stack after unpopping twice:");

        for (Object item : stack.toArray())
            System.out.println(item.toString());
    }

    public static void main(String args[]) {
        editHistoryStackDemo();
    }
}
