package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.Version;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface VersionDao extends CrudRepository<Version, BigInteger> {

    List<Version> findAll();

}
