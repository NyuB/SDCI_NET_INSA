FROM node:sdci

WORKDIR	/

ADD ./*_vnf.js /home/vnf.js

# VIM EMU set entry point for emulator gatekeeper | ending '&' IS IMPORTANT
ENV VIM_EMU_CMD "node /home/vnf.js --port 8888 --ip_A 10.0.0.3 --ip_B 10.0.0.4 --port_A 8888 --port_B 8888 &"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# VIM-EMU CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
