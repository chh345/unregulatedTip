package classes;

import java.util.ArrayList;

public class State {

	String desc;
	ArrayList <State> neighbors; 
	
	public State(String description) {
		desc = description;
		neighbors = new ArrayList<State>();
	}
	
	public void addNeighbors(State s) {
		if (!neighbors.contains(s))
			neighbors.add(s);
	}
	
}
