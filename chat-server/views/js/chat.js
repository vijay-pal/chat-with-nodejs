(function () {
    $('#live-chat').fadeOut(300);
    $('#live-chat header').on('click', function () {
        $('.chat').slideToggle(300, 'swing');
        // $('.chat-message-counter').fadeToggle(300, 'swing');

    });

    $('.chat-close').on('click', function (e) {
        e.preventDefault();
        $('#live-chat').fadeOut(300);

    });

    $('.list-group-item').on('click', function (e) {
        e.preventDefault();
        var displayName = $(this).children("#f_display_name").attr("value");
        var email = $(this).children("#f_display_name").attr("value");
        console.log("email::" + email + " | name::" + displayName);
        $("#_id_room_title").text(displayName);
        $("#receiver_id").val(email);
        if(!$('#live-chat').is(':visible')){
            $('#live-chat').fadeIn(300);
        }
    });

    var receiverId = $('#receiver_id').val();
    var senderId = $('#sender_id').val();

    var socket = io();

    socket.on("connect", function (event) {
        socket.emit("connect_user", senderId);
    });

    socket.on('disconnect', function (event) {
        console.log('Disconnect::' + event);
    });

    socket.on("typing", function (args) {
        console.log('typing');
        $('#id_type').append('typing...');
    });
    socket.on("stop_typing", function (args) {
        $('#id_type').text('');
    });

    socket.on('new_message', function (msg) {
        console.log(msg);
        var chatMsg = "<div class=\"yours messages\">" +
            "<div class=\"message\">"
            + msg.message +
            "</div>" +
            "</div>";
        $(chatMsg).appendTo("#id_chat_history");
        // $('#messages').append($('<li>').text(msg));
        var scroll = $('#id_chat_history');
        scroll.animate({ scrollTop: scroll.prop("scrollHeight") });
    });
    console.log('sender:' + senderId);
    console.log('receiver:' + receiverId);
    $('form').submit(function (event) {
        event.preventDefault();
        var msg = $('#m').val();
        socket.emit('one_to_one', JSON.stringify({
            created_at: 0,
            message: msg,
            received_at: 0,
            receiver: receiverId,
            sender: senderId,
            status: '',
            type: '',
            url: ''
        }));
        var chatMsg = "<div class=\"mine messages\">" +
            "<div class=\"message\">"
            + msg +
            "</div>" +
            "</div>";
        $(chatMsg).appendTo("#id_chat_history");
        // $("#id_chat_history").append($(chatMsg).fadeIn('slow'));
        var scroll = $('#id_chat_history');
        scroll.animate({ scrollTop: scroll.prop("scrollHeight") });
        // return false;
        $('#m').val('');
    });

    var typing = false;
    var callback;
    function updatePreview() {
        if (typing) {
            typing = false;
            socket.emit("stop_typing", receiverId);
        }
    }

    $('#m').bind('keypress', function () {
        if (!typing) {
            typing = true;
            socket.emit("typing", receiverId);
        }
        if (callback) {
            clearTimeout(callback)
        }
        callback = setTimeout(updatePreview, 600);
    }
    );
})();