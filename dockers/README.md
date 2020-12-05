Tous les Dockerfile utilisés seront regroupés ici
+ node\_sdci : docker à build sous node:sdci, base pour tous les docker js, adapté à un hôte tandalone ou un déploiement via vim-emu
+ vnfds : 
	- contient tous nos docker destinés à être lancé dans le datacenter via vim-emu (i.e. nos vnf)
	- à build sous vnf:myvnfname 
	- ATTENTION : bien paramétrer les variables d'environements VIM\_EMU\_CMD , pas d'ENTRYPOINT bloquant
+ hosts :
	- contiens nos docker destinés à être déployé sur les hôtes containernet, hors DC (i.e. les gateway/device/server/client)
	-à build sous host:myhostname
