$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:1999/api/info',
        type: 'GET',
        dataType: 'json',
        success: function(user) {
            $('#fullname').text(user.name);
            const imageUrl = user.avatar;
            $('#userAvatar').attr('src', imageUrl);
            displayUserAccountInfo(user, user.role);
            
        },
        error: function(error) {
            console.error('Lỗi khi lấy thông tin người dùng:', error);
        }
    });

    
});

function displayUserAccountInfo(user, role) {
    $('#loadperson').empty();
    var userAccHtml = `
    <div class="row">
        <div class="col-md-4">
            <div id="bio" class="bg-white p-4 text-center rounded-lg">
                <div class="aspect-square rounded-full overflow-hidden mx-auto bg-slate-200 mb-3">
                    <img src="${user.avatar ? user.avatar : 'placeholder.jpg'}" alt="avatar" class="avatarprofile">
                </div>
            </div>
        </div>
        <div class="col-md-8 my-auto">
            <h3>${user.name}</h3>
    `;
    if (role === 'ROLE_DOCTOR') {
        userAccHtml += `
            <p class="mb-1">Bác Sĩ | ${user.numberofyear} năm kinh nghiệm</p>
            <p class="mb-1">Chuyên Khoa: ${user.specially}</p>
            <p class="mb-1">Chức Vụ: ${user.workplace}</p>
        `;
    } else if (role === 'ROLE_PATIENT') {
        userAccHtml += `
            <p class="mb-1">Bệnh Nền: ${user.underlyingDisease}</p>
        `;
    }
    userAccHtml += `   
        </div>
    </div>
    `;
    $('#loadperson').append(userAccHtml);
}