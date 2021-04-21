package app;

import java.util.ArrayList;

import agents.Agent;
import agents.AgentDeadly;
import agents.AgentHealthy;
import agents.AgentInfected;
import agents.AgentMedic;

public class Utils {
		
	/** function for creating agents */
	public static ArrayList<Agent> createAgents(int h, int m, int i, int d) {
		ArrayList<Agent> agents = new ArrayList<>();
		
		Log.success("Creating " + h + " healthy agents");
		Log.success("Creating " + m + " medic agents");
		Log.success("Creating " + i + " infected agents");

		for(int j=0; j<h; j++) {
			agents.add(new AgentHealthy());
		}
		for(int j=0; j<m; j++) {
			agents.add(new AgentMedic());
		}
		for(int j=0; j<i; j++) {
			agents.add(new AgentInfected());
		}
		for(int j=0; j<d; j++) {
			agents.add(new AgentDeadly());
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
