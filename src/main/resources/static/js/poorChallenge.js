let TAmount=0;
let EAmount = 0;

document.addEventListener('DOMContentLoaded', function() {
    const userId = document.getElementById('userId').getAttribute('data-user-id');
    document.getElementById('targetButton').addEventListener('click', function() {
        const targetAmount = document.getElementById('target_amount').value;
        fetch('/userChallenges/addUserChallenge', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                targetAmount: targetAmount
            })
        })
            .then(response => response.json())
            .then(data => {
                alert('성공적으로 저장되었습니다.');
            })
            .catch((error) => {
                alert('저장에 실패했습니다.');
                console.error('Error:', error);
            });
        displayTarget(userId);
        displayExpense(userId);
        update();
    });
    checkParticipationThisMonth(userId).then(isJoined => {
        if (isJoined) {
            document.querySelector('.goalset-group').style.display = 'none';
        }
    });
    displayTarget(userId);
    displayExpense(userId);
    update();
});
    function displayTarget(userId) {
        fetch(`/userChallenges/displayTarget/${userId}`)
            .then(response => response.json())
            .then(data => {
                displayTargetAmount(data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

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

    function displayTargetAmount(targetAmount) {
        TAmount = targetAmount;
        const targetAmountBox = document.getElementById('target');
        targetAmountBox.textContent = `목표 금액: ${targetAmount}₩`;
        update();
    }

    function displayExpenseAmount(expenseAmount) {
        EAmount = expenseAmount;
        const expenseAmountBox = document.getElementById('expense');
        expenseAmountBox.textContent = `지출 금액: ${expenseAmount}₩`;
        update();
    }

    function update(){
        const remain = TAmount-EAmount;
        const remainAmountBox = document.querySelector('.alert a');
        remainAmountBox.textContent = `${remain}₩`;
    }

    function checkParticipationThisMonth(userId) {
    return fetch(`/userChallenges/isJoinDate/${userId}`)
        .then(response => response.json())
        .catch((error) => {
            console.error('Error:', error);
        });
}