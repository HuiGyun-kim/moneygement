$(document).ready(function() {
    // 현재 사용자 정보 가져오기
    const currentUserId = $('#userId').data('userId').toString();
    const profileId = window.location.pathname.split('/').pop();

    console.log(currentUserId)
    console.log(profileId)

    // 댓글 목록 순회
    $('.comment-item').each(function() {

        // // 작성자만 수정 링크 보이게 하기
        // if (currentUserId === commentUserId) {
        //     $(this).find('.edit-button').show();
        // } else {
        //     $(this).find('.edit-button').hide();
        // }


        $(this).find('.edit-button').hide();


        // 작성자와 페이지 주인만 삭제 버튼 보이게 하기
        if (currentUserId === profileId) {
            $(this).find('.delete-button').show();
        }
        else {
            $(this).find('.delete-button').hide();
        }
    });

    // 새 댓글 작성 폼 제출
    $('#commentForm').submit(function(event) {
        event.preventDefault();
        const formData = {
            content: $('#content').val()
        };
        $.ajax({
            type: 'POST',
            url: '/comments',
            data: JSON.stringify(formData),
            contentType: 'application/json',
            dataType: 'json',
            success: function(data) {
                // 새로운 댓글 추가
                const newComment = $('<div class="col-md-4 mb-4">' +
                    '<div class="card">' +
                    '<div class="card-body">' +
                    '<h5 class="card-title">' + '[[${session.user.nickname}]]' + '</h5>' +
                    '<p class="card-text">' + data.content + '</p>' +
                    '<p class="card-text"><small class="text-muted">' + data.createdAt + '</small></p>' +
                    '</div>' +
                    '</div>' +
                    '</div>');
                $('.row').prepend(newComment);
                $('#content').val('');
            },
            error: function(xhr, status, error) {
                console.error('Error creating comment:', error);
            }
        });
    });
});