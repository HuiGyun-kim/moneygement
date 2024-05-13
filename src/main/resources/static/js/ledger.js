function toggleEditMode(button, ledgerId) {
    const row = button.parentNode.parentNode;
    const titleSpan = row.querySelector(`#title-${ledgerId}`);
    const editInput = row.querySelector(`#edit-${ledgerId}`);
    const editButton = row.querySelector('.edit-button');
    const deleteButton = row.querySelector('.delete-button');
    const saveButton = row.querySelector('.save-button');

    titleSpan.style.display = titleSpan.style.display === 'none' ? 'inline' : 'none';
    editInput.style.display = editInput.style.display === 'none' ? 'inline' : 'none';
    editButton.style.display = 'none';
    deleteButton.style.display = 'none';
    saveButton.style.display = 'inline';

    if (editInput.style.display === 'inline') {
        editInput.focus();
    }
}

function saveEdit(button, ledgerId) {
    const row = button.parentNode.parentNode;
    const editInput = row.querySelector(`#edit-${ledgerId}`);
    const newTitle = editInput.value.trim();

    if (newTitle !== '') {
        fetch(`/ledgers/edit/${ledgerId}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({title: newTitle})
        })
            .then(response => {
                if (response.ok) {
                    const titleSpan = row.querySelector(`#title-${ledgerId}`);
                    titleSpan.textContent = newTitle;
                    const editButton = row.querySelector('.edit-button');
                    const deleteButton = row.querySelector('.delete-button');
                    const saveButton = row.querySelector('.save-button');
                    titleSpan.style.display = 'inline';
                    editInput.style.display = 'none';
                    editButton.style.display = 'inline';
                    deleteButton.style.display = 'inline';
                    saveButton.style.display = 'none';
                } else {
                    alert('가계부 이름 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류 발생');
            });
    } else {
        alert('가계부 이름을 입력해주세요.');
    }
}

function confirmDelete() {
    return confirm('정말로 이 가계부를 삭제하시겠습니까?');
}


function showLedgerDetails(titleId) {
    const ledgerId = titleId.split('-')[1];
    const detailsContainer = document.getElementById('ledgerDetails');
    const ledgerQuote = document.getElementById('ledgerQuote');

    ledgerQuote.style.opacity = '0';
    setTimeout(() => {
        ledgerQuote.style.display = 'none';
        detailsContainer.style.display = 'block';
        setTimeout(() => {
            detailsContainer.style.opacity = '1';
        }, 50);
    }, 500);

    loadEntries(ledgerId, false, 0, 'incomeTable', 'incomePagination');
    loadEntries(ledgerId, true, 0, 'expenseTable', 'expensePagination');
}
function hideLedgerDetails() {
    const detailsContainer = document.getElementById('ledgerDetails');
    const ledgerQuote = document.getElementById('ledgerQuote');

    detailsContainer.style.opacity = '0';
    setTimeout(() => {
        detailsContainer.style.display = 'none';
        ledgerQuote.style.display = 'block';
        setTimeout(() => {
            ledgerQuote.style.opacity = '1';
        }, 50);
    }, 500);
}

function loadEntries(ledgerId, isExpense, page, tableId, paginationId) {
    fetch(`/ledgers/entries/${ledgerId}?isExpense=${isExpense}&page=${page}`)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById(tableId);
            tableBody.innerHTML = '';

            const tableHeader = document.createElement('tr');
            tableHeader.innerHTML = `
                <th>날짜</th>
                <th>금액</th>
                <th>메모</th>
                <th>카테고리</th>
            `;
            tableBody.appendChild(tableHeader);

            if (data.content && data.content.length === 0) {
                const row = document.createElement('tr');
                row.innerHTML = '<td colspan="4">내역이 없습니다.</td>';
                tableBody.appendChild(row);
            } else {
                data.content.forEach(entry => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${entry.date}</td>
                        <td>${entry.amount}</td>
                        <td>${entry.description}</td>
                        <td>${entry.categoryName}</td>
                    `;
                    tableBody.appendChild(row);
                });
            }

            const pagination = document.getElementById(paginationId);
            pagination.innerHTML = '';

            if (data.totalPages > 0) {
                if (data.hasPrevious) {
                    const prevLink = document.createElement('a');
                    prevLink.href = '#';
                    prevLink.textContent = '<';
                    prevLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadEntries(ledgerId, isExpense, data.number - 1, tableId, paginationId);
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
                        loadEntries(ledgerId, isExpense, pageNumber, tableId, paginationId);
                    });
                    pagination.appendChild(pageLink);
                });

                if (data.hasNext) {
                    const nextLink = document.createElement('a');
                    nextLink.href = '#';
                    nextLink.textContent = '>';
                    nextLink.addEventListener('click', (event) => {
                        event.preventDefault();
                        loadEntries(ledgerId, isExpense, data.number + 1, tableId, paginationId);
                    });
                    pagination.appendChild(nextLink);
                }
            }
        })
        .catch(error => console.error('Error fetching entries:', error));
}