package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.EtatImpro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EtatImproDao extends CrudRepository<EtatImpro, String> {

}
