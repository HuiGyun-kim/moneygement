document.addEventListener('DOMContentLoaded', function () {
    var faqImage = document.getElementById('faqImage');
    var faqModal = document.getElementById('faqModal');
    var faqModalClose = document.getElementById('faqModalClose');

    faqImage.addEventListener('click', function () {
        faqModal.style.display = 'block';
        fetchFrequentlyAskedQuestions();
    });

    faqModalClose.addEventListener('click', function () {
        faqModal.style.display = 'none';
    });

    // 모달 창 밖을 클릭하면 모달을 닫는 이벤트 리스너
    window.addEventListener('click', function (event) {
        if (!faqModal.contains(event.target) && event.target !== faqImage) {
            faqModal.style.display = 'none';
        }
    });
});

function fetchFrequentlyAskedQuestions() {
    fetch('/qna/faq')
        .then(response => response.json())
        .then(data => {
            displayQuestions(data);
        })
        .catch(error => console.error('Error:', error));
}

function displayQuestions(questions) {
    var questionList = document.getElementById('questionList');
    questionList.innerHTML = '';

    questions.forEach(question => {
        var listItem = document.createElement('li');
        listItem.textContent = question;
        listItem.addEventListener('click', function () {
            fetchAnswer(question);
        });
        questionList.appendChild(listItem);
    });
}

function fetchAnswer(question) {
    fetch('/qna/ask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            question: question,
        }),
    })
        .then(response => response.json())
        .then(data => {
            displayAnswer(question, data.answer);
        })
        .catch(error => console.error('Error:', error));
}

function displayAnswer(question, answer) {
    var answerBox = document.getElementById('answerBox');
    answerBox.innerHTML = '';

    var questionElement = document.createElement('div');
    questionElement.innerHTML = `<strong>Q:</strong> ${question}`;
    answerBox.appendChild(questionElement);

    var answerElement = document.createElement('div');
    answerElement.innerHTML = `<strong>A:</strong> ${answer}`;
    answerBox.appendChild(answerElement);
}