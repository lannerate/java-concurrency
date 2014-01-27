package com.tommo.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

public class CacheTest<T,V> {

	interface ComputeFn<T, V> {
		T compute(V arg); // some functional non-side effect computation
	}

	ConcurrentHashMap<V,FutureTask<T>> cachedRes = new ConcurrentHashMap<V,FutureTask<T>>();
	final ComputeFn<T,V> computeFunction;
	
	

	public CacheTest(ComputeFn<T, V> computeFunction) {
		this.computeFunction = computeFunction;
	}

	T fetchResult(final V computeParam) {
		try {
			FutureTask<T> task = cachedRes.get(computeParam);

			if(task == null) { // just an optimization for object creation

				FutureTask<T> newTask = new FutureTask<T>(new Callable<T>() {
					@Override
					public T call() throws Exception {
						return computeFunction.compute(computeParam);
					}

				}); 

				FutureTask<T> exeTask = cachedRes.putIfAbsent(computeParam, newTask); 

				if(exeTask == null) { 
					newTask.run();
					return newTask.get();
				} else
					return exeTask.get();
			} else {
				return task.get();
			}
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}

	}




}
