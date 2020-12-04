FROM ubuntu:trusty

WORKDIR	/

RUN 	apt-get update \
	&& apt-get install -y curl \
	&& apt-get install -y nodejs \ 
	&& curl http://homepages.laas.fr/smedjiah/tmp/server.js > /home/server.js ;
	
# set entry point for emulator gatekeeper
ENV VIM_EMU_CMD "node /home/server.js --local_ip \"127.0.0.1\" --local_port 8080 --local_name \"srv\" ; tail -f /dev/null"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
