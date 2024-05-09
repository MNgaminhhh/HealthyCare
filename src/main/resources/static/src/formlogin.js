$(document).ready(function() {
    $('#loginForm').submit(function(event) {
        event.preventDefault();
        
        var formData = JSON.stringify({
            email: $('#email').val(),
            password: $('#password').val()
        });

        $.ajax({
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            url: 'api/login',
            data: formData,
            xhrFields: {
                withCredentials: false
            },
            success: function(response) {
                console.log('Đăng nhập thành công:', response);
                window.location.href = '/';
            },
            error: function(xhr, status, error) {
                console.error('Đăng nhập thất bại:', xhr.responseText);
            }
        });
    });
});
