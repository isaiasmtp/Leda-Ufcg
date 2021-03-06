package adt.bst;

import java.util.ArrayList;

import adt.bt.BTNode;

public class BSTImpl<T extends Comparable<T>> implements BST<T> {

	protected BSTNode<T> root;

	public BSTImpl() {
		root = new BSTNode<T>();
	}

	public BSTNode<T> getRoot() {
		return this.root;
	}

	@Override
	public boolean isEmpty() {
		return root.isEmpty();
	}

	@Override
	public int height() {
		return height(root);
	}

	private int height(BSTNode<T> node) {
		int height = -1;
		
		if(!node.isEmpty()) {
			height = 1 + Math.max(height((BSTNode<T>) node.getLeft()), height((BSTNode<T>) node.getRight()));
		}
		
		return height;
	}

	@Override
	public BSTNode<T> search(T element) {
		return search(element, root);
	}

	private BSTNode<T> search(T element, BSTNode<T> node) {
		BSTNode<T> result =  new BSTNode<T>();
		
		if(element != null && node != null && !node.isEmpty()) {
			if(element.compareTo(node.getData()) == 0) {
				result = node;
			}else {
				if(element.compareTo(node.getData()) < 0) {
					result = search(element, (BSTNode<T>) node.getLeft());	
				}else {
					result = search(element, (BSTNode<T>) node.getRight());	
				}
			}
		}
		return result;
	}

	@Override
	public void insert(T element) {
		if (element != null) {
			insert(element, root);
		}
	}

	private void insert(T element, BSTNode<T> node) {
		if(node.isEmpty()) {
			node.setData(element);
			node.setLeft(new BSTNode<T>());
			node.setRight(new BSTNode<T>());
			node.getRight().setParent(node);
			node.getLeft().setParent(node);
		}else {
			if(element.compareTo(node.getData()) > 0) {
				insert(element, (BSTNode<T>) node.getRight());
			}else {
				insert(element, (BSTNode<T>) node.getLeft());
			}		
		}	
	}

	@Override
	public BSTNode<T> maximum() {
		return maximum(root);
	}

	private BSTNode<T> maximum(BSTNode<T> node) {
		BSTNode<T> result = null;
		
		if(!isEmpty()) {
			if(node.getRight().isEmpty()) {
				result = node;
			}else {
				result = maximum((BSTNode<T>) node.getRight());
			}
		}
		return result;
	}

	@Override
	public BSTNode<T> minimum() {
		return minimum(root);
	}

	private BSTNode<T> minimum(BSTNode<T> node) {
		BSTNode<T> result = null;
		
		if(!isEmpty()) {
			if(node.getLeft().isEmpty()) {
				result = node;
			}else {
				result = minimum((BSTNode<T>) node.getLeft());
			}	
		}
		return result;
	}

	@Override
	public BSTNode<T> sucessor(T element) {
		BSTNode<T> node = search(element);
		
		if(node.isEmpty()) {
			return null;
		}else if(!node.getRight().isEmpty()) {
			return minimum((BSTNode<T>) node.getRight());
		}else {
			
			BSTNode<T> aux = (BSTNode<T>) node.getParent();
			
			while(aux!=null && node.equals(aux.getRight())) {
				node = aux;
				aux = (BSTNode<T>) node.getParent();
			}
			return aux;
		}
	}

	@Override
	public BSTNode<T> predecessor(T element) {
		BSTNode<T> node = search(element);
		
		if(node.isEmpty()) {
			return null;
		}else if(!node.getLeft().isEmpty()) {
			return maximum((BSTNode<T>) node.getLeft());
		}else {
			
			BSTNode<T> aux = (BSTNode<T>) node.getParent();
			
			while(aux!=null && node.equals(aux.getLeft())) {
				node = aux;
				aux = (BSTNode<T>) node.getParent();
			}
			return aux;
		}
	}

	@Override
	public void remove(T element) {
		if (element == null) return;
		
		BSTNode<T> node = search(element);
		
		if (node == null) return;
		
		int degree = degree(node);
		
		if (degree == 0) { //Leaf
			node.setData(null);
		} else if (degree == 1) {
			removeOneDegree(node);
		} else if (degree == 2){
			removeTwoDegree(node);
		} else {
			return;
		}
		
	}

	private void removeOneDegree(BSTNode<T> node) {
		if(node.getParent() == null) {
			if(!node.getLeft().isEmpty()) {
				node.getLeft().setParent(null);
				root = (BSTNode<T>) node.getLeft();
				return;
				
			}else if(!node.getRight().isEmpty()) {
				node.getRight().setParent(null);
				root = (BSTNode<T>) node.getRight();
				return;
			}
		}
		
		BSTNode<T> aux = null;	
		
		if(!node.getLeft().isEmpty()) {
			aux = (BSTNode<T>) node.getLeft();
		}else {
			aux = (BSTNode<T>) node.getRight();
		}
		
		aux.setParent(node.getParent());
		
		if(node.equals(aux.getParent().getLeft())) {
			node.getParent().setLeft(aux);
		}else if(node.equals(aux.getParent().getRight())) {
			node.getParent().setRight(aux);
		}
	}

	private void removeTwoDegree(BSTNode<T> node) {

		BSTNode<T> sucessor = sucessor(node.getData());
		
		if(sucessor == null) return;
		node.setData(sucessor.getData());
		
		int degree = degree(sucessor);
		
		if(degree == 0) {
				sucessor.setData(null);	
		}else if(degree == 1) {
			removeOneDegree(sucessor);
		}
	}

	private int degree(BSTNode<T> node) {
		int degree = -1;
		
		if (node == null || node.isEmpty()) {
			return -1;
		}
		
		if(node!=null && !node.isEmpty()) {
			if((node.getLeft().isEmpty()) && (node.getRight().isEmpty()) ) {
				return 0;
			}else if((!node.getLeft().isEmpty()) && (node.getRight().isEmpty()) ) {
				return 1;
			}
			else if((node.getLeft().isEmpty()) && (!node.getRight().isEmpty()) ) {
				return 1;
			}
			else if((!node.getLeft().isEmpty()) && (!node.getRight().isEmpty()) ) {
				return 2;
			}
		}
		return degree;
	}
	@Override
	public T[] preOrder() {
		ArrayList<T> array = new ArrayList<T>();
		preOrder(root, array);
		
		int size = array.size();
		T[] result = (T[])new Comparable[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = array.get(i);
		}
		
		return result;
	}

	private void preOrder(BSTNode<T> node, ArrayList<T> array) {
		if (node.isEmpty()) {
			return;
		}
		
		array.add(node.getData());
		preOrder((BSTNode<T>)node.getLeft(), array);
		preOrder((BSTNode<T>)node.getRight(), array);
	}
	
	@Override
	public T[] order() {
		ArrayList<T> array = new ArrayList<T>();
		
		order(root, array);
		
		int size = array.size();
		T[] result = (T[])new Comparable[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = array.get(i);
		}
		
		return result;
	}

	private void order(BSTNode<T> node, ArrayList<T> array) {
		if (node.isEmpty()) {
			return;
		}
		
		order((BSTNode<T>)node.getLeft(), array);
		array.add(node.getData());
		order((BSTNode<T>)node.getRight(), array);
	}
	
	@Override
	public T[] postOrder() {
		ArrayList<T> array = new ArrayList<T>();
		
		postOrder(root, array);
		
		int size = array.size();
		T[] result = (T[])new Comparable[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = array.get(i);
		}
		
		return result;
	}
	
	private void postOrder(BSTNode<T> node, ArrayList<T> array) {
		if (node.isEmpty()) return;
		
		postOrder((BSTNode<T>)node.getLeft(), array);
		postOrder((BSTNode<T>)node.getRight(), array);
		array.add(node.getData());
	}

	/**
	 * This method is already implemented using recursion. You must understand
	 * how it work and use similar idea with the other methods.
	 */
	@Override
	public int size() {
		return size(root);
	}

	private int size(BSTNode<T> node) {
		int result = 0;
		// base case means doing nothing (return 0)
		if (!node.isEmpty()) { // indusctive case
			result = 1 + size((BSTNode<T>) node.getLeft())
					+ size((BSTNode<T>) node.getRight());
		}
		return result;
	}

}
