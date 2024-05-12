
$(document).ready(function() {
    getBlog(null);
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
            <div class="right" id="${blogId}">
                <h5 class="email"><small class="text-muted h5">Từ: </small>${userEmail}</h5>
                <h5 class="title"><small class="text-muted h5">Nội dung: </small>${title}</h5>
                <h5 class="content"><small class="text-muted">${truncatedContent}</small></h5><br>
            </div>
        </div>
    `;

    return element;
}

function createNearBlogItem(blogId, title, content, userEmail, imageHeader) {
    const truncatedContent = content.length > 100 ? content.substring(0, 100) + '...' : content;
    const truncatedTitle = title.length > 100 ? title.substring(0, 100) + '...' : title;
    var element = `
        <div class="container2 " id="${blogId}">
            <div class="border-left border-left-1 ">
                <p class="ml-2 email"><small class="text-muted">Từ: </small>${userEmail}</p>
                <p class="ml-2 title">${truncatedTitle}</p>
            </div>
            
        </div>
    `;

    return element;
}

var blogId = 0
var listBlog = []
var item = ""
function getBlog(item) {
    $.ajax({
        url: 'http://localhost:1999/api/getAllBlog',
        type: 'GET',
        dataType: 'json',
        success: function(listBlog) {
            listBlog.forEach(function(blog) {
                console.log(blog.title);
                addItem(blog.blogId, blog.title, blog.content, blog.email, blog.imageHeader, );
            });
        },
        error: function(error) {

        }
    })
}

function getData(json) {
    $.ajax({
        type: GET,
        contentType: "application/json",
      url: "http://localhost:1999/api/register",
      data: JSON.stringify(userInfo),
      success: function (response) {
          alert("User registered successfully");
          window.location.href = "/verification?token=" + response.token;
      },
      error: function (error) {
          $("#errorMessage").text("Error registering user: " + error.responseJSON.message);
      }
    })
}

function addItem(blogId, name, content, userEmail, imageHeader) {

    element = createBlogItem(blogId, name, content, userEmail, imageHeader);
    elem = createNearBlogItem(blogId, name, content, userEmail, imageHeader);
    $(".main-body").append(element);
    $(".nearblog").append(elem);
    var clickElement = document.getElementById(blogId);
    clickElement.addEventListener('click', function() {
       window.location.href= window.location.href + "/viewBlog?blogId="+ blogId;
    });
}