$(document).ready(function() {
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

    var searchTriggered = false;

    $('#search').click(function (e) {
        e.preventDefault();
        var query = $('input[name="q"]').val();
        var searchType = $('#searchType').val();

        $.ajax({
            type: 'GET',
            url: '/api/search',
            data: {
                q: query,
                type: searchType
            },
            success: function (data) {
                $('#displayfind').empty();
                $('#displayfinddisease').empty();
                var query = $('input[name="q"]').val();
                var searchType = $('#searchType').val();
                var newUrl = window.location.origin + '/search?q=' + encodeURIComponent(query);
                history.pushState(null, null, newUrl);
                $.each(data, function (index, item) {
                    if (searchType === 'all') {
                        var active;
                        $.ajax({
                            type: "GET",
                            url: "http://localhost:1999/api/account/" + item.email,
                            dataType: "json",
                            async: false,
                            success: function(account) {
                                active = account.verified;
                                if (item.hasOwnProperty('specially')) {
                                    $.ajax({
                                        type: "GET",
                                        url: "http://localhost:1999/api/user/" + item.email,
                                        dataType: "json",
                                        success: function(user) {
                                            if(active){
                                                $('#displayfind').append(`
                                                    <div class="container1 col-3" id="${user.email}">
                                                        <div class="left">
                                                            <img src="${user.avatar}" alt="Avatar">
                                                        </div>
                                                        <div class="right mt-4" id="${user.email}">
                                                            <h5 class="email"><small class="text-muted h5">Tên bác sĩ: </small>${user.name}</h5>
                                                            <h5 class="title"><small class="text-muted h5">Chuyên khoa: </small>${user.specially}</h5>
                                                        </div>
                                                    </div>
                                                `);
                                                var clickElement = document.getElementById(user.email);
                                                clickElement.addEventListener('click', function() {
                                                    window.location.href = "http://localhost:1999/doctor/" + user.email;
                                                });
                                            }

                                        },
                                        error: function(xhr, status, error) {

                                        }
                                    });
                                } else {
                                    $.ajax({
                                        url: 'http://localhost:1999/api/getAllBlog',
                                        type: 'GET',
                                        dataType: 'json',
                                        success: function(listBlog) {
                                            if(active){
                                                listBlog.forEach(function(blog) {
                                                    if (item.name === blog.title) {
                                                        const truncatedContent = blog.content.length > 320 ? blog.content.substring(0, 320) + '...' : blog.content;
                                                        $('#displayfinddisease').append(`
                                                            <div class="container2" id="${blog.blogId}">
                                                                <div class="left">
                                                                    <img src="${blog.imageHeader}" alt="">
                                                                </div>
                                                                <div class="right mt-4" id="${blog.blogId}">
                                                                    <h5 class="email"><small class="text-muted h5">Từ: </small>${blog.email}</h5>
                                                                    <h5 class="title"><small class="text-muted h5">Nội dung: </small>${blog.title}</h5>
                                                                    <h5 class="content"><small class="text-muted">${truncatedContent}</small></h5><br>
                                                                </div>
                                                            </div>
                                                        `);
                                                        var clickElement = document.getElementById(blog.blogId);
                                                        clickElement.addEventListener('click', function() {
                                                            window.location.href = "http://localhost:1999/community/viewBlog?blogId=" + blog.blogId;
                                                        });
                                                    }
                                                });
                                            }

                                        },
                                        error: function(error) {
                                            console.error('Lỗi khi lấy danh sách bài viết:', error);
                                        }
                                    });

                                }
                            },
                            error: function(xhr, status, error) {
                            }
                        });

                    }else if (searchType === 'symptom') {
                        $.ajax({
                            url: 'http://localhost:1999/api/getAllBlog',
                            type: 'GET',
                            dataType: 'json',
                            success: function(listBlog) {
                                listBlog.forEach(function(blog) {
                                    if (item.name === blog.title) {
                                        const truncatedContent = blog.content.length > 320 ? blog.content.substring(0, 320) + '...' : blog.content;
                                        $('#displayfinddisease').append(`
                                            <div class="container2" id="${blog.blogId}">
                                                <div class="left">
                                                    <img src="${blog.imageHeader}" alt="">
                                                </div>
                                                <div class="right mt-4" id="${blog.blogId}">
                                                    <h5 class="email"><small class="text-muted h5">Từ: </small>${blog.email}</h5>
                                                    <h5 class="title"><small class="text-muted h5">Nội dung: </small>${blog.title}</h5>
                                                    <h5 class="content"><small class="text-muted">${truncatedContent}</small></h5><br>
                                                </div>
                                            </div>
                                        `);
                                        var clickElement = document.getElementById(blog.blogId);
                                        clickElement.addEventListener('click', function() {
                                            window.location.href = "http://localhost:1999/community/viewBlog?blogId=" + blog.blogId;
                                        });
                                    }
                                });
                            },
                            error: function(error) {
                                console.error('Lỗi khi lấy danh sách bài viết:', error);
                            }
                        });
                    }else if (searchType === 'doctor') {
                        if (item.hasOwnProperty('specially')) {
                            $.ajax({
                                type: "GET",
                                url: "http://localhost:1999/api/user/" + item.email,
                                dataType: "json",
                                success: function (user) {
                                    $('#displayfind').append(`
                                        <div class="container1 col-3" id="${user.email}">
                                            <div class="left">
                                                <img src="${user.avatar}" alt="Avatar">
                                            </div>
                                            <div class="right mt-4" id="${user.email}">
                                                <h5 class="email"><small class="text-muted h5">Tên bác sĩ: </small>${user.name}</h5>
                                                <h5 class="title"><small class="text-muted h5">Chuyên khoa: </small>${user.specially}</h5>
                                            </div>
                                        </div>
                                    `);
                                    var clickElement = document.getElementById(user.email);
                                    clickElement.addEventListener('click', function() {
                                        window.location.href = "http://localhost:1999/doctor/" + user.email;
                                    });
                                },
                                error: function (xhr, status, error) {

                                }
                            });
                        }
                    }
                });
            }
        });
        searchTriggered = true;
    });
    function getUrlParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }
    function triggerSearchOnce() {
        var queryFromURL = getUrlParameter('q');
        if (queryFromURL && !searchTriggered) {
            $('input[name="q"]').val(queryFromURL);
            $('#search').trigger('click');
        }
    }
    triggerSearchOnce();
});