var express = require('express'),
    app = express();
const port = process.env.PORT || 3000;

var http = require('http').Server(app);
var io = require('socket.io')(http);

var mongoose = require('mongoose'),
    mongoUri = 'mongodb://localhost/noderest';

mongoose.connect(mongoUri);
var db = mongoose.connection;

db.on('error', function () {
    throw new Error('unable to connect to database at ' + mongoUri);
});

var ip = require('ip');

var bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: true
}));

var router = express.Router();


server = http.listen(port, function () {
    console.log('todo list RESTful API server started on:: http://' + ip.address() + ':' + port);
});

require('./api/models/User');
require('./api/models/User');
require('./api/models/Conversation');

require('./api/routers/ConversationRouter.js')(io);

require('./api/routers/UserRouter.js')(router);
require('./api/routers/FileRouter.js')(router,port);
app.use('/api/v1/', router);
