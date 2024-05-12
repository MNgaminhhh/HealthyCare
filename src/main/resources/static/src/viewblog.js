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
                content.text(blog.content);
                userEmail.text(blog.name);
                avatar.attr('src', blog.avt);
                listImg = blog.files;
                listImg.forEach(function(data) {
                    ulImage.append('<li>'
                    +'<img src="'+data+'" alt=""></img>'
                    +'</li>')
                })
            }
        ,
        error: function(error) {

        }
    });

    const buttonComment = document.getElementById("btn-submit");
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
        success: function(data) {
        },
        error: function(error) {
        } 
    })
}