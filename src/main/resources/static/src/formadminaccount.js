$(document).ready(function() {
    var maxItemsToShow = 4;
    var startIdx = 0;

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
            console.error('Lỗi khi lấy thông tin người dùng:', error);
        }
    });

    function loadUsers(roleFilter = 'all') {

        $.ajax({
            type: "GET",
            url: "http://localhost:1999/api/alluser",
            dataType: "json",
            success: function(response) {
                var filteredUsers = response.filter(function(user) {
                    return roleFilter === 'all' || (user.role === roleFilter);
                });

                var endIdx = Math.min(startIdx + maxItemsToShow, filteredUsers.length);
                for (var i = startIdx; i < endIdx; i++) {
                    var user = filteredUsers[i];
                    var isAdmin = '';
                    var active;
                    $.ajax({
                        type: "GET",
                        url: "http://localhost:1999/api/account/" + user.email,
                        dataType: "json",
                        async: false,
                        success: function(account) {
                            isAdmin = account.isadmin ? 'checked' : '';
                            active = account.verified;
                        },
                        error: function(xhr, status, error) {
                            console.error('Lỗi khi lấy thông tin tài khoản:', error);
                        }
                    });
                    if (active){
                        var cardHtml = '';
                        if (user.role === 'ROLE_DOCTOR') {
                            cardHtml = `
                            <div class="container1 col-3" id="${user.email}">
                                <div class="left">
                                    <img src="${user.avatar}" alt="Avatar">
                                </div>
                                <div class="right mt-4 d-flex flex-column justify-content-center zone" id="${user.email}">
                                    <h5 class="email"><small class="text-muted h5">Tên bác sĩ: </small>${user.name}</h5>
                                    <h5 class="title"><small class="text-muted h5">Chuyên khoa: </small>${user.specially}</h5>
                                </div>
                                <div class="d-flex justify-content-center mt-2">
                                    <div class="custom-control custom-switch">
                                        <input type="checkbox" class="custom-control-input admin-checkbox" id="adminCheckbox-${user.email}" data-email="${user.email}" ${isAdmin}>
                                        <label class="custom-control-label" for="adminCheckbox-${user.email}">Admin</label>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-2">
                                    <button class="btn btn-danger btn-sm delete-btn" data-email="${user.email}">Xóa tài khoản</button>
                                </div>
                            </div>
                        `;

                        } else if (user.role === 'ROLE_PATIENT') {
                            cardHtml = `
                            <div class="container1 col-3" id="${user.email}">
                                <div class="left">
                                    <img src="${user.avatar}" alt="Avatar">
                                </div>
                                <div class="right mt-4 d-flex flex-column justify-content-center zone" id="${user.email}">
                                    <h5 class="email"><small class="text-muted h5">Tên bệnh nhân: </small>${user.name}</h5>
                                    <h5 class="title"><small class="text-muted h5">Địa chỉ: </small>${user.address}</h5>
                                </div>
                                <div class="d-flex justify-content-center mt-2">
                                    <div class="custom-control custom-switch">
                                        <input type="checkbox" class="custom-control-input admin-checkbox" id="adminCheckbox-${user.email}" data-email="${user.email}" ${isAdmin}>
                                        <label class="custom-control-label" for="adminCheckbox-${user.email}">Admin</label>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-center mt-2">
                                    <button class="btn btn-danger btn-sm delete-btn" data-email="${user.email}">Xóa tài khoản</button>
                                </div>
                            </div>
                        `;
                        }
                        $('#alluser').append(cardHtml);
                    }

                }
                if (endIdx >= filteredUsers.length) {
                    $('#loadMoreBtn').hide();
                } else {
                    $('#loadMoreBtn').show();
                }
                startIdx += maxItemsToShow;
            },
            error: function(xhr, status, error) {
                console.error('Lỗi khi lấy danh sách người dùng:', error);
            }
        });
    }

    loadUsers();

    $('#roleFilter').change(function() {
        var selectedRole = $(this).val();
        startIdx = 0;
        $('#alluser').empty();
        loadUsers(selectedRole);
    });

    $('#loadMoreBtn').click(function() {
        loadUsers($('#roleFilter').val());
    });

    $(document).on('click', '.delete-btn', function() {
        var userEmail = $(this).data('email');
        $.ajax({
            type: "PUT",
            url: "http://localhost:1999/api/user/deactivate/" + userEmail,
            dataType: "json",
            success: function(response) {
                location.reload();
            },
            error: function(xhr, status, error) {
                console.error('Lỗi khi cập nhật trạng thái isAdmin:', error);
            },
        });
        location.reload();
    });
});
$(document).on('change', '.admin-checkbox', function() {
    var userEmail = $(this).data('email');
    var isChecked = $(this).prop('checked');
    $.ajax({
        type: "PUT",
        url: "http://localhost:1999/api/account/isadmin/" + userEmail,
        dataType: "json",
        success: function(response) {
        },
        error: function(xhr, status, error) {
        },
        data: JSON.stringify({ isAdmin: isChecked })
    });
});
