package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.Jingle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;
import java.util.List;

@RepositoryRestResource
public interface JingleDao extends CrudRepository<Jingle, BigInteger> {

    List<Jingle> findAll();

}
