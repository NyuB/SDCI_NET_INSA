const http = require('http');
const url = require('url');
const forward = require('http-forward');
var LOCAL_PORT = 8888;
var ENDPOINTS = [];
var range = 0;
var index = 0;//Remote index, 0 to N-1 whre N is the number of remote targets

var configured = false;//Indicate if the VNF has been configured

http.createServer(function (req, res) {
	if(req.url.endsWith("config")){
		let data = '';
		var parsed;
		//Load complete request body
  		req.on('data', chunk => {
    			data += chunk;
  		});
  		//Parse Json request and apply configuration
  		req.on('end', () => {
    		parsed = JSON.parse(data);
    		ENDPOINTS = parsed.endpoints; //Update the list of gateways available
    		index = 0;//Reset the next gateway index
    		range = ENDPOINTS.length;//Update the number of gateways available
    		configured = (range > 0);
		});
		res.write("Ack\n");
		res.end();
	}
	else if(configured){//Wait configuration before applying NF
		index ++;
		index %= range;
		endpoint = ENDPOINTS[index];
		console.log(endpoint);
		var urle = "http://"+ endpoint.ip + ":" + endpoint.port;
		req.forward = {target:urle};
		forward(req, res);
	}
}).listen(LOCAL_PORT);
