package adt.queue;

public class QueueImpl<T> implements Queue<T> {

	private T[] array;
	private int tail;

	@SuppressWarnings("unchecked")
	public QueueImpl(int size) {
		array = (T[]) new Object[size];
		tail = -1;
	}

	@Override
	public T head() {
		T saida = null;
		if(!this.isEmpty()) {
			saida = array[0];	
		}
		return saida;
	}

	@Override
	public boolean isEmpty() {
		return (tail == -1);
	}

	@Override
	public boolean isFull() {
		return (tail == array.length-1);
	}

	private void shiftLeft() {
		for (int i = 0; i < tail; i++) {
			array[i] = array[i+1];
		}
		tail --;
	}

	@Override
	public void enqueue(T element) throws QueueOverflowException {
		if(this.isFull()) {
			throw new QueueOverflowException();
		}else {
			array[tail + 1] = element;
			tail ++;
		}
	}

	@Override
	public T dequeue() throws QueueUnderflowException {
		if(this.isEmpty()){
			throw new QueueUnderflowException();
		}else {
			T head = this.head();	
			this.shiftLeft();
			return head;
		}	
	}
}
