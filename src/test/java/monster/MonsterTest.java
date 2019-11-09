package monster;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

public class MonsterTest {
	public Monster monster = null;

	@Test
	public void givenParametes_whenCreateMonster_assertAttributesAllDefaultValue() {
		monster = new Monster(0,0,0,0,"Unicorn"); // we are using Unicorn as it's the only one that doesn't have move() overrided.
		
		assertEquals(monster.getXGrid(), 0);
		assertEquals(monster.getYGrid(), 0);
		assertEquals(monster.getHP(), 0);
		assertEquals(monster.getSpeed(),0);
		assertEquals(monster.getType(), "Unicorn");
	}
}
