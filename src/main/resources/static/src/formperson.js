$(document).ready(function() {
    var maxItemsToShow = 3;
    var startIdx = 0;

    $.ajax({
        url: 'http://localhost:1999/api/info',
        type: 'GET',
        dataType: 'json',
        success: function(user) {
            $('#fullname').text(user.name);
            const imageUrl = user.avatar;
            $('#userAvatar').attr('src', imageUrl);
            displayUserAccountInfo(user, user.role);

            $.ajax({
                url: 'http://localhost:1999/api/getAllBlog',
                type: 'GET',
                dataType: 'json',
                success: function(listBlog) {
                    displayBlogItems(listBlog);
                },
                error: function(error) {
                    console.error('Lỗi khi lấy danh sách bài viết:', error);
                }
            });

            function displayBlogItems(listBlog) {
                var userBlogs = listBlog.filter(function(blog) {
                    return blog.email === user.email;
                });

                var endIdx = Math.min(startIdx + maxItemsToShow, userBlogs.length);
                for (var i = startIdx; i < endIdx; i++) {
                    addItem(userBlogs[i].blogId, userBlogs[i].title, userBlogs[i].content, userBlogs[i].email, userBlogs[i].imageHeader);
                }

                if (endIdx >= userBlogs.length) {
                    $('#loadMoreBtn').hide();
                }
            }

            $('#loadMoreBtn').click(function() {
                startIdx += maxItemsToShow;
                $.ajax({
                    url: 'http://localhost:1999/api/getAllBlog',
                    type: 'GET',
                    dataType: 'json',
                    success: function(listBlog) {
                        displayBlogItems(listBlog);
                    },
                    error: function(error) {
                        console.error('Lỗi khi lấy danh sách bài viết:', error);
                    }
                });
            });
        },
        error: function(error) {
            console.error('Lỗi khi lấy thông tin người dùng:', error);
        }
    });
});

function addItem(blogId, name, content, userEmail, imageHeader) {
    element = createBlogItem(blogId, name, content, userEmail, imageHeader);
    $(".loadblog").append(element);
    var clickElement = document.getElementById(blogId);
    clickElement.addEventListener('click', function() {
        window.location.href = "http://localhost:1999/community/viewBlog?blogId=" + blogId;
    });
    var buttonEdit = document.getElementById("edit"+blogId);
    buttonEdit.addEventListener("click", function() {
        alert(blogId);
    })
    buttonDelete = document.getElementById("delete"+blogId);
    buttonDelete.addEventListener('click', function() {
        deleteBlog(blogId);
    })
}

function createBlogItem(blogId, title, content, userEmail, imageHeader) {
    const truncatedContent = content.length > 320 ? content.substring(0, 320) + '...' : content;
    var element = `
        <div class="container1" id="${blogId}">
            <div class="left">
                <img src="${imageHeader}" alt="">
            </div>
            <div class="right mt-4" id="${blogId}">
                <h5 class="email"><small class="text-muted h5">Từ: </small>${userEmail}</h5>
                <h5 class="title"><small class="text-muted h5">Nội dung: </small>${title}</h5>
                <h5 class="content"><small class="text-muted">${truncatedContent}</small></h5><br>
            </div>
        </div>
        <button class="delete-btn" id="delete${blogId}">Xóa bài viết</button>
        <button class="edit-btn" id="edit${blogId}">Sửa bài viết</button>
    `;
    return element;
}

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

function deleteBlog(blogId) {
    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:1999/api/deleteBlog?blogId='+blogId,
        success: function() {
            updateStatus(id, "Đã hủy")
            alert("Đã hủy!")   
        },
        error: function(error) {
            alert("error");
        }
    })
}
