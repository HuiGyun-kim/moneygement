document.addEventListener('DOMContentLoaded', function() {
    const dateButton = document.getElementById('date');
    const expendsList = document.getElementById('expends');

    function generateDateButtons() {
        const today = new Date();
        const day = today.getDate();
        const weeks = ['일', '월', '화', '수', '목', '금', '토'];

        for (let i = -3; i <= 3; i++) {
            const date = new Date(today);
            date.setDate(day + i);
            const week = date.getDay()
            const button = document.createElement('button');
            button.innerHTML = `${weeks[week]}<br>${date.getDate()}`;

            if (date.getDay() === 6) { //토요일
                button.style.color = 'blue';
            }
            else if (date.getDay() === 0) { //일요일
                button.style.color = 'red';
            }

            if (i===0){ //여기서부터 작업하면 될 것 같음!!
                button.style.border
            }

            button.onclick = function() {
                showExpenditureDetails(date);
            };
            dateButton.appendChild(button);
        }
    }

    function showExpenditureDetails(date) {
        expendsList.textContent = `${date.getMonth() + 1}월 ${date.getDate()}일의 지출 내역`;
    }

    generateDateButtons();
});
