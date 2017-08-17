package src.control;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import src.share.Message;
import src.share.Queue;
import src.share.Type;

public class ControllerTest extends TestCase {
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        return new TestSuite(ControllerTest.class);
    }

    private Queue queue;

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /***************************************************************************
     * Rules3: When the elevator is idle, and a request comes from upwards or
     * downwards, it moves upwards or downwards. If a request comes from the
     * story where it stays, the elevator opens its door to serve these
     * customers.
     **************************************************************************/

    /**
     * Test "When the elevator is idle, and a request comes from upwards , it
     * moves ."
     */
    public void testRulesThree1()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setLocation(5);
        queue.enqueue(new OuterDownMessage(10));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }// this makes the controller has enough time to response to the

        // message
        assertEquals(controller.need_start(), 1);
    }

    /**
     * Test "When the elevator is idle, If a request comes from the story where
     * it stays, the elevator opens its door to serve these customers."
     */
    public void testRulesThree2()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setLocation(5);
        queue.enqueue(new OuterDownMessage(5));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }// this makes the controller has enough time to response to the

        // message
        assertEquals(controller.need_start(), 0);
    }

    /**
     * Test "When the elevator is idle, and a request comes from downwards, it
     * moves downwards."
     */
    public void testRulesThree3()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setLocation(5);
        queue.enqueue(new OuterDownMessage(3));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }// this makes the controller has enough time to response to the

        // message
        assertEquals(controller.need_start(), 2);
    }

    /***************************************************************************
     * Rules 4:When the elevator can act more than one way according to
     * aforementioned rules, it chooses the action with the highest priority.
     * The priorities for actions, from the highest to the lowest, are: let
     * customers get out; let customers get in; go downstairs; go upstairs
     **************************************************************************/
    /** The elevator will go downstairs first. */
    public void testRulesFour1()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setStatus(true);
        queue.enqueue(new OuterDownMessage(10));
        queue.enqueue(new OuterDownMessage(2));
        int[] a = new int[1];

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(controller.need_open_door(a));
    }

    /** pick up people,when he is in the same direction. */
    public void testRulesFour2()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setStatus(true);
        queue.enqueue(new OuterDownMessage(10));
        queue.enqueue(new OuterUpMessage(2));
        int[] a = new int[1];

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(controller.need_open_door(a));
    }

    /**
     * The elevator will satisfy the inner request first.
     */
    public void testRulesFive1()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setLocation(5);
        queue.enqueue(new InnerMessage(7));
        queue.enqueue(new OuterUpMessage(3));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(controller.need_start(), 1);
    }

    /**
     * test: The elevator will come down first when there is both up out panel
     * request and outter request.
     */
    public void testRulesFive2()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setLocation(5);
        queue.enqueue(new OuterDownMessage(7));
        queue.enqueue(new OuterUpMessage(3));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(controller.need_start(), 2);
    }

    /**
     * test: The elevator will come down first when there is both inner panel
     * request.
     */
    public void testRulesFive3()
    {
        queue = new Queue();
        Controller controller = new Controller(queue);
        controller.setLocation(5);
        queue.enqueue(new InnerMessage(7));
        queue.enqueue(new InnerMessage(3));

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(controller.need_start(), 2);
    }

}

/** the classes below is exactly copyed from Messages in src.panels */
class InnerMessage implements Message {
    private int floor;

    public InnerMessage(int i)
    {
        floor = i;
    }

    public int getNumber()
    {
        return floor;
    }

    public Type getType()
    {
        return Type.INNER;
    }

}

class OuterDownMessage implements Message {

    private int floor;

    public OuterDownMessage(int i)
    {
        floor = i;
    }

    public int getNumber()
    {
        return floor;
    }

    public Type getType()
    {
        return Type.OUTER_DOWN;
    }

}

class OuterUpMessage implements Message {

    private int floor;

    public OuterUpMessage(int i)
    {
        floor = i;
    }

    public int getNumber()
    {
        return floor;
    }

    public Type getType()
    {
        return Type.OUTER_UP;
    }

}
