#exec node_startup to start server
FROM node:sdci

WORKDIR	/

ADD ./gateway.js /home/gateway.js
Add ./device.js /home/device.js
ADD ./startup /bin/startup
ADD ./start_devices /bin/start_devices
RUN chmod +x /bin/startup 
RUN chmod +x /bin/start_devices 
CMD /bin/bash
