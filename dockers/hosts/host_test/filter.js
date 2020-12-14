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
// --local_ip
// --local_port
// --remote_ip
// --remote_port
// --vip_ip
// --vip_port

var LOCAL_ENDPOINT = {IP : argv.local_ip, PORT : argv.local_port};
var VIP_ENDPOINT = {IP : argv.vip_ip, PORT : argv.vip_port};
var REMOTE_ENDPOINT = {IP : argv.remote_ip, PORT : argv.remote_port};

http.createServer(function (req, res) {
	index = 1 - index;
	console.log(req.ip);
	if (req.ip == VIP_ENDPOINT.IP) {
    	   req.forward = {target:"http://"+REMOTE_ENDPOINT.IP+":"+REMOTE_ENDPOINT.PORT};
    	   forward(req, res);
	}
}).listen(LOCAL_ENDPOINT.PORT);