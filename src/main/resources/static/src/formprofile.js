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
    $('#UserAccContainer').empty();
    var userAccHtml = `
        <div class="user-account-info">
            <hr>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="name">Họ và Tên:</label>
                        <input type="text" class="form-control" id="name" value="${user.name}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="address">Địa chỉ:</label>
                        <input type="text" class="form-control" id="address" value="${user.address}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="phone">Số điện thoại:</label>
                        <input type="text" class="form-control" id="phone" value="${user.phone}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="birthday">Ngày sinh:</label>
                        <input type="date" class="form-control" id="birthday" value="${user.birthday}" disabled>
                    </div>
                    <div class="form-group">
                        <label for="gender">Giới tính:</label>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="maleGender" value="Nam" ${user.gender === 'male' ? 'checked' : ''} disabled>
                            <label class="form-check-label" for="maleGender">Nam</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="femaleGender" value="Nữ" ${user.gender === 'female' ? 'checked' : ''} disabled>
                            <label class="form-check-label" for="femaleGender">Nữ</label>
                        </div>
                    </div>
                </div>
    `;
    
    if (role === 'ROLE_DOCTOR') {
        userAccHtml += `
            <div class="col-md-6">
                <div class="form-group">
                    <label for="specially">Chuyên môn:</label>
                    <input type="text" class="form-control" id="specially" value="${user.specially ? user.specially : ''}" disabled>
                </div>
                <div class="form-group">
                    <label for="workplace">Nơi làm việc:</label>
                    <input type="text" class="form-control" id="workplace" value="${user.workplace ? user.workplace : ''}" disabled>
                </div>
                <div class="form-group">
                    <label for="numberofyear">Số năm kinh nghiệm:</label>
                    <input type="text" class="form-control" id="numberofyear" value="${user.numberofyear ? user.numberofyear : ''}" disabled>
                </div>
                <div class="form-group">
                    <label for="education">Học vấn:</label>
                    <input type="text" class="form-control" id="education" value="${user.education ? user.education : ''}" disabled>
                </div>
                <div class="form-group">
                    <label for="introduction">Giới thiệu bản thân:</label>
                    <textarea class="form-control" id="introduction" rows="3" disabled>${user.introduction ? user.introduction : ''}</textarea>
                </div>
            </div>
            <div class="col-md-12 text-center">
                <button onclick="updateUserAccount()" class="btn btn-primary">Cập Nhật</button>
            </div>
        `;
    } else if (role === 'ROLE_PATIENT') {
        userAccHtml += `
            <div class="col-md-6">
                <div class="form-group">
                    <label for="underlyingDisease">Bệnh nền:</label>
                    <input type="text" class="form-control" id="underlyingDisease" value="${user.underlyingDisease ? user.underlyingDisease : ''}" disabled>
                </div>
            </div>
            <div class="col-md-12 text-center">
                <button onclick="updateUserAccount()" class="btn btn-primary">Cập Nhật</button>
            </div>
        `;
    }
    
    userAccHtml += `</div></div>`;
    
    $('#UserAccContainer').append(userAccHtml);
    
    $('#accountInfo').empty();
    var accountinfor = `
        <hr>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label for="avatar">Ảnh đại diện:</label>
                    <div class="avatar-container">
                        <img src="${user.avatar ? user.avatar : 'placeholder.jpg'}" alt="Avatar" class="avatar-large fit-image">
                        <div class="text-left"> 
                            <button class="btn btn-primary btn-change-avatar my-2 justify-content-center">Thay đổi ảnh</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" class="form-control" id="email" value="${user.email ? user.email : ''}" disabled>
                </div>
                <div class="form-group">
                    <label for="newPassword">Mật khẩu mới:</label>
                    <input type="password" class="form-control" id="newPassword">
                </div>
                <div class="form-group">
                    <label for="confirmNewPassword">Nhập lại mật khẩu mới:</label>
                    <input type="password" class="form-control" id="confirmNewPassword">
                </div>
                <div class="text-right"> 
                    <button class="btn btn-primary btn-change-password my-2 justify-content-center">Đổi mật khẩu</button>
                </div>
            </div>
        </div>
    `;
    $('#accountInfo').append(accountinfor);
}
