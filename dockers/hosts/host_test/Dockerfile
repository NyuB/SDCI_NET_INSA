FROM node:sdci

WORKDIR	/

ADD ./server.js /home/server.js

#ending '&' IS IMPORTANT
ENTRYPOINT node /home/server.js --local_ip 127.0.0.1 --local_port 8888 --local_name srv & tail -f /dev/null

CMD /bin/bash
