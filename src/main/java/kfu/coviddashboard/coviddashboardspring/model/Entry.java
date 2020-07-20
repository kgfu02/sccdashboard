package kfu.coviddashboard.coviddashboardspring.model;
import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
//create table dashboardtable (id int not null auto_increment, timestamp Timestamp not null, count int, city varchar(255),primary key (id));
@MappedSuperclass
public abstract class Entry{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private long id;
    private Timestamp timestamp;
    private Integer count;

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

    abstract public String getName();
    abstract public void setName(String name);

}
