package main.java.ic.doc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Demo {

	public static void main(String[] args) throws InterruptedException,
			ExecutionException, EmptyListException, SingletonListException {
		MapReduceList<Integer> list = new MapReduceList<Integer>(
				new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8)));

		UnaryFunction<Integer> square = new UnaryFunction<Integer>() {
			@Override
			public Integer apply(Integer n) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// Interrupted;
				}
				return n * n;
			}
		};

		@SuppressWarnings("unused")
		UnaryFunction<Integer> randomFunc = new UnaryFunction<Integer>() {
			@Override
			public Integer apply(Integer input) {
				Random r1 = new Random();
				try {
					int x = r1.nextInt(2000);
					Thread.sleep(2000 + x);
				} catch (InterruptedException e) {
					// Interrupted;
				}
				return input * input;
			}
		};

		BinaryFunction<Integer> add = new BinaryFunction<Integer>() {
			@Override
			public Integer apply(Integer input1, Integer input2) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// Interrupted;
				}
				return input1 + input2;
			}
		};

		System.out.println(list);
		float initial1 = System.nanoTime();
		MapReduceList<Integer> squares = list.map(square);
		float final_val1 = System.nanoTime();
		System.out.println("Total Time Taken: " + (final_val1 - initial1));
		System.out.println();
		System.out.println(squares);
		float initial2 = System.nanoTime();
		Integer answer = squares.reduce(add);
		float final_val2 = System.nanoTime();
		System.out.println("Total Time Taken: " + (final_val2 - initial2));
		System.out.println("The result is: " + answer);
	}
}
