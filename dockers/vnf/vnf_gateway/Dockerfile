FROM node:sdci

WORKDIR	/

ADD ./*_vnf.js /home/vnf.js

# VIM EMU set entry point for emulator gatekeeper | ending '&' IS IMPORTANT
ENV VIM_EMU_CMD "node /home/vnf.js --local_ip 10.0.0.1 --remote_ip 10.0.0.2 --local_port 8888 --remote_port 8080 --local_name gw_vnf --remote_name srv &"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# VIM-EMU CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
