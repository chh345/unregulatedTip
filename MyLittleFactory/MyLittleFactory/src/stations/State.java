package stations;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class State <T> implements Callable<T>{
	
	public PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	T result;
	Callable<T> c;
	boolean persistent = false;
	
	public State(boolean p) {
		persistent = p;
	}
	
	public void setState(Callable<T> ca) {
		c = ca;
	}
	
	public void stop() {
		persistent = false;
	}
	
	public static void main(String [] args) {
		
		final ExecutorService executor = Executors.newFixedThreadPool(
				Math.max(1, Runtime.getRuntime().availableProcessors() - 1)
		);
		
		State <String> s1 = new State <String> (true) {
			@Override
			public String call() throws InterruptedException {
				int counter = 0;
				while (persistent) {
				Thread.sleep(2000);
				counter += 2000;
				if (counter > 15000)
					changes.firePropertyChange( "sensor", 0, 15000 );
				System.out.println("nignog");
				}
				return "hans";	
			}
		};
						
		State <Boolean> s2 = new State <Boolean> (true) {
			@Override
			public Boolean call() throws InterruptedException {
				s1.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						String property = e.getPropertyName();
						if ("sensor".equals(property)) {
							persistent = false;
						}
					}
				});
				while (persistent) {
					Thread.sleep(1000);
					System.out.println("Sleeping in s2");
				}
				System.out.println("persistent was set via s1");
				s1.stop();
				return false;
			}
		};
		
		Future <String> f1 = executor.submit(s1);
		Future <Boolean> f2 = executor.submit(s2);
		
		try {
			s1.result = f1.get();
			s2.result = f2.get();
			System.out.println(s1.result + " -> " + s2.result);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// System.out.println(s1);
		
		executor.shutdown();
		
		
		
		/*
		ArrayList<String> states = new ArrayList();
		
		states.add("First Phase");
		states.add("Second Phase");
		states.add("Third Phase");
		
		int endState = states.size();
		int currentState = 0;
		while (currentState < endState) {
			System.out.println(states.get(currentState++));
		}
		
		System.out.println(states.size() < 43);
		
		for (String s : states)
			System.out.println(s); */		
	}

	@Override
	public T call() throws Exception {
		// TODO Auto-generated method stub
		return c.call();
	}
	
	  public void addPropertyChangeListener(PropertyChangeListener l) {
	    changes.addPropertyChangeListener(l);
	  }

	  public void removePropertyChangeListener(PropertyChangeListener l) {
	    changes.removePropertyChangeListener(l);
	  }
	
}
