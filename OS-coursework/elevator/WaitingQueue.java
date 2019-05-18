import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

class People{
    public int from;
    public int to;
    People(int aFrom, int aTo){
        from = aFrom;
        to = aTo;
    }
}
class floorManager{
    public int floor;
    public int up = 0;
    public int down = 0;
    private boolean isUP;
    private boolean isDOWN;
    private LinkedList<People> UP = new LinkedList<>();
    private LinkedList<People> DOWN = new LinkedList<>();

    floorManager(int aFloor){floor = aFloor;}
    public synchronized boolean dealUP(){
        if(isUP){
            isUP = false;
            return true;
        }
        return false;
    }
    public synchronized boolean dealDOWN(){
        if(isDOWN){
            isDOWN = false;
            return true;
        }
        return false;
    }
    public synchronized void addPassenger(People passenger){
        if(passenger.to>floor){
            UP.add(passenger);
            isUP = true;
            up++;
        }
        else if(passenger.to<floor){
            DOWN.add(passenger);
            isDOWN = true;
            down++;
        }
    }
    public synchronized void addUP(){
        if(UP.size()!=0){
            isUP = true;
        }
    }
    public synchronized void addDOMN(){
        if(DOWN.size()!=0){
            isDOWN = true;
        }
    }
    public synchronized LinkedList<People> getUP_passenger(int capacity){
        int actual_movement = Math.min(capacity,UP.size());
        LinkedList<People> onBoard = new LinkedList<>();
        for (int i =0;i < actual_movement; i++){
            onBoard.add(UP.getFirst());
            UP.removeFirst();
        }
        up -= actual_movement;
        return onBoard;
    }
    public synchronized LinkedList<People> getDOWN_passenger(int capacity){
        int actual_movement = Math.min(capacity,DOWN.size());
        LinkedList<People> onBoard = new LinkedList<>();
        for (int i =0;i < actual_movement; i++){
            onBoard.add(DOWN.getFirst());
            DOWN.removeFirst();
        }
        down -= actual_movement;
        return onBoard;
    }
}


public class WaitingQueue extends JPanel {

    public static ArrayList<floorManager> waiting = new ArrayList<>();
    private ArrayList<JLabel> notifys = new ArrayList<>();
    private int floorNumber = 20;
    private int DEFAULT_WIDTH ;
    private int DEFAULT_HEIGHT ;



    WaitingQueue(int afloorNumber){
        floorNumber = afloorNumber;
        DEFAULT_WIDTH = (int)(3*Elevator.WIDTH);
        DEFAULT_HEIGHT = (int)(floorNumber*Elevator.HEIGHT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int i = 0 ;i <= floorNumber; i++){
            waiting.add(new floorManager(i));
        }
        for (int i = 0; i < floorNumber; i++){
            notifys.add(new JLabel("<html><body>"+waiting.get(floorNumber-i).floor+"<br>"+"UP:"+waiting.get(floorNumber-i).up+"  "+"DOWN:"
                    +waiting.get(floorNumber-i).down+"</body></html>"));
        }
        for(int i =0;i<floorNumber;i++){
            notifys.get(i).setAlignmentX(CENTER_ALIGNMENT);
            add(notifys.get(i));
        }

    }

    public void reset(int afloorNumber){
        waiting.clear();
        notifys.clear();
        removeAll();
        repaint();
        floorNumber = afloorNumber;
        DEFAULT_WIDTH = (int)(3*Elevator.WIDTH);
        DEFAULT_HEIGHT = (int)(floorNumber*Elevator.HEIGHT);
        for(int i = 0 ;i <= floorNumber; i++){
            waiting.add(new floorManager(i));
        }
        for (int i =0;i<floorNumber;i++){
            notifys.add(new JLabel("<html><body>"+waiting.get(floorNumber-i).floor+"<br>"+"UP:"+waiting.get(floorNumber-i).up+"  "+"DOWN:"
                    +waiting.get(floorNumber-i).down+"</body></html>"));
        }
        for(int i =0;i<floorNumber;i++){
            notifys.get(i).setAlignmentX(CENTER_ALIGNMENT);
            add(notifys.get(i));
        }
        revalidate();
    }

    public void update(){
        for(int i =0;i<floorNumber;i++){
            notifys.get(i).setText("<html><body>"+waiting.get(floorNumber-i).floor+"<br>"+"UP:"+waiting.get(floorNumber-i).up+"  "+"DOWN:"
                    +waiting.get(floorNumber-i).down+"</body></html>");
        }
        revalidate();
    }

    public void add(People people){
        waiting.get(people.from).addPassenger(people);
    }

    @Override
    public Dimension getPreferredSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }


}
