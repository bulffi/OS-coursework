import org.omg.PortableInterceptor.INACTIVE;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;


public class Elevator {
    class Schedual{
        private LinkedList<Integer> to_stop = new LinkedList<>();
        private int state = 0;// 0 stop 1 up -1 down
        private boolean nobody = true;
        private int nobody_state = 0;
        public synchronized void add_nobody_schedual(int goal,boolean up){
            assert to_stop.size()==0;
            to_stop.add(goal);
            nobody = true;
            if(up){
                nobody_state = 1;
            }
            else {
                nobody_state = -1;
            }
        }
        public synchronized boolean getNobody(){
            return nobody;
        }

        public synchronized void add_schedual(int goal,boolean up){
            if(to_stop.contains(goal)) return;
            nobody = false;
            to_stop.add(goal);
            if(state!=0) {
                if(state==-1) {
                    to_stop.sort((i,j)->{
                    if(i<j) return 1;
                    else if (i>j) return -1;
                    else return 0;
                    });
                }
                else {
                    to_stop.sort((i,j)->{
                        if(i<j) return -1;
                        else if (i>j) return 1;
                        else return 0;
                    });
                }
            }
            else {
                if(up){
                    state = 1;
                }
                else {
                    state = -1;
                }
            }
        }
        public synchronized int get_next() {
            if (to_stop.size() != 0) {
                return to_stop.getFirst();
            } else {
                return -1;
            }
        }
        public synchronized void arrive_at(int position){
            assert position == to_stop.getFirst();
            to_stop.removeFirst();
            nobody = false;
            if (to_stop.size()==0){
                state = 0;
            }
        }
        public synchronized int getState(){
            if(nobody){
                return nobody_state;
            }
            return state;
        }
        public synchronized void setState(int aState){
            if(nobody){
                nobody_state = aState;
            }
            else state = aState;
        }
    }

    public static final int WIDTH = 20;
    public static final int HEIGHT = 48;

    private double X;
    public double Y;

    private int ID;
    private int floorNumber = 20;

    private int passenger_number = 0;
    public final int MAX_CAPACITY = 20;
    private int position = 1;
    public Schedual scheduler = new Schedual();

    private LinkedList<People> passengers = new LinkedList<>();
    private ArrayList<floorManager> waiting = WaitingQueue.waiting;

    private Boolean to_pause = false;
    private int interval = 0;

    Elevator(int aID, int aFloorNumber){
        floorNumber = aFloorNumber;
        ID = aID;
        Y = (floorNumber - 1)*HEIGHT;
        X = (ID)*2*WIDTH;

    }
    public void set_pause(int aInterval){
            to_pause = true;
            interval = aInterval;

    }

    public int getPassenger_number()
    {
        return passenger_number;
    }

    private double positionToY(int position){
        return (floorNumber-position)*Elevator.HEIGHT;
    }

    public void working() throws InterruptedException {
        Thread.sleep(50);
        int next_position = scheduler.get_next();
        if (next_position == -1){
            int up_search = position;
            int down_search = position;
            while (up_search<=floorNumber||down_search>=1){
                if(up_search<=floorNumber&&waiting.get(up_search).dealUP()){
                    scheduler.add_schedual(up_search,true);
                    return;
                }
                if (down_search>=1&&waiting.get(down_search).dealDOWN()){
                    scheduler.add_schedual(down_search,false);
                    return;
                }
                up_search++;
                down_search--;
            }
            up_search = position;
            down_search = position;
            while (up_search<=floorNumber||down_search>=1){
                if(up_search<=floorNumber&&waiting.get(up_search).dealDOWN()){
                    scheduler.add_nobody_schedual(up_search,true);
                    return;
                }
                if (down_search>=1&&waiting.get(down_search).dealUP()){
                    scheduler.add_nobody_schedual(down_search,false);
                    return;
                }
                up_search++;
                down_search--;
            }
            try {
                Thread.sleep(200);
            }
            catch (InterruptedException e){}
            return;
        }
        else {
            if (positionToY(next_position)==Y) {
                passengers.removeIf((passenger) -> {
                    return passenger.to == next_position;
                });
                passenger_number = passengers.size();
                if (passenger_number == MAX_CAPACITY) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {}
                    if (scheduler.getState() == 1) {
                        Y -= 4;
                        waiting.get(next_position).addUP();
                    } else {
                        Y += 4;
                        waiting.get(next_position).addDOMN();
                    }
                    scheduler.arrive_at(next_position);
                    return;
                }
                if (scheduler.getState() == 1) {
                    if(scheduler.getNobody()){
                        LinkedList<People> passenger_to_board = waiting.get(next_position).getDOWN_passenger(MAX_CAPACITY - passenger_number);
                        for (People p : passenger_to_board) {
                            scheduler.add_schedual(p.to, false);
                            passengers.add(p);
                            passenger_number = passengers.size();
                        }
                    }
                    else {
                        LinkedList<People> passenger_to_board = waiting.get(next_position).getUP_passenger(MAX_CAPACITY - passenger_number);
                        for (People p : passenger_to_board) {
                            scheduler.add_schedual(p.to, true);
                            passengers.add(p);
                            passenger_number = passengers.size();
                        }
                    }
                    waiting.get(next_position).addUP();
                } else if(scheduler.getState()==-1) {
                    if(scheduler.getNobody()){
                        LinkedList<People> passenger_to_board = waiting.get(next_position).getUP_passenger(MAX_CAPACITY - passenger_number);
                        for (People p : passenger_to_board) {
                            scheduler.add_schedual(p.to, true);
                            passengers.add(p);
                            passenger_number = passengers.size();
                        }
                    }
                    else {
                        LinkedList<People> passenger_to_board = waiting.get(next_position).getDOWN_passenger(MAX_CAPACITY - passenger_number);
                        for (People p : passenger_to_board) {
                            scheduler.add_schedual(p.to, false);
                            passengers.add(p);
                            passenger_number = passengers.size();
                        }
                    }
                }
                waiting.get(next_position).addUP();
                waiting.get(next_position).addDOMN();
                scheduler.arrive_at(next_position);
                try {
                        if(to_pause){
                            Thread.sleep(interval*1000);
                            to_pause = false;
                        }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                return;
            }
            else {
                int state_of_move;
                if(positionToY(next_position)>Y){state_of_move = -1;}
                else {state_of_move = 1;}
                scheduler.setState(state_of_move);
                if(state_of_move==1){
                    Y -= 4;
                    position = floorNumber - (int) Y/Elevator.HEIGHT;
                    if(MAX_CAPACITY-passenger_number>0) {
                        int upSearch = position + 1;
                        while (upSearch <= floorNumber) {
                            if(waiting.get(upSearch).dealUP()){
                                scheduler.add_schedual(upSearch,true);
                                return;
                            }
                            upSearch++;
                        }
                    }

                }
                else {
                    Y += 4;
                    position = floorNumber - (int) Y/Elevator.HEIGHT;
                    if(MAX_CAPACITY - passenger_number >0){
                        int downSearch = position -1;
                        while (downSearch>=1){
                            if(waiting.get(downSearch).dealDOWN()){
                                scheduler.add_schedual(downSearch,false);
                                return;
                            }
                            downSearch--;
                        }
                    }
                }


            }
        }
    }

    public Rectangle2D getShape() {
        return new Rectangle2D.Double(X,Y,WIDTH,HEIGHT);
    }

}
