var path = require('path');
var mongoose = require('mongoose'),
    user = mongoose.model('User');

exports.index = function (req, res) {
    console.log("VVVVVVVV");
    res.render('index');
};

exports.login = function (req, res, next) {
    console.log("request::" + JSON.stringify(req.body));
    user.findOne({ email: req.body.email }, function (error, result) {
        if (error || !result) {
            return next(error);
        }
        console.log(result);
        req.session.user_id = result.id;
        res.redirect('/chat');
    });
};

exports.logout = function (req, res) {
    if (req.session.user_id) {
        delete req.session.user_id;
    }
    res.redirect('/')
};

exports.chat = function (req, res) {
    console.log("vijay::" + req.session.user_id);
    console.log("vijay::" + req.query.id);
    if (!req.session.user_id) {
        res.redirect('/');
        return;
    }

    user.find({ "_id": req.session.user_id }, function (err, result) {
        if (err) {
            return res.send(err);
        }
        return res.render('chat', { "user": result[0]});
    });

};