package main.java.ic.doc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapReduceList<T> implements List<T> {

	private final List<T> delegate;
	private int num_extra = 0;

	public MapReduceList(List<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return delegate.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return delegate.iterator();
	}

	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	@Override
	public boolean add(T e) {
		return delegate.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return delegate.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return delegate.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public T get(int index) {
		return delegate.get(index);
	}

	@Override
	public T set(int index, T element) {
		return delegate.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		delegate.isEmpty();
	}

	@Override
	public T remove(int index) {
		return delegate.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return delegate.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return delegate.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public MapReduceList<T> map(UnaryFunction<T> mapper) throws InterruptedException,
			ExecutionException, EmptyListException {
		if (delegate.size() == 0) {
			throw new EmptyListException("Empty List from Reduce");
		}
		List<T> result = new ArrayList<T>();
		List<Future<T>> futures = new ArrayList<Future<T>>();
		ExecutorService executor = Executors.newFixedThreadPool(delegate.size());
		for (T elem : delegate) {
			futures.add((executor.submit(new Command<T>(mapper, elem))));
		}
		executor.shutdown();
		for (Future<T> fut : futures) {
			result.add(fut.get());
		}
		return new MapReduceList<T>(result);
	}

	public T reduce(BinaryFunction<T> mapper) throws InterruptedException,
			ExecutionException, EmptyListException, SingletonListException {
		if (delegate.size() == 1) {
			throw new SingletonListException("Can't apply a binary function.");
		} else if (delegate.size() == 0) {
			throw new EmptyListException("Empty List from Reduce");
		}
		List<Future<T>> futureList = new ArrayList<Future<T>>();
		ExecutorService executor = Executors.newFixedThreadPool(delegate.size());
		T result = reduceDown(delegate, mapper, futureList, executor);
		executor.shutdown();
		return result;
	}

	private T reduceDown(List<T> elems, BinaryFunction<T> mapper,
			List<Future<T>> futureList, ExecutorService executor)
			throws InterruptedException, ExecutionException {
		if (elems.size() == 1) {
			return elems.get(0);
		}
		if (!(elems.size() % 2 == 0)) {
			num_extra++;
		}
		for (int j = 0; j < elems.size() - 1; j += 2) {
			futureList.add(executor.submit(new Command<T>(mapper, elems.get(j), elems
					.get(j + 1))));
		}
		List<T> vals = new ArrayList<T>();
		for (Future<T> fut : futureList) {
			vals.add(fut.get());
		}
		if (!(elems.size() % 2 == 0)) {
			vals.add(elems.get(elems.size() - 1));
		}
		elems = vals;
		return reduceDown(elems, mapper, new ArrayList<Future<T>>(), executor);
	}

	@Override
	public String toString() {
		return this.delegate.toString();
	}
	
	@Override
	public boolean equals(Object that){
		if(!(that instanceof MapReduceList)){
			return false;
		}
	 @SuppressWarnings("unchecked")
		MapReduceList<T> that2 = (MapReduceList<T>)that;
	 return that2.delegate.equals(this.delegate);
	}
	
	@Override
	public int hashCode(){
		return 0;
	}

}
