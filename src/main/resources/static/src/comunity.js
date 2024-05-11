

$(document).ready(function() {
    getBlog(null);
    alert("ready");
})


function createBlogItem(title, content, username) {
    var element = "<h2 class="+title+">Title</h2><br><span class="+content+">Content</span><br><span class="+username+">UserName</span>";
    return element;
}

var blogId = 0
var listBlog = []
var item = ""
function getBlog(item) {
    $.ajax({
        type:GET,
        contentType: "application/json",
        url: "http://localhost:1999/api/getBlogBy?blogId=1",
        success: function(data){
            populatedBlog(data);
        },
        error: function() {
            alert("error");
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

function populatedBlog(data) {
    var title = data.title;
    var username = data.account;
    var item = createBlogItem(title, null, username);
    $('.right').append(item);
}