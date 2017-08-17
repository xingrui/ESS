package src.share;

import javax.swing.*;

/** The class to store the messages sending from panel to controller. */
public class Queue {
    private static final int SIZE = 1;

    private Message[] message = new Message[SIZE];

    private int last = 0, length = 0;

    public Queue()
    {
    }

    /**
     * Take a message from the queue.When the queue is empty, the thread will
     * blocked here.
     */
    synchronized public Message dequeue()
    {
        if (length == 0)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        if (length == 0) {
            JOptionPane.showMessageDialog(null, "dequeue error");
            System.exit(0);
        }

        Message temp = message[last];
        last = (last + 1) % SIZE;
        length--;
        notify();
        return temp;
    }

    /**
     * Put a message to the queue.When the queue is full,the thread will blocked
     * here.However this will not happen when the system is normal.Because the
     * controller will pop the messages continusly.
     */
    synchronized public void enqueue(Message message)
    {
        if (length == SIZE)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        if (length == SIZE) {
            JOptionPane.showMessageDialog(null, "dequeue error");
            System.exit(0);
        }

        this.message[(last + length) % SIZE] = message;
        length++;
        notify();
    }
}
