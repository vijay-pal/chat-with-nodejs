module.exports = function (router) {
    var apiUsers = require('../api/controllers/UserController');

    router.post('/register', apiUsers.register);
    router.get('/users/:id', apiUsers.findUsers);
    router.get('/:id/friends', apiUsers.findFriends);
    router.post('/add_friend', apiUsers.addFriend);
    router.put('/friend_request/:id/confirmation', apiUsers.friendRequestConfirmed);
    router.delete('/delete/:id', apiUsers.delete);

};
