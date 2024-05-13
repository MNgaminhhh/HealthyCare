$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function (response) {
            $('#fullname').text(response.name);
            const imageUrl = response.avatar;
            document.getElementById("userAvatar").src = imageUrl;
        },
        error: function (xhr, status, error) {
        }
    });


});