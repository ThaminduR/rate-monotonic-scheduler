package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class Scheduler {

    static List<Task> schedule(final List<Task> taskList) {

        int lcm = calcLCM(taskList);
        List<Task> waitingList = new ArrayList<>();
        List<Task> outList = new ArrayList<>();

        for(int timeUnit = 0; timeUnit < lcm; timeUnit++){

            //add iterative tasks into the waiting list
            for(Task p : taskList)
                if(timeUnit % p.getPeriod() == 0)
                    for(int i = 0; i < p.getET(); i++)
                        waitingList.add(p);

            if(! waitingList.isEmpty()) {
                //the highest priority task has the minimum period
                Collections.sort(waitingList);
                outList.add(waitingList.get(0));
                waitingList.remove(0);
            } else
                outList.add(null);
        }

        return outList;
    }


    static int calcLCM(List<Task> taskList) {

        int lcm = taskList.get(0).getPeriod();
        for(boolean flag = true; flag; ) {
            for(Task x : taskList) {
                if(lcm % x.getPeriod() != 0) {
                    flag = true;
                    break;
                }
                flag = false;
            }
            lcm = flag? (lcm + 1) : lcm;
        }

        return lcm;
    }

    static String Cal(List<Task> taskList) {
        double cal1 = 0.00;
        for (Task process : taskList) {
            cal1 = cal1 + (((double) process.getET()) / ((double) process.getPeriod()));
        }
        int n = taskList.size();
        double cal2 = ((double) n) * ((Math.pow((double) 2, ((1 / ((double) n)))) - (double) 1));
        boolean schedulable = (cal1 <= cal2);
        if (schedulable){
        	return "This process set can be scheduled by RMS";
        }else{
        	return "This process set may not be able to scheduled by RMS";
        }
    }
}
