INSERT INTO IMP_PARAMETRE(CONTEXT, KEY, VALUE_TYPE, VALUE, DESCRIPTION) VALUES ('GENERAL', 'MODAL_LOADING', 'BOOLEAN', 'false', 'Si vrai, il y aura un écran de chargement noir (avec progression) au démarrage de l''application. Si faux, l''écran de chargement sera caché.');
INSERT INTO IMP_PARAMETRE(CONTEXT, KEY, VALUE_TYPE, VALUE, DESCRIPTION) VALUES ('GENERAL', 'UPDATE_URL', 'STRING', 'http://localhost:8000/versioning', 'URL vers le fichier JSON de publication des mises à jour.');

INSERT INTO IMP_VERSION(BUILD_NUMBER, BUILD_DATE, BUILD_DESCRIPTION, URL_DOWNLOAD) VALUES ('1.0.0', null, 'Version PHP - WebSocket', null);
INSERT INTO IMP_VERSION(BUILD_NUMBER, BUILD_DATE, BUILD_DESCRIPTION, URL_DOWNLOAD) VALUES ('2.0.0', null, 'Version PHP - Popup', null);
INSERT INTO IMP_VERSION(BUILD_NUMBER, BUILD_DATE, BUILD_DESCRIPTION, URL_DOWNLOAD) VALUES ('3.0.0', null, 'Version Java - WebSocket', null);
INSERT INTO IMP_VERSION(BUILD_NUMBER, BUILD_DATE, BUILD_DESCRIPTION, URL_DOWNLOAD) VALUES ('3.1.8', TO_DATE('01/06/2018', 'dd/mm/YYYY'), 'Refonte Backoffice + consolidation des WebSockets', null);
INSERT INTO IMP_VERSION(BUILD_NUMBER, BUILD_DATE, BUILD_DESCRIPTION, URL_DOWNLOAD) VALUES ('3.1.9', TO_DATE('15/09/2018', 'dd/mm/YYYY'), 'Version exportable pour Raspberry', null);
INSERT INTO IMP_VERSION(BUILD_NUMBER, BUILD_DATE, BUILD_DESCRIPTION, URL_DOWNLOAD) VALUES ('3.1.9', TO_DATE('15/09/2018', 'dd/mm/YYYY'), 'Version exportable pour Raspberry', null);
