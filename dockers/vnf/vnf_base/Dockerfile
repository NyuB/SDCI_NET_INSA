FROM node:sdci

WORKDIR	/
RUN touch /test.txt && \
	echo 'echo b >> /test.txt' > /scr.sh && \
	chmod +x /scr.sh
# VIM EMU set entry point for emulator gatekeeper
ENV VIM_EMU_CMD "bash /scr.sh"
ENV VIM_EMU_CMD_STOP "echo 'Stopping the container now.'"

# VIM-EMU CMD should always point to /bin/bash to not block the emulator
CMD /bin/bash
