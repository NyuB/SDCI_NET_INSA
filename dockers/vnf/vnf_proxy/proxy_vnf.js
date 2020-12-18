const request = require('request');
const http = require('http');
const url = require('url');
const forward = require('http-forward');
var LOCAL_PORT = 8888;

//http://@proxy:8888/mininet/host/uri?remote=@mnhost:port
http.createServer(function (req, res) {
	console.log(req.ip);
	const queryObject = url.parse(req.url,true).query;
	var remote = queryObject.remote;
	req.forward = {target:"http://"+remote};
	forward(req, res);
}).listen(LOCAL_PORT);