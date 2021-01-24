# SDCI_NET_INSA 5SDBD
## Group Brice DECAESTECKER | Denis GOUVINE-BIRRER
## Ressources and production for INSA SDCI virtual network management project

### dockers/
Dockerfiles et ressources de build associées(scripts bash, codes nodejs, etc)
- vnf/ Docker images for vnfs
- hosts/ Docker images for server, gateways and devices
- js/ JS codes

### topology/
Python scripts pour instancier nos topologies
La topologie utilisée pour la simulation complète est topology_final.py

### gctrl/
Code source du General-Controller, modifié d'après une copie du template initialement fourni.
- api/ package regroupant nos classes de communications avec Ryu, VimEmu, VNFs ...
Manual : Main alternatif pour tester nos fonctions hors de la boucle MAPE-K


### errors/
Notes sur les problèmes, bugs, difficultés, screenshots, logs...

### docker_make/
Outils de build custom