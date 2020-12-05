FROM node:sdci

WORKDIR	/

ADD ./*_vnf.js /home/vnf.js

# VIM EMU set entry point for emulator gatekeeper | ending '&' IS IMPORTANT
ENV VIM_EMU_CMD "node /home/vnf.js --local_ip 127.0.0.1 --local_port 8888 --local_name srv &"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# VIM-EMU CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
