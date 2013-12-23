package test.java.ic.doc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import main.java.ic.doc.BinaryFunction;
import main.java.ic.doc.EmptyListException;
import main.java.ic.doc.MapReduceList;
import main.java.ic.doc.SingletonListException;

import org.junit.Test;

public class ReduceTest {
	MapReduceList<Integer> integerList;	
	MapReduceList<String> stringList;
	
	@Test(expected=SingletonListException.class)
	public void TestReduceSingletonList() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(1));	
		BinaryFunction<Integer> square = new BinaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input1,Integer input2) {
				return input1*input2;
			}			
		};
		integerList.reduce(square);
	}
	
	@Test(expected=EmptyListException.class)
	public void TestReduceEmptyList() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		integerList = new MapReduceList<Integer>(new ArrayList<Integer>());	
		BinaryFunction<Integer> square = new BinaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input1,Integer input2) {
				return input1*input2;
			}			
		};
		integerList.reduce(square);
	}
	
	@Test
	public void TestIntegersWithMultiply() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(1,2,3,4,5));	
		BinaryFunction<Integer> multiply = new BinaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input1,Integer input2) {
				return input1 * input2;
			}			
		};
		assert(integerList.reduce(multiply) == 120);
	}
	
	@Test
	public void TestStringsWithConcat() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		stringList = new MapReduceList<String>(Arrays.asList("Hello ","World ","!"));	
		BinaryFunction<String> concat = new BinaryFunction<String>(){
			@Override
			public String apply(String input1,String input2) {
				return (input1 + input2);
			}			
		};
		assertEquals(stringList.reduce(concat),"Hello World !");
	}
	
	@Test
	public void TestIntegersWithAdd() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(16,2,2,2,2));	
		BinaryFunction<Integer> add = new BinaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input1,Integer input2) {
				return input1 + input2;
			}			
		};
		assert(integerList.reduce(add) == 24);
	}
	
	
	@Test 
	public void TestConcurrentMultiply() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(1,2,3,4));	
		final int nanoSecond = 1000000000;
		float start = System.nanoTime(); 
		BinaryFunction<Integer> delay_multiply = new BinaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input1,Integer input2) {
			 try{
			  Thread.sleep(2000);
			 }catch(InterruptedException e){
			   //Interrupted
			 }
			 return input1*input2;
			}			
		};
		Integer result = integerList.reduce(delay_multiply);
		float end = System.nanoTime();	
		assert(result == 24);
		assert((end-start)/nanoSecond < 5);
	}
	
	@Test 
	public void TestConcurrentAdd() throws InterruptedException, ExecutionException, EmptyListException, SingletonListException{
		integerList = new MapReduceList<Integer>(Arrays.asList(8,2,2,2));	
		final int nanoSecond = 1000000000;
		float start = System.nanoTime(); 
		BinaryFunction<Integer> delay_add = new BinaryFunction<Integer>(){
			@Override
			public Integer apply(Integer input1,Integer input2) {
			 try{
			  Thread.sleep(2000);
			 }catch(InterruptedException e){
			   //Interrupted
			 }
			 return input1+input2;
			}			
		};
		Integer result = integerList.reduce(delay_add);
		float end = System.nanoTime();	
		assert(result == 14);
		assert((end-start)/nanoSecond < 5);
	}	
}
