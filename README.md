# Impro Photo

## Installation

### Sur PC et Raspberry
#### Installer Java 8
Aller sur le site d'Oracle [...]

#### Charger les fichiers
Télécharger sur le poste le fichier *.jar* et le fichier *launchImpro.bat* / *launchImpro.sh*.
[...]
* Sur Raspberry, si on veut lancer le Chrome automatiquement, ajouter l'option -browser
* Sur Raspberry, déposer et dézipper le fichier dans le dossier /var/www/impro-photo.

### Sur Raspberry uniquement
#### Activation du SSH et changement du mot de passe
Lancer la commande `raspi-config`, choisir l'option `Interfacing options > SSH` puis `Enable`.

*Password par défaut du user "pi" : "raspberry" . Password temporaire du user "pi" : "legit".*

Choisir ensuite `change user password` pour redifinir un nouveau mot de passe (par défaut = "raspberry", changé la première fois en "legit").

**Important** : Une fois que le Raspberry aura une IP statique (plus loin dans cette notice), on ne pourra plus y accéder par SSH. 
Il faudra lancer la commande `sudo dhclient -v` **avec un câble éthernet branché** à votre box pour pouvoir renouveler l'IP.

### Lancer le programme au démarrage du Raspberry
*(Facultatif)* Modifier le fichier .bashrc et ajouter la ligne
```
alias impro='cd /var/www/impro-photo && sudo ./laucnhImpro.sh'
```

Créer un fichier dans `~/.config/autostart/impro.desktop` et y insérer le contenu suivant : 
```
[Desktop Entry]
Type=Application
Exec=xterm -geometry '75x15+10+50 -e 'sudo /var/www/impro-photo/launchImpro.sh; bash'
Hidden=false
NoDisplay=false
X-GNOME-Autostart-enabled=true
```

#### Configuration d'un écran 3.5 pouces (facultatif)
[Tutoriel pour installer les drivers](https://www.waveshare.com/wiki/3.5inch_RPi_LCD_(A))

#### Désactiver l'extinction automatique de l'écran
Dans le fichier `/boot/config.txt`, ajouter les lignes suivantes : 
```
# power down monitor when lockscreen enabled
hdmi_blanking=1
```

Dans le fichier `~/.config/lxsession/LXDE-pi/autostart`, ajouter les lignes suivantes :
```
@xset s 0 0
@xset s noblank
@xset s noexpose
@xset dpms 0 0 0
```

#### Configurer le DNS (nom de domaine)
Lancer la commande suivante `sudo vim /etc/hosts` et ajouter les lignes suivantes :
```
172.24.1.1  impro
172.24.1.1  impro.fr
172.24.1.1  impro-diapo
172.24.1.1  impro-diapo.fr
``` 

#### Configuration des droits d'accès
Lancer le script [... script qui donne les droits au script download-update.sh]

#### Point d'accès Wifi et DNS
[Tutoriel pour passer le Raspberry en mode routeur](https://frillip.com/using-your-raspberry-pi-3-as-a-wifi-access-point-with-hostapd/)
* SSID du réseau : impro-diapo-prive
* Mot de passe : legitlegit

## Configuration de Chrome pour le PC spectateur
Depuis avril 2018, pour lutter contre les pubs intrusives, Chrome a une nouvelle politique d'autoplay. 

Le logiciel contrôle des vidéos et de l'audio sans contrôle direct de l'utilisateur (c'est le code qui s'en charge). Il faut donc débloquer ce comportement sur Chrome.

Pour ça, aller sur l'URL suivante : ```chrome://flags/#autoplay-policy``` et placer la propriété ```Autoplay policy``` sur la valeur ```No user gesture is required```.

## Démarrer l'application

### Sur Windows
Lancer le script launchImpro.exe. Une fois que l'adresse IP est affichée, vous pouvez lancer Chrome 

### Sur le Raspberry
L'application se lance toute seule au démarrage. Le raspberry se lance en mode point d'accès Wifi. Il faut se connecter au réseau impro-diapo-prive.

## Liens utiles (si lancé sur Raspberry)
* [http://impro-diapo.fr](http://impro-diapo.fr) : URL principale de l'application
* [http://impro-diapo.fr/console](http://impro-diapo.fr/console) : Accès à la base de données (fichier ```jdbc:h2:file:./datas/dbImproPhoto```, login : ```sa```)

**Si l'application est lancée sur un PC** : 
Remplacer _impro-diapo.fr_ par l'adresse IP et le port fourni par le script .bat ou .sh 

## Accéder au Raspberry via SSH (Windows)
Connecter le raspberry en éthernet sur une box ou un routeur et le mettre en route. Ouvrir une invite de commande et taper la commande
suivante `arp -a`. L'adresse du Raspberry commence par `192.168.0` (il faudra les tester une à une pour avoir la bonne adresse).

Pour transfert des fichiers, passer par une application telle que Filezilla. Pour accéder à l'invite de commande du Raspberry, 
utiliser Putty :
* user : pi
* password : _le password SSH précédemment défini_ 
