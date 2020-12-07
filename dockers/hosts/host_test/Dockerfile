#exec node_startup to start server
FROM node:sdci

WORKDIR	/

ADD ./server.js /home/server.js
ADD ./startup /bin/startup
RUN chmod +x /bin/startup 

CMD /bin/bash
