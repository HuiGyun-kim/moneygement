function checkSubmit() {
    var submitButton = document.querySelector('.btn-submit button');
    submitButton.disabled = !isUser;
}

var isUser = false;
var isNickname = false;

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
            event.preventDefault();
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
    });

});