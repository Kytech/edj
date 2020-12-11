package edu.snow.kylern.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditHistoryStackTest {

    HistoryStack<Integer> histStack;

    @Before
    public void setUp() throws Exception {
        histStack = new EditHistoryStack<>();
        histStack.add(1);
        histStack.add(2);
        histStack.add(3);
    }

    @After
    public void tearDown() throws Exception {
        histStack.clear();
    }

    @Test
    public void size() {
        assertTrue(histStack.size() == 3);
    }

    @Test
    public void unpop() {
        histStack.pop();
        histStack.pop();
        assertTrue(histStack.size() == 1);
        int unPopped = histStack.unpop();
        assertTrue(unPopped == 2);
        assertTrue(histStack.peek() == 2);
        assertTrue(histStack.historySize() == 1);
        assertTrue(histStack.size() == 2);
    }

    @Test
    public void push() {
        histStack.push(4);
        assertTrue(histStack.size() == 4);
        assertTrue(histStack.peek() == 4);
    }

    @Test
    public void pop() {
        int popped = histStack.pop();
        assertTrue(popped == 3);
        assertTrue(histStack.peek() == 2);
        assertTrue(histStack.historySize() == 1);
    }

    @Test
    public void peekHistory() {
        histStack.pop();
        assertTrue(histStack.peekHistory() == 3);
        assertTrue(histStack.historySize() == 1);
    }

    @Test
    public void popHistory() {
        histStack.pop();
        histStack.pop();
        assertTrue(histStack.size() == 1);
        int histPopped = histStack.popHistory();
        assertTrue(histPopped == 2);
        assertTrue(histStack.historySize() == 1);
        assertTrue(histStack.peekHistory() == 3);
        assertTrue(histStack.size() == 1);
    }

    @Test
    public void popNoHistory() {
        histStack.popNoHistory();
        assertTrue(histStack.historySize() == 0);
    }

    @Test
    public void clearHistory() {
        histStack.pop();
        histStack.pop();
        histStack.clearHistory();
        assertTrue(histStack.size() == 1);
        assertTrue(histStack.historySize() == 0);
    }

    @Test
    public void clearRetainHistory() {
        histStack.pop();
        histStack.pop();
        histStack.clearRetainHistory();
        assertTrue(histStack.size() == 0);
        assertTrue(histStack.historySize() == 2);
    }

    @Test
    public void clear() {
        histStack.pop();
        histStack.pop();
        histStack.clear();
        assertTrue(histStack.size() == 0);
        assertTrue(histStack.historySize() == 0);
    }
}