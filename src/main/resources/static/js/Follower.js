function goBack() {
    window.history.back();
}

document.addEventListener('DOMContentLoaded', function() {
    const unfollowButtons = document.querySelectorAll('.unfollow-button');
    unfollowButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const userId = button.getAttribute('data-user-id');

            $.ajax({
                url: `/unfollow/${userId}`,
                type: 'DELETE',
                success: function() {
                    // 언팔로우 성공 시 해당 사용자를 리스트에서 삭제합니다.
                    const followerElement = button.closest('li');
                    followerElement.remove();
                },
                error: function() {
                    // 언팔로우 실패 시 에러 처리를 수행합니다.
                    console.error('언팔로우 실패');
                }
            });
        });
    });
});
