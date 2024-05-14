const editButton = document.querySelector('.edit-button');
const imageInput = document.getElementById('imageInput');
const submitButton = document.querySelector('.submit-button');

editButton.addEventListener('click', () => {
    imageInput.click();
});

imageInput.addEventListener('change', (event) => {
    const file = event.target.files[0];
    if (file) {
        previewImage(file);
    }
});

function previewImage(file) {
    const profileImage = document.getElementById('profileImage');

    const reader = new FileReader();
    reader.onload = function (event) {
        profileImage.src = event.target.result;
    };

    reader.readAsDataURL(file);
}

//------------------------------등록 버튼 클릭 이벤트 핸들러-------------------------------------------

submitButton.addEventListener('click', (event) => {
    event.preventDefault();

    const profileImage = document.getElementById('profileImage');
    const imageDataUrl = profileImage.src;

    const file = dataURLtoFile(imageDataUrl, 'profileImage.jpg');

    const formData = new FormData();
    formData.append('profileImg', file);

    const userId = document.getElementById('userId').value;

    fetch(`/profileDetail/upload`, {
        method: 'POST',
        body: formData,
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then((data) => {
            if (data && data.imageUrl) {
                profileImage.src = data.imageUrl;
                updateProfileImageUrl(data.imageUrl);
            } else {
                throw new Error('Invalid response from server');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('프로필 이미지 업로드 중 오류가 발생했습니다.');
        });
});

//---------------------프로필 이미지 URL을 서버로 전송하여 데이터베이스 업데이트--------------------------------------------

function updateProfileImageUrl(imageUrl) {
    const userId = document.getElementById('userId').value;

    fetch(`/updateProfileImage`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ imageUrl }),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            console.log('Profile image updated successfully');
            window.location.href = `/profile/${userId}`;
        })
        .catch((error) => {
            console.error('Error updating profile image:', error);
            alert('프로필 이미지 업데이트 중 오류가 발생했습니다.');
        });
}

//---------------------데이터 URL을 파일 객체로 변환하는 함수---------------------------------------------
function dataURLtoFile(dataUrl, filename) {
    const arr = dataUrl.split(',');
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);

    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, { type: mime });
}

//----------------------취소 버튼 클릭 이벤트 핸들러--------------------------------------------------

document.querySelector('.cancel-button').addEventListener('click', function() {
    history.back();
});


//-------------------------------------------------------------------------------

document.addEventListener('DOMContentLoaded', function () {
    const introductionTextarea = document.getElementById('introductionTextarea');
    const submitButton = document.getElementById('submitButton');

    submitButton.addEventListener('click', () => {
        const introduction = introductionTextarea.value;

        fetch(`${userId}/profile/introduction`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(introduction)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(result => {
                console.log('Introduction updated successfully:', result);
                window.location.href = `/profile/${userId}`;
            })
            .catch(error => {
                console.error('Error updating introduction:', error);
            });
    });
});