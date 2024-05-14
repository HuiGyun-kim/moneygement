document.addEventListener('DOMContentLoaded', function () {
    const matchList = document.getElementById('match-list');
    if (matchList) {
        matchList.style.display = 'none';
    }

    document.querySelector('.btn-code-send').addEventListener('click', function () {
        const username = document.getElementById('find-user-id').value;

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
        const username = document.getElementById('find-user-id').value;
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
                    fetch('/users/send-reset-link', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `username=${encodeURIComponent(username)}`
                    })
                        .then(response => response.text())
                        .then(message => {
                            alert('비밀번호 초기화 링크가 이메일로 전송되었습니다.');
                        })
                        .catch(error => console.error('비밀번호 초기화 링크 전송 오류:', error));
                }
            })
            .catch(error => console.error('인증번호 확인 오류:', error));
    });



});

