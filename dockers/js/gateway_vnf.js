/**
 *  Author: Samir MEDJIAH medjiah@laas.fr
 *  File : gateway.js
 *  Version : 0.1.0
 */

var express = require('express');
var app = express();
app.use(express.json()); // for parsing application/json

var request = require('request');
const si = require('systeminformation');
// --local_ip
// --local_port
// --local_name
// --remote_ip
// --remote_port
// --remote_name

var LOCAL_PORT = 8888;
var LOCAL_ENDPOINT;
var REMOTE_ENDPOINT;
var configured = false;

const E_OK              = 200;
const E_CREATED         = 201;
const E_FORBIDDEN       = 403;
const E_NOT_FOUND       = 404;
const E_ALREADY_EXIST   = 500;


var db = {
        gateways : new Map()
    };

function addNewGateway(gw) {
    var res = -1;
    if (!db.gateways.get(gw.Name)) {
        db.gateways.set(gw.Name, gw);
        res = 0;
    }
    return res;
}

function removeGateway(gw) {
    if (db.gateways.get(gw.Name))
        db.gateways.delete(gw.Name);
}
    

function doPOST(uri, body, onResponse) {
    request({method: 'POST', uri: uri, json : body}, onResponse); 
}

function register() {
    doPOST(
        'http://' + REMOTE_ENDPOINT.IP + ':' + REMOTE_ENDPOINT.PORT + '/gateways/register', 
        {
            Name : LOCAL_ENDPOINT.NAME, 
            PoC : 'http://' + LOCAL_ENDPOINT.IP + ':' + LOCAL_ENDPOINT.PORT, 
        },
        function(error, response, respBody) {
            console.log(respBody);
        }
    );
}


app.post('/gateways/register', function(req, res) {
    console.log(req.body);
    var result = addNewGateway(req.body);
    if (result === 0)
        res.sendStatus(E_CREATED);  
    else
        res.sendStatus(E_ALREADY_EXIST);  
 });
app.post('/devices/register', function(req, res) {
    console.log(req.body);
    doPOST(
        'http://' + REMOTE_ENDPOINT.IP + ':' +REMOTE_ENDPOINT.PORT + '/devices/register',
        req.body,
        function(error, response, respBody) {
            console.log(respBody);
            res.sendStatus(E_OK); 
        }
    )
 });
 app.post('/device/:dev/data', function(req, res) {
    console.log(req.body);
    var dev = req.params.dev;
    doPOST(
        'http://' + REMOTE_ENDPOINT.IP + ':' +REMOTE_ENDPOINT.PORT + '/device/' + dev + '/data',
        req.body,
        function(error, response, respBody) {
            console.log(respBody);
            res.sendStatus(E_OK); 
        }
    )
});
app.get('/gateways', function(req, res) {
    console.log(req.body);
    let resObj = [];
    db.gateways.forEach((v,k) => {
        resObj.push(v);
    });
    res.send(resObj);
});
app.get('/gateway/:gw', function(req, res) {
    console.log(req.body);
    var gw = req.params.gw;
    var gateway = db.gateways.get(gw);
    if (gateway)
        res.status(E_OK).send(JSON.stringify(gateway));
    else
        res.sendStatus(E_NOT_FOUND);
});

app.get('/ping', function(req, res) {
    console.log(req.body);
    res.status(E_OK).send({pong: Date.now()});
});
app.get('/health', function(req, res) {
    console.log(req.body);
    si.currentLoad((d) => {
        console.log(d);
        res.status(E_OK).send(JSON.stringify(d));
    })
});

//VNF configuration
app.put("/config", function(req,res) {
	console.log(req.body);
	var configObj = req.body;
	LOCAL_ENDPOINT = {IP : configObj.local_ip, PORT : LOCAL_PORT, NAME : configObj.local_name};
	REMOTE_ENDPOINT =  {IP : configObj.remote_ip, PORT : configObj.remote_port, NAME : configObj.remote_name};
	configured = true;
	register();
	res.status(E_OK).send("");
});

app.listen(LOCAL_PORT , function () {
    console.log("Vnf Gateway waiting config on "+LOCAL_PORT);
});