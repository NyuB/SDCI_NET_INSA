var http = require('http');
var argv = require('yargs').argv;
var msg = argv.message;
http.createServer(function (req, res) {
	res.write("Roger, Wilco : "+msg);
	res.write("\n\n");
	res.end();
}).listen(8888);