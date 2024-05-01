window.onload = function() {
    var error = getCookie('loginError');
    if (error) {
        error = decodeURIComponent(error.replace(/\+/g, ' ')); // "+"를 공백으로 변환하고 디코딩
        alert(error);
        // 쿠키에서 에러 메시지를 삭제
        document.cookie = 'loginError=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    }

    // "아이디 저장" 쿠키 처리
    var rememberUser = getCookie('rememberUser');
    if (rememberUser) {
        rememberUser = decodeURIComponent(rememberUser.replace(/\+/g, ' ')); // 쿠키에서 가져온 값을 디코딩
        document.getElementById('user-id').value = rememberUser;
        document.getElementById('remember-id').checked = true;
    }
};

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}
