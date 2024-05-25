$(document).ready(function() {
    var title = $(".title");
    var content = $(".content");
    var userEmail = $(".user-name");
    var ulImage = $(".list-img");
    var avatar = $(".avarta");
    var currentUser = null;

    var currentUrl = window.location.search;

    var urlParams = new URLSearchParams(currentUrl);

    var blogId = urlParams.get('blogId');
    var listImg;

    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function(response) {
            currentUser = response.email;
        },
        error: function(xhr, status, error) {
        }
    });
    
    $.ajax({
        url: 'http://localhost:1999/api/getBlogBy?blogId='+blogId,
        type: 'GET',
        dataType: 'json',
        success: function(blog) { 
                title.text(blog.title);
                content.text("Nội dung: " +blog.content);
                userEmail.text("Tên: " + blog.name);
                avatar.attr('src', blog.avt);
                listImg = blog.files;
                listImg.forEach(function(data) {
                    ulImage.append('<div class="itemm">'
                    +'<img src="'+data+'" alt=""></img>'
                    +'</div>')
                })
            }
        ,
        error: function(error) {

        }
    });

    $.ajax({
        url: 'http://localhost:1999/api/getCommentByBlog?blogId='+blogId,
        type: 'GET',
        dataType: 'json',
        success: function(comment) { 
                var comments = comment;
                comments.forEach(function(comment) {
                    var name = comment.name;
                    var time = comment.time;
                    var content = comment.content;
                    var url = comment.avt;
                    populatedComment(name, time, content, url);
                })
            }
        ,
        error: function(error) {

        } 
    })

    const buttonComment = document.getElementById("button-comment");
    const formSubmit = document.getElementById("comment-form");
    
    buttonComment.addEventListener("click", function() {
        addComment(currentUser, blogId);
        formSubmit.reset();
    })
})



function addComment(email, blogId) {
    var content = $("#comment").val() || null

    var data = {
        "content": content,
        "email": email,
        "blogId": blogId
    }

    var jsonData = JSON.stringify(data);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "http://localhost:1999/api/createNewComment",
        data: jsonData,
        success: function(response) {
            populatedComment(response.account.email, response.time, response.content, response.avt);
        },
        error: function(xhr, textStatus, errorThrown) {

        }
    });
}

function populatedComment(name, time, content, avt) {
    var item = `
        <div class="row mb-3">
            <div class="col-md-2 user-comment">
                <img src="${avt}" alt="Avatar" class="avatar img-fluid">
                <div class="user-name mt-2"><span>${name}</span></div>
            </div>
            <div class="col-md-10 content-comment">
                <p>${content}</p>
                <div class="created-at"><span>${time}</span></div>
            </div>
        </div>
    `;
    $('#container-comment').append(item);
}

