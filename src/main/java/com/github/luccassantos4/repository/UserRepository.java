package com.github.luccassantos4.repository;

import javax.enterprise.context.ApplicationScoped;

import com.github.luccassantos4.entities.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{

}
