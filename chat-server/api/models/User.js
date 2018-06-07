var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var UserSchema = new Schema({
    login_id: String,
    display_name: {
        type: String,
        required: true
    },
    family_name: String,
    email: {
        type: String,
        required: true,
        unique: true
    },
    given_name: String,
    photo_url: String,
    login_token: String,
    server_auth_code: String,
    mobile: String,
    login_via: String,
    created_at: Number,
    update_at: Number,
    friends: [{
        display_name: String,
        email:{ 
            type: String,
            unique:true
        },
        status: { type: Number, default: 0 }
    }]
});

mongoose.model('User', UserSchema);
