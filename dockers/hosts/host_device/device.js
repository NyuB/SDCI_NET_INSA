/**
 *  Author: Samir MEDJIAH medjiah@laas.fr
 *  File : device.js
 *  Version : 0.1.0
 */

var express = require('express');
var app = express();
app.use(express.json());
var request = require('request');

var argv = require('yargs').argv;
// --local_ip
// --local_port
// --local_name
// --remote_ip
// --remote_port
// --remote_name
// --send_period (ms)

var LOCAL_ENDPOINT = {IP : argv.local_ip, PORT : argv.local_port, NAME : argv.local_name};
var REMOTE_ENDPOINT = {IP : argv.remote_ip, PORT : argv.remote_port, NAME : argv.remote_name};

var DATA_PERIOD = argv.send_period;
var intervalID;
function doPOST(uri, body, onResponse) {
    request({method: 'POST', uri: uri, json : body}, onResponse); 
}

function register() {
    doPOST(
        'http://' + REMOTE_ENDPOINT.IP + ':' + REMOTE_ENDPOINT.PORT + '/devices/register', 
        {
            Name : LOCAL_ENDPOINT.NAME, 
            PoC : 'http://' + LOCAL_ENDPOINT.IP + ':' + LOCAL_ENDPOINT.PORT, 
        },
        function(error, response, respBody) {
            console.log(respBody);
        }
    );
}

var dataItem = 0;
function sendData() {
    doPOST(
        'http://' + REMOTE_ENDPOINT.IP + ':' + REMOTE_ENDPOINT.PORT + '/device/'+ LOCAL_ENDPOINT.NAME + '/data', 
        {
            Name : LOCAL_ENDPOINT.NAME, 
            Data : dataItem++,
            Time : Date.now(),
        },
        function(error, response, respBody) {
            console.log(respBody);
        }
    );
}

app.post("/rate", function(req,res){
	DATA_PERIOD = req.body.rate;
	clearInterval(intervalID);
	intervalID = setInterval(sendData, DATA_PERIOD);
	res.status(200).write("");
});

register();

intervalID = setInterval(sendData, DATA_PERIOD);

app.listen(LOCAL_ENDPOINT.PORT);
