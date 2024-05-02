document.addEventListener('DOMContentLoaded', () => {
    const followersButton = document.querySelector('.follow-button');
    const followingButton = document.querySelector('.following-button');
    const modal = document.getElementById('followersModal'); // 모달 요소 ID

    // 팔로워 버튼 클릭 시 이벤트 처리
    followersButton.addEventListener('click', async () => {
        // 서버로부터 팔로워 데이터 가져오기 (예를 들어, API 요청)
        const followersData = await fetch('/api/followers'); // 서버에서 팔로워 데이터 가져오는 API 엔드포인트

        // 가져온 데이터를 HTML 형식으로 변환하여 모달에 삽입
        const followersList = await followersData.json(); // JSON 형식으로 변환
        const followersContainer = document.getElementById('followersContainer'); // 팔로워 리스트를 표시할 컨테이너 ID
        followersContainer.innerHTML = ''; // 리스트 초기화

        followersList.forEach(follower => {
            const followerElement = document.createElement('div');
            followerElement.textContent = follower.username; // 팔로워 정보를 여기에 삽입 (예: username)
            followersContainer.appendChild(followerElement);
        });

        // 모달 열기
        modal.style.display = 'block';
    });

    // 팔로잉 버튼 클릭 시 이벤트 처리
    followingButton.addEventListener('click', async () => {
        // 서버로부터 팔로잉 데이터 가져오기 (예를 들어, API 요청)
        const followingData = await fetch('/api/following'); // 서버에서 팔로잉 데이터 가져오는 API 엔드포인트

        // 가져온 데이터를 HTML 형식으로 변환하여 모달에 삽입
        const followingList = await followingData.json(); // JSON 형식으로 변환
        const followingContainer = document.getElementById('followingContainer'); // 팔로잉 리스트를 표시할 컨테이너 ID
        followingContainer.innerHTML = ''; // 리스트 초기화

        followingList.forEach(following => {
            const followingElement = document.createElement('div');
            followingElement.textContent = following.username; // 팔로잉 정보를 여기에 삽입 (예: username)
            followingContainer.appendChild(followingElement);
        });

        // 모달 열기
        modal.style.display = 'block';
    });

    // 모달 닫기 버튼 클릭 시 이벤트 처리
    const closeModalButton = document.querySelector('.close-modal-button');
    closeModalButton.addEventListener('click', () => {
        modal.style.display = 'none';
    });
});
