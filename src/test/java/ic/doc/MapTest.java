package test.java.ic.doc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import main.java.ic.doc.EmptyListException;
import main.java.ic.doc.UnaryFunction;
import main.java.ic.doc.MapReduceList;

import org.junit.Test;

public class MapTest {
	
	MapReduceList<Integer> integerList;	
	
	@Test(expected=EmptyListException.class)
	public void TestMapEmptyList() throws InterruptedException, ExecutionException, EmptyListException{
		integerList = new MapReduceList<Integer>(new ArrayList<Integer>());	
		UnaryFunction<Integer> square = new UnaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input) {
				return input*input;
			}			
		};
		integerList.map(square);
	}
	
	@Test
	public void TestIntegersWithMultiply() throws InterruptedException, ExecutionException, EmptyListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(1,2,3,4,5));	
		UnaryFunction<Integer> square = new UnaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input) {
				return input*input;
			}			
		};
		assertEquals(integerList.map(square),new MapReduceList<Integer>(Arrays.asList(1,4,9,16,25)));
	}
	
	@Test 
	public void TestIntegersWithDivideByTwo() throws InterruptedException, ExecutionException, EmptyListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(2,4,6,8,10));	
		UnaryFunction<Integer> divide = new UnaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input) {
				return input/2;
			}			
		};
		assertEquals(integerList.map(divide),new MapReduceList<Integer>(Arrays.asList(1,2,3,4,5)));	
	}
	
	@Test 
	public void TestConcurrentMultiply() throws InterruptedException, ExecutionException, EmptyListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(1,2,3,4,5));	
		final int nanoSecond = 1000000000;
		float start = System.nanoTime(); 
		UnaryFunction<Integer> delay_multiply = new UnaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input) {
			 try{
			  Thread.sleep(2000);
			 }catch(InterruptedException e){
			   //Interrupted
			 }
			 return input*input;
			}			
		};
		MapReduceList<Integer> result = integerList.map(delay_multiply);
		float end = System.nanoTime();	
		assertEquals(result,new MapReduceList<Integer>(Arrays.asList(1,4,9,16,25)));
		assert(((end-start)/nanoSecond) < 3);
	}

@Test 
public void TestConcurrentDivideByTwo() throws InterruptedException, ExecutionException, EmptyListException{
	integerList = new MapReduceList<Integer>(Arrays.asList(2,4,6,8,10));	
	final int nanoSecond = 1000000000;
	float start = System.nanoTime(); 
	UnaryFunction<Integer> delay_divide = new UnaryFunction<Integer>(){
		@Override
		public Integer apply(Integer input) {
		 try{
		  Thread.sleep(2000);
		 }catch(InterruptedException e){
		   //Interrupted
		 }
		 return input/2;
		}			
	};
	MapReduceList<Integer> result = integerList.map(delay_divide);
	float end = System.nanoTime();	
	assertEquals(result,new MapReduceList<Integer>(Arrays.asList(1,2,3,4,5)));
	assert(((end-start)/nanoSecond) < 3);
}
}
