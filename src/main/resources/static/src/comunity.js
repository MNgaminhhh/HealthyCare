
$(document).ready(function() {
    getBlog(null);
});



function createBlogItem(title, content, username) {
                  
            
    var element = '<div class="left">'
    + '<img src="https://vtitech.vn/wp-content/uploads/2020/10/test-100.png" alt="">'
    + '</div>'
    + '<div class="right">'
    +"<h2 class='title'>" + title + "</h2><br>" 
    + "<span class='content'>" + content + "</span><br>" 
    + "<span class='>" + username + "</span>"+'</div>'; 
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
                addItem(blog.title, blog.content, blog.email);
            });
        },
        error: function(error) {
            console.error('Lỗi khi lấy thông tin người dùng:', error);
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

function addItem(name, content, userEmail) {

    element = createBlogItem(name, content, userEmail);
    $(".container1").append(element);
}