import com.sun.javaws.util.JfxHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainScene {
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            JFrame frame = new ElevatorFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }



}

class ElevatorFrame extends JFrame {

    private ElevatorComponent eleShow;
    private WaitingQueue queue;
    private static int eleNumber = 5;
    private static int floorNumber = 15;
    private JPanel buttonPanel;
    private ElevatorReport report;

    public ElevatorFrame() {
        setTitle("ElevatorScene");
        queue = new WaitingQueue(floorNumber);
        add(queue,BorderLayout.EAST);
        eleShow = new ElevatorComponent(floorNumber, eleNumber);
        add(eleShow, BorderLayout.CENTER);
        report = new ElevatorReport();
        add(report,BorderLayout.SOUTH);



        // Text is here
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Elevator Simulation");
        title.setAlignmentX(CENTER_ALIGNMENT);
        JLabel author = new JLabel("1752894 ZhangZijian");
        author.setAlignmentX(CENTER_ALIGNMENT);
        JLabel notify = new JLabel("default 20 floors with 5 elevators");
        notify.setAlignmentX(CENTER_ALIGNMENT);
        textPanel.add(title);
        textPanel.add(author);
        textPanel.add(notify);
        // Button is here
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton setElevator = new JButton("Set elevator number.");
        JButton setFloor = new JButton("Set floor number.");
        JButton start = new JButton("Start!");
        JButton addPassenger = new JButton("Add passenger");
        JButton stopElevator = new JButton("Stop elevator");
        JButton pauseElevator = new JButton("Pause elevator");
        setElevator.addActionListener(event -> setEleNumber());
        setFloor.addActionListener(event -> setFloorNumber());
        start.addActionListener(event -> toStart());
        addPassenger.addActionListener(event -> toAddPassenger());
        stopElevator.addActionListener(event -> stopElevator());
        pauseElevator.addActionListener(event -> toPauseElevator());
        buttonPanel.add(Box.createVerticalStrut((int)(Math.max((int)((floorNumber-4)*Elevator.HEIGHT),0))));
        buttonPanel.add(setElevator);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(setFloor);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(addPassenger);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(stopElevator);
        buttonPanel.add(pauseElevator);
        buttonPanel.add(Box.createHorizontalStrut(20),BorderLayout.WEST);


        //add them all
        add(textPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.WEST);

        pack();
    }

    private void toPauseElevator(){
        String pause_info = JOptionPane.showInputDialog(eleShow,"Enter 2 numbers indicating the elevator number " +
                "and the time you want it to pause.\n 2 5\nmeans that elevator-2 will pause for additional 5 second in its" +
                "next stop.");
        String[] pause = {"0","0"};
        try {
            pause = pause_info.split("\\s+");
        }
        catch (NullPointerException e){}
        int target = 0;
        int time = 0;
        try {
            target = Integer.parseInt(pause[0]);
            time = Integer.parseInt(pause[1]);
        }
        catch (NumberFormatException err){

        }
        if(target<eleNumber && time >0){
            ElevatorComponent.elevators.get(target).set_pause(time);
        }
    }

    private void toAddPassenger() {
        String Movement = JOptionPane.showInputDialog(eleShow,"Enter 2 numbers indicating starting point" +
                ", end point and number. For example:\n 2 5 4\nmeans 4 people from 2 to 5");
        String[] fromAndTo = {"1","1","1"};
        try {
            fromAndTo = Movement.split("\\s+");
        }
        catch (NullPointerException e){}
        int from = 1;
        int to = 1;
        int number = 1;
        try {
            from = Integer.parseInt(fromAndTo[0]);
            to = Integer.parseInt(fromAndTo[1]);
            number = Integer.parseInt(fromAndTo[2]);
        }
        catch (NumberFormatException err){

        }
        from = Math.max(from,1);
        to = Math.min(to,floorNumber);
        for(int i = 0;i<number;i++) {
            queue.add(new People(from, to));
        }
    }

    private void toStart() {
        Mover toMove = new Mover();
        toMove.execute();

    }

    private void stopElevator(){
        String elevatorNums = JOptionPane.showInputDialog(eleShow, "Which elevator will you stop?");
        int aElevatorNumber = -1;
        try {
            aElevatorNumber = Integer.parseInt(elevatorNums);
        } catch (NumberFormatException err) {

        }
        if (aElevatorNumber != -1&& aElevatorNumber<ElevatorComponent.running_elevators.size()){
            // TODO stop an elevator
            ElevatorComponent.running_elevators.get(aElevatorNumber).interrupt();
        }
    }

    private void setEleNumber() {
        String elevatorNums = JOptionPane.showInputDialog(eleShow, "How many elevators do you want?" +
                "(note: any invalid inputs mean 5)");
        int aElevatorNumber = 5;
        try {
            aElevatorNumber = Integer.parseInt(elevatorNums);
        } catch (NumberFormatException err) {

        }
        eleNumber = aElevatorNumber;

        queue.reset(floorNumber);
        eleShow.reset(floorNumber,eleNumber);
        eleShow.repaint();

        report.reset();


    }

    private void setFloorNumber() {
        String FloorNum = JOptionPane.showInputDialog(eleShow, "How many floors do you want?" +
                "(note: any invalid inputs mean 20)");
        int afloorNumber = 5;
        try {
            afloorNumber = Integer.parseInt(FloorNum);
        } catch (NumberFormatException err) {

        }

        floorNumber = afloorNumber;

        queue.reset(floorNumber);
       // try{Thread.sleep(100);}catch (InterruptedException e){}
        eleShow.reset(floorNumber,eleNumber);
        eleShow.repaint();

        report.reset();
        buttonPanel.removeAll();

        JButton setElevator = new JButton("Set elevator number.");
        JButton setFloor = new JButton("Set floor number.");
        JButton start = new JButton("Start!");
        JButton addPassenger = new JButton("Add passenger");
        JButton stopElevator = new JButton("Stop elevator");
        JButton pauseElevator = new JButton("Pause elevator");
        setElevator.addActionListener(event -> setEleNumber());
        setFloor.addActionListener(event -> setFloorNumber());
        start.addActionListener(event -> toStart());
        addPassenger.addActionListener(event -> toAddPassenger());
        stopElevator.addActionListener(event -> stopElevator());
        pauseElevator.addActionListener(event -> toPauseElevator());
        buttonPanel.add(Box.createVerticalStrut((int)(Math.max((int)((floorNumber-4)*Elevator.HEIGHT),0))));
        buttonPanel.add(setElevator);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(setFloor);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(addPassenger);
        buttonPanel.add(Box.createVerticalStrut((int)((0.2)*Elevator.HEIGHT)));
        buttonPanel.add(stopElevator);
        buttonPanel.add(pauseElevator);
        buttonPanel.add(Box.createHorizontalStrut(20),BorderLayout.WEST);

        buttonPanel.repaint();

    }



    private class Mover extends SwingWorker<Integer,Integer>{
        @Override
        public Integer doInBackground(){
           try{
               while (true) {
                   queue.update();
                   eleShow.repaint();
                   report.update();
                   Thread.sleep(100);
               }
           }
           catch (InterruptedException e){}
           return 0;
        }

        @Override
        public void process(List<Integer> data){
            if(isCancelled()) return;
            eleShow.elevators.get(0).Y-=10;
            eleShow.repaint();
        }

        @Override
        public void done(){

        }

    }
}