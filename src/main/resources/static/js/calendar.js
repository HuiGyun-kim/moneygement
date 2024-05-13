const prevMonthButton = document.getElementById('prevMonth');
const nextMonthButton = document.getElementById('nextMonth');
const calendarDates = document.getElementById('calendarDates');
const currentYear = document.getElementById('currentYear');
const currentMonth = document.getElementById('currentMonth');
let date = new Date();
let selectedLedgerId;
let selectedUserId;

document.addEventListener('DOMContentLoaded', () => {
    selectedUserId = window.currentUserId;
    renderCalendar();
});

// 가계부 선택 드롭다운 변경 시 처리
document.getElementById('ledgerSelect').addEventListener('change', (event) => {
    selectedUserId = event.target.options[event.target.selectedIndex].dataset.userId;
});
// 각 날짜에 대한 수입과 지출 데이터를 서버에서 불러오는 함수
async function fetchDataForDate(userId, year, month) {
    try {
        const response = await fetch(`/ledgerEntry/entriesAll?userId=${userId}&year=${year}&month=${month}`);

        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }
        const data = await response.json();

        // 데이터를 날짜별로 합치기
        const indexedData = {};
        data.forEach(entry => {
            const day = new Date(entry.date).getDate();
            if (!indexedData[day]) {
                indexedData[day] = { income: 0, expense: 0 };
            }
            if (entry.ledgerType) {
                indexedData[day].expense += entry.amount;
            } else {
                indexedData[day].income += entry.amount;
            }
        });

        return indexedData;
    } catch (error) {
        console.error('Data loading error:', error);
        alert('Error loading data: ' + error.message);
        return {};
    }
}

// 달력을 렌더링하는 비동기 함수
async function renderCalendar() {
    calendarDates.innerHTML = '';

    const year = date.getFullYear();
    const month = date.getMonth();
    const userId = selectedUserId; // 로그인한 유저의 ID
    const dataForMonth = await fetchDataForDate(userId, year, month + 1);

    currentYear.textContent = year;
    currentMonth.textContent = month + 1 + '월';

    const firstDayOfMonth = new Date(year, month, 1).getDay();
    const lastDateOfMonth = new Date(year, month + 1, 0).getDate();

    let totalIncome = 0;
    let totalExpense = 0;

    for (let i = 0; i < firstDayOfMonth; i++) {
        calendarDates.appendChild(createEmptyElement());
    }

    for (let day = 1; day <= lastDateOfMonth; day++) {
        const dateElement = document.createElement('div');
        dateElement.classList.add('calendar-date');

        // 데이터 로딩
        const income = dataForMonth[day]?.income || 0; // 해당 날짜의 수입
        const expense = dataForMonth[day]?.expense || 0; // 해당 날짜의 지출

        totalIncome += income;
        totalExpense += expense;

        const formattedIncome = income.toLocaleString();
        const formattedExpense = expense.toLocaleString();

        const tooltip = document.createElement('div');
        tooltip.classList.add('tooltip');
        tooltip.innerHTML = `수입: <span class="income">+${formattedIncome}</span>, 지출: <span class="expense">-${formattedExpense}</span>`;

        const dayElement = document.createElement('div');
        dayElement.textContent = day;
        dateElement.appendChild(dayElement);
        dateElement.appendChild(tooltip);

        const dayOfWeek = (firstDayOfMonth + day - 1) % 7;
        if (dayOfWeek === 0 || dayOfWeek === 6) {
            dateElement.classList.add('no-background');
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