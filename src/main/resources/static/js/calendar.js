const prevMonthButton = document.getElementById('prevMonth');
const nextMonthButton = document.getElementById('nextMonth');
const calendarDates = document.getElementById('calendarDates');
const currentYear = document.getElementById('currentYear');
const currentMonth = document.getElementById('currentMonth');
const ledgerIdInput = document.getElementById('ledgerIdValue');
const ledgerId = ledgerIdInput.value; // ledgerId 값을 가져옵니다.
let date = new Date();

// // 현재 남은 자산, 월 평균 지출, 전 달 대비 지출 계산을 추가
// async function updateFinancialSummary(ledgerId, year, month) {
//     const currentData = await fetchDataForDate(ledgerId, year, month);
//     const lastMonthData = await fetchDataForDate(ledgerId, year, month - 1);
//
//     const totalIncome = Object.values(currentData).reduce((acc, curr) => acc + (curr.income || 0), 0);
//     const totalExpense = Object.values(currentData).reduce((acc, curr) => acc + (curr.expense || 0), 0);
//
//     const netAssets = totalIncome - totalExpense; // 현재 자산
//
//     const lastMonthExpense = Object.values(lastMonthData).reduce((acc, curr) => acc + (curr.expense || 0), 0);
//
//     const monthsActive = month - 1; // 활동 월 수 계산, 실제 사용에서는 데이터가 시작된 시점부터 계산해야 함
//     const averageMonthlyExpense = (totalExpense + lastMonthExpense) / (monthsActive || 1); // 0으로 나누는 것 방지
//
//     const expenseChange = lastMonthExpense ? ((totalExpense - lastMonthExpense) / lastMonthExpense) * 100 : 0; // 전달 대비 변화율
//
//     // HTML 업데이트
//     document.querySelector('.asset-info').innerHTML = `
//         <p>이번 달 수입은 ${totalIncome.toLocaleString()}원 입니다</p>
//         <p>이번 달 지출은 ${totalExpense.toLocaleString()}원 입니다</p>
//         <p>현재 당신의 순 자산은 ${netAssets.toLocaleString()}원 입니다</p>
//         <p>당신의 월 평균 지출은 ${averageMonthlyExpense.toLocaleString()}원 입니다</p>
//         <p>전 달 대비 지출은 ${expenseChange.toFixed(2)}% 입니다</p>
//     `;
// }
//
// // 각 페이지 로드 시 또는 사용자가 달력을 변경할 때 이 함수 호출
// updateFinancialSummary(ledgerId, currentYear, currentMonth);

// 각 날짜에 대한 수입과 지출 데이터를 서버에서 불러오는 함수
async function fetchDataForDate(ledgerId, year, month) {
    try {
        const incomePromise = fetch(`/ledgerEntry/monthly-income-summary?ledgerId=${ledgerId}&year=${year}&month=${month}`);
        const expensePromise = fetch(`/ledgerEntry/monthly-expense-summary?ledgerId=${ledgerId}&year=${year}&month=${month}`);

        const [incomeResponse, expenseResponse] = await Promise.all([incomePromise, expensePromise]);

        if (!incomeResponse.ok || !expenseResponse.ok) {
            throw new Error('Failed to fetch data');
        }
        const incomeData = await incomeResponse.json();
        const expenseData = await expenseResponse.json();

        // 데이터를 날짜별로 합치기
        const indexedData = {};
        incomeData.forEach(entry => {
            const day = new Date(entry.date).getDate();
            indexedData[day] = {...indexedData[day], income: entry.amount};
        });
        expenseData.forEach(entry => {
            const day = new Date(entry.date).getDate();
            indexedData[day] = {...indexedData[day], expense: entry.amount};
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
    const dataForMonth = await fetchDataForDate(ledgerId, year, month + 1); // 데이터 로드
    Object.entries(dataForMonth).forEach(([day, entry]) => { // 수정된 부분
        const income = entry.income || 0; // 해당 날짜의 수입
        const expense = entry.expense || 0; // 해당 날짜의 지출
        // 날짜별로 데이터 인덱싱하는 부분은 필요 없으므로 삭제
    });

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

renderCalendar();
