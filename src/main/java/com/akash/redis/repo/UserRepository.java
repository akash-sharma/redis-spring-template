package com.akash.redisEsDemo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.akash.redisEsDemo.data.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long>
{

}