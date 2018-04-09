package utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

import main.MyException;

public class BiIterable<T> {
	
	List<T> list1;
	List<T> list2;

	public BiIterable(List<T> list1, List<T> list2) {
		if (list1.size() != list2.size()) {
			throw new MyException("biiterable requires lists of the same length " + list1.size() + " " + list2.size());
		}
		this.list1 = list1;
		this.list2 = list2;
	}
	
	public void forEach(BiConsumer<T, T> consumer) {
		Iterator<T> i1 = list1.iterator();
		Iterator<T> i2 = list2.iterator();
		while (i1.hasNext()) {
			consumer.accept(i1.next(), i2.next());
		}
	}

}
