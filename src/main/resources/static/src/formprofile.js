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
            $('#updateUserAccount').click(updateUserAccount);
            $('#changePassword').click(changePassword);
            $('#uploadForm').prop('disabled', true);
            $('#fileInput').change(function(event) {
                var file = event.target.files[0];
                var reader = new FileReader();
                
                reader.onload = function(event) {
                    var img = $('#previewImage');
                    img.attr('src', event.target.result);
                    img.css('display', 'block');
                };
                
                reader.readAsDataURL(file);
                var fileInput = $('#fileInput')[0].files[0];
                if (!fileInput) {
                    $('#uploadForm').prop('disabled', true);
                } else {
                    $('#uploadForm').prop('disabled', false);
                }
            });
            $('#uploadForm').click(uploadForm);
        },
        error: function(error) {
            console.error('Lỗi khi lấy thông tin người dùng:', error);
        }
    });

    
});

function uploadForm(event) {
    event.preventDefault();
    var fileInput = $('#fileInput')[0].files[0];
    var formData = new FormData();
    formData.append('file', fileInput);
    $.ajax({
        url: 'http://localhost:1999/api/uploadavatar',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function(response) {
            const avatarUrl = response;
            $.ajax({
                url: 'http://localhost:1999/api/changeavatar',
                type: 'POST',
                data: avatarUrl,
                contentType: 'text/plain',
                success: function(changeResponse) {
                    alert(changeResponse);
                },
                error: function(changeError) {
                    console.error('Lỗi khi thay đổi avatar:', changeError);
                    alert('Có lỗi xảy ra khi thay đổi avatar. Vui lòng thử lại sau.');
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi tải ảnh lên:', error);
            alert('Có lỗi xảy ra khi tải ảnh lên. Vui lòng thử lại sau.');
        }
    });
}


function changePassword() {
    const newPassword = $('#newPassword').val();
    const confirmNewPassword = $('#confirmNewPassword').val();
    if (newPassword !== confirmNewPassword) {
        alert("Mật khẩu mới và xác nhận mật khẩu mới không khớp. Vui lòng thử lại.");
        return;
    }
    const requestData = {
        newPassword: newPassword
    };
    $('#changePassword').prop('disabled', true);
    $.ajax({
        url: 'http://localhost:1999/api/changepassword',
        type: 'POST',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(requestData),
        success: function(response) {
            alert('Mật khẩu đã được thay đổi thành công!');
            $('#newPassword').val('');
            $('#confirmNewPassword').val('');
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi thay đổi mật khẩu:', error);
            alert('Có lỗi xảy ra khi thay đổi mật khẩu. Vui lòng thử lại sau.');
        },
        complete: function() {
            $('#changePassword').prop('disabled', false);
        }
    });
}


function updateUserAccount() {
    var userInfo = {
        name: $('#name').val() || null,
        address: $('#address').val() || null,
        phone: $('#phone').val() || null,
        birthday: $('#birthday').val() || null,
        gender: $('input[name="gender"]:checked').val() || null,
        specially: $('#specially').val() || null,
        workplace: $('#workplace').val() || null,
        numberofyear: $('#numberofyear').val() || null,
        education: $('#education').val() || null,
        introduction: $('#introduction').val() || null,
        underlyingDisease: $('#underlyingDisease').val() || null
    };
    $('#updateUserAccount').prop('disabled', true);
    
    $.ajax({
        url: 'http://localhost:1999/api/update',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(userInfo),
        success: function(response) {
            alert('Thông tin người dùng đã được cập nhật thành công!');
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi cập nhật thông tin người dùng:', error);
            alert('Có lỗi xảy ra khi cập nhật thông tin người dùng. Vui lòng thử lại sau.');
        },
        complete: function() {
            $('#updateUserAccount').prop('disabled', false);
        }
    });
}

function displayUserAccountInfo(user, role) {
    $('#UserAccContainer').empty();
    var userAccHtml = `
        <div class="user-account-info">
            <hr>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="name">Họ và Tên:</label>
                        <input type="text" class="form-control" id="name" value="${user.name}" >
                    </div>
                    <div class="form-group">
                        <label for="address">Địa chỉ:</label>
                        <input type="text" class="form-control" id="address" value="${user.address}" >
                    </div>
                    <div class="form-group">
                        <label for="phone">Số điện thoại:</label>
                        <input type="number" class="form-control" id="phone" value="${user.phone}" >
                    </div>
                    <div class="form-group">
                        <label for="birthday">Ngày sinh:</label>
                        <input type="date" class="form-control" id="birthday" value="${user.birthday}" >
                    </div>
                    <div class="form-group">
                        <label for="gender">Giới tính:</label>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="maleGender" value="Nam" ${user.gender === 'Nam' ? 'checked' : ''} >
                            <label class="form-check-label" for="maleGender">Nam</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="femaleGender" value="Nữ" ${user.gender === 'Nữ' ? 'checked' : ''} >
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
                    <input type="text" class="form-control" id="specially" value="${user.specially ? user.specially : ''}" >
                </div>
                <div class="form-group">
                    <label for="workplace">Nơi làm việc:</label>
                    <input type="text" class="form-control" id="workplace" value="${user.workplace ? user.workplace : ''}" >
                </div>
                <div class="form-group">
                    <label for="numberofyear">Số năm kinh nghiệm:</label>
                    <input type="number" class="form-control" id="numberofyear" value="${user.numberofyear ? user.numberofyear : ''}" >
                </div>
                <div class="form-group">
                    <label for="education">Học vấn:</label>
                    <input type="text" class="form-control" id="education" value="${user.education ? user.education : ''}" >
                </div>
                <div class="form-group">
                    <label for="introduction">Giới thiệu bản thân:</label>
                    <textarea class="form-control" id="introduction" rows="3" >${user.introduction ? user.introduction : ''}</textarea>
                </div>
            </div>
            <div class="col-md-12 text-center">
                <button type="button" id="updateUserAccount" class="btn btn-primary">Cập Nhật</button>
            </div>
        `;
    } else if (role === 'ROLE_PATIENT') {
        userAccHtml += `
            <div class="col-md-6">
                <div class="form-group">
                    <label for="underlyingDisease">Bệnh nền:</label>
                    <input type="text" class="form-control" id="underlyingDisease" value="${user.underlyingDisease ? user.underlyingDisease : ''}" >
                </div>
            </div>
            <div class="col-md-12 text-center">
                <button type="button" id="updateUserAccount" class="btn btn-primary">Cập Nhật</button>
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
                <label for="avatar">Ảnh đại diện:</label>
                <div class="avatar-container">
                    <img src="${user.avatar ? user.avatar : 'placeholder.jpg'}" alt="Avatar" class="avatar-large fit-image" id="avatarImage">
                    <div class="text-left"> 
                        <div class="upload-container">
                            <form enctype="multipart/form-data">
                                <div class="input-group my-4">
                                    <div class="custom-file">
                                        <input type="file" class="custom-file-input" id="fileInput" accept="image/*">
                                        <label class="custom-file-label" for="fileInput">Thay đổi ảnh</label>
                                    </div>
                                </div>
                                <img src="#" alt="Preview" id="previewImage" class="img-thumbnail">
                                <button id="uploadForm" class="btn btn-primary">Tải ảnh lên</button>
                            </form>
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
                    <button class="btn btn-primary btn-change-password my-2 justify-content-center" id="changePassword">Đổi mật khẩu</button>
                </div>
            </div>
        </div>
    `;
    $('#accountInfo').append(accountinfor);
}
