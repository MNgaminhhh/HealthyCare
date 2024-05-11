
$(document).ready(function() {
    getBlog(null);
});



function createBlogItem(blogId, title, content, userEmail, imageHeader) {   
    var element = 
    '<div class="container1" id = "' + blogId + '">' 
    + '<div class="left">'
    + '<img src="'+ imageHeader + '" alt="">'
    + '</div>'
    + '<div class="right" id="'+blogId+'">'
    +"<h2 class='title'>" + title + "</h2><br>" 
    + "<span class='content'>" + content + "</span><br>" 
    + '<span class="email">' + userEmail + "</span>"+'</div>'; 
    +'</div>'
    
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
                addItem(blog.blogId, blog.title, blog.content, blog.email, blog.imageHeader);
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
    $(".main-body").append(element);
    var clickElement = document.getElementById(blogId);
    clickElement.addEventListener('click', function() {
       window.location.href= window.location.href + "/viewBlog?blogId="+ blogId;
    });
}