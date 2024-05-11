
$(document).ready(function() {
    $('#resetPasswordButton').click(function(event) {
        event.preventDefault();

        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        if (password !== confirmPassword) {
            alert("Mật khẩu và xác nhận mật khẩu không khớp.");
            return;
        }
        
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');
        var formData = JSON.stringify({
            newPassword: password
        });
        $.ajax({
            type: 'POST',
            contentType: 'application/json; charset=UTF-8',
            url: 'http://localhost:1999/api/reset-password?token='+token,
            data: formData,
            success: function(response) {
                alert('Mật khẩu đã được đặt lại thành công.');
                window.location.href = '/login';
            },
            error: function(xhr, status, error) {
                alert('Đã xảy ra lỗi khi đặt lại mật khẩu.');
            }
        });
    });
});
