/**
 *  Author: Samir MEDJIAH medjiah@laas.fr
 *  File : gateway.js
 *  Version : 0.1.0
 */
var http = require('http');
var url = require('url');
const si = require('systeminformation');
const forward = require('http-forward');
// --ip_A
// --port_A
// --ip_B
// --port_B
var LOCAL_PORT = 8888;
var ENDPOINT_A;
var ENDPOINT_B;
var index = 1;
var configured = false;
http.createServer(function (req, res) {
	if(configured){
		index = 1 - index;
		console.log(req.ip);
		if (index>0) {
	    	   req.forward = {target:"http://"+ENDPOINT_A.IP+":"+ENDPOINT_A.PORT};
	    	   forward(req, res);
		}
		else{
	    	   req.forward = {target:"http://"+ENDPOINT_B.IP+":"+ENDPOINT_B.PORT};
	    	   forward(req, res);
		}
	}
	else if(req.url.endsWith("config")){
		let data = '';
		var parsed;
  		req.on('data', chunk => {
    			data += chunk;
  		});
  		req.on('end', () => {
    		parsed = JSON.parse(data);
			ENDPOINT_A = {IP : parsed.ip_A, PORT : parsed.port_A};
			ENDPOINT_B = {IP : parsed.ip_B, PORT : parsed.port_B};
			configured=true;
		});
		res.write("Ack\n");
		res.end();
	}
}).listen(LOCAL_PORT);
