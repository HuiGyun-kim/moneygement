document.addEventListener('DOMContentLoaded', function () {
    const date = new Date();
    let currYear = date.getFullYear();
    let currMonth = date.getMonth();

    // 월 이름 배열
    const months = [
        '1월', '2월', '3월', '4월', '5월', '6월',
        '7월', '8월', '9월', '10월', '11월', '12월'
    ];

    // 현재 날짜를 화면에 표시하는 함수
    const currentDate = document.querySelector('.current-date');
    currentDate.innerHTML = `${currYear}년 ${months[currMonth]}`;

    /*이전 달, 다음 달 이동*/

// 이전 달로 이동하는 버튼 클릭 이벤트 처리
    document.querySelector('.prev').addEventListener('click', function () {
        currMonth--;
        if (currMonth < 0) {
            currMonth = 11;
            currYear--;
        }
        currentDate.innerHTML = `${currYear}년 ${months[currMonth]}`;
        renderCalendar();
    });

    // 다음 달로 이동하는 버튼 클릭 이벤트 처리
    document.querySelector('.next').addEventListener('click', function () {
        currMonth++;
        if (currMonth > 11) {
            currMonth = 0;
            currYear++;
        }
        currentDate.innerHTML = `${currYear}년 ${months[currMonth]}`;
        renderCalendar();
    });

    // 캘린더를 렌더링하는 함수
    const renderCalendar = () => {
        // 현재 월의 첫 날의 요일을 구하기
        const firstDayOfWeek = new Date(currYear, currMonth, 1).getDay(); // 첫째주에서 빈 칸 처리하는 부분을 수정
        // 마지막 날짜 구하기
        const lastDateOfMonth = new Date(currYear, currMonth + 1, 0).getDate();

        // 일자를 표시할 요소를 선택
        const daysTag = document.querySelector('.days');
        let liTag = '';

        // 현재 월의 첫 날의 요일을 포함하여 그 전 요일까지의 빈 칸 처리
        for (let i = 0; i < firstDayOfWeek; i++) {
            liTag += '<li class="inactive"></li>';
        }

        // 1부터 마지막 날짜까지 반복하면서 일자를 추가
        for (let i = 1; i <= lastDateOfMonth; i++) {
            liTag += `<li class="day-${i}">${i}</li>`;
        }

        // 일자를 표시할 요소에 일자를 추가
        daysTag.innerHTML = liTag;
    };


// 캘린더 렌더링 함수를 호출
    renderCalendar();

    /* 출석체크 클릭 */
    // 출석체크 버튼 클릭 이벤트 처리
    document.getElementById('attendanceButton').addEventListener('click', function () {
        // 출석체크 이벤트 처리 로직 작성
        alert('출석체크가 완료되었습니다!');

        // 출석체크 횟수 증가
        attendanceCount++;

        // 출석체크 횟수가 25일 이상인 경우 리워드 제공
        if (attendanceCount >= 25){
            provideReward();
        }
    });

    //리워드 확인 함수
    // 리워드 확인 함수
    function checkRewardEligibility() {
        // AJAX를 사용하여 백엔드로 리워드 확인 요청 보내기
        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/attendance/reward/eligible');
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onload = function () {
            if (xhr.status === 200) {
                // 리워드 지급 가능한 경우 메시지 출력
                alert('축하합니다! 리워드를 받을 수 있습니다.');
            } else {
                // 리워드 지급 불가능한 경우 메시지 출력
                alert('리워드 지급 대상이 아닙니다.');
            }
        };
        xhr.send();
    }

});

/* AJAX 통신 */
document.addEventListener('DOMContentLoaded', function () {
    // 출석체크 버튼 클릭 이벤트 처리
    document.getElementById('attendanceButton').addEventListener('click', async function () {
        try {
            const response = await fetch('/attendance/check', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ date: new Date().toISOString().split('T')[0] })
            });

            if (response.ok) {
                // 통신 성공 시 출석체크 완료 메시지 표시
                alert('출석체크가 완료되었습니다!');
                //화면 업데이트 로직 추가
                updateAttendanceUI();

                // 출석체크 횟수 및 리워드 제공 여부 확인
                checkRewardEligibility();
            } else {
                // 통신 실패 시 오류 메시지 표시
                alert('출석체크에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('출석체크에 실패했습니다.');
        }
    });

    async function checkRewardEligibility() {
        try {
            const userId = document.getElementById('userId').getAttribute('data-user-id');
            const response = await fetch('/attendance/reward/eligible', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userId: userId })
            });

            if (response.ok) {
                const data = await response.json();
                if (data.isEligible) {
                    provideReward();
                }
            } else {
                console.log('리워드 지급 가능 여부 확인 실패');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    }

    //화면 업데이트 함수
    function updateAttendanceUI(){

    }

    //리워드 제공 함수
    function provideReward(){

    }
});



document.addEventListener('DOMContentLoaded', function() {
    // 출석체크 버튼을 클릭할 때마다 실행되는 함수
    document.getElementById('attendanceButton').addEventListener('click', function() {
        // 오늘 날짜의 요소에 'active' 클래스를 추가하여 색을 변경
        const today = new Date();
        const todayDate = today.getDate();
        const daysTag = document.querySelector('.days');
        const todayElement = daysTag.querySelector(`li.day-${todayDate}`);
        todayElement.classList.add('active');

        // 출석체크 완료가 된 날짜의 요소에 'completed' 클래스를 추가하여 색을 변경
        const completedDates = []; // 출석체크 완료된 날짜로 예시
        completedDates.forEach(date => {
            const completedElement = daysTag.querySelector(`li:nth-child(${date})`);
            completedElement.classList.add('completed');
        });
    });
});


/*출석체크 리워드*/

// //출석체크 횟숫 저장 변수
 let attendanceCount = 0;


// 리워드 제공 함수
function provideReward() {
    // 리워드 지급 로직 작성
    alert('리워드 50p를 제공합니다!');
}


