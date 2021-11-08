package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArrayQueueTest {

	protected Queue<Integer> q;
	
	@BeforeEach
	void setUp() throws Exception {
		q = new ArrayQueue<>();
		q.enqueue(7833);
		q.enqueue(5422);
		q.enqueue(7834);
	}
	
	@Test
	void testWraparound() {
		try {
			q.dequeue();
			q.enqueue(1);
			q.enqueue(2);
			assertEquals(4, q.size());
			assertEquals(5422, q.front());
			q.enqueue(3);
			
		} catch (Exception ex) {
			fail("Should not have thrown exception.");
		}
	}

	@Test
	void testEnqueue() {
		// testing the "setup" enqueue calls
		try {
			assertEquals(7833, q.front());
			q.enqueue(1234);
			assertEquals(4, q.size());
			q.dequeue();
			q.dequeue();
			q.dequeue();
			assertEquals(1234, q.front());
			q.dequeue();
			assertTrue(q.isEmpty());
			for (int i=0; i<996; i++) {
				q.enqueue(i);
				q.dequeue();
			}
			q.enqueue(999);
		} catch (EmptyQueueException e) {
			// this intentionally blank
		}
	}

	@Test
	void testDequeue() {
		try {
			assertEquals(7833, q.dequeue());
			assertEquals(2, q.size());
			assertEquals(5422, q.dequeue());
			assertEquals(7834, q.dequeue());
			q.dequeue();
			fail("should have thrown exception");
		} catch (EmptyQueueException e) {
			
		}
	}

	@Test
	void testFront() {
		try {
			assertEquals(7833, q.front());
			assertEquals(3, q.size());
			q.dequeue();
			assertEquals(5422, q.front());
			q.dequeue();
			assertEquals(7834, q.front());
			q.dequeue();
			q.front();
			fail("should have thrown an error");
		} catch (EmptyQueueException e) {
			
		}
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
		assertFalse(q.isEmpty());
		try {
			// let's make our existing queue empty by dequeuing all the data
			q.dequeue();
			q.dequeue();
			q.dequeue();
			assertTrue(q.isEmpty());
			// let's test a queue that is empty from the beginning below:
			q = new ArrayQueue<>();
			assertTrue(q.isEmpty());
		} catch (Exception ex) {
			
		}
	}

}
