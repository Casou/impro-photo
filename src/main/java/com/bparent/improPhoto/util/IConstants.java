package com.bparent.improPhoto.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public interface IConstants {

    String DATE_FR_FORMAT = "dd/MM/yyyy";
    SimpleDateFormat DATE_FR_FORMATTER = new SimpleDateFormat(DATE_FR_FORMAT);

    Long LIMIT_PICTURES = 20L;
    List<String> PICTURE_EXTENSION_ACCEPTED = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");
    List<String> AUDIO_EXTENSION_ACCEPTED = Arrays.asList("mp3", "wav", "ogg");
    List<String> VIDEO_EXTENSION_ACCEPTED = Arrays.asList("mp4");
    String ZIP_EXTENSION = "zip";

    interface IPath {
        String MEDIAS = "medias/";
        String MEDIAS_TEMP = IPath.MEDIAS + "temp/";

        interface IPhoto {
            String PHOTOS = MEDIAS + "photos/";

            String PHOTOS_IMPRO = PHOTOS + "impro/";
            String PHOTOS_INTRODUCTION = PHOTOS + "intro/";
            String PHOTOS_JOUEURS = PHOTOS + "joueurs/";
            String PHOTOS_PRESENTATION_DATES = PHOTOS + "dates/";
        }

        interface IAudio {
            String AUDIOS = MEDIAS + "audios/";

            String AUDIOS_PLAYLIST = AUDIOS + "playlist/";
            String AUDIOS_JINGLES = AUDIOS + "jingles/";
            String AUDIOS_RESSOURCES = AUDIOS + "ressources/";
        }

        interface IVideo {
            String VIDEOS = MEDIAS + "videos/";
            String VIDEO_NAME_JOUEURS = "joueurs.mp4";
            String VIDEO_NAME_PRESENTATEUR = "presentateur.mp4";

            String VIDEO_INTRO = VIDEOS + "intro/";
            String VIDEO_INTRO_JOUEURS = VIDEO_INTRO + VIDEO_NAME_JOUEURS;
            String VIDEO_INTRO_PRESENTATEUR = VIDEO_INTRO + VIDEO_NAME_PRESENTATEUR;
        }
    }

    interface IEtatImproField {
        String ECRAN = "ecran";
        String ID_CATEGORIE = "id_categorie";
        String TYPE_ECRAN = "type_ecran";
        String BLOCK_MASQUES = "block_masques";
        String INTEGRALITE = "integralite";
        String PHOTO_COURANTE = "photo_courante";
        String PHOTOS_CHOISIES = "photos_choisies";
        String STATUT_DIAPO = "statut_diapo";
        String CURRENT_SONG = "current_song";
        String PLAYLIST_STATUS = "playlist_status";
        String PLAYLIST_VOLUME = "playlist_volume";
        String CATEGORIES_SHOWN = "categories_shown";

        interface IStatutDiapo {
            String LAUNCHED = "launched";
        }

        interface IStatutPlaylist {
            String PLAYING = "playing";
            String PAUSED = "paused";
        }
    }


    interface IImpro {
        String FIRST_SCREEN = "SALLE_ATTENTE";
    }

}
