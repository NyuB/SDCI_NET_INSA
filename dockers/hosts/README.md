Utiliser un éxécutable(voir startup\_template, à étoffer) pour paramétrer les différents serveurs/client au lancement
Dans tous les cas, placer un script de démarrage exécutable dans l'image, qui sera lancé au démarrage du réseau avec les paramètres appropriés(pour ne pas créer une image par device par exemple...)

- host:server : serveur applicatif
- host:gateway : gateway de redirection
- host:device : emulation de capteur
- host:test : serveur basique pour tester les connectivités http