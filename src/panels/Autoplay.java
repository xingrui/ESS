package src.panels;

public class Autoplay {
    InnerPanel in[];

    OuterPanel out[];

    boolean a[][] = new boolean[10][10];

    private boolean flag = true;

    public Autoplay(InnerPanel in[], OuterPanel out[])
    {
        this.in = in;
        this.out = out;
        new Generate().start();
    }

    /** Stop the anto play. */
    public void stop()
    {
        flag = false;
    }

    /**
     * Tell that the elevator is in which floor and the person in the floor can
     * come in now.
     */
    public void comeIn(int floor)
    {
        for (int i = 0; i < 10; i++) {
            if (a[floor - 1][i]) {
                final int num = i + 1;
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        in[0].click(num);
                    }

                } .start();
                a[floor - 1][i] = false;
            }

        }

    }

    /* A thread generatng randam number. */
    private class Generate extends Thread {
        @Override
        public void run()
        {
            while (flag) {
                int begin = (int)(Math.random() * 10);
                int end = begin;

                while (end == begin) {
                    end = (int)(Math.random() * 10);
                }

                if (end > begin)
                    out[begin].click(true);
                else
                    out[begin].click(false);

                a[begin][end] = true;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
