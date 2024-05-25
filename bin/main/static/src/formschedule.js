$(document).ready(function(){
    var currentUser;
    var inputName = document.getElementById("name");

    var urlParams = window.location.search;

    var currentUrl = window.location.href;

    var radios = document.getElementsByName("gender");
    if (currentUrl.includes("view")) {
        var inputs = document.getElementsByClassName("form-control");
        var params = new URLSearchParams(urlParams);
        var sId = params.get("id");
        var canEdit = params.get("edit");
        if (canEdit ==="false") {
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].readOnly = true;
            };
            $("#confirm").css("display", "none"); 
        }

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

        $.ajax({
            type: "GET",
            url: "http://localhost:1999/api/getAppointmentById?id="+sId,
            dataType: "json",
            success: function(response) {
                currentUser = response.email;
    
                $("#name").val(response.pName);
                $("#address").val(response.pAddress);
                $("#phone").val(response.pPhone);
                $("#birthday").val(response.birthday);
                $("#underlying").val(response.uderlying);
                $("#infor-name").text(response.pName);
                $("#shedule-date").val(response.sDate);
                $("#infor-date").text(response.sDate)
                $("#shedule-time").val(response.sTime);
                $("#infor-time").text(response.sTime);
                $("#notes").val(response.notes)
                $("#doctor-name").text(response.dName)
                $(".address-doctor span").text(response.dAddress);
                $(".doctor-img img").attr("src", response.dAvt);

                
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
        })

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
    } else {
        var params = new URLSearchParams(urlParams);
        var doctorEmail = params.get('email');
        
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
                $("#infor-name").text(response.dName);
                
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
    }
    
    function changeConfirmed(content, elementId) {
        var dest = document.getElementById(elementId);
        dest.textContent = content;
    }
})