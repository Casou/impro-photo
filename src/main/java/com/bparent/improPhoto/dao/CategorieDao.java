package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.Categorie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.math.BigInteger;

@RepositoryRestResource
public interface CategorieDao extends CrudRepository<Categorie, BigInteger> {

    // http://localhost:8000/categories/all
//    @RestResource(path = "all")
//    List<Categorie> findAll();

    // http://localhost:8000/categories/1
    // http://localhost:8000/categories/search/by-id?id=1
    @RestResource(path = "by-id")
    Categorie findById(@Param("id") BigInteger id);

    // http://localhost:8000/categories/search/by-name?name=Test
//    @Query("Select m from Categorie m where m.nom like %:name%")
//    @RestResource(path = "by-name")
//    List<Categorie> findByName(@Param("name") String name);

    Categorie save(Categorie categorie);
    void delete(Categorie categorie);
    void delete(BigInteger id);
    
}
