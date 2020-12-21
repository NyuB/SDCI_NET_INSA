Tous les Dockerfile utilisés seront regroupés ici
+ node\_sdci : docker à build sous node:sdci, base pour tous les docker js, adapté à un hôte standalone ou un déploiement via vim-emu
+ vnf : 
	- contient tous nos docker destinés à être lancé dans le datacenter via vim-emu
	- les dossiers vnf_vnfname sont à build sous le tag vnf:vnfname
	- ATTENTION : bien paramétrer les variables d'environements VIM\_EMU\_CMD , pas d'ENTRYPOINT bloquant, pas de commande bloquante dans VIM_EMU_CMD non plus (utiliser &)
+ hosts :
	- contient nos docker destinés à être déployé sur les hôtes containernet, hors DC (i.e. les gateway/device/server/client)
	- les dossiers host_hostname sont à build sous le tag host:hostname
+ js :
	- contient les fichiers javascripts des vnf/hôtes
+ tree/dockermakefile :
	- fichiers de build custom à utiliser avec docker_make.py pour regénérer toutes les images
