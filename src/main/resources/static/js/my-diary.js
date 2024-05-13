document.addEventListener('DOMContentLoaded', function () {
    const dateButton = document.getElementById('date');
    const expendsList = document.getElementById('expends');
    let current = new Date();
    let today = new Date();
    let select = null;
    const userId = document.getElementById('userId').getAttribute('data-user-id');
    console.log(userId);

    document.getElementById('before').addEventListener('click', function () {
        current.setDate(current.getDate() - 7);
        generateDateButtons();
        showExpend(current, userId);
        checkDiary(current, userId);
    });

    document.getElementById('after').addEventListener('click', function () {
        current.setDate(current.getDate() + 7);
        generateDateButtons();
        showExpend(current, userId);
        checkDiary(current, userId);
    });

    document.getElementById('today').addEventListener('click', function () {
        current = new Date(today);
        generateDateButtons();
        showExpend(current, userId);
        checkDiary(current, userId);
    });

    document.querySelectorAll('.circleBox').forEach(circle => {
        circle.addEventListener('click', function () {
            if (select === this) {
                this.classList.remove('selected');
                select = null;
            } else {
                if (select) {
                    select.classList.remove('selected')
                }
                select = this;
                this.classList.add('selected');
            }
        });
    });

    document.querySelector('.write').addEventListener('click', function () {
        if (select) {
            console.log(select.querySelector('.circleText').textContent);
            const theme = select.querySelector('.circleText').textContent;
            const expenseList = [];

            document.querySelectorAll('.expendBox').forEach(box => {
                const amount = box.querySelector('p').textContent.split('원')[0];
                const description = box.querySelector('p').textContent.trim().split(' ').slice(1).join(' ');
                expenseList.push({amount, description});
            })

            const combined = expenseList.map(item => `${item.amount}원을 ${item.description}에다가 사용`).join(', ');

            console.log(combined);
            const urlData = {
                detail: [{
                    // loc: [theme, 0],
                    msg: `${theme}인 버전으로 돈을 ${combined}했는데, 이것에 대한 소비일기를 써주세요.`,
                    type: "string"
                }]
            };
            console.log("urlData : ")
            console.log(urlData);

            const urlContent = new URLSearchParams({
                content: JSON.stringify(urlData),
                client_id: "65cef0e5-ce7a-4655-a5a8-5f6414f55d03"
            }).toString();

            console.log("urlContent : ")
            console.log(urlContent);

            fetch(`/diary/diaryRequest?${urlContent}`, {
                method: 'GET',
            })
                .then(response => response.json())
                .then(data => {
                    console.log("데이터가 성공적으로 전송되었습니다.");
                    const diaryBox = document.getElementById('diaryBox');
                    let textContent = data.content.replace(/<[^>]*>?/gm, '');
                    textContent = textContent.replace(/https?:\/\/[a-zA-Z0-9./?=_-]*/gm, '');
                    textContent = textContent.replace(/\[\(출처\d+\)\]/gm, '');
                    textContent = textContent.replace(/\([^\)]*\)/g, '');
                    diaryBox.textContent = textContent;
                })
                .catch(error => console.error('에러:', error));
        } else {
            alert('테마를 선택해주세요!')
        }
    });

    document.getElementById('saveDiary').addEventListener('click', function () {
        var diaryContent = document.getElementById('diaryBox').innerText.trim();

        if (!diaryContent) {
            alert('일기를 작성해주세요!');
            return;
        }

        fetch('/diary/saveDiary', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: userId,
                content: diaryContent,
                expenseAt: current.toISOString(),
            })
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                alert('저장되었습니다.');
            })
            .catch(error => console.error('에러:', error));
    });

    function generateDateButtons() {
        dateButton.innerHTML = '';
        const day = current.getDate();
        const weeks = ['일', '월', '화', '수', '목', '금', '토'];

        for (let i = -3; i <= 3; i++) {
            const date = new Date(current);
            date.setDate(day + i);
            const week = date.getDay()
            const button = document.createElement('button');
            button.innerHTML = `${weeks[week]}<br>${date.getDate()}`;

            if (date.getDay() === 6) { //토요일
                button.style.color = 'blue';
            } else if (date.getDay() === 0) { //일요일
                button.style.color = 'red';
            }

            if (date.getFullYear() === today.getFullYear() &&
                date.getMonth() === today.getMonth() &&
                date.getDate() === today.getDate()) {
                button.style.borderColor = '#1A4F32';
                button.style.borderWidth = '2px';
                button.style.borderStyle = 'dashed';
                button.style.borderRadius = '10px';
            }

            button.onclick = function () {
                current = new Date(date);
                showExpend(current, userId);
                checkDiary(current, userId);
            };
            dateButton.appendChild(button);
        }
    }

    function showExpend(date, userId) {
        const dateString = date.toISOString().split('T')[0];

        fetch(`/ledgerEntry/expenses?date=${dateString}&userId=${userId}`)
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data)) {
                    const filteredData = data.filter(entry => entry.ledgerType === true);
                    displayExpenses(filteredData, date);
                }
                else {
                    console.error('Expected an array but got:', data);
                    expendsList.innerHTML = '올바른 데이터 형식이 아닙니다.';
                }
            })
            .catch(error => {
                console.error('에러:', error);
                expendsList.innerHTML = '데이터를 불러오는 중 에러가 발생했습니다.';
            });
    }

    function displayExpenses(expensesData, date) {
        expendsList.innerHTML = '';

        const dateInfo = document.createElement('h4');
        dateInfo.textContent = `${date.getMonth() + 1}월 ${date.getDate()}일의 지출 내역`;
        expendsList.appendChild(dateInfo);

        if (expensesData.length === 0) {
            expendsList.innerHTML += '해당 날짜에 대한 지출 내역이 없습니다.';
            return;
        }


        expensesData.forEach(item => {
            const time = document.createElement('div');
            time.className = 'time';
            time.textContent = item.time;
            expendsList.appendChild(time);

            const expendBox = document.createElement('div');
            expendBox.className = 'expendBox';
            expendBox.innerHTML = `
            <p>${item.amount}원 ${item.description}</p>
            `;

            expendsList.appendChild(expendBox);
        });

        adjustHeight();
    }

    function adjustHeight() {
        const timelineBox = document.querySelector('.timelineBox');
        const lastExpendBox = document.querySelector('.expendBox:last-child');

        if (lastExpendBox) {
            const dividerHeight = lastExpendBox.offsetTop + lastExpendBox.offsetHeight - timelineBox.offsetTop;
            // 마지막 비용의 상단과 높이를 더해서 부모요소부터 박스까지의 상단 간격을 빼서 마지막 비용 항목의 아랫쪽 위치를 구할 수 있음.

            const divider = document.querySelector('.deco-flex .divider');
            divider.style.height = `${dividerHeight}px`;
        }
    }

    function checkDiary(date, userId) {
        const dateString = date.toISOString().split('T')[0];
        fetch(`/diary/checkDiary?date=${dateString}&userId=${userId}`)
            .then(response => response.json())
            .then(diary => {
                document.getElementById('diaryBox').textContent = diary[0].content;
                document.getElementById('saveDiary').disabled = true;
                document.getElementById('saveDiary').style.display = 'none';
                document.querySelector('.write').style.display = 'none';
            })
            .catch(error => {
                document.getElementById('diaryBox').textContent = '저장된 일기가 없습니다.';
                document.getElementById('saveDiary').disabled = false;
                document.getElementById('saveDiary').style.display = '';
                document.querySelector('.write').style.display = '';
            });
    }

    checkDiary(new Date(), userId);
    generateDateButtons();
    showExpend(new Date(), userId);
});

//     function showExpend(date) {
//         const expendData = [
//             {date: new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1), time:'12:00', category: '식사', amount: '12000원', description: '점심 식사'},
//             {date: new Date(today.getFullYear(), today.getMonth(), today.getDate()), time:'13:00', category: '교통', amount: '3500원', description: '지하철 카드 충전'},
//             {date: new Date(today.getFullYear(), today.getMonth(), today.getDate()), time:'18:30', category: '쇼핑', amount: '59000원', description: '옷 구매'},
//             {date: new Date(2024, 4, 4), time:'13:00', category: '교통', amount: '3500원', description: '점심먹기'},
//             // monthidx는 0부터 시작해서 4.4해야 5월4일임
//             {date: new Date(2024, 4, 4), time:'18:30', category: '쇼핑', amount: '59000원', description: '노래방가기'},
//             {date: new Date(2024, 4, 7), time:'18:30', category: '쇼핑', amount: '59000원', description: '노래방가기'}
//         ];
//
//         const filterData = expendData.filter(item =>
//             item.date.getFullYear() === date.getFullYear() &&
//             item.date.getMonth() === date.getMonth() &&
//             item.date.getDate() === date.getDate()
//         );
//
//         expendsList.innerHTML = '';
//
//         const dateInfo = document.createElement('h4');
//         dateInfo.textContent = `${date.getMonth() + 1}월 ${date.getDate()}일의 지출 내역`;
//         expendsList.appendChild(dateInfo);
//
//
//         if (filterData.length === 0) {
//             expendsList.innerHTML += '해당 날짜에 대한 지출 내역이 없습니다.';
//             return;
//         }
//
//
//         filterData.forEach(item => {
//             const time = document.createElement('div');
//             time.className = 'time';
//             time.textContent = item.time;
//             expendsList.appendChild(time);
//
//             const expendBox = document.createElement('div');
//             expendBox.className = 'expendBox';
//             expendBox.innerHTML = `
//             <p>${item.amount} ${item.description}</p>
//             `;
//
//             expendsList.appendChild(expendBox);
//
//         });
//
//         adjustHeight();
//     }
//
//     function adjustHeight() {
//         const timelineBox = document.querySelector('.timelineBox');
//         const lastExpendBox = document.querySelector('.expendBox:last-child');
//
//         if (lastExpendBox) {
//             const dividerHeight = lastExpendBox.offsetTop + lastExpendBox.offsetHeight - timelineBox.offsetTop;
//             // 마지막 비용의 상단과 높이를 더해서 부모요소부터 박스까지의 상단 간격을 빼서 마지막 비용 항목의 아랫쪽 위치를 구할 수 있음.
//
//             const divider = document.querySelector('.deco-flex .divider');
//             divider.style.height = `${dividerHeight}px`;
//         }
//     }
//     generateDateButtons();
//     showExpend(new Date(today.getFullYear(), today.getMonth(), today.getDate()));
// });