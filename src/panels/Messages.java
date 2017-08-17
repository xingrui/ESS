package src.panels;

import src.share.Message;
import src.share.Type;

/** Messages that generated from AdminPanel. */
class AdminMessage implements Message {

    private int number;

    public final int START = 0, STOP = 1;

    public AdminMessage(String s)
    {
        if (s.equalsIgnoreCase("start"))
            number = START;
        else if (s.equalsIgnoreCase("stop"))
            number = STOP;
        else if (s.equals("Auto Play"))
            number = 2;
        else
            number = 3;
    }

    public Type getType()
    {
        return Type.ADMIN;
    }

    public int getNumber()
    {
        return number;
    }

}

/** Messages that generated from InnerPanel. */
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

/** Messages that somebody want to get downstairs use this elevator. */
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

/** Messages that somebody want to get upstairs use this elevator. */
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
/**Another Messages used in comunicating from panels and control models*/
class OtherMessage implements Message {

    private Type type;

    public OtherMessage(String s)
    {
        if (s.startsWith("OPEN"))
            type = Type.OPEN;
        else if (s.startsWith("CLOSE"))
            type = Type.CLOSE;
        else if (s.startsWith("EMERGENCY"))
            type = Type.EMERGENCY;
        else if (s.startsWith("STOP"))
            type = Type.STOP;
        else
            System.err.println("No Such Message!");
    }

    public Type getType()
    {

        return type;
    }

    public int getNumber()
    {

        return 0;
    }
}