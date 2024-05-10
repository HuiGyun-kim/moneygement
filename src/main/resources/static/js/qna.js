// JavaScript 코드를 작성합니다.
document.getElementById('messageForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 동작을 중지합니다.
    var userInput = document.getElementById('userInput').value;

    // 사용자가 입력한 메시지를 채팅 창에 표시합니다.
    displayMessage('User', userInput);

    // 앨런 AI와 대화하는 함수를 호출합니다.
    communicateWithAlan(userInput);

    fetch('/qna/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            question: userInput
        })
    })
        .then(response => response.json())
        .then(data => {
            // 받은 답변을 채팅 창에 표시합니다.
            displayMessage('Alan AI', data.answer);
        })
        .catch(error => console.error('Error:', error));

    // 입력 폼을 비웁니다.
    document.getElementById('userInput').value = '';
});

function displayMessage(sender, message) {
    var chatBox = document.getElementById('chatBox');
    var newMessage = document.createElement('div');
    newMessage.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatBox.appendChild(newMessage);
    chatBox.scrollTop = chatBox.scrollHeight; // 자동으로 스크롤을 아래로 이동합니다.
}

function communicateWithAlan(userInput) {

}
