This document is used to introduce our basic method we adopted and the basic thoughts we thought to finish this elevator simulation system.

In our source code directory there are the following things
The lib directory, we put all the libraries we need into this directory.
The src directory, we put all our source code in this directory.
The classes directory, we put out .class file into this directory for easy packing.
The createTables.sql file is our sql script to build the database we need.
The build.xml file is the ant script file to create databases, javac, package and other things.
In the setup directory, we put our java3D setup file into it.

I think our most important character is that we adopted the java3D graphic user interface. In out program, we used queues and inner panels rather than queue and inner panel in constructing some classes. By doing this, we increase the expansibility of our elevator simulation system.

Our main function is in the src.main.myMain.

One of the most important classes in our simulation system is Message and Queue. Because it is the basic way that panels pass the messages to the controller. We will show it below.
There are three important panels in out elevator simulation system.

One is InnerPanel. This Panel get the requiring of a user who in the elevator. When it gets a requiring, it sends this message into the Queue. The message includes the floor of the request.

Another is OutterPanels. There is an outerPanel in each floor. These panels are used to get the requiring of a person who is not in the elevator. It sends the message into the Queue.

The last is the AdminPanel; this panel is used for the administrator. This is used for administrator to start and stop the elevator. It also sends the messages into the Queue.

In the controller, there is a thread always removing elements from the Queue, so that the elevator will not be blocked by the full of the queue. Our most important algorithm is in this part, including our basic schedule algorithm. We used arrays to solve the schedule problem. We following below rules.

1. The elevator stays 3 seconds for customers to get in or get out. If some customers
want to get in and some want to get out, the elevator stays 6 seconds.
2. When there is no request for service, the elevator stays where it is. Namely, it is
idle.
3. When the elevator is idle, and a request comes from upwards or downwards, it
moves upwards or downwards. If a request comes from the story where it stays,
      the elevator opens its door to serve these customers.
      4. When the elevator goes upwards or downwards, it keeps its direction until there is
      no request from upwards or downwards. However, it breaks off to let customers
      get out or pick up customers if necessary.
      5. When the elevator can act more than one way according to aforementioned rules,
      it chooses the action with the highest priority. The priorities for actions, from the
      highest to the lowest, are:
      let customers get out
      let customers get in
      go downstairs
      go upstairs
      which is the same as those in the project information
      Our basic method to drive the elevator is by the requiring of the src.elevator.Elevator.
      When the elevator is idle, every certain time, the Elevator ask the controller whether to start moving or not. When the elevator is moving, Elevator asks the controller whether to stop to pick up somebody or not.

      When the controller get the asking of the Elevator, it changes the state of the elevator, and then tell the message center ( src.share.MessageCenter)  this message.

      When the message center knows this message, it dispatches this message to the inner panel, outer panel, admin panel.

