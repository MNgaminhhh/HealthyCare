$(document).ready(function() {
    var maxBlogToShow = 8;
    var blogCounter = 0;
    var maxItemsToShow = 8;
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
    $('#search').click(function(e) {
        e.preventDefault();
        var query = $('input[name="q"]').val();
        var searchType = $('input[name="type"]').val();
        window.location.href = "/search?q=" + query + "&type=" + searchType;
    });
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/alluser",
        dataType: "json",
        success: function(response) {

            response.forEach(function(doctor) {
                var itemsCounter = 0;
                var active;
                $.ajax({
                    type: "GET",
                    url: "http://localhost:1999/api/account/" + doctor.email,
                    dataType: "json",
                    async: false,
                    success: function(account) {
                        active = account.verified;
                    },
                    error: function(xhr, status, error) {
                    }
                });
                if (doctor.role === "ROLE_DOCTOR" && itemsCounter < maxItemsToShow && active) {
                    var cardHtml = `
                        <div class="card m-2 item" style="width: 18rem;">
                            <a href="/doctor/${doctor.email}">
                                <img src="${doctor.avatar}" class="avatarImg" alt="Doctor Avatar">
                                <div class="card-body">
                                    <h5 class="card-title text-center">${doctor.name}</h5>
                                    <h5 class="card-text text-center"><small class="text-muted">${doctor.specially}</small></h5>
                                    <h5 class="card-text text-center"><small class="text-muted">${doctor.workplace}</small></h5>
                                </div>
                            </a>
                        </div>`;
                    $('#alldoctor').append(cardHtml);

                    itemsCounter++;
                }
            });
        },
        error: function(xhr, status, error) {
        }
    });

    $.ajax({
        url: 'http://localhost:1999/api/getAllBlog',
        type: 'GET',
        dataType: 'json',
        success: function(listBlog) {
            listBlog.forEach(function(blog) {
                if (blogCounter < maxBlogToShow) {
                    addItem(blog.blogId, blog.title, blog.content, blog.email, blog.imageHeader);
                    blogCounter++;
                } else {
                    return;
                }
            });
        },
        error: function(error) {

        }
    })

});
var blogId = 0
var listBlog = []
var item = ""
function addItem(blogId, name, content, userEmail, imageHeader) {
    element = createBlogItem(blogId, name, content, userEmail, imageHeader);
    elem = createNearBlogItem(blogId, name, content, userEmail, imageHeader);
    $(".allblog").append(element);
    $(".allnearblog").append(elem);
    var clickElement = document.getElementById(blogId);
    clickElement.addEventListener('click', function() {
        window.location.href = window.location.href + "community/viewBlog?blogId="+ blogId
    });
    var clickNearElement = document.getElementById("hi"+blogId);
    clickNearElement.addEventListener('click', function() {
        window.location.href = window.location.href + "community/viewBlog?blogId="+ blogId
    });
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
    `;

    return element;
}

function createNearBlogItem(blogId, title, content, userEmail, imageHeader) {
    const truncatedContent = content.length > 100 ? content.substring(0, 100) + '...' : content;
    const truncatedTitle = title.length > 100 ? title.substring(0, 100) + '...' : title;
    var element = `
        <div class="container2 " id="hi${blogId}">
            <div class="border-left border-left-1 ">
                <p class="ml-2 email"><small class="text-muted">Từ: </small>${userEmail}</p>
                <p class="ml-2 title">${truncatedTitle}</p>
            </div>
            
        </div>
    `;

    return element;
}