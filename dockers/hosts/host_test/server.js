var http = require('http');
http.createServer(function (req, res) {
	res.write("Roger, Wilco");
	res.write("\n\n");
	res.end();
}).listen(8888);
