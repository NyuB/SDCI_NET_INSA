const http = require('http');
const url = require('url');
const forward = require('http-forward');

var LOCAL_PORT = 8888;
var VIP;//Must be configured
var REMOTE_ENDPOINT;//------

var configured = false;//Indicate if the VNF has been configured

http.createServer(function (req, res) {
	if(configured){//Wait configuration before applying NF
		console.log(req.ip);
		if (req.ip == VIP) {
	    	   req.forward = {target:"http://"+REMOTE_ENDPOINT.IP+":"+REMOTE_ENDPOINT.PORT};
	    	   forward(req, res);
		}
		else{
	    	   res.writeHead(200, {"Content-Type":"text/html"});
	    	   res.write("Fayawol\n");
	    	   res.end();
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
			REMOTE_ENDPOINT = {IP : parsed.remote_ip, PORT : parsed.remote_port};
			VIP = parsed.vip;
			configured=true;
		});
		res.write("Ack\n");
		res.end();
	}
}).listen(LOCAL_PORT);
