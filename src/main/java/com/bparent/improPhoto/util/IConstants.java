package com.bparent.improPhoto.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public interface IConstants {

    public static final String PATH_PHOTOS = "photos/impro/";
    public static final String PATH_VIDEOS_INTRO = "photos/videos_intro/";
    public static final String PATH_VIDEOS_INTRO_JOUEURS = PATH_VIDEOS_INTRO + "joueurs.mp4";
    public static final String PATH_VIDEOS_INTRO_PRESENTATEUR = PATH_VIDEOS_INTRO + "presentateur.mp4";

    public static final String PATH_PHOTOS_JOUEURS = "photos/joueurs/";
    public static final String PATH_PHOTOS_PRESENTATION_DATES = "photos/dates/";

    public static final String DATE_FR_FORMAT = "dd/MM/yyyy";
    public static final SimpleDateFormat DATE_FR_FORMATTER = new SimpleDateFormat(DATE_FR_FORMAT);

    public static final List<String> PICTURE_EXTENSION_ACCEPTED = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");

    public interface IFiles {
        public static final String PHOTOS_IMPRO_FOLDER = "photos/impro/";
        public static final String PHOTOS_INTRODUCTION_FOLDER = "photos/intro/";
    }

}
