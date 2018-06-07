module.exports = function (router) {
    var users = require('../controllers/Users');

    router.post('/register', users.register);
    router.get('/users/:id', users.findUsers);
    router.get('/:id/friends', users.findFriends);
    router.post('/add_friend', users.addFriend);
    router.put('/friend_request/:id/confirmation', users.friendRequestConfirmed);
    router.delete('/delete/:id', users.delete);
};
