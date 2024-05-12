document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('birthYearInput');

    input.addEventListener('input', () => {
        if (input.value.length > 4) {
            input.value = input.value.slice(0, 4);
        }
    });
});

document.getElementById('fortuneButton').addEventListener('click', function () {
        const birthYear = document.getElementById('birthYearInput').value;
        const lastTwoDigits = birthYear.slice(-2);
        const fortuneQuery = lastTwoDigits + '년생에 오늘의 금전운을 검색해서 알려주되, 텍스트는 150자 이내로 해서 답변을 줘. 만약 150자가 넘어가면 요약해서 답해줘.';

        const urlData = {
            detail: [{
                msg: fortuneQuery,
                type: "string"
            }]
        };
        console.log(urlData)

        const urlContent = new URLSearchParams({
            content: JSON.stringify(urlData),
            client_id: "f6c2c7c5-d7c8-49c7-bf30-a2a6bdc28548"
        }).toString();

        console.log(urlContent)

        fetch(`/ledgerEntry/fortuneRequest?${urlContent}`, {
            method: 'GET',
        })
            .then(response => response.json())
            .then(data => {
                console.log("데이터가 성공적으로 전송되었습니다.");
                const resultBox = document.getElementById('fortuneResult');
                resultBox.textContent = data.content.replace(/\[\(출처\d+\)\]\(https?:\/\/[^\s]+\)/g, "");
            })
            .catch(error => console.error('에러:', error));
    }
);