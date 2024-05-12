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
        <div class="col-md-4 border-right">
            <div id="bio" class="bg-white text-center rounded-lg">
                <div class="aspect-square rounded-full overflow-hidden mx-auto bg-slate-200 mb-3">
                    <img src="${user.avatar ? user.avatar : 'placeholder.jpg'}" alt="avatar" class="avatarprofile">
                </div>
            </div>
        </div>
        <div class="col-md-8 my-auto">
            <h1>${user.name}</h1>
    `;
    if (role === 'ROLE_DOCTOR') {
        userAccHtml += `
            <h5 class="mb-1 ">Bác Sĩ | ${user.numberofyear} năm kinh nghiệm</h5>
            <h5 class="mb-1 "><small class="text-muted">Chuyên Khoa:</small> ${user.specially}</h5>
            <h5 class="mb-1 "><small class="text-muted">Học Vấn:</small> ${user.education}</h5>
            <h5 class="mb-1 "><small class="text-muted">Nơi Công Tác:</small> ${user.workplace}</h5>
        </div>
    </div>
    <h5 class="mb-1 mt-5">Giới thiệu:</h5>
    <h5 class="mb-1 "><small class="text-muted">${user.introduction}</small></h5>
    <hr/>
        `;
    } else if (role === 'ROLE_PATIENT') {
        userAccHtml += `
            <h5 class="mb-1">Bệnh Nền: ${user.underlyingDisease}</h5>
        </div>
    </div>
    <h5 class="mb-1 mt-5">Bệnh nền:</h5>
    <h5 class="mb-1">${user.underlyingDisease}</h5>
    `;
    }
    $('#loadperson').append(userAccHtml);
}
