package com.akash.redis.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    private String name;
    private Long followers;

    public User() {
    }

    public User(String name, Long followers) {
        this.name = name;
        this.followers = followers;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFollowers() {
		return followers;
	}

	public void setFollowers(Long followers) {
		this.followers = followers;
	}
	
	@Override
    public String toString() {
        return "User{id="+id+", name="+name+", followers="+followers+"}";
    }
}