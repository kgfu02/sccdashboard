package kfu.coviddashboard.coviddashboardspring.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "keymetrictable")
public class KeyMetric {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private long id;
    private Timestamp timestamp;
    private float metric;
    private String type;

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

    public float getMetric() {return metric;}
    public void setMetric(float metric) {this.metric = metric;}

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
