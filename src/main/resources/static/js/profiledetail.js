$(document).ready(function() {
    // 수정 버튼 클릭 시 파일 선택 창 열기
    $('.edit-button').click(function() {
        $('#imageInput').click();
    });

    // 프로필 이미지 미리보기
    $('input[name="profileImg"]').on('change', function() {
        var file = this.files[0];
        var reader = new FileReader();
        reader.onload = function(e) {
            $('.profile-image').attr('src', e.target.result);
        }
        reader.readAsDataURL(file);
    });

    // 프로필 이미지 업로드
    $('.submit-button').click(function() {
        var formData = new FormData();
        formData.append('profileImg', $('input[name="profileImg"]')[0].files[0]);
        formData.append('userId', $('#userId').val());

        $.ajax({
            type: 'POST',
            url: '/profile/upload',
            data: formData,
            processData: false,
            contentType: false,
            success: function(data) {
                alert(data);
                window.location.href ='/profile/${userId}';
            },
            error: function(xhr, status, error) {
                alert('프로필 사진 업로드에 실패했습니다.');
            }
        });
    });

    // 기본 이미지로 설정
    $('.basic-image').click(function() {
        $.ajax({
            type: 'POST',
            url: '/profile/delete',
            success: function(data) {
                alert(data);
                $('.profile-image').attr('src', '/img/main/img_2.png');
            },
            error: function(xhr, status, error) {
                alert('기본 이미지 설정에 실패했습니다.');
            }
        });
    });

    // 취소 버튼 클릭 시 메인 프로필 페이지로 이동
    $('.cancel-button').click(function() {
        window.location.href = '/profile/${userId}'
    });
});

// profile.html
$('#myDropdown a').click(function() {
    window.location.href = '/profile/detail';
});