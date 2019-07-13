package classes;

import java.util.ArrayList;

public class State {

	String desc;
	ArrayList <State> neighbors;
	boolean persistent = false;
	
	public State(String description) {
		desc = description;
		neighbors = new ArrayList<State>();
	}
	
	public void setPersistent(boolean p) {
		persistent = p;
	}
	
	public boolean isPersistent() {
		return persistent;
	}
	
	public void addNeighbors(State s) {
		if (!neighbors.contains(s))
			neighbors.add(s);
	}
	
}
