package edu.utdallas.cs6303.finalproject.model.database;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public String getName() {
        return this.name;
    }

    public Long getID() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }
}