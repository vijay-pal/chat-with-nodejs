
const multer = require('multer');
var ip = require('ip');
var path = require('path');

var storage = multer.diskStorage({
    destination: function (req, file, callback) {
        callback(null, __dirname + '/../media');
    },
    filename: function (req, file, callback) {
        callback(null, Date.now() + '_' + file.originalname);
    }
});

var upload = multer({ storage: storage }).single('newFile');

module.exports = function (router, port) {
    router.post('/upload', function (req, res) {
        upload(req, res, function (err) {
            console.log('RileRouter::', req.file.filename);
            if (err) {
                console.log('Error', "Error while uploading file ::" + err);
                return res.send(err);
            }
            console.log("Success", 'File successfully uploaded.:');
            return res.send({
                status: true,
                message: 'File Successfully uploaded.',
                url: ip.address() + ':' + port + '/api/v1/media/' + req.file.filename
            });
        });
    });

    router.get('/media/:file_name', function (req, res) {
        console.log("Path", path.resolve(__dirname, '../media/'));
        res.sendFile(req.params.file_name, { root: path.resolve(__dirname, '../media/') });
    });
}