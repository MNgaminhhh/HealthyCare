function registerUser(){
  let role = '';
  if ($("#role").val() === 'doctor') {
      role = 'ROLE_DOCTOR';
  } else if ($("#role").val() === 'patient') {
      role = 'ROLE_PATIENT';
  }else{
    role = null;
  }
  const email = $("#email").val() || null;
  const password = $("#password").val() || null;
  const confirmPassword = $("#confirmPassword").val() || null;
  const fullname = $("#fullname").val() || null;
  const phoneNumber = $("#phoneNumber").val() || null;
  const gender = $("input[name='gender']:checked").val() || null;
  const dob = $("#dob").val() || null;
  const address = $("#address").val() || null;
  const education = $("#education").val() || null;
  const workplace = $("#workplace").val() || null;
  const introduction = $("#introduction").val() || null;
  const specially = $("#specially").val() || null;
  const numberofyear = $("#numberofyear").val() || null;
  const underlying_disease = $("#underlying_disease").val() || null;
  const avatar = "https://firebasestorage.googleapis.com/v0/b/healthycare-16dac.appspot.com/o/Microsoft_Account_Logo.svg.png?alt=media&token=ec35178e-3742-4ae1-951d-02685cef510a";

  if (password !== confirmPassword) {
      alert("Mật khẩu và nhập lại mật khẩu không khớp. Vui lòng kiểm tra lại!");
      return;
  }

  const userInfo = {
      "email": email,
      "password": password,
      "name": fullname,
      "phone": phoneNumber,
      "gender": gender,
      "birthday": dob,
      "address": address,
      "role": role,
      "education": education,
      "workplace": workplace,
      "introduction": introduction,
      "specially": specially,
      "numberofyear": numberofyear,
      "underlyingDisease": underlying_disease,
      "avatar": avatar
  };

  $.ajax({
      type: "POST",
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
  });
}

$(document).ready(function(){
  $("#registerButton").click(registerUser);
  $('#role').change(function(){
    var selectedRole = $(this).val();
    var additionalInfoHtml = '';

    if(selectedRole == 'patient'){
        additionalInfoHtml = `
            <div class="form-group">
                <label for="underlying_disease">Bệnh nền:</label>
                <input type="text" class="form-control" name="underlying_disease" id="underlying_disease" placeholder="Nhập bệnh nền" required>
            </div>
        `;
    } else if(selectedRole == 'doctor'){
        additionalInfoHtml = `
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="education">Học vấn:</label>
                    <input type="text" class="form-control" name="education" id="education" placeholder="Nhập học vấn" required>
                </div>
                <div class="form-group">
                    <label for="workplace">Nơi làm việc:</label>
                    <input type="text" class="form-control" name="workplace" id="workplace" placeholder="Nhập nơi làm việc" required>
                </div>
                <div class="form-group">
                    <label for="specially">Chuyên môn:</label>
                    <input type="text" class="form-control" name="specially" id="specially" placeholder="Nhập chuyên môn" required>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="introduction">Giới thiệu bản thân:</label>
                    <textarea class="form-control" name="introduction" id="introduction" placeholder="Giới thiệu bản thân" required></textarea>
                </div>
                <div class="form-group">
                    <label for="numberofyear">Số năm kinh nghiệm:</label>
                    <input type="number" class="form-control" name="numberofyear" id="numberofyear" placeholder="Nhập số năm kinh nghiệm" required>
                </div>
            </div>
        </div>
        `;
        
    }

    $('#additionalInfo').html(additionalInfoHtml);
  });

});

