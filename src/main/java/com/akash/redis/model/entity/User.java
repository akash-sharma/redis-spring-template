package com.akash.redis.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQ_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    private String name;
    private Long followers;

    private LocalDateTime createdAt = LocalDateTime.now();

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
    public String toString() {
        return "User{id="+id+", name="+name+", followers="+followers+"}";
    }
}
