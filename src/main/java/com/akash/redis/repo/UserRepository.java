package com.akash.redis.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akash.redis.data.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long>
{

}