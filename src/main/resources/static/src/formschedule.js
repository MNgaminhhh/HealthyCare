$(document).ready(function(){
    var currentUser;
    var inputName = document.getElementById("name");
    
    var currentUrl = window.location.search;
    var urlParams = new URLSearchParams(currentUrl);
    var doctorEmail = urlParams.get('email');

    inputName.addEventListener('input', function() {
        changeConfirmed(inputName.value, "infor-name");
    })

    var inputScheduledDate = document.getElementById("shedule-date");
    inputScheduledDate.addEventListener('input', function() {
        changeConfirmed(inputScheduledDate.value, "infor-date");
    })

    var inputScheduledTime = document.getElementById("shedule-time");
    inputScheduledTime.addEventListener('input', function() {
        changeConfirmed(inputScheduledTime.value.toString(), "infor-time");
    })

    var radios = document.getElementsByName("gender");
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function(response) {
            currentUser = response.email;
            $("#name").val(response.name);
            $("#address").val(response.address);
            $("#phone").val(response.phone);
            $("#birthday").val(response.birthday);
            $("#underlying").val(response.underlyingDisease);
            $("#infor-name").text(response.name);

            for (var i=0; i<radios.length; i++) {
                var gender = null;
                if (response.gender==='male') {
                    gender = 'Nam';
                } else {
                    gender = 'Nữ';
                }
                if (radios[i].value ===gender) {
                    radios[i].checked = true;
                }
            }

        },
        error: function(xhr, status, error) {
        }
    });

    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/getDoctorByEmail?email="+doctorEmail,
        dataType: "json",
        success: function(response) {
            $("#doctor-name").text(response.dName);
            $(".address-doctor span").text(response.address);
            $(".doctor-img img").attr("src", response.avt)
        },
        error: function(xhr, status, error) {
        }
    })

    function changeConfirmed(content, elementId) {
        var dest = document.getElementById(elementId);
        dest.textContent = content;
    }

    $("#confirm").click(function() {
        const data = {
            "date": inputScheduledDate.value,
            "time": inputScheduledTime.value,
            "patient": currentUser,
            "doctor": doctorEmail,
            "notes": $("#notes").val()
        };
        const jsonData = JSON.stringify(data);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:1999/api/createAppointment",
            data: jsonData,
            success: function(jsonData) {
                alert("Bạn đã đặt lịch khám thành công!")
            },
            error: function(error) {
                alert("Không thành công!")
            }
        });    
    })
})