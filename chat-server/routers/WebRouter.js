module.exports = function (router) {
    var webUsers = require('../web/controller/UserController');
    
    router.get('/', webUsers.index);
    router.post('/login', webUsers.login);
    router.get('/logout', webUsers.logout);
    router.get('/chat', webUsers.chat);
};