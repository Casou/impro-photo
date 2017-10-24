package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.Remerciement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RemerciementDao extends CrudRepository<Remerciement, Integer> {

    List<Remerciement> findAll();

}
