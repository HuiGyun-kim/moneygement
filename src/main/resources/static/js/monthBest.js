document.addEventListener('DOMContentLoaded', function() {
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
    });
});