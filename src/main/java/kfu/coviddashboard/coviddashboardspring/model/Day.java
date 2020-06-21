package kfu.coviddashboard.coviddashboardspring.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;
import java.sql.Timestamp;
//create table dashboardtable (id int not null auto_increment, timestamp Timestamp not null, count int, city varchar(255),primary key (id));
@Entity
@Table(name = "dashboardtable")
public class Day{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private long id;
    private Timestamp timestamp;
    private Integer count;
    private String city;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Integer getcount(){
        return count;
    }
    public void setCount(Integer count){
        this.count = count;
    }

    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city = city;
    }


}
