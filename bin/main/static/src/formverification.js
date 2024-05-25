function verifyCode() {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const code = $('#code').val();
    const tokenData = { "token": token, "code": code };
    var url = 'http://localhost:1999/api/email/check';
    $.ajax({
        url: url,
        type: 'GET',
        contentType: "application/json",
        data: tokenData, 
        success: function(response) {
                alert('Xác thực thành công!'); 
                window.location.href = '/login';
           
        },
        error: function(xhr) {
            alert('Xác thực thất bại. Vui lòng kiểm tra lại mã xác thực.');
        }
    });
}

function resendCode(){
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const tokenData = { "token": token };
    var url = 'http://localhost:1999/api/email/resend';
    $.ajax({
        url: url,
        type: 'GET',
        contentType: "application/json",
        data: tokenData, 
        success: function(response) {
            alert('Gửi lại mã xác thực thành công!'); 
        },
        error: function(xhr) {
            alert('Gửi lại mã xác thực thất bại. Vui lòng kiểm tra lại');
        }
    });
}

$(document).ready(function() {
    $("#verificationButton").click(verifyCode);
    $("#resendCode").click(resendCode);
    var urlParams = new URLSearchParams(window.location.search);
    var token = urlParams.get('token');
    var tokenData = { "token": token };
    $.ajax({
        url: 'http://localhost:1999/api/email/checktoken',
        type: 'GET',
        contentType: "application/json",
        data: tokenData,
        success: function(response) {
        },
        error: function(xhr) {
            window.location.href = '/error';
        }
    });
});
