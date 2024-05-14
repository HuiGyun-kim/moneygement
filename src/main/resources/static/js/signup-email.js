function checkSubmit() {
    var submitButton = document.querySelector('.btn-submit button');
    submitButton.disabled = !(isUser && isEmail && isMail);
}

var isUser = false;
var isEmail = false;
var isMail = false;

document.addEventListener('DOMContentLoaded', function (){
    document.getElementById('password_check').addEventListener('keyup', function (event){
        var password = document.getElementById('password').value;
        var password_check = document.getElementById('password_check').value;
        var pw_check_msg = document.getElementById('pw_check_msg');

        if (password === password_check){
            pw_check_msg.textContent = '비밀번호가 일치합니다';
        }
        else{
            pw_check_msg.textContent = '비밀번호 일치하지 않습니다. 다시 확인해주세요.';
            event.preventDefault();
        }
    });
});

document.addEventListener('DOMContentLoaded', function (){
    var form = document.getElementById('form')
    form.addEventListener('submit', function (event){
        var password = document.getElementById('password').value;
        var password_check = document.getElementById('password_check').value;

        if (password !== password_check){
            alert("비밀번호가 일치하지 않습니다. 다시 작성해주세요.")
            event.preventDefault();
        }

        else {
            localStorage.removeItem('isEmail');
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('usernameOverlay').addEventListener('click', function () {
        var username = document.getElementById('username').value;
        if(username) {
            fetch('/users/checkUsername?username=' + username)
                .then(response => response.json())
                .then(data => {
                    if(data.isAvailable) {
                        alert('사용 가능한 아이디입니다.');
                        isUser = true;
                    }
                    else {
                        alert('이미 사용중인 아이디입니다.');
                        isUser = false;
                    }
                    checkSubmit();
                });
        }
    });


    document.getElementById('form').addEventListener('submit', function (event) {
        if(!isUser) {
            alert('아이디가 이미 사용중입니다.');
            event.preventDefault();
        }

        if(!isEmail) {
            alert('이메일 인증이 되지 않았습니다.')
            event.preventDefault();
        }

        if(!isMail){
            alert('이메일 인증이 되지 않았습니다.')
            event.preventDefault();
        }
    });


});

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("emailOverlay").addEventListener("click", function() {
        var email = document.getElementById("email").value;

        if(!isMail) {
            alert("이메일 중복 체크를 먼저 해주세요.");
            return;
        }

        if(email){
            fetch('/users/sendEmail', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'email=' + encodeURIComponent(email)
            })
                .then(response => {
                    if(response.ok) {
                        return response.text();
                    }
                    else {
                        throw new Error('서버로부터 응답을 받는데 실패했습니다.');
                    }
                })
                .then(data => {
                    if(data.isAvailable) {
                        alert('사용 가능한 아이디입니다.');
                        isUser = true;
                    }
                    else {
                        alert('이미 사용중인 아이디입니다.');
                        isUser = false;
                    }
                    checkSubmit();
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
        else {
            alert("이메일을 입력해주세요.");
        }
    });
});

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("emailcheck").addEventListener("click", function() {
        var mailCheck = localStorage.getItem("isEmail");

        if(mailCheck === "true") {
            alert("이메일 인증이 확인되었습니다.");
            isEmail = true;
        }
        else {
            alert("이메일 인증이 완료되지 않았습니다. 인증을 진행해 주세요.");
            isEmail = false;
        }
        checkSubmit();
    });
});

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("mailOverlay").addEventListener("click", function() {
        var email = document.getElementById("email").value;
        if(email){
            fetch('/users/checkEmail?email=' + email)
                .then(response => response.json())
                .then(data => {
                    if(data.isAvailable) {
                        alert('사용 가능한 이메일입니다.');
                        isMail = true;
                    }
                    else {
                        alert('이미 사용중인 이메일입니다.');
                        isMail = false;
                    }
                    checkSubmit();
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
        else {
            alert("이메일을 입력해주세요.");
        }
    });
});
