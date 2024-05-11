// 사용자가 입력한 질문을 서버에 전송하고 답변을 받아오는 함수
function communicateWithAlan(userInput) {
    // 질문을 서버에 전송하고 답변을 받아옵니다.
    fetch(`/qna/ask?question=${userInput}`)
        .then(response => response.json())
        .then(data => {
            // 받은 답변을 채팅 창에 표시합니다.
            displayMessage('Alan AI', data.answer);
        })
        .catch(error => console.error('Error:', error));
}

// 폼 제출 이벤트를 처리하는 함수
document.getElementById('messageForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 동작을 중지합니다.
    var userInput = document.getElementById('userInput').value;
    // 사용자가 입력한 메시지를 채팅 창에 표시합니다.
    displayMessage('User', userInput);
    // 앨런 AI와 대화하는 함수를 호출합니다.
    communicateWithAlan(userInput);
    // 입력 폼을 비웁니다.
    document.getElementById('userInput').value = '';
});

// 메시지를 채팅 창에 표시하는 함수
function displayMessage(sender, message) {
    var chatBox = document.getElementById('chatBox');
    var newMessage = document.createElement('div');
    newMessage.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatBox.appendChild(newMessage);
    // 자동으로 스크롤을 아래로 이동합니다.
    chatBox.scrollTop = chatBox.scrollHeight;
}
