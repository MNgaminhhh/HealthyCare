$(document).ready(function() {
    var title = $(".title");
    var content = $(".content");
    var userEmail = $(".user-name");
    var ulImage = $(".list-img");
    var avatar = $(".avarta");

    var currentUrl = window.location.search;

    var urlParams = new URLSearchParams(currentUrl);

    var blogId = urlParams.get('blogId');
    var listImg;

    alert(blogId);

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
})