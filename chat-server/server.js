var express = require('express'),
    app = express();
const port = process.env.PORT || 3000;

var http = require('http').Server(app);
var io = require('socket.io')(http);

// mongodb Connection
var mongoose = require('mongoose'),
    mongoUri = 'mongodb://localhost/noderest';
mongoose.connect(mongoUri);
var db = mongoose.connection;

//mongo error
db.on('error', function () {
    throw new Error('unable to connect to database at ' + mongoUri);
});

//session
var session = require('express-session');
app.use(session( {
  secret: 'treehouse loves you',
  resave: true,
  saveUninitialized: false
}));


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

require('./models/User');
require('./models/Conversation');

require('./routers/ConversationRouter')(io);

require('./routers/UserRouter')(router);
require('./routers/FileRouter.js')(router,port);
app.use('/api/v1/', router);

path = require('path');
app.set('view engine', 'ejs');
app.use(express.static(path.join(__dirname, 'views')));
var router2 = express.Router();
require('./routers/WebRouter')(router2);
app.use('/', router2);
