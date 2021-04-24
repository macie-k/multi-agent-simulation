package utils;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentYoung;

public class Utils {
		
	/** function for creating agents */
	public static ArrayList<Agent> createAgents(int y, int e, int d, int i) {
		ArrayList<Agent> agents = new ArrayList<>();		
		Log.success("Creating " + y + " young agents");
		Log.success("Creating " + e + " elderly agents");
		Log.success("Creating " + d + " doctor agents");
		Log.success("Creating " + i + " infected agents");

		for(int j=0; j<y; j++) agents.add(new AgentYoung());
		for(int j=0; j<e; j++) agents.add(new AgentElderly());
		for(int j=0; j<d; j++) agents.add(new AgentDoctor());
		for(int j=0; j<i; j++) {
			Agent a = new AgentYoung();
				a.setInfected(true);
			agents.add(a);
		}
		return agents;
	}
	
	/** function to apply CLI arguments */
	public static void parseArguments(String[] args) {
		if(args.length > 0) {
			for(String arg : args) {
				switch(arg) {
					case "--nocolors":		// disables logging coloring
						Log.IDE = true;
						Log.success("Colored logging is disabled");
						break;
				}
			}
		}
	}
}
