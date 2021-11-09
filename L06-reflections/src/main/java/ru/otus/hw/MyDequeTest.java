package ru.otus.hw;

/**
 * @author Aleksandr Semykin
 */
public class MyDequeTest {

    private MyDeque<Integer> deque;
    
    @Before
    public void init() {
        log("init");
        deque = new MyDeque<>();
    }

    @Before
    public void init1() {
        log("init1");
//        throw new RuntimeException("in init1");
    }

    @Test
    public void givenDequeWithTwoElements_whenCallRemoveFirst_thenReturnFirst() {
        log("passedTest");
        deque.addFirst(1);
        deque.addFirst(2);
        int first = deque.peekFirst();

        int removeFirst = deque.removeFirst();


        if (first != removeFirst) {
            throw new RuntimeException("expected: " + first + ", actual: " + removeFirst);
        }
    }
    
    @Test
    public void givenDequeWithTwoElements_whenCallRemoveFirstWrongImpl_thenReturnFirst() {
        log("failedTest");
        deque.addFirst(1);
        deque.addFirst(2);
        int first = deque.peekFirst();

        int removeFirst = deque.removeFirstWrongImpl();

        if (first != removeFirst)
            throw new RuntimeException("expected: " + first + ", actual: " + removeFirst);
    }
    
    @After
    public void tearDown() {
        log("tearDown");
        deque = null;
    }

    @After
    public void tearDown1() {
        log("tearDown1");
        deque = null;
        throw new RuntimeException("in tearDown1");
    }

    private void log(String methodName) {
        System.out.println(new StringBuilder()
                .append("hashCode: ").append(hashCode())
                .append(", method: ").append(methodName));
    }
}
