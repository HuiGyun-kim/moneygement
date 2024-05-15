document.addEventListener('DOMContentLoaded', function() {
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;

    var deleteAccountBtn = document.getElementById('deleteAccountBtn');
    deleteAccountBtn.addEventListener('click', function(event) {
        var password = document.getElementById('password').value;
        console.log(password);
        console.log(header);
        console.log(token);
        if (!password) {
            alert('비밀번호를 입력해주세요.');
            return;
        }

        if (!confirm('정말로 계정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
            return;
        }

        fetch('/api/delete-account', {
            method: 'POST',
            headers: {
                'header': header, // 가져온 header 값을 사용
                'X-Requested-With': 'XMLHttpRequest',
                "Content-Type": "application/json",
                'X-CSRF-Token': token
            },
            body: JSON.stringify({ password: password })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message); // 성공 메시지
                    window.location.href = '/login'; // 로그인 페이지로 리디렉션
                } else {
                    alert(data.message); // 실패 메시지
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('회원 탈퇴 처리 중 오류가 발생했습니다.');
            });
    });
});