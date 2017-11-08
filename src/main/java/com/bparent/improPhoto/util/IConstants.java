package com.bparent.improPhoto.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public interface IConstants {

    public static final String DATE_FR_FORMAT = "dd/MM/yyyy";
    public static final SimpleDateFormat DATE_FR_FORMATTER = new SimpleDateFormat(DATE_FR_FORMAT);

    public static final List<String> PICTURE_EXTENSION_ACCEPTED = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");
    public static final List<String> AUDIO_EXTENSION_ACCEPTED = Arrays.asList("mp3", "wmv");

    public interface IPath {
        public static final String MEDIAS = "medias/";

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
        }

        public interface IVideo {
            public static final String VIDEOS = MEDIAS + "videos/";

            public static final String VIDEO_INTRO = VIDEOS + "intro/";
            public static final String VIDEO_INTRO_JOUEURS = VIDEO_INTRO + "joueurs.mp4";
            public static final String VIDEO_INTRO_PRESENTATEUR = VIDEO_INTRO + "presentateur.mp4";
        }

    }

}
