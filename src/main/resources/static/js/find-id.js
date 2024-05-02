// CSRF 토큰 추출
const csrfToken = document.getElementById('csrf-token').value;
// 페이지 로드 시 매치 리스트 숨기기
document.addEventListener('DOMContentLoaded', function() {
    const matchList = document.getElementById('match-list');
    if (matchList) {
        matchList.style.display = 'none';
    }
});
// 인증번호 받기
document.querySelector('.btn-code-send').addEventListener('click', function() {
    const email = document.getElementById('email').value;

    fetch('/users/send-id-verification-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded', // JSON 대신 x-www-form-urlencoded로 전송
            'X-CSRF-TOKEN': csrfToken // CSRF 토큰 추가
        },
        body: `email=${encodeURIComponent(email)}`
    })
        .then(response => response.text())
        .then(message => alert(message))
        .catch(error => console.error('인증번호 요청 오류:', error));
});

// 인증번호 확인 및 다음 버튼 활성화
document.querySelector('.btn-code-verify').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    const code = document.querySelector('.code-input input').value;

    fetch('/users/verify-id-code', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded', // JSON 대신 x-www-form-urlencoded로 전송
            'X-CSRF-TOKEN': csrfToken // CSRF 토큰 추가
        },
        body: `email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`
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
// 아이디 찾기 서밋 처리
function handleSubmit(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;

    fetch('/users/find-id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': csrfToken
        },
        body: `email=${encodeURIComponent(email)}`
    })
        .then(response => {
            if (response.ok && response.headers.get('content-type')?.includes('application/json')) {
                return response.json(); // JSON 응답을 파싱
            } else {
                throw new Error('Unexpected response type');
            }
        })
        .then(data => {
            const matchList = document.getElementById('match-list');
            const userLabel = document.querySelector('label[for="user1"]');
            const signupDate = document.getElementById('signup-date');

            if (userLabel && signupDate && data.username) { // null 검사 추가
                userLabel.textContent = data.username;
                signupDate.textContent = `가입 ${data.signupDate}`;
                matchList.style.removeProperty('display'); // match-list의 display 속성 제거
                document.querySelector('.btn-next').style.display = 'none'; // 다음 버튼을 숨깁니다.
            } else {
                alert('일치하는 아이디가 없습니다.');
            }
        })
        .catch(error => console.error('아이디 찾기 오류:', error));

    return false;
}
