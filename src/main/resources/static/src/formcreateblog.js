$(document).ready(function() {
    
    var listFiles = [];
    var imageForBlog = [];
    var currentUser = null;
    var listImg = [];

    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function(response) {
            currentUser = response.email;
        },
        error: function(xhr, status, error) {
            console.error(error);
        }
    });

    $('#createblog').submit(function(event) {
        event.preventDefault();

        for (let i=0; i<listFiles.length; i++) {
            if (listFiles[i] != null) {
                listImg.push(listFiles[i]);
            }
        }

        for (let i=0; i<listFiles.length; i++) {
            var formImgData = new FormData();
            if (listFiles[i] != null) {
                formImgData.append('file', listFiles[i]);
                uploadImageToFirebase(formImgData);
                console.log(listFiles[i]);
                console.log(imageForBlog[i])
            } 
        }
    });

    function addBlog(data) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:1999/api/createNewBlog",
            data: data,
            success: function(data) {
            },
            error: function(error) {
            }
        });        
    }

    function uploadImageToFirebase(formData) {
        $.ajax({
            type: "POST",
            processData: false,
            contentType: false,
            url: "http://localhost:1999/api/upload",
            data: formData,
            success: function (response) {
                imageForBlog.push({"name": response});
                handleAfterFinistUpload();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                if (jqXHR.responseJSON && jqXHR.responseJSON.error) {
                    alert("Error: " + jqXHR.responseJSON.error);
                } else {
                    console.log("XHR:", jqXHR);
                    console.log("Status:", textStatus);
                    console.log("Error:", errorThrown);
                    alert("An error occurred. Please check the console for details.");
                }
            }
        });
    };

    $('#uploadImage').change(function() {
        var image = $('#uploadImage')[0].files[0];
        listFiles.push(image);
        var reader = new FileReader();
    
        reader.onload = function(event) {
            var item = '<li class="item" id="item'+listFiles.length+'"><img src="'+event.target.result+'" alt=""></li>';
            $('.list-img').append(item);
        };
        reader.readAsDataURL(image);
        console.log(listFiles.length);
    });
    
    $('.list-img').on('click', '.item', function() {
        var selectedId = $(this).attr('id');
        $(this).remove();
        var index = parseInt(selectedId.slice(4))-1;
        listFiles[index] = null;
    });

    function handleAfterFinistUpload() {
        if (imageForBlog.length < listImg.length) {
            
        }
        else  {
            var title = $('#title').val() || null;
            var content = $('#content').val() || null;
            var email = currentUser;

            const data = {
                "title": title,
                "content": content,
                "email": email,
                "files": imageForBlog
            }
            var jsonData = JSON.stringify(data);
    
            addBlog(jsonData);

            alert("Bạn đã đăng bài mới thành công!")
        }
    }
});

