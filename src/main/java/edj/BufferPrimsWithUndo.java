package edj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BufferPrimsWithUndo extends AbstractBufferPrims {

	class UndoableCommand {
		public UndoableCommand(String name, Runnable r) {
			this.name = name;
			this.r = r;
		}
		String name;
		protected Runnable r;
	}
	
	Stack<UndoableCommand> undoables = new Stack<>();
	
	private void pushUndo(String name, Runnable r) {
		undoables.push(new UndoableCommand(name, r));
	}

	/** pop an undo - for authorized use only */
	public void popUndo() {
		if (!undoables.isEmpty()) {
			undoables.pop();
		}
	}

	public void printTOS() {
		// System.out.println("Undo TOS: " + (undoables.isEmpty() ? "(empty)" : undoables.peek().name));
	}
	
	public void clearBuffer() {
		current = NO_NUM;
		buffer.clear();
		undoables.clear();		// can't undo after this!
	}

	/* (non-Javadoc)
	 * @see edj.BufferPrims#addLines(int, java.util.List)
	 */
	@Override
	public void addLines(int startLnum, List<String> newLines) {
		// System.out.printf("BufferPrimsWithUndo.addLines(): start %d, size %d%n", startLnum, newLines.size());
		int startIx = lineNumToIndex(startLnum);
		buffer.addAll(startIx, newLines);
		current += newLines.size();
		pushUndo("add " + newLines.size() + " lines", () -> deleteLines(startLnum, startLnum + newLines.size()));
	}
	
	/* (non-Javadoc)
	 * @see edj.BufferPrims#removeLines(int, int)
	 */
	@Override
	public void deleteLines(int startLnum, int end) {
		// System.out.println("BufferPrimsWithUndo.deleteLines(" + startLnum + ", " + end +")");
		int startIx = lineNumToIndex(startLnum);
		List<String> undoLines = new ArrayList<>();
		for (int i = startIx; i < end; i++) {
			// System.out.println("BufferPrimsWithUndo.deleteLines(): inner:");
			if (buffer.isEmpty()) {
				System.out.println("?Deleted all lines!");
				return;
			}
			undoLines.add(buffer.remove(startIx)); // not i!
		}
		current = startLnum;
		pushUndo("delete lines " + startLnum + " to " + end, 
				() -> addLines(startLnum, undoLines));
	}
	
	private int nl = 0, nch = 0; // Only accessed single-threadedly, only from readBuffer

	public void readBuffer(String fileName) {
		int startLine = current;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
			bufferedReader.lines().forEach((s) -> {
				nl++; nch += s.length();
				buffer.add(s);
				current++;
			});
		} catch (FileNotFoundException e) {
			throw new BufferException("File " + fileName + " not found", e);
		} catch (IOException e) {
			throw new BufferException("File " + fileName + " failed during read", e);
		}
		println(String.format("%dL, %dC", nl, nch));
		pushUndo("read", () -> deleteLines(startLine, startLine + nl));
	}
	
	public void writeBuffer(String fileName) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void replace(String old, String newStr) {
		// TODO Auto-generated method stub
	}

	@Override
	public void replaceAll(String old, String newStr) {
		// TODO Auto-generated method stub
	}

	@Override
	public void replace(String old, String newStr, int startLine, int endLine) {
		// TODO Auto-generated method stub
	}

	@Override
	public void replaceAll(String old, String newStr, int startLine, int endLine) {
		// TODO Auto-generated method stub
	}
	
	/** If there are any undoable actions, pop the top one and run it. */
	public void undo() {
		if (undoables.empty()) {
			println("?Nothing to undo");
			return;
		}
		UndoableCommand undoable = undoables.pop();
		// System.out.println("Undoing " + undoable.name);
		undoable.r.run();
		if (!undoables.empty()) {
			undoables.pop();		// all actions create undos, drop them so undo works normally
		}
	}
	public boolean isUndoSupported() {
		return true;
	}
	
	public void println(String s) {
		System.out.println(s);
	}
}
