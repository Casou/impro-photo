package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.DateImpro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;
import java.util.List;

@RepositoryRestResource
public interface DateImproDao extends CrudRepository<DateImpro, BigInteger> {

    List<DateImpro> findAll();

    void deleteByIdNotIn(List<BigInteger> ids);

}
