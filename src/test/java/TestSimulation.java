import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;

import utils.Log;
import utils.Utils;
import utils.argsparser.*;
import agents.*;

public class TestSimulation {
	
	@Test
	/** Testing number conversion */
	public void testNumber() {
		assertTrue(Utils.isNumber("69"));
	}
	
	@Test
	/** Testing argument */
	public void testArg() {
		Argument a = new Argument("--test", "-t", "Argument for testing", new ArgAction() {
			@Override
			public void run() {
				Log.success("Just testin");
			}
		});
		assertEquals("test", a.getLongName());
		assertEquals("t", a.getShortName());
		assertEquals("Argument for testing", a.getHelp());
		assertEquals(ArgType.TOGGLE, a.getType());
	}
	
	@Test
	/** Testing agent's properties */
	public void testAgent() {
		Agent a = new AgentYoung();	
		assertEquals(AgentColor.YOUNG, a.getColor());
		
		/* test default values */
		assertFalse(a.isInfected());
		assertFalse(a.isDeadlyInfected());
		assertFalse(a.isImmune());
		assertFalse(a.isDead());
		
		/* change values */
		a.setInfected(true);
		a.setDeadlyInfected();
		a.setImmune(true);
		a.kill();
		
		/* test changed values */
		assertTrue(a.isInfected());
		assertTrue(a.isDeadlyInfected());
		assertTrue(a.isImmune());
		assertTrue(a.isDead());
	}	
}
