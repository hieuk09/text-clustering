package data;

public class Item<T> {
	public T data;	
	public Item<T> next;
	
	public Item(T data_) {
		data = data_;
		next = null;
	}
}
