$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function(response) {
            $('#fullname').text(response.name);
            const imageUrl = response.avatar;
            document.getElementById("userAvatar").src = imageUrl;
        },
        error: function(xhr, status, error) {
        }
    });
    
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/alluser", 
        dataType: "json",
        success: function(response) {
            response.forEach(function(doctor) {
                if (doctor.role === "ROLE_DOCTOR") {
                    var cardHtml = `
                        <div class="card m-2" style="width: 18rem;">
                            <a href="/doctor/${doctor.email}">
                                <img src="${doctor.avatar}" class="avatarImg" alt="Doctor Avatar">
                                <div class="card-body">
                                    <h5 class="card-title text-center">${doctor.name}</h5>
                                    <h5 class="card-text text-center"><small class="text-muted">${doctor.specially}</small></h5>
                                    <h5 class="card-text text-center"><small class="text-muted">${doctor.workplace}</small></h5>
                                </div>
                            </a>
                        </div>`;
                    $('#alldoctor').append(cardHtml);
                }
            });
        },
        error: function(xhr, status, error) {
        }
    });
});
