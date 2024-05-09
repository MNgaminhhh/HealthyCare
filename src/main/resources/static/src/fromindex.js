$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function(response) {
            $('#fullname').text(response.name);
        },
        error: function(xhr, status, error) {
            console.error(error);
        }
    });
});
