import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElevatorComponent extends JPanel {
    private static int eleNumber = 5;
    private static int floorNumber = 20;
    private static int DEFAULT_WIDTH = 500;
    private static int DEFAULT_HEIGHT = 800;
    public static ArrayList<Thread> running_elevators = new ArrayList<>();

    public static LinkedList<Elevator> elevators = new LinkedList<>();


    ElevatorComponent(int afloorNumber, int aeleNumber) {
        floorNumber = afloorNumber;
        eleNumber = aeleNumber;
        DEFAULT_HEIGHT = (int) Elevator.HEIGHT * floorNumber;
        DEFAULT_WIDTH = (int) Elevator.WIDTH * eleNumber * 2;
        for (int i = 0; i < eleNumber; i++) {
            elevators.add(new Elevator(i, floorNumber));
            Elevator tempt = elevators.get(i);
            int movement = i+1;
            Runnable r =()->{
                try {
                    while (true) {
                        tempt.working();
                        Thread.sleep(100);
                    }
                }
                catch (InterruptedException e){

                }
            };
            Thread up = new Thread(r);
            up.setName("ele"+movement);
            running_elevators.add(up);
            up.start();
        }
    }


    public void reset(int afloorNumber,int aEleNumber){
        for(Thread t:running_elevators){
            t.stop();
        }
        try{Thread.sleep(200);}catch (InterruptedException e){}
        elevators.clear();
        floorNumber = afloorNumber;
        eleNumber = aEleNumber;
        DEFAULT_HEIGHT = (int)Elevator.HEIGHT * floorNumber;
        DEFAULT_WIDTH = (int)Elevator.WIDTH * eleNumber * 2;
        for (int i = 0; i < eleNumber; i++) {
            elevators.add(new Elevator(i, floorNumber));
            Elevator tempt = elevators.get(i);
            int movement = i+1;
            Runnable r =()->{
                try {
                    while (true) {
                        tempt.working();
                        Thread.sleep(50);
                    }
                }
                catch (InterruptedException e){

                }
            };
            Thread up = new Thread(r);
            up.setName("ele"+movement);
            running_elevators.add(up);
            up.start();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g); // erase background
        Graphics2D g2 = (Graphics2D) g;
        for (Elevator b : elevators)
        {
            g2.fill(b.getShape());
        }

    }
    @Override
    public Dimension getPreferredSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }
}
