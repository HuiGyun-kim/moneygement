document.addEventListener('DOMContentLoaded', function() {
    const unfollowButtons = document.querySelectorAll('.unfollow-button');
    const userIdInputs = document.querySelectorAll('.user-id');

    unfollowButtons.forEach(function(button, index) {
        button.addEventListener('click', function() {
            const userId = userIdInputs[index].value;

            fetch(`/follow/unfollow/${userId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        // 언팔로우 성공 시 해당 사용자를 리스트에서 삭제합니다.
                        const followerElement = button.closest('li');
                        followerElement.remove();
                    } else {
                        // 언팔로우 실패 시 에러 처리를 수행합니다.
                        console.error('언팔로우 실패');
                    }
                })
                .catch(error => {
                    console.error('네트워크 오류:', error);
                });
        });
    });
});