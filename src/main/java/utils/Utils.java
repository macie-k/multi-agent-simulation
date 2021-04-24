package utils;

import java.util.ArrayList;
import java.util.Random;

import agents.Agent;
import agents.AgentDoctor;
import agents.AgentElderly;
import agents.AgentType;
import agents.AgentYoung;

public class Utils {
		
	/** function for creating agents */
	public static ArrayList<Agent> createAgents(int young, int elderly, int doctors, int infected) {
		ArrayList<Agent> agents = new ArrayList<>();
		Random r = new Random();
		
		Log.success("Creating " + young + " young agents");
		Log.success("Creating " + elderly + " elderly agents");
		Log.success("Creating " + doctors + " doctor agents");
		Log.success("Creating " + infected + " infected agents");

		for(int i=0; i<young; i++) agents.add(new AgentYoung());
		for(int i=0; i<elderly; i++) agents.add(new AgentElderly());
		for(int i=0; i<doctors; i++) agents.add(new AgentDoctor());
		
		int i=0;
		while(i < infected) {
			Agent a = agents.get(r.nextInt(agents.size()));
			if(!a.isInfected() && a.getType() != AgentType.DOCTOR) {
				a.setInfected(true);
				i++;
			}
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
