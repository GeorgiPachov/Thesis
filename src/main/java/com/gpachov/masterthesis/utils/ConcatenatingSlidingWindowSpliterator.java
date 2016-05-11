package com.gpachov.masterthesis.utils;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ConcatenatingSlidingWindowSpliterator implements Spliterator<String> {

	private int index;
	public List<String> list;
	private int windowLength;

	public ConcatenatingSlidingWindowSpliterator(List<String> elements, int windowLength) {
		this.list = elements;
		this.windowLength = windowLength;
	}

	@Override
	public boolean tryAdvance(Consumer<? super String> action) {
		if (index == list.size()) {
			return false;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			int indexBegin = index;
			while (index < list.size() && index < indexBegin + windowLength) {
				stringBuilder.append(list.get(index++) + " ");
			}
			action.accept(stringBuilder.toString().trim());
			return true;
		}
	}

	@Override
	public Spliterator<String> trySplit() {
//		return new ConcatenatingSlidingWindowSpliterator(Utils.subList(list, index, endIdex), windowLength);
		return null; //disable parallel execution for now
	}

	@Override
	public long estimateSize() {
		return (list.size() - index) / windowLength;
	}

	@Override
	public int characteristics() {
		return Spliterator.ORDERED | Spliterator.SIZED;
	}

}
