document.getElementById('messageForm').addEventListener('submit', function(event) {
    event.preventDefault();
    var userInput = document.getElementById('userInput').value;
    displayMessage('User', userInput);

    // 서버로 질문을 전송하고 답변을 받아옵니다.
    fetch('/qna/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            question: userInput,
        }),
    })
        .then(response => response.json())
        .then(data => {
            displayMessage('Moneygement', data.answer);
        })
        .catch(error => console.error('Error:', error));

    document.getElementById('userInput').value = '';
});

function displayMessage(sender, message) {
    var chatBox = document.getElementById('chatBox');
    var newMessage = document.createElement('div');
    newMessage.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatBox.appendChild(newMessage);
    chatBox.scrollTop = chatBox.scrollHeight;
}