document.addEventListener('DOMContentLoaded', function() {
    const unfollowButtons = document.querySelectorAll('.unfollow-button');

    unfollowButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const followerId = button.getAttribute('data-follower-id');

            fetch(`/follow/unfollow-me/${followerId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        const followerElement = button.closest('li');
                        followerElement.remove();
                    } else {
                        console.error('Unfollow failed');
                    }
                })
                .catch(error => {
                    console.error('Network error:', error);
                });
        });
    });

    const followingUnfollowButtons = document.querySelectorAll('.following-unfollow-button');

    followingUnfollowButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const followingId = button.getAttribute('data-following-id');

            fetch(`/follow/unfollow/${followingId}`, {
                method: 'DELETE'
            })
                .then(response => {
                    if (response.ok) {
                        const followingElement = button.closest('li');
                        followingElement.remove();
                    } else {
                        console.error('Unfollow failed');
                    }
                })
                .catch(error => {
                    console.error('Network error:', error);
                });
        });
    });
});

function goBack() {
    window.history.back();
}