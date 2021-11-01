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
		airportStack = new ArrayStack<>();
		assertEquals(0, airportStack.size());
		assertEquals(ArrayStack.DEFAULT_MAXSIZE, airportStack.maxsize);
		for (int i=0; i<airportStack.maxsize; i++) {
			airportStack.push("ZZZ");
		}
		try {
			airportStack.push("ZZZ");
			fail("Should have thrown error");
		} catch (Error er) {
			System.out.println("This error was thrown correctly: " + er);
		}
	}

	/**
	 * Test method for {@link data.ArrayStack#ArrayStack(int)}.
	 */
	@Test
	void testArrayStackInt() {
		airportStack = new ArrayStack<>(3);
		assertEquals(0, airportStack.size());
		assertEquals(3, airportStack.maxsize);
		airportStack.push("ZZZ");
		airportStack.push("ZZZ");
		airportStack.push("ZZZ");
		try {
			airportStack.push("ZZZ");
			fail("Should have thrown error");
		} catch (Error er) {
			System.out.println("This error was thrown correctly: " + er);
		}
	}

	/**
	 * Test method for {@link data.ArrayStack#push(java.lang.Object)}.
	 */
	@Test
	void testPush() {
		airportStack.push("HOU");
		assertEquals(4, airportStack.size());
		assertEquals("HOU", airportStack.pop());
		airportStack.pop();
		airportStack.pop();
		airportStack.pop();
		airportStack.push("XYZ");
		assertEquals(1, airportStack.size());
		assertEquals("XYZ", airportStack.pop());
	}

	/**
	 * Test method for {@link data.ArrayStack#pop()}.
	 */
	@Test
	void testPop() {
		assertEquals("MSP", airportStack.pop());
		assertEquals("ATL", airportStack.pop());
		assertEquals("BHM", airportStack.pop());
		try {
			airportStack.pop();
			fail("An exception was NOT thrown");
		} catch(Exception ex) {
		
		}
	}

	/**
	 * Test method for {@link data.ArrayStack#top()}.
	 */
	@Test
	void testTop() {
		assertEquals("MSP", airportStack.top());
		assertEquals("MSP", airportStack.top());
		assertEquals(2, airportStack.size());
		airportStack.pop();
		airportStack.pop();
		airportStack.pop();
		try {
			airportStack.top();
			fail("An exception was NOT thrown");
		} catch(Exception ex) {
		
		}
	}

	/**
	 * Test method for {@link data.ArrayStack#size()}.
	 */
	@Test
	void testSize() {
		assertEquals(3,airportStack.size());
		airportStack.pop();
		airportStack.pop();
		airportStack.pop();
		assertEquals(0, airportStack.size());
	}

	/**
	 * Test method for {@link data.ArrayStack#isEmpty()}.
	 */
	@Test
	void testIsEmpty() {
		assertFalse(airportStack.isEmpty());
		airportStack.pop();
		airportStack.pop();
		airportStack.pop();
		assertTrue(airportStack.isEmpty());
	}

}
