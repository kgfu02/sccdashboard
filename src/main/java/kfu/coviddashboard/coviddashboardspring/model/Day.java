package kfu.coviddashboard.coviddashboardspring.model;
import javax.persistence.Entity;
import javax.persistence.Table;

//create table dashboardtable (id int not null auto_increment, timestamp Timestamp not null, count int, city varchar(255),primary key (id));
@Entity
@Table(name = "dashboardtable")
public class Day extends Entry{
    private String city;

    public String getName(){
        return city;
    }
    public void setName(String name){ this.city = name; }

}
