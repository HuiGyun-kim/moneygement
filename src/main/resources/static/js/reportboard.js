document.addEventListener('DOMContentLoaded', function () {
    const spendingCtx = document.getElementById('spendingChart').getContext('2d');
    const spendingData = window.spendingData;

    const labels = spendingData.map(data => `${data.year}년 ${data.month}월`);
    const incomeData = spendingData.map(data => data.income);
    const expenseData = spendingData.map(data => data.expense);

    new Chart(spendingCtx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: '수입',
                    data: incomeData,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                },
                {
                    label: '지출',
                    data: expenseData,
                    backgroundColor: 'rgba(255, 99, 132, 0.6)',
                },
            ]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});