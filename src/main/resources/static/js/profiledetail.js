const editButton = document.querySelector('.edit-button');
const imageInput = document.getElementById('imageInput');
const submitButton = document.querySelector('.submit-button');

editButton.addEventListener('click', () => {
    imageInput.click();
});

// 파일 선택 이벤트 핸들러
imageInput.addEventListener('change', (event) => {
    const file = event.target.files[0];
    if (file) {
        previewImage(file); // 선택된 파일을 미리보기로 표시
    }
});

// 이미지 미리보기 함수
function previewImage(file) {
    const profileImage = document.getElementById('profileImage'); // 프로필 이미지 요소 선택

    const reader = new FileReader();
    reader.onload = function (event) {
        profileImage.src = event.target.result; // 파일 객체의 데이터를 읽어서 프로필 이미지 업데이트
    };

    reader.readAsDataURL(file); // 파일 객체를 읽어서 이미지 URL로 변환
}


// 등록 버튼 클릭 이벤트 핸들러
submitButton.addEventListener('click', (event) => {
    event.preventDefault();

    const profileImage = document.getElementById('profileImage');
    const imageDataUrl = profileImage.src; // 프로필 이미지의 데이터 URL 가져오기

    const formData = new FormData();
    formData.append('profileImg', dataURLtoFile(imageDataUrl, 'profileImage.jpg')); // 데이터 URL을 파일로 변환하여 FormData에 추가

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
                // 서버 응답에서 imageUrl을 제대로 받았을 경우
                profileImage.src = data.imageUrl;
                updateProfileImageUrl(data.imageUrl);// 프로필 이미지 업데이트
            } else {
                // 서버 응답에서 imageUrl이 없거나 비정상적인 경우
                throw new Error('Invalid response from server');
            }
        })
        .catch((error) => {
            // fetch 요청 중 에러가 발생한 경우
            console.error('Error:', error);
            alert('프로필 이미지 업로드 중 오류가 발생했습니다.');
        });
});

// 데이터 URL을 파일 객체로 변환하는 함수
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

function updateProfileImageUrl(imageUrl) {
}