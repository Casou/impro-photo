package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.Parametre;
import com.bparent.improPhoto.domain.ParametrePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametreDao extends CrudRepository<Parametre, ParametrePK> {

    List<Parametre> findAll();
    Parametre findById(ParametrePK id);
    Parametre save(Parametre categorie);

}
