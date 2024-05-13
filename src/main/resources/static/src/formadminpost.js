$(document).ready(function() {
    getBlog();
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
            <button class="delete-btn btn btn-primary" id="delete${blogId}">Xóa bài viết</button>
        </div>
    `;
    return element;
}

var blogId = 0
var listBlog = []
function getBlog() {
    $.ajax({
        url: 'http://localhost:1999/api/getAllBlog',
        type: 'GET',
        dataType: 'json',
        success: function(listBlog) {
            listBlog.forEach(function(blog) {
                addItem(blog.blogId, blog.title, blog.content, blog.email, blog.imageHeader);
            });
        },
        error: function(error) {

        }
    })
}
function addItem(blogId, name, content, userEmail, imageHeader) {
    element = createBlogItem(blogId, name, content, userEmail, imageHeader);
    $(".loadisease").append(element);
    var buttonDelete = document.getElementById("delete"+blogId);
    buttonDelete.addEventListener('click', function() {
        deleteBlog(blogId);
    });
}
function deleteBlog(blogId) {
    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:1999/api/deleteBlog?blogId='+blogId,
        success: function() {
            updateStatus(id, "Đã hủy");
            location.reload();
        },
        error: function(error) {
            alert("error");
        }
    });
}