package com.akash.redis.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akash.redis.model.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

	List<User> findAllByName(String name);
}