document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modalWindow');
    const modalAddIncome = document.getElementById('modalAddIncome');

    // 모달을 닫는 함수
    function closeModal(modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
    }

    // 각 모달의 닫기 버튼에 클릭 이벤트 핸들러 연결
    document.getElementById('closeModal').onclick = () => closeModal(modal);
    modalAddIncome.querySelector('.close').onclick = () => closeModal(modalAddIncome);

    // 모달 외부를 클릭하면 모달 닫기
    window.onclick = (event) => {
        if (event.target == modal || event.target == modalAddIncome) {
            closeModal(event.target);
        }
    };

    // 탭을 전환하는 함수
    function openTab(evt, tabName) {
        const tabcontent = document.getElementsByClassName("tabcontent");
        for (let i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }
        const tablinks = document.getElementsByClassName("tablinks");
        for (let i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
        document.getElementById(tabName).style.display = "block";
        evt.currentTarget.className += " active";
    }

    window.openTab = openTab;

    // 연도와 월을 업데이트하는 함수
    function updateYearMonth() {
        const date = new Date();
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        document.getElementById('currentExpenseYearMonth').textContent = `${year}년 ${month}월`;
        document.getElementById('currentIncomeYearMonth').textContent = `${year}년 ${month}월`;
    }

    updateYearMonth();

    // 체크박스 선택 상태 업데이트
    function updateCheckboxState(allCheckbox, checkboxes) {
        const anyChecked = [...checkboxes].some(checkbox => checkbox.checked);
        allCheckbox.checked = anyChecked;
    }

    const selectAllExpenseCheckbox = document.querySelector('.selectAllExpense');
    const expenseCheckboxes = document.querySelectorAll('#expenseData input[type="checkbox"]');
    selectAllExpenseCheckbox.addEventListener('change', () => {
        expenseCheckboxes.forEach(checkbox => checkbox.checked = selectAllExpenseCheckbox.checked);
    });
    expenseCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => updateCheckboxState(selectAllExpenseCheckbox, expenseCheckboxes));
    });
    updateCheckboxState(selectAllExpenseCheckbox, expenseCheckboxes);

    const selectAllIncomeCheckbox = document.querySelector('.selectAllIncome');
    const incomeCheckboxes = document.querySelectorAll('#incomeData input[type="checkbox"]');
    selectAllIncomeCheckbox.addEventListener('change', () => {
        incomeCheckboxes.forEach(checkbox => checkbox.checked = selectAllIncomeCheckbox.checked);
    });
    incomeCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => updateCheckboxState(selectAllIncomeCheckbox, incomeCheckboxes));
    });
    updateCheckboxState(selectAllIncomeCheckbox, incomeCheckboxes);

    // 수입 내역 추가 모달 열기
    window.openAddIncomeModal = function () {
        modalAddIncome.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }

    // 폼 제출 시 AJAX로 데이터 전송
    document.getElementById('formAddIncome').onsubmit = function (event) {
        event.preventDefault();

        const ledgerId = document.getElementById('ledgerId').value;
        const amount = document.getElementById('amount').value;
        const date = document.getElementById('date').value;
        const description = document.getElementById('description').value;

        const data = {
            ledgerId: parseInt(ledgerId),
            amount: parseInt(amount),
            date: date,
            description: description,
            ledgerType: true
        };

        fetch('/ledgerEntry/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok) {
                    alert('수입 내역이 성공적으로 추가되었습니다.');
                    closeModal(modalAddIncome);
                } else {
                    alert('내역 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류 발생');
            });
    }
});

// 수입/지출 내역 모달 열기
function openModal(tabName = 'defaultOpen') {
    const modal = document.getElementById('modalWindow');
    modal.style.display = 'block';
    document.body.style.overflow = 'hidden';

    const tabButton = document.querySelector(`.tablinks[onclick="openTab(event, '${tabName}')"]`);
    if (tabButton) {
        openTab({ currentTarget: tabButton }, tabName);
    }
}
