const firebaseConfig = {
    apiKey: "AIzaSyDNV58mKZgeHPrAOuDqDLCdjx440NOTz68",
    authDomain: "healthycare-16dac.firebaseapp.com",
    databaseURL: "https://healthycare-16dac-default-rtdb.firebaseio.com",
    projectId: "healthycare-16dac",
    storageBucket: "healthycare-16dac.appspot.com",
    messagingSenderId: "732902186001",
    appId: "1:732902186001:web:51b549ed77b1c3826aa5b0",
    measurementId: "G-NRGVY89SFJ"
};
firebase.initializeApp(firebaseConfig);

const db = firebase.database().ref('conversations');
const msdb = firebase.database().ref('Messages');

$(document).ready(function() {
    $("#addButton").click(function() {
        const email = $("#inlineFormInputGroup").val();
        if (validateEmail(email)) {
            window.location.href = "http://localhost:1999/message/" + email;
        } else {
            alert("Email không hợp lệ!");
        }
    });
    $.ajax({
        type: "GET",
        url: "http://localhost:1999/api/info",
        dataType: "json",
        success: function(response) {
            $('#fullname').text(response.name);
            const imageUrl = response.avatar;
            userCurrent = response.email;
            document.getElementById("userAvatar").src = imageUrl;
            userEmail = window.location.pathname.split('/').pop();
            $.ajax({
                type: "GET",
                url: "http://localhost:1999/api/user/" + userEmail,
                dataType: "json",
                success: function(user) {
                    displayUserAccountInfo(user, user.role);
                    $.ajax({
                        type: "GET",
                        url: "http://localhost:1999/api/conversation/find?email1=" + userCurrent + "&email2=" + userEmail,
                        contentType: 'application/json',
                        success: function(user) {
                            userid = user.id
                            loadData(user.id)
                            $("#btn-send").show();
                            $("input.form-control").show();
                            $("#btn-start").hide();
                        },
                        error: function(xhr, status, error) {
                            $("#btn-send").hide();
                            $("input.form-control").hide();
                            $("#btn-start").show();
                        }
                    });
                },
                error: function(xhr, status, error) {
                    $("#btn-send").hide();
                    $("input.form-control").hide();
                    $("#btn-start").hide();
                }
            });
        },
        error: function(xhr, status, error) {
        }
    });
    $("#btn-start").click(startButton);
    $("#btn-send").click(sendButton);
    loadFriend()
});
let userEmail
let userCurrent
let userid
function validateEmail(email) {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
}
function sendButton(){
    let messageContent = $(".form-control").val();
    if (messageContent.trim() !== "") {
        let currentTime = new Date().toISOString();
        let msg = {
            "content": messageContent.trim(),
            "sendOn": currentTime,
            "seen": false,
        }
        $.ajax({
            type: "POST",
            url: "http://localhost:1999/api/messages/conversation/" + userid,
            contentType: 'application/json',
            data: JSON.stringify(msg),
            success: function(user) {
            },
            error: function(xhr, status, error) {
            }
        });
    } else {
        alert("Vui lòng nhập nội dung tin nhắn!");
    }
}

function startButton(){
    let newConversation = {
    }
    $.ajax({
        type: "POST",
        url: "http://localhost:1999/api/conversation?email1=" + userCurrent + "&email2=" + userEmail,
        contentType: 'application/json',
        data: JSON.stringify(newConversation),
        success: function(user) {
            location.reload();
        },
        error: function(xhr, status, error) {
        }
    });
}
function displayUserAccountInfo(user, role) {
    $('#userload').empty();
    var userAccHtml = `
        <span class="chat-icon">
            <img class="img-fluid" src="https://mehedihtml.com/chatbox/assets/img/arroleftt.svg" alt="image title">
        </span>
        <div class="flex-shrink-0">
            <img class="img-fluid" src="${user.avatar ? user.avatar : 'placeholder.jpg'}" alt="user img">
        </div>
        <div class="flex-grow-1 ms-3">
            <h3>${user.name}</h3>
    `;
    if (role === 'ROLE_DOCTOR') {
        userAccHtml += `
            <p>Bác Sĩ | ${user.numberofyear} năm kinh nghiệm</p>
        </div>
        `;
    } else if (role === 'ROLE_PATIENT') {
        userAccHtml += `
            <p>Bệnh Nhân</p>
        </div>
    `;
    }
    $('#userload').append(userAccHtml);
}

function loadData(conversationId) {
    const messagesRef = firebase.database().ref(`Messages/${conversationId}`);

    messagesRef.orderByChild('send').on('value', (snapshot) => {
        $("#msgcontainer").empty();
        snapshot.forEach((childSnapshot) => {
            const message = childSnapshot.val();
            let messageClass = (message.receiver === userCurrent) ? "sender" : "repaly";
            const sendTime = new Date(message.send);
            const formattedTime = `${sendTime.getHours()}:${(sendTime.getMinutes() < 10 ? '0' : '') + sendTime.getMinutes()} ${sendTime.getDate()}-${(sendTime.getMonth() < 9 ? '0' : '') + (sendTime.getMonth() + 1)}-${sendTime.getFullYear()}`;
            $("#msgcontainer").append(`
                <li class="${messageClass}">
                    <p>${message.content}</p>
                    <span class="time">${formattedTime}</span>
                </li>
            `);
        });
    });
}

function loadFriend() {
    const conversationsRef = firebase.database().ref('conversations');

    conversationsRef.on('value', (snapshot) => {
        $("#friendlist").empty();

        snapshot.forEach((conversationSnapshot) => {
            const conversation = conversationSnapshot.val();
            const user1 = conversation.user1;
            const user2 = conversation.user2;
            if (user1 === userCurrent || user2 === userCurrent) {
                const friendName = (user1 === userCurrent) ? user2 : user1;
                $.ajax({
                    type: "GET",
                    url: "http://localhost:1999/api/user/" + friendName,
                    dataType: "json",
                    success: function(user) {
                        let userAccHtml;
                        if (user.role === 'ROLE_DOCTOR') {
                            userAccHtml = "Bác Sĩ";
                        } else if (user.role === 'ROLE_PATIENT') {
                            userAccHtml = "Bệnh Nhân";
                        }
                        $("#friendlist").append(`
                            <a href="http://localhost:1999/message/${user.email}" class="d-flex align-items-center">
                                <div class="flex-shrink-0">
                                    <img class="img-fluid" src="${user.avatar}" alt="user img">
                                </div>
                                <div class="flex-grow-1 ms-3">
                                    <h3>${user.name}</h3>
                                    <p>${userAccHtml}</p>
                                </div>
                            </a>
                        `);

                    },
                    error: function(xhr, status, error) {
                        console.error(error);
                    }
                });


            }
        });
    });
}


