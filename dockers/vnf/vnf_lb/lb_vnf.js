/**
 *  Author: Samir MEDJIAH medjiah@laas.fr
 *  File : gateway.js
 *  Version : 0.1.0
 */
var request = require('request');
var http = require('http')
const si = require('systeminformation');
const forward = require('http-forward');
var argv = require('yargs').argv;
// --port
// --ip_A
// --port_A
// --ip_B
// --port_B

var LOCAL_PORT = argv.port
var ENDPOINT_A = {IP : argv.ip_A, PORT : argv.port_A};
var ENDPOINT_B = {IP : argv.ip_B, PORT : argv.port_B};
var index = 1;
http.createServer(function (req, res) {
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
}).listen(LOCAL_PORT);
