let TAmount=0;
let EAmount = 0;

document.addEventListener('DOMContentLoaded', function() {
    const userId = document.getElementById('userId').getAttribute('data-user-id');

    displayExpense(userId);
});

function displayExpense(userId) {
    fetch(`/userChallenges/displayExpense/${userId}`)
        .then(response => response.json())
        .then(data => {
            displayExpenseAmount(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function displayExpenseAmount(expenseAmount) {
    EAmount = expenseAmount;
    const expenseAmountBox = document.getElementById('expense');
    expenseAmountBox.textContent = `지출 금액: ${expenseAmount}₩`;
    update();
}