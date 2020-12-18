FROM node:sdci

WORKDIR	/

ADD ./*_config_vnf.js /home/vnf.js

# VIM EMU set entry point for emulator gatekeeper | ending '&' IS IMPORTANT
ENV VIM_EMU_CMD "node /home/vnf.js &"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# VIM-EMU CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
