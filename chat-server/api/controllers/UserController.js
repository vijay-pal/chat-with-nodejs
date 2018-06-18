var mongoose = require('mongoose'),
    user = mongoose.model('User');
var url = require('url');

exports.findUsers = function (req, res) {
    const query = req.params.id;//url.parse(req.url, true).query;
    console.log(query);
    console.log(req.connection.remoteAddress);
    
    user.find({ 'display_name': new RegExp(query, 'i') }, function (err, result) {
        if (err) {
            console.log("error::" + err);
            return res.send(err);
        }
        const ln = result.length;
        if (ln == 0) {
            return res.send({
                status: false,
                message: "Record not matches.",
                data: result
            });
        }

        return res.send({
            status: true,
            message: "Total " + ln + " Record(s) found.",
            data: result
        });
    });
};

exports.findFriends = function (req, res) {
    var id = req.params.id;
    console.log("id", id);
    user.find({ "_id": id }, function (err, result) {
        if (err) {
            return res.send(err);
        }
        return res.send({
            "status": true,
            "friends": result[0].friends
        });
    });
};

exports.register = function (req, res) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader("Access-Control-Allow-Method", "'GET, POST, OPTIONS, PUT, PATCH, DELETE'");

    const reqBody = req.body;
    console.log("body::", reqBody);

    user.findOne({ 'email': reqBody.email }, function (err, result) {
        if (err) {
            console.log("error::", err);
            return res.send(err);
        }

        const d = new Date();
        const timeMils = d.getTime();
        reqBody.update_at = timeMils;

        if (result == null) {
            reqBody.created_at = timeMils;
            user.create(reqBody, function (err, result) {
                if (err) {
                    console.log("error::", err);
                    return req.send(err);
                }
                return res.send({
                    status: true,
                    message: "Registration Successful.",
                    data: result
                });
            });
        } else {
            delete reqBody.created_at;
            user.findOneAndUpdate({ 'email': reqBody.email }, reqBody, function (err, result) {
                if (err) {
                    console.log("error::", err);
                    return res.send(err);
                }
                return res.send({
                    status: true,
                    message: "Registration Updated Successfully.",
                    data: result
                });
            });
        }

    });
};

exports.addFriend = function (req, res) {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader("Access-Control-Allow-Method", "'GET, POST, OPTIONS, PUT, PATCH, DELETE'");

    const reqBody = req.body;
    console.log("body::", reqBody);
    user.find({ "_id": reqBody._id, "friends.email": reqBody.sender_email }, function (err, result) {
        if (err) {
            return res.send(err);
        }
        const ln = result.length;
        console.log("Friend request : " + ln);
        if (ln != 0) {
            return res.send({
                "status": false,
                "message": "Friend request already sent.",
                "data": result
            });
        }

        console.log("Pushing Friend request : " + result);
        user.update({ "_id": reqBody._id }, {
            $addToSet: {
                friends: {
                    'display_name': reqBody.sender_name,
                    'email': reqBody.sender_email,
                    'status': 0
                }
            }
        }, function (err, result) {
            if (err) {
                return res.send(err);
            }
            res.send({
                status: true,
                message: "Friend request has beeen send.",
                data: result
            })
        });
    });
};

exports.friendRequestConfirmed = function (req, res) {
    const id = req.params.id;
    console.log("User Id::" + id);
    const reqBody = req.body;
    console.log("body::" + JSON.stringify(reqBody));

    if (reqBody.confirm == "yes") {
        user.update({ _id: id, "friends._id": reqBody._id },
            {
                $set: { "friends.$.status": 1 }
            }, function (err, result) {
                if (err) {
                    return res.send(err);
                }
                //Adding friend in own list.
                user.update({ "email": reqBody.requester_email }, {
                    $addToSet: {
                        friends: {
                            display_name: reqBody.sender_name,
                            email: reqBody.sender_email,
                            status: 1
                        }
                    }
                }, function (err, result) {
                    if (err) {
                        return res.send(err);
                    }
                });
                return res.send({
                    status: true,
                    "message": "Friend request has been confirmed."
                });
            });
    } else {
        user.update({ _id: id },
            {
                $pull: {
                    friends: { _id: reqBody._id }
                }
            }, function (err, result) {
                if (err) {
                    return res.send(err);
                }
                return res.send({
                    status: false,
                    message: "Friend request has been removed."
                })
            });
    }
};

exports.delete = function (req, res) {
    var id = req.params.id;
    user.remove({ '_id': id }, function (result) {
        return res.send(result);
    });
};