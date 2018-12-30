package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.JingleCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;
import java.util.List;

@RepositoryRestResource
public interface JingleCategoryDao extends CrudRepository<JingleCategory, BigInteger> {

    List<JingleCategory> findAll();

}
