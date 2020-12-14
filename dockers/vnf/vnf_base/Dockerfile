FROM node:sdci

WORKDIR	/
ADD ./startup /bin/startup
RUN chmod +x /bin/startup

# VIM EMU set entry point for emulator gatekeeper
ENV VIM_EMU_CMD "startup"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# VIM-EMU CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash