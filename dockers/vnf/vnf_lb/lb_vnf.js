const http = require('http');
const url = require('url');
const forward = require('http-forward');

var LOCAL_PORT = 8888;
var ENDPOINTS = [];
var ENDPOINT_A;//Must be configured
var ENDPOINT_B;//------------------

var index = 1;//Remote index, 0 to N-1 whre N is the number of remote targets

var configured = false;//Indicate ifthe VNF has been configured

http.createServer(function (req, res) {
	if(configured){//Wait configuration before applying NF
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
		//Load complete request body
  		req.on('data', chunk => {
    			data += chunk;
  		});
  		//Parse Json request and apply configuration
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
