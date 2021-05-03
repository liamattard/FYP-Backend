package com.example.fypBackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "System", schema = "public")
public class System {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer system_id;

    public Integer getSystem_id() {
        return this.system_id;
    }

    public void setSystem_id(Integer system_id) {
        this.system_id = system_id;
    }

    public String getSystem_name() {
        return this.system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    @Column(name = "system_name")
    private String system_name;

}
