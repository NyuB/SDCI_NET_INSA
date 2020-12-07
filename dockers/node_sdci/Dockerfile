#Base image for INSA SDCI project
#All npm and vim-emu ip dependencies installed

#Uses node debian stretch-12-slim as base 
# => Correctly acquires an IP addr when started on containernet (where alpine distributions do not)

#NB:this base image does not start any server nor set the VIM_EMU_CMD environment variables

#Test : when deploying this image with vim-emu, you should be able to ping it from any host connected to the datacenter (i.e inside or via switch path)

FROM node:12-slim

WORKDIR	/

RUN 	apt-get update \
	#VIM-EMU-Requirements
	&& apt-get install iproute -y \
	&& apt-get install net-tools -y \
	#\VER
	#UTILS
	#procps allows to use ps => usefull to verify if a node server is actually running
	&& apt-get install procps -y \
	&& apt-get install iputils-ping -y \
	#curl allows to test http connectitvity between containernet hosts
	&& apt-get install curl -y \
	#\UTILS
	#NPM
	&& npm install url \
	&& npm install http-forward \
	&& npm install express \
	&& npm install yargs \
	&& npm install systeminformation \
	&& npm install request
	#\NPM
# VIM-EMU-Requirement : CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
