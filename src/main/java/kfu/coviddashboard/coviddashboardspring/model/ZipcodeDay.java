package kfu.coviddashboard.coviddashboardspring.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "zipcodetable")
public class ZipcodeDay extends Entry{
    private String zipcode;

    public String getName(){
        return zipcode;
    }
    public void setName(String name){
        this.zipcode= zipcode;
    }
}
