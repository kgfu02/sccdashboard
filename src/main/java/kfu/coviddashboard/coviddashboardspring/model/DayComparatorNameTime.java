package kfu.coviddashboard.coviddashboardspring.model;

import java.util.Comparator;

public class DayComparatorNameTime implements Comparator<Entry> {
    public int compare(Entry d1, Entry d2){
        int ret = d1.getName().compareTo(d2.getName());
        if(ret!=0)
            return ret;
        else
        {
            return d1.getTimestamp().compareTo(d2.getTimestamp());
        }
    }
}
