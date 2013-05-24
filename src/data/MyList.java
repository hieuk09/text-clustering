package data;

public class MyList<T> {
	private Item<T> first;
	private int length;
	
	public MyList() {
		first = null;
		length = 0;
	}
	
	public void insert(T data) {
		Item<T> item = new Item<T>(data);
		
		if (first == null) {
			first = item;		
		} else {
			first.next = item;
		}
		
		length++;
	}
	
	public boolean isEmpty() {
		return length == 0;
	}
}
