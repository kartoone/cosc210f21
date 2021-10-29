/**
 * 
 */
package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author brtoone
 *
 */
class ArrayStackTest {

	protected ArrayStack<String> airportStack;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		airportStack = new ArrayStack<>();
		airportStack.push("BHM");
		airportStack.push("ATL");
		airportStack.push("MSP");		
	}

	/**
	 * Test method for {@link data.ArrayStack#ArrayStack()}.
	 */
	@Test
	void testArrayStack() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link data.ArrayStack#ArrayStack(int)}.
	 */
	@Test
	void testArrayStackInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link data.ArrayStack#push(java.lang.Object)}.
	 */
	@Test
	void testPush() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link data.ArrayStack#pop()}.
	 */
	@Test
	void testPop() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link data.ArrayStack#top()}.
	 */
	@Test
	void testTop() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link data.ArrayStack#size()}.
	 */
	@Test
	void testSize() {
		assertEquals(3,airportStack.size());
	}

	/**
	 * Test method for {@link data.ArrayStack#isEmpty()}.
	 */
	@Test
	void testIsEmpty() {
		assertFalse(airportStack.isEmpty());
	}

}
