package com.bparent.improPhoto.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public interface IConstants {

    public static final String DATE_FR_FORMAT = "dd/MM/yyyy";
    public static final SimpleDateFormat DATE_FR_FORMATTER = new SimpleDateFormat(DATE_FR_FORMAT);

    public static final Long LIMIT_PICTURES = 20L;
    public static final List<String> PICTURE_EXTENSION_ACCEPTED = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");
    public static final List<String> AUDIO_EXTENSION_ACCEPTED = Arrays.asList("mp3", "wav", "ogg");
    public static final List<String> VIDEO_EXTENSION_ACCEPTED = Arrays.asList("mp4");
    public static final String ZIP_EXTENSION = "zip";

    public interface IPath {
        public static final String MEDIAS = "medias/";
        public static final String MEDIAS_TEMP = IPath.MEDIAS + "temp/";

        public interface IPhoto {
            public static final String PHOTOS = MEDIAS + "photos/";

            public static final String PHOTOS_IMPRO = PHOTOS + "impro/";
            public static final String PHOTOS_INTRODUCTION = PHOTOS + "intro/";
            public static final String PHOTOS_JOUEURS = PHOTOS + "joueurs/";
            public static final String PHOTOS_PRESENTATION_DATES = PHOTOS + "dates/";
        }

        public interface IAudio {
            public static final String AUDIOS = MEDIAS + "audios/";

            public static final String AUDIOS_PLAYLIST = AUDIOS + "playlist/";
            public static final String AUDIOS_JINGLES = AUDIOS + "jingles/";
            public static final String AUDIOS_RESSOURCES = AUDIOS + "ressources/";
        }

        public interface IVideo {
            public static final String VIDEOS = MEDIAS + "videos/";
            public static final String VIDEO_NAME_JOUEURS = "joueurs.mp4";
            public static final String VIDEO_NAME_PRESENTATEUR = "presentateur.mp4";

            public static final String VIDEO_INTRO = VIDEOS + "intro/";
            public static final String VIDEO_INTRO_JOUEURS = VIDEO_INTRO + VIDEO_NAME_JOUEURS;
            public static final String VIDEO_INTRO_PRESENTATEUR = VIDEO_INTRO + VIDEO_NAME_PRESENTATEUR;
        }
    }

    public interface IEtatImproField {
        public static final String ECRAN = "ecran";
        public static final String ID_CATEGORIE = "id_categorie";
        public static final String TYPE_ECRAN = "type_ecran";
        public static final String BLOCK_MASQUES = "block_masques";
        public static final String INTEGRALITE = "integralite";
        public static final String PHOTO_COURANTE = "photo_courante";
        public static final String PHOTOS_CHOISIES = "photos_choisies";
        public static final String STATUT_DIAPO = "statut_diapo";
        public static final String CURRENT_SONG_NAME = "current_song_name";
        public static final String PLAYLIST_STATUS = "playlist_status";
        public static final String PLAYLIST_VOLUME = "playlist_volume";
        public static final String CATEGORIES_SHOWN = "categories_shown";

        public interface IStatutDiapo {
            public static final String LAUNCHED = "launched";
        }

        public interface IStatutPlaylist {
            public static final String PLAYING = "playing";
            public static final String PAUSED = "paused";
        }
    }


    public interface IImpro {
        public static final String FIRST_SCREEN = "SALLE_ATTENTE";
    }

}
