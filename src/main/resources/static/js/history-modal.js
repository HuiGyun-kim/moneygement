
// 탭 전환 함수 수정
function openTab(evt, tabName, ledgerType) {
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

function updateYearMonth() {
    const date = new Date();
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    document.getElementById('currentExpenseYearMonth').textContent = `${year}년 ${month}월`;
    document.getElementById('currentIncomeYearMonth').textContent = `${year}년 ${month}월`;
}

// 데이터 제출 및 로드 관련 함수
document.getElementById('formAddIncome').onsubmit = function (event) {
    event.preventDefault();
    submitFormData();
};

function submitFormData() {
    const categoryId = document.getElementById('edit-category').value;
    const amount = document.getElementById('amount').value;
    const date = document.getElementById('date').value;
    const description = document.getElementById('description').value;

    const data = {
        categoryId: parseInt(categoryId),
        ledgerId: selectedLedgerId,
        amount: parseInt(amount),
        date: date,
        description: description,
        ledgerType: false
    };

    fetch('/ledgerEntry/add', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            alert('수입 내역이 성공적으로 추가되었습니다.');
            closeModal(modalAddIncome);
            loadIncomeData();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류 발생');
        });
}
function loadLedgers() {
    const ledgerSelect = document.getElementById('ledgerSelect');
    const userId = window.currentUserId;
    if (userId) {
        fetch(`/ledgers/user/${userId}`)
            .then(response => response.json())
            .then(ledgers => {
                ledgerSelect.addEventListener('change', (event) => {
                    selectedLedgerId = event.target.value;
                    selectedUserId = event.target.options[event.target.selectedIndex].dataset.userId;
                    renderCalendar(); // 가계부 변경 시 달력 다시 렌더링
                });
                ledgers.forEach(ledger => {
                    const option = document.createElement('option');
                    option.value = ledger.ledgerId;
                    option.textContent = ledger.title;
                    option.dataset.userId = userId; // dataset.userId 값 설정
                    ledgerSelect.appendChild(option);
                });
            })
            .catch(error => console.error('Failed to load ledgers:', error));
    } else {
        console.error('currentUserId element not found');
    }
}
function loadCategories() {
    fetch('/ledgerEntry/categories')
        .then(response => response.json())
        .then(data => {
            categories = data;
            const categorySelect = document.getElementById('edit-category');
            const categoryExpenseSelect = document.getElementById('edit-category-expense');
            categorySelect.innerHTML = '';
            categoryExpenseSelect.innerHTML = '';
            data.forEach(category => {
                const option = document.createElement('option');
                option.value = category.categoryId;
                option.textContent = category.categoryName;
                categorySelect.appendChild(option.cloneNode(true));
                categoryExpenseSelect.appendChild(option.cloneNode(true));
            });
        })
        .catch(error => console.error('Failed to load categories:', error));
}

function loadIncomeData(page = 0) {
    const ledgerId = document.getElementById('ledgerSelect').value;
    if (!ledgerId) {
        clearIncomeData();
        return;
    }

    fetch(`/ledgerEntry/entries?ledgerId=${encodeURIComponent(ledgerId)}&page=${page}&sort=date,desc`)
        .then(response => response.json())
        .then(data => {
            const incomeData = document.getElementById('incomeData');
            incomeData.innerHTML = '';

            if (data.content && data.content.length === 0) {
                const message = document.createElement('tr');
                message.innerHTML = '<td colspan="5">수입 내역이 없습니다.</td>';
                incomeData.appendChild(message);
            } else {
                data.content.forEach(entry => {
                    const id = entry.entryId;
                    const row = document.createElement('tr');
                    row.id = `row-${id}`;
                    row.innerHTML = `
                        <td id="date-${id}">${entry.date}</td>
                        <td id="amount-${id}">${entry.amount}</td>
                        <td id="description-${id}">${entry.description}</td>
                        <td id="edit-category-${id}">${entry.categoryName}</td>
                        <td id="button-container-${id}">
                            <button class="edit" onclick="editEntry(${id}, false)">수정</button>
                            <button class="delete" onclick="deleteEntry(${id}, false)">삭제</button>
                        </td>
                    `;
                    incomeData.appendChild(row);
                });
            }
            // 페이지네이션 처리
            // 페이지네이션 처리 수정
            const pagination = document.getElementById('incomePagination');
            pagination.innerHTML = '';

            if (data.totalPages > 0) {
                if (data.hasPrevious) {
                    const prevLink = document.createElement('a');
                    prevLink.href = '#';
                    prevLink.textContent = '<';
                    prevLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadIncomeData(data.number - 1);
                    });
                    pagination.appendChild(prevLink);
                }

                const pageNumberSequence = Array.from(
                    { length: Math.min(data.number + 2, data.totalPages) - Math.max(data.number - 1, 0) },
                    (_, i) => Math.max(0, data.number - 1) + i
                );

                pageNumberSequence.forEach(pageNumber => {
                    const pageLink = document.createElement('a');
                    pageLink.href = '#';
                    pageLink.textContent = pageNumber + 1;
                    if (pageNumber === data.number) {
                        pageLink.classList.add('active');
                    }
                    pageLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadIncomeData(pageNumber);
                    });
                    pagination.appendChild(pageLink);
                });

                if (data.hasNext) {
                    const nextLink = document.createElement('a');
                    nextLink.href = '#';
                    nextLink.textContent = '>';
                    nextLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadIncomeData(data.number + 1);
                    });
                    pagination.appendChild(nextLink);
                }
            }
        })
        .catch(error => console.error('Error fetching income data:', error));
}
function clearIncomeData() {
    const incomeData = document.getElementById('incomeData');
    incomeData.innerHTML = '';
    const message = document.createElement('tr');
    message.innerHTML = '<td colspan="5">가계부를 선택하세요.</td>';
    incomeData.appendChild(message);
}
// 수정 및 삭제 관련 함수
function submitEditEntry(id,isExpense = false) {
    if (typeof id !== 'number' || isNaN(id)) {
        alert("수정하려는 항목의 ID가 유효하지 않습니다.");
        return;
    }

    const dateField = document.getElementById(`date-${id}`).innerText;
    const amountField = document.getElementById(`amount-${id}`).innerText;
    const descriptionField = document.getElementById(`description-${id}`).innerText;
    const categoryIdElement = document.getElementById(`edit-category-select-${id}`);
    if (!categoryIdElement) {
        console.error('Category select element not found');
        return;
    }
    const categoryId = parseInt(categoryIdElement.value);

    const data = {
        ledgerId: selectedLedgerId,
        date: dateField,
        amount: parseInt(amountField),
        description: descriptionField,
        categoryId: categoryId,
        ledgerType: isExpense
    };

    fetch(`/ledgerEntry/edit/${id}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                alert(`${isExpense?'지출':'수입'}내역이 성공적으로 수정되었습니다.`);
                resetEditing(id, isExpense);
            } else {
                alert('내역 수정에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류 발생');
        });
}

function resetEditing(id, isExpense=false) {
    const dateField = document.getElementById(`date-${id}`);
    const amountField = document.getElementById(`amount-${id}`);
    const descriptionField = document.getElementById(`description-${id}`);
    const categorySelect = document.getElementById(`edit-category-select-${id}`);
    const buttonContainer = document.getElementById(`button-container-${id}`);

    dateField.contentEditable = false;
    amountField.contentEditable = false;
    descriptionField.contentEditable = false;

    categorySelect.replaceWith(categorySelect.options[categorySelect.selectedIndex].text);

    buttonContainer.innerHTML = `
       <button class="edit" onclick="editEntry(${id}, ${isExpense})">수정</button>
       <button class="delete" onclick="deleteEntry(${id}, ${isExpense})">삭제</button>
   `;
}

function editEntry(id, isExpense = false) {
    if (typeof id !== 'number' || isNaN(id)) {
        console.error("Invalid ID for editEntry:", id);
        alert("수정하려는 항목의 ID가 유효하지 않습니다.");
        return;
    }

    const dateField = document.getElementById(`date-${id}`);
    const amountField = document.getElementById(`amount-${id}`);
    const descriptionField = document.getElementById(`description-${id}`);

    if (!dateField || !amountField || !descriptionField) {
        console.error("Unable to find date, amount, or description field.");
        return;
    }

    dateField.contentEditable = true;
    amountField.contentEditable = true;
    descriptionField.contentEditable = true;

    const categorySelect = document.createElement('select');
    categorySelect.id = `edit-category-select-${id}`;
    window.categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category.categoryId;
        option.textContent = category.categoryName;
        categorySelect.appendChild(option);
    });

    const currentCategoryText = document.getElementById(`edit-category-${id}`).textContent;
    const currentCategory = window.categories.find(cat => cat.categoryName === currentCategoryText);
    if (currentCategory) {
        categorySelect.value = currentCategory.categoryId;
    }

    const oldCategoryField = document.getElementById(`edit-category-${id}`);
    oldCategoryField.innerHTML = '';
    oldCategoryField.appendChild(categorySelect);

    const buttonContainer = document.getElementById(`button-container-${id}`);
    buttonContainer.innerHTML = '';
    const editButton = document.createElement('button');
    editButton.textContent = '수정 완료';
    editButton.onclick = () => submitEditEntry(id, isExpense);
    buttonContainer.appendChild(editButton);
}

function deleteEntry(id, isExpense = false) {
    if (typeof id !== 'number' || isNaN(id)) {
        alert("삭제하려는 항목의 ID가 유효하지 않습니다.");
        return;
    }

    if (confirm(`정말로 이 ${isExpense ? '지출' : '수입'}내역을 삭제하시겠습니까?`)) {
        fetch(`/ledgerEntry/delete/${id}`, {method: 'DELETE'})
            .then(response => {
                if (response.ok) {
                    const row = document.getElementById(`row-${id}`);
                    if (row) {
                        row.parentNode.removeChild(row);
                        alert(`${isExpense ? '지출' : '수입'} 내역이 성공적으로 삭제되었습니다.`);
                    } else {
                        alert('삭제할 항목을 찾을 수 없습니다.');
                    }
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error deleting entry:', error);
                alert('오류 발생');
            });
    }
}

// 모달 열기 및 닫기 관련 함수
function openModal(tabName = 'defaultOpen') {
    const modal = document.getElementById('modalWindow');
    modal.style.display = 'block';
    document.body.style.overflow = 'hidden';

    const tabButton = document.querySelector(`.tablinks[onclick="openTab(event, '${tabName}')"]`);
    if (tabButton) {
        openTab({currentTarget: tabButton}, tabName);
    }
}

function closeModal(modal) {
    modal.style.display = 'none';
    document.body.style.overflow = 'auto';

    // defaultOpen 모달이 닫힐 때 페이지 새로고침
    if (modal.id === 'modalWindow') {
        location.reload();
    }
}

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modalWindow');
    const modalAddIncome = document.getElementById('modalAddIncome');
    const modalAddExpense = document.getElementById('modalAddExpense');

    window.openAddExpenseModal = function () {
        modalAddExpense.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }

    window.openAddIncomeModal = function () {
        modalAddIncome.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }

    document.getElementById('closeModal').onclick = () => closeModal(modal);
    modalAddIncome.querySelector('.close').onclick = () => closeModal(modalAddIncome);
    modalAddExpense.querySelector('.close').onclick = () => closeModal(modalAddExpense);

    window.onclick = (event) => {
        if (event.target == modal || event.target == modalAddIncome || event.target == modalAddExpense) {
            closeModal(event.target);
        }
    };
    loadCategories();
    updateYearMonth();
    loadIncomeData();
    loadExpenseData();
    loadLedgers();
});

function submitExpenseData() {
    const categoryId = document.getElementById('edit-category-expense').value;
    const amount = document.getElementById('amount-expense').value;
    const date = document.getElementById('date-expense').value;
    const description = document.getElementById('description-expense').value;

    const data = {
        categoryId: parseInt(categoryId),
        ledgerId: selectedLedgerId,
        amount: parseInt(amount),
        date: date,
        description: description,
        ledgerType: true // 지출 내역이므로 true
    };

    fetch('/ledgerEntry/addExpense', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            alert('지출 내역이 성공적으로 추가되었습니다.');
            closeModal(modalAddExpense);
            loadExpenseData(); // 지출 내역을 불러오는 함수 호출
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류 발생');
        });
}
function loadExpenseData(page = 0) {
    const ledgerId = document.getElementById('ledgerSelect').value;
    if (!ledgerId) {
        clearExpenseData();
        return;
    }
    fetch(`/ledgerEntry/entries?ledgerId=${encodeURIComponent(ledgerId)}&ledgerType=true&page=${page}&sort=date,desc`)
        .then(response => response.json())
        .then(data => {
            const expenseTableBody = document.getElementById('expenseTableBody');
            expenseTableBody.innerHTML = '';

            if (data.content && data.content.length === 0) {
                const row = document.createElement('tr');
                row.innerHTML = '<td colspan="5">지출 내역이 없습니다.</td>';
                expenseTableBody.appendChild(row);
            } else {
                data.content.forEach(entry => {
                    const id = entry.entryId;
                    const row = document.createElement('tr');
                    row.id = `row-${id}`;
                    row.innerHTML = `
                   <td id="date-${id}">${entry.date}</td>
                   <td id="amount-${id}">${entry.amount}</td>
                   <td id="description-${id}">${entry.description}</td>
                   <td id="edit-category-${id}">${entry.categoryName}</td>
                   <td id="button-container-${id}">
                       <button class="edit" onclick="editEntry(${id}, true)">수정</button>
                       <button class="delete" onclick="deleteEntry(${id}, true)">삭제</button>
                   </td>
               `;
                    expenseTableBody.appendChild(row);
                });
            }
            // 페이지네이션 처리
            const pagination = document.getElementById('expensePagination');
            pagination.innerHTML = '';

            if (data.totalPages > 0) {
                if (data.hasPrevious) {
                    const prevLink = document.createElement('a');
                    prevLink.href = '#';
                    prevLink.textContent = '<';
                    prevLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadExpenseData(data.number - 1);
                    });
                    pagination.appendChild(prevLink);
                }

                const pageNumberSequence = Array.from(
                    { length: Math.min(data.number + 2, data.totalPages) - Math.max(data.number - 1, 0) },
                    (_, i) => Math.max(0, data.number - 1) + i
                );

                pageNumberSequence.forEach(pageNumber => {
                    const pageLink = document.createElement('a');
                    pageLink.href = '#';
                    pageLink.textContent = pageNumber + 1;
                    if (pageNumber === data.number) {
                        pageLink.classList.add('active');
                    }
                    pageLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadExpenseData(pageNumber);
                    });
                    pagination.appendChild(pageLink);
                });

                if (data.hasNext) {
                    const nextLink = document.createElement('a');
                    nextLink.href = '#';
                    nextLink.textContent = '>';
                    nextLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadExpenseData(data.number + 1);
                    });
                    pagination.appendChild(nextLink);
                }
            }
        })
        .catch(error => console.error('Error fetching expense data:', error));
}
function clearExpenseData() {
    const expenseTableBody = document.getElementById('expenseTableBody');
    expenseTableBody.innerHTML = '';
    const row = document.createElement('tr');
    row.innerHTML = '<td colspan="5">가계부를 선택하세요.</td>';
    expenseTableBody.appendChild(row);
}
