

var currentUser = null;
$(document).ready(function() {
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
});

$(document).ready(function() {
    $('#createblog').submit(function(event) {
        event.preventDefault();

        var title = $('#title').val() || null;
        var content = $('#content').val() || null;
        var email = currentUser;

        var listImg = [];
        
        for (var i=0; i<listFiles.length; i++) {
            if (listFiles[i] != null) {
                var name = listFiles[i].name;
                listImg.push({"name": name})
            }
        } 
        const data = {
            "title": title,
            "content": content,
            "email": email,
            "files": listImg
        }
        console.log(JSON.stringify(data));
        var jsonData = JSON.stringify(data);

        addBlog(jsonData);

        

        for (let i=0; i<listFiles.length; i++) {
            var formImgData = new FormData();
            if (listFiles[i] != null) {
                formImgData.append('file', listFiles[i]);
                uploadImageToFirebase(formImgData);
            } 
        }
    });

    function addBlog(data) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:1999/api/createNewBlog",
            data: data,
            success: function(responseData) {
                alert("Success");
            },
            error: function(error) {
                alert('error');
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

    let listFiles = []; 

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
});

