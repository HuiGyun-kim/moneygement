import './userInfo.js';
document.addEventListener('DOMContentLoaded', () => {
    const followButton = document.getElementById('followButton');
    let userId = followButton?.dataset.userId; // userId 값 가져오기

    if (userId) {
        userId = userId.trim(); // userId 값의 앞뒤 공백 제거
    }
    followButton.addEventListener('click', (event) => {
        event.preventDefault(); // 기본 폼 제출 동작 방지

        if (userId) { // userId 값이 존재하는지 확인
            $.ajax({
                url: `/follow/${userId}`,
                type: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                success: function(response) {
                    getUserInfo();
                    updateFollowCounts(); // 팔로워 수와 팔로잉 수 업데이트
                },
                error: function(xhr, status, error) {
                    console.error('error');
                }
            });
        } else {
            console.error('User ID is missing.');
        }
    });
    function updateFollowCounts() { //팔로우카운트
        const followersCountElement = document.getElementById('followersCount');
        const followingCountElement = document.getElementById('followingCount');

        // 팔로워 수와 팔로잉 수 업데이트
        followersCountElement.textContent = "${followersCount}";
        followingCountElement.textContent = "${followingCount}";
    }
});