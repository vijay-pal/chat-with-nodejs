var mongoose = require('mongoose'),
    conversation = mongoose.model('Conversation');

var ss = require('socket.io-stream');
var path = require('path');
var fs = require('fs');

var users = {};
var keys = {};

module.exports = function (io) {
    io.on('connection', function (socket) {
        socket.on('connect_user', function (user) {
            io.to(socket.id).emit('user', user);
            users[user] = socket.id;
            keys[socket.id] = user;
            console.log("User : " + users[user]);
            console.log("key : " + keys[socket.id]);
        });

        //One to One conversation handling
        socket.on('one_to_one', function (message) {
            var msg = JSON.parse(message);
            console.log('Message::', msg);
            sender = msg.sender;
            receiver = msg.receiver;
            console.log('Reveiver::', users[receiver]);
            io.to(users[receiver]).emit('new_message', msg);

        });
        //Group Converation
        socket.on('group_message', function (message) {

        });

        socket.on('typing', function (on) {
            console.log('On::', users[on]);
            io.to(users[on]).emit('typing', 'typing');
        });

        socket.on('stop_typing', function (on) {
            console.log('On::', on);
            console.log('On::', users[on]);
            io.to(users[on]).emit('stop_typing', 'stop typing');
        });

        socket.on('image-message', function (data) {
            var msg = JSON.parse(data);
             var filePath = path.resolve('/home/vijay/Pictures', new Date().getTime() + '.' + "jpg");
             console.log('file::', filePath);
             fs.writeFile(filePath, msg.message, 'base64', function (err) {
                 console.log("err::" + err);
             });

            sender = msg.sender;
            receiver = msg.receiver;
            console.log('Reveiver::', users[receiver]);
            io.to(users[receiver]).emit('image_message', msg);
        });

        socket.on('disconnect', function () {
            delete users[keys[socket.id]];
            delete keys[socket.id];
            io.emit('users', users);
            console.log(users);
        });
    })
}