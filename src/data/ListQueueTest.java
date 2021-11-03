package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListQueueTest {

	protected Queue<Integer> q;
	
	@BeforeEach
	void setUp() throws Exception {
		q = new ListQueue<>();
		q.enqueue(7833);
		q.enqueue(5422);
		q.enqueue(7834);
	}

	@Test
	void testEnqueue() {
		fail("Not yet implemented");
	}

	@Test
	void testDequeue() {
		try {
			q.dequeue();
			assertEquals(2, q.size());
			q.dequeue();
			q.dequeue();
			q.dequeue();
			fail("should have thrown exception");
		} catch (EmptyQueueException e) {
			
		}
	}

	@Test
	void testFront() {
		fail("Not yet implemented");
	}

	@Test
	void testSize() {
		assertEquals(3, q.size());
		try {
			q.dequeue();
		} catch (EmptyQueueException e) {
			e.printStackTrace();
		}
		assertEquals(2, q.size());
	}

	@Test
	void testIsEmpty() {
		fail("Not yet implemented");
	}

}
