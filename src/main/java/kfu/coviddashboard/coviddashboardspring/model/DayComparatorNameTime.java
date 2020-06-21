package kfu.coviddashboard.coviddashboardspring.model;

import java.util.Comparator;

public class DayComparatorNameTime implements Comparator<Day> {
    public int compare(Day d1, Day d2){
        int ret = d1.getCity().compareTo(d2.getCity());
        if(ret!=0)
            return ret;
        else
        {
            return d1.getTimestamp().compareTo(d2.getTimestamp());
        }
    }
}
