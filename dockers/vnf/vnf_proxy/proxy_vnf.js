/**
 *  Author: Samir MEDJIAH medjiah@laas.fr
 *  File : gateway.js
 *  Version : 0.1.0
 */
var request = require('request');
var http = require('http');
var url = require('url');
const forward = require('http-forward');
http.createServer(function (req, res) {
	console.log(req.ip);
	const queryObject = url.parse(req.url,true).query;
	var remote = queryObject.remote;
	req.forward = {target:"http://"+remote};
	forward(req, res);
}).listen(LOCAL_PORT);