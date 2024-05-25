$(document).ready(function() {
    $('#loginForm').submit(function(event) {
        event.preventDefault();
        
        var formData = JSON.stringify({
            email: $('#email').val(),
            password: $('#password').val()
        });

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: 'http://localhost:1999/api/login',
            data: formData,
            success: function(response) {
                alert('Đăng nhập thành công:');
                window.location.href = '/';
            },
            error: function(xhr, status, error) {
                alert('Đăng nhập thất bại vui lòng kiểm tra lại tài khoản và mật khẩu')
            }
        });
    });
});
