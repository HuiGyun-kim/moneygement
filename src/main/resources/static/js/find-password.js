document.addEventListener('DOMContentLoaded', function () {
    const matchList = document.getElementById('match-list');
    let username = "";
    if (matchList) {
        matchList.style.display = 'none';
    }

    document.querySelector('.btn-code-send').addEventListener('click', function () {
        username = document.getElementById('find-user-id').value;

        fetch('/users/send-password-verification-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${encodeURIComponent(username)}`
        })
            .then(response => response.text())
            .then(message => {
                alert(message);
                document.getElementById('find-user-id').disabled = true;
            })
            .catch(error => console.error('인증번호 요청 오류:', error));
    });


    document.querySelector('.btn-code-verify').addEventListener('click', function () {
        username = document.getElementById('find-user-id').value;
        const code = document.querySelector('.code-input input').value;

        fetch('/users/verify-password-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${encodeURIComponent(username)}&code=${encodeURIComponent(code)}`
        })
            .then(response => response.text())
            .then(message => {
                alert(message);
                if (message === '인증되었습니다.') {
                    document.querySelector('.btn-next').disabled = false;
                }
            })
            .catch(error => console.error('인증번호 확인 오류:', error));
    });

    document.querySelector('.btn-next').addEventListener('click', function () {
        document.getElementById('passwordForm').style.display = 'block';
    });

    document.getElementById('passwordForm').addEventListener('submit', function (event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 방지합니다.
        var password = document.getElementById('password').value;
        var confirmPassword = document.getElementById('confirmPassword').value;
        console.log(username);

        if(password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
            return false;
        }

        fetch('/users/reset', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({username: username, password: password, confirmPassword: confirmPassword})
        })
            .then(response => response.json())
            .then(data => {
                alert('비밀번호가 성공적으로 변경되었습니다.');
            })
            .catch((error) => {
                console.error('Error:', error);
            });

        return false;
    });

});


