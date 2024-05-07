document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    // FullCalendar 초기화
    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth' // 월별 캘린더 표시
    });

    calendar.render(); // 캘린더를 화면에 렌더링
});

const date = new Date();
const currYear = date.getFullYear();
const currMonth = date.getMonth();

// 월 이름 배열
const months = [
    '1월', '2월', '3월', '4월', '5월', '6월',
    '7월', '8월', '9월', '10월', '11월', '12월'
];

// 현재 날짜를 화면에 표시하는 함수
const currentDate = document.querySelector('.current-date');
currentDate.innerHTML = `${months[currMonth]} ${currYear}`;

// 캘린더를 렌더링하는 함수
const renderCalendar = () => {
    // 마지막 날짜를 구합니다.
    const lastDateOfMonth = new Date(currYear, currMonth + 1, 0).getDate();

    // 일자를 표시할 요소를 선택합니다.
    const daysTag = document.querySelector('.days');
    let liTag = '';

    // 1부터 마지막 날짜까지 반복하면서 일자를 추가합니다.
    for (let i = 1; i <= lastDateOfMonth; i++) {
        liTag += `<li>${i}</li>`;
    }

    // 일자를 표시할 요소에 일자를 추가합니다.
    daysTag.innerHTML = liTag;
};

// 캘린더 렌더링 함수를 호출합니다.
renderCalendar();


// 출석체크 버튼 클릭 이벤트 처리
document.getElementById('attendanceButton').addEventListener('click', function () {
    // 출석체크 이벤트 처리 로직 작성
    alert('출석체크가 완료되었습니다!');
});


document.addEventListener('DOMContentLoaded', function() {
    // 출석체크 버튼을 클릭할 때마다 실행되는 함수
    document.getElementById('attendanceButton').addEventListener('click', function() {
        // 오늘 날짜의 요소에 'active' 클래스를 추가하여 색을 변경
        const today = new Date();
        const todayDate = today.getDate();
        const daysTag = document.querySelector('.days');
        const todayElement = daysTag.querySelector(`li:nth-child(${todayDate})`);
        todayElement.classList.add('active');

        // 출석체크 완료가 된 날짜의 요소에 'completed' 클래스를 추가하여 색을 변경
        const completedDates = []; // 출석체크 완료된 날짜로 예시
        completedDates.forEach(date => {
            const completedElement = daysTag.querySelector(`li:nth-child(${date})`);
            completedElement.classList.add('completed');
        });
    });
});

