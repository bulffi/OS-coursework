import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class ElevatorReport extends JPanel {


    private LinkedList<Elevator> elevatorLinkedList = ElevatorComponent.elevators;
    private ArrayList<JLabel> notify = new ArrayList<>();
    public ElevatorReport(){
        for (int i = 0 ; i <  elevatorLinkedList.size();i++){
            String state = new String();
            if(elevatorLinkedList.get(i).scheduler.getState()==0){
                state = "Waiting";
            }
            else if (elevatorLinkedList.get(i).scheduler.getState()==1){
                state = "Up";
            }
            else {
                state = "Down";
            }
            notify.add(new JLabel("<html><body>"+i+"<br>"+"On/All:"+elevatorLinkedList.get(i).getPassenger_number()+
                    "/"+elevatorLinkedList.get(i).MAX_CAPACITY+"<br>"+"State: "
                    +state+"<br>:to"+elevatorLinkedList.get(i).scheduler.get_next()+" "+elevatorLinkedList.get(i).scheduler
                    .getNobody()+"</body></html>"));
        }
        for (int i = 0;i<elevatorLinkedList.size();i++){
            notify.get(i).setAlignmentY(CENTER_ALIGNMENT);
            add(notify.get(i));
        }
    }

    public void reset(){
        removeAll();
        notify.clear();
        String state ;
        for (int i = 0 ; i <  elevatorLinkedList.size();i++){
            if(elevatorLinkedList.get(i).scheduler.getState()==0){
                state = "Waiting";
            }
            else if (elevatorLinkedList.get(i).scheduler.getState()==1){
                state = "Up";
            }
            else {
                state = "Down";
            }
            notify.add(new JLabel("<html><body>"+i+"<br>"+"On/All:"+elevatorLinkedList.get(i).getPassenger_number()+
                    "/"+elevatorLinkedList.get(i).MAX_CAPACITY+"<br>"+"State: "
                    +state+"<br>:to"+elevatorLinkedList.get(i).scheduler.get_next()+" "+elevatorLinkedList.get(i).scheduler
                    .getNobody()+"</body></html>"));
        }
        for (int i = 0;i<elevatorLinkedList.size();i++){
            notify.get(i).setAlignmentY(CENTER_ALIGNMENT);
            add(notify.get(i));
        }
        revalidate();

    }

    public void update(){
        String state;
        for(int i =0;i < elevatorLinkedList.size();i++){
            if(elevatorLinkedList.get(i).scheduler.getState()==0){
                state = "Waiting";
            }
            else if (elevatorLinkedList.get(i).scheduler.getState()==1){
                state = "Up";
            }
            else {
                state = "Down";
            }
            notify.get(i).setText("<html><body>"+i+"<br>"+"On/All:"+elevatorLinkedList.get(i).getPassenger_number()+
                    "/"+elevatorLinkedList.get(i).MAX_CAPACITY+"<br>"+"State: "
                    +state+"<br>:to"+elevatorLinkedList.get(i).scheduler.get_next()+" "+elevatorLinkedList.get(i).scheduler
                    .getNobody()+"</body></html>");
        }
    }




}
