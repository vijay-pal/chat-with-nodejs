var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var ConversationSchema = new Schema({
    users: [],
    messages: [{
        message: String,
        type: String,
        url: String,
        sender: String,
        receiver: String,
        created_at: Number,
        received_at: Number,
        status: String
    }]
});
mongoose.model('Conversation', ConversationSchema);