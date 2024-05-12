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
})