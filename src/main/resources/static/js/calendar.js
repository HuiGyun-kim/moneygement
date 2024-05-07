const prevMonthButton = document.getElementById('prevMonth');
const nextMonthButton = document.getElementById('nextMonth');
const calendarDates = document.getElementById('calendarDates');
const currentYear = document.getElementById('currentYear');
const currentMonth = document.getElementById('currentMonth');

let date = new Date();

function renderCalendar() {
    calendarDates.innerHTML = '';

    const year = date.getFullYear();
    const month = date.getMonth();

    currentYear.textContent = year;
    currentMonth.textContent = month + 1 + '월';

    const firstDayOfMonth = new Date(year, month, 1).getDay();
    const lastDateOfMonth = new Date(year, month + 1, 0).getDate();

    let totalIncome = 0; // 전체 수입을 저장할 변수
    let totalExpense = 0; // 전체 지출을 저장할 변수

    for (let i = 0; i < firstDayOfMonth; i++) {
        calendarDates.appendChild(createEmptyElement());
    }

    for (let day = 1; day <= lastDateOfMonth; day++) {
        const dateElement = document.createElement('div');
        dateElement.classList.add('calendar-date');

        // 수입 및 지출 데이터를 여기서 불러옵니다. 예시 데이터로 대체합니다.
        const income = Math.floor(Math.random() * 10000000); // 랜덤한 수입 금액
        const expense = Math.floor(Math.random() * 10000000); // 랜덤한 지출 금액

        // 수입과 지출의 합을 계산
        totalIncome += income;
        totalExpense += expense;

        // 천 단위 콤마를 붙이기 위해 toLocaleString 사용
        const formattedIncome = income.toLocaleString();
        const formattedExpense = expense.toLocaleString();

        const tooltip = document.createElement('div');
        tooltip.classList.add('tooltip');
        tooltip.innerHTML = `수입: <span class="income">+${formattedIncome}</span>, 지출: <span class="expense">-${formattedExpense}</span>`;

        // 날짜와 툴팁을 함께 보여주기 위해
        const dayElement = document.createElement('div');
        dayElement.textContent = day;
        dateElement.appendChild(dayElement);

        dateElement.appendChild(tooltip);

        const dayOfWeek = (firstDayOfMonth + day - 1) % 7;
        if (dayOfWeek === 0 || dayOfWeek === 6) {
            dateElement.classList.add('no-background'); // 토요일과 일요일에 대한 스타일 변경
        }
        calendarDates.appendChild(dateElement);
    }
    // 총 수입과 지출을 HTML에 표시
    document.querySelector('.month-income').textContent = `이번 달 수입 +${totalIncome.toLocaleString()}`;
    document.querySelector('.month-expense').textContent = `이번 달 지출 -${totalExpense.toLocaleString()}`;
}

function createEmptyElement() {
    const emptyElement = document.createElement('div');
    emptyElement.classList.add('calendar-date', 'empty');
    return emptyElement;
}

prevMonthButton.addEventListener('click', () => {
    date.setMonth(date.getMonth() - 1);
    renderCalendar();
});

nextMonthButton.addEventListener('click', () => {
    date.setMonth(date.getMonth() + 1);
    renderCalendar();
});

renderCalendar();
