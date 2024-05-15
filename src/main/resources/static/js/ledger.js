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
document.getElementById('create-ledger').addEventListener('click', function() {
    const formContainer = document.getElementById('ledger-form-container');
    formContainer.style.display = 'block';
});

document.getElementById('save-ledger').addEventListener('click', function() {
    const ledgerTitle = document.getElementById('ledger-title').value;
    if (ledgerTitle.trim() !== '') {
        fetch('/ledgers/create', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({title: ledgerTitle})
        })
            .then(response => {
                if (response.ok) {
                    location.reload(); // 페이지 새로고침
                } else {
                    alert('가계부 생성에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류 발생');
            });
    } else {
        alert('가계부 이름을 입력해주세요.');
    }
});

const quotes = [
    "삶이 있는 한 희망은 있다. - 키케로",
    "산다는것 그것은 치열한 전투이다. - 로망로랑",
    "하루에 3시간을 걸으면 7년 후에 지구를 한바퀴 돌 수 있다. - 사무엘존슨",
    "언제나 현재에 집중할수 있다면 행복할것이다. - 파울로 코엘료",
    "진정으로 웃으려면 고통을 참아야하며 , 나아가 고통을 즐길 줄 알아야 해 - 찰리 채플린",
    "직업에서 행복을 찾아라. 아니면 행복이 무엇인지 절대 모를 것이다 - 엘버트 허버드",
    "신은 용기있는자를 결코 버리지 않는다 - 켄러",
    "피할수 없으면 즐겨라 - 로버트 엘리엇",
    "단순하게 살아라. 현대인은 쓸데없는 절차와 일 때문에 얼마나 복잡한 삶을 살아가는가? - 이드리스 샤흐",
    "먼저 자신을 비웃어라. 다른 사람이 당신을 비웃기 전에 - 엘사 맥스웰",
    "먼저핀꽃은 먼저진다 남보다 먼저 공을 세우려고 조급히 서둘것이 아니다 - 채근담"
];

const quoteElement = document.getElementById('quote');
const generateButton = document.getElementById('generate-quote');

function generateRandomQuote() {
    const randomIndex = Math.floor(Math.random() * quotes.length);
    const selectedQuote = quotes[randomIndex];
    quoteElement.textContent = selectedQuote;
}

// 페이지 로드 시 초기 명언 생성
generateRandomQuote();

// 취소 버튼 클릭 이벤트 핸들러 추가
document.getElementById('cancel-ledger').addEventListener('click', function() {
    const formContainer = document.getElementById('ledger-form-container');
    const ledgerTitle = document.getElementById('ledger-title');
    ledgerTitle.value = ''; // 가계부 이름 입력 필드 초기화
    formContainer.style.display = 'none'; // 가계부 생성 폼 숨기기
});
