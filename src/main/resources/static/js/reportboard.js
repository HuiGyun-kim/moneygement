

document.addEventListener('DOMContentLoaded', function () {
    fetch('/ledgerEntry/spendingReport')
        .then(response => response.json())
        .then(data => {
            document.getElementById('incomeAmount').textContent = formatCurrency(data.income);
            document.getElementById('expenseAmount').textContent = formatCurrency(data.expense);
            document.getElementById('incomeAmountThreeMonthsAgo').textContent = formatCurrency(data.incomeThreeMonthsAgo);
            document.getElementById('expenseAmountThreeMonthsAgo').textContent = formatCurrency(data.expenseThreeMonthsAgo);

            const spendingCtx = document.getElementById('spendingChart').getContext('2d');
            const spendingData = data.monthlyData;

            const labels = spendingData.map(item => `${item.year}년 ${item.month}월`);
            const incomeData = spendingData.map(item => item.income);
            const expenseData = spendingData.map(item => item.expense);

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
                            beginAtZero: true,
                            ticks: {
                                callback: function (value, index, values) {
                                    return formatCurrency(value);
                                }
                            }
                        }
                    },
                    tooltips: {
                        callbacks: {
                            label: function (tooltipItem, data) {
                                let label = data.datasets[tooltipItem.datasetIndex].label || '';
                                if (label) {
                                    label += ': ';
                                }
                                label += formatCurrency(tooltipItem.yLabel);
                                return label;
                            }
                        }
                    }
                }
            });

            const incomeExpenseCtx = document.getElementById('incomeExpenseChart').getContext('2d');
            const lastMonthIncome = data.lastMonthIncome;
            const lastMonthExpense = data.lastMonthExpense;

            new Chart(incomeExpenseCtx, {
                type: 'doughnut',
                data: {
                    labels: ['전달 수입', '전달 지출'],
                    datasets: [{
                        data: [lastMonthIncome, lastMonthExpense],
                        backgroundColor: ['rgba(75, 192, 192, 0.6)', 'rgba(255, 99, 132, 0.6)'],
                    }]
                },
                options: {
                    tooltips: {
                        callbacks: {
                            label: function (tooltipItem, data) {
                                let label = data.labels[tooltipItem.index] || '';
                                if (label) {
                                    label += ': ';
                                }
                                label += formatCurrency(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
                                return label;
                            }
                        }
                    }
                }
            });

            // 전달 대비 이번 달의 지출 비교 표시
            const expenseDifference = data.expenseDifference;
            const chartLabel = document.querySelector('.chart-label');
            if (expenseDifference > 0) {
                chartLabel.textContent = `전달보다 ${formatCurrency(expenseDifference)} 덜 소비하셨어요!`;
            } else if (expenseDifference < 0) {
                chartLabel.textContent = `전달보다 ${formatCurrency(Math.abs(expenseDifference))} 더 소비하셨어요.`;
            } else {
                chartLabel.textContent = '전달과 이번 달의 소비가 같습니다.';
            }
        });
    const incomeExpenseCtx = document.getElementById('incomeExpenseChart').getContext('2d');
    const lastMonthIncome = data.lastMonthIncome;
    const lastMonthExpense = data.lastMonthExpense;
    const incomeThisMonth = data.incomeThisMonth;
    const expenseThisMonth = data.expenseThisMonth;

    new Chart(incomeExpenseCtx, {
        type: 'doughnut',
        data: {
            labels: ['전달 수입', '전달 지출', '이번 달 수입', '이번 달 지출'],
            datasets: [{
                data: [lastMonthIncome, lastMonthExpense, incomeThisMonth, expenseThisMonth],
                backgroundColor: ['rgba(75, 192, 192, 0.6)', 'rgba(255, 99, 132, 0.6)', 'rgba(54, 162, 235, 0.6)', 'rgba(255, 206, 86, 0.6)'],
            }]
        },
        options: {
            tooltips: {
                callbacks: {
                    label: function (tooltipItem, data) {
                        let label = data.labels[tooltipItem.index] || '';
                        if (label) {
                            label += ': ';
                        }
                        label += formatCurrency(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
                        return label;
                    }
                }
            }
        }
    });
});


function formatCurrency(amount) {
    return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(amount);
}
