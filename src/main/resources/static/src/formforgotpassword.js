$(document).ready(function() {
    $('#forgotPasswordForm').submit(function(event) {
        event.preventDefault();
        
        var email = $('#email').val();

        $.ajax({
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            url: 'http://localhost:1999/api/forgot-password',
            data: {
                email: email
            },
            success: function(response) {
                console.log('Yêu cầu đặt lại mật khẩu thành công:', response);
                setTimeout(function() {
                    window.location.href = '/';
                }, 2000);
            },
            error: function(xhr, status, error) {
                console.error('Lỗi khi gửi yêu cầu đặt lại mật khẩu:', error);
            }
        });
    });
});
