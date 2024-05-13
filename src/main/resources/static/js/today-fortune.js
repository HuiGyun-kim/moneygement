function limitInputLength(input) {
    if (input.value.length > 4) {
        input.value = input.value.slice(0, 4);  // 입력값을 4자리로 제한
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('birthYearInput');
    const fortuneButton = document.getElementById('fortuneButton');

    input.addEventListener('input', () => {
        limitInputLength(input);
        // 사용자가 입력을 시작하면 결과를 초기화하고, 버튼 활성화 상태를 제어
        fortuneButton.disabled = false;
    });

    fortuneButton.addEventListener('click', function () {
        if (input.value.length !== 4) {
            alert("4자리 년도를 입력해주세요.");
            return;
        }

        input.disabled = true;
        fortuneButton.disabled = true;

        // 결과 표시 영역을 초기화
        const fortuneResult = document.getElementById('fortuneResult');
        fortuneResult.style.display = 'none';
        fortuneResult.textContent = '';


        const birthYear = input.value;
        const currentDate = new Date();
        const formattedDate = currentDate.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
        console.log(formattedDate)
        const lastTwoDigits = birthYear.slice(-2);
        const fortuneQuery = lastTwoDigits + `년생에 ${formattedDate} 의 금전운을 검색해서 알려주되, 텍스트는 150자 이내로 해서 답변을 줘. 만약 150자가 넘어가면 요약해서 답해줘.`;

        const urlData = {
            detail: [{
                msg: fortuneQuery,
                type: "string"
            }]
        };
        console.log(urlData);

        const urlContent = new URLSearchParams({
            content: JSON.stringify(urlData),
            client_id: "f6c2c7c5-d7c8-49c7-bf30-a2a6bdc28548"
        }).toString();

        console.log(urlContent);

        const loadingSpinner = document.getElementById('loadingSpinner');
        loadingSpinner.style.display = 'block';

        fetch(`/ledgerEntry/fortuneRequest?${urlContent}`, {
            method: 'GET',
        })
            .then(response => response.json())
            .then(data => {
                console.log("데이터가 성공적으로 전송되었습니다.");
                fortuneResult.textContent = data.content.replace(/\[\(출처\d+\)\]\(https?:\/\/[^\s]+\)/g, "").replace(/undefined/g, "").replace(/\s*:.*$/, "");
                loadingSpinner.style.display = 'none';
                fortuneResult.style.display = 'block';
                input.disabled = false;
                fortuneButton.disabled = false;
            })
            .catch(error => {
                console.error('에러:', error);
                loadingSpinner.style.display = 'none';
                input.disabled = false;
                fortuneButton.disabled = false;
            });
    });
});
