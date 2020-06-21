package kfu.coviddashboard.coviddashboardspring.model;

import java.util.Comparator;

public class DayComparatorTime implements Comparator<Day> {
    public int compare(Day d1, Day d2){
        if(d1.getTimestamp().before(d2.getTimestamp()))
            return -1;
        if(d1.getTimestamp().after(d2.getTimestamp()))
            return 1;
        return 0;
    }
}
