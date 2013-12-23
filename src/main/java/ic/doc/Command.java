package main.java.ic.doc;
import java.util.concurrent.Callable;


public class Command<T> implements Callable<T> {
	boolean type;
	UnaryFunction<T> func;
	BinaryFunction<T> binFunc;
	T arg1;
	T arg2;
	public Command(UnaryFunction<T> func,T arg1){
		this.func = func;
		this.arg1 = arg1;
		this.type = true;
	}
	
	public Command(BinaryFunction<T> binFunc,T arg1,T arg2){
		this.binFunc = binFunc;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.type = false;
	}
	
	@Override
	public T call() throws Exception {
		if(type){
		 return func.apply(arg1);
		}
		return binFunc.apply(arg1,arg2);		
	}	
}
