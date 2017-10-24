package com.bparent.improPhoto.dao;

import com.bparent.improPhoto.domain.Categorie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@DataJpaTest
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql({"/schema.sql", "/data.sql"})
public class CategorieDaoTest {

    @Autowired
    private CategorieDao categorieDao;

    @Test
    public void shouldFindThreeCategories() {
        List<Categorie> cat = categorieDao.findAll();
        assertNotNull(cat);
        assertEquals(3, cat.size());
    }

    @Test
    public void shouldFindOneCategorieById() {
        Categorie cat = categorieDao.findOne(BigInteger.valueOf(1));
        assertNotNull(cat);
        assertEquals("cat1-nom", cat.getNom());
    }


}