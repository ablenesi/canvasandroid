package edu.ubbcluj.canvasAndroid.persistence;

import java.util.ArrayList;
import java.util.List;

import edu.ubbcluj.canvasAndroid.model.Folder;

public class FolderStack {
	private int head;
	private List<Folder> stack;

	public FolderStack() {
		stack = new ArrayList<Folder>();
		stack.add(null);
		head = 0;
	}

	public void push(Folder folder) {
		stack.add(folder);
		head++;
	}

	public Folder getHead() {
		return stack.get(head);
	}

	public Folder getHeadParent() {
		return stack.get(head - 1);
	}

	public void removeHead() {
		stack.remove(head--);
	}
}
