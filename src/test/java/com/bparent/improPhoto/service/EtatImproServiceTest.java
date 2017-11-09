package com.bparent.improPhoto.service;

import com.bparent.improPhoto.dao.EtatImproDao;
import com.bparent.improPhoto.domain.Categorie;
import com.bparent.improPhoto.domain.EtatImpro;
import com.bparent.improPhoto.dto.EtatImproDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EtatImproServiceTest {

    @InjectMocks
    private EtatImproService etatImproService;

    @Mock
    private EtatImproDao statutDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(this.statutDao.findAll()).thenReturn(() -> new ArrayList<EtatImpro>().iterator());
    }

    @Test
    public void shouldGetFilledDto() {
        when(this.statutDao.findAll()).thenReturn(() -> Arrays.asList(
                new EtatImpro("ecran", "ecran1"),
                new EtatImpro("id_ecran", "1"),
                new EtatImpro("type_ecran", "type_ecran1"),
                new EtatImpro("block_masques", "1,2,3"),
                new EtatImpro("integralite", "0"),
                new EtatImpro("photo_courante", "4"),
                new EtatImpro("photos_choisies", "4,5,6"),
                new EtatImpro("statut_diapo", "statut_diapo1")
        ).iterator());
        EtatImproDto statut = this.etatImproService.getStatut();
        assertEquals("ecran1", statut.getEcran());
        assertEquals(Integer.valueOf(1), statut.getIdEcran());
        assertEquals("type_ecran1", statut.getTypeEcran());
        assertEquals(Arrays.asList(1, 2, 3), statut.getBlockMasques());
        assertFalse(statut.getIntegralite());
        assertEquals(Integer.valueOf(4), statut.getPhotoCourante());
        assertEquals(Arrays.asList(4, 5, 6), statut.getPhotosChoisies());
        assertEquals("statut_diapo1", statut.getStatutDiapo());
    }

    @Test
    public void shouldResetImproWithDefaultValues() {
        ArgumentCaptor<EtatImpro> etatImproCaptor = ArgumentCaptor.forClass(EtatImpro.class);
        this.etatImproService.resetImpro();

        verify(this.statutDao, times(8)).save(etatImproCaptor.capture());
        List<EtatImpro> allEntities = etatImproCaptor.getAllValues();
        int i = 0;
        assertEquals("ecran", allEntities.get(i++).getChamp());
        assertEquals("id_ecran", allEntities.get(i++).getChamp());
        assertEquals("type_ecran", allEntities.get(i++).getChamp());
        assertEquals("block_masques", allEntities.get(i++).getChamp());
        assertEquals("integralite", allEntities.get(i++).getChamp());
        assertEquals("photo_courante", allEntities.get(i++).getChamp());
        assertEquals("photos_choisies", allEntities.get(i++).getChamp());
        assertEquals("statut_diapo", allEntities.get(i++).getChamp());

        i = 0;
        assertEquals("SALLE_ATTENTE", allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
        assertNull(allEntities.get(i++).getValeur());
    }

}