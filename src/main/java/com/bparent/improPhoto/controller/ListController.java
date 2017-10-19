package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dao.CategorieDao;
import com.bparent.improPhoto.domain.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private CategorieDao dao;

    @RequestMapping("/categories")
    public List<Categorie> getAllCategories() {
        List<Categorie> target = new LinkedList<>();
        this.dao.findAll().forEach(target::add);
        return target;
    }

}
