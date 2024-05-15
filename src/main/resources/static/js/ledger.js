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
    "가난 때문에 돈에 지배당해서는 안 된다. - 에픽테토스",
    "가난은 사람을 현명하게도 처절하게도 만든다. -베르톨트 브레히트-",
    "게으름에 대한 하늘의 보복은 두 가지가 있다.하나는 자신의 실패요, 또하나는 그가 하지 않은 일을 한 옆 사람의 성공이다. -르나르",
    "꾸어주는 사람은 그냥 주는 사람이다. -조지 허버트",
    "나는 임금이 되어 내 돈을 거지처럼 쓰기보다는 차라리 거지가 되어 내 마지막 1달러를 임금처럼 써보련다. -잉거슬",
    "남의 돈에는 날카로운 이빨이 돋아 있다. -러시아 속담",
    "내 주머니의 푼돈은 남의 주머니에 있는 거금보다 낫다. -세르반테스",
    "노력하는 자에게 돈이 붙는다.",
    "누구에게도 자금은 무한한 것이 아니다. -손자병법",
    "도둑질로 잘사는 사람도 있으나 잘사는 사람이라고 모두 도둑질한것은 아니다. 또한 청렴해서 가난하게 사는 사람도 있으나, 가난한 사람이다 청렴한 것은 아니다. -회남자",
    "도박하는 사람들은 불확실한 것을 얻기위해 확실한 것을 건다. -파스칼",
    "돈 빌려 달라는 것을 거절함으로써 친구를 잃는 일은 적지만, 반대로 돈을 빌려줌으로써 도리어 친구를 잃기 쉽다. -쇼펜하우어",
    "돈 속에, 돈 자체 속에, 그리고 돈을 취득하고 소유한다는 그 속에 무엇인가 비도덕적인 점이 있습니다. -톨스토이",
    "돈 쓰는 것을 겁내는 사람은 부자가 될 수 없다.",
    "돈 없이 연애 결혼을 하면 즐거운 밤과 슬픈 낮을 갖게 된다. -영국속담",
    "돈과 쾌락 혹은 명예를 사랑하는 사람은 남을 사랑하지 못한다. -에픽테토스",
    "돈에 관한 욕심은 모든 악의 근원으로 여겨지고 있다. 그러나 돈이 없는 것도 이 점에서는 똑같다. -버틀러",
    "돈에 너무 집착하면 돈을 벌기도 , 번 돈을 갖고 있기도 힘들다. 돈을 벌거나 모으려면 우선 돈을 편하게 생각해야 한다. 돈을 거북하게 느낀다면 의식적으로든 무의식적으로든 결국 돈을 안 가지는 쪽으로 해결을 보려 든다. -앤드류 매튜스",
    "돈으로 살 수 있는 행복이라 불리는 상품은 없다. -헨리 벤 다이크",
    "돈으로 행복해질 수가 없다. 그러니 가난한 사람에게 돈을 주다고 하는 잔혹한 일은 하지 말지어다. -엠마뉴엘 제롬",
    "돈은 거름과 같아서 뿌리지 않으면 썩기 쉽다. -베이컨",
    "돈은 누군지도 묻지 않고, 그 소유자에게 권리를 준다. -라스킨",
    "돈은 모든 불평등을 평등하게 만든다. -도스토예프스키",
    "돈은 무자비한 주인이지만, 유익한 종이 되기도 한다. -유태격언",
    "돈은 밑 없는 깊은 물 속과 같다. 명예도 양심도 진리도 모두 그 속에 빠지고 만다. -카스레",
    "돈은 바닥이 없는 바다 같은 것. 양심도 명예도 빠져서 떠오르지 않는다. – B.프랭클린",
    "돈은 비료와 같은 것으로 뿌리지 않으면 쓸모가 없다. -베이컨",
    "돈은 빌려주지도 말고 빌리지도 말라. 빌린 사람은 기가 죽고, 빌려준 사람도 자칫하면, 그 본전은 물론, 그 친구까지도 잃게 된다. -셰익스피어",
    "돈은 사업을 위해 쓰여야 할 것이며, 술을 위해 쓰여야 할 것은 아니다. -탈무드",
    "돈은 악이 아니며, 저주도 아니다. 돈은 사람을 축복하는 것이다. -탈무드",
    "돈은 양으로 따질 뿐, 질로 따지는 것이 아니다. -짐멜",
    "돈은 제6감 같은 것이다. 그것이 없으면 다른 5감을 완전히 가동시킬 수가 없다. – S.몸",
    "돈은 좋은 머슴이기는 하지만, 나쁜 주인이기도 하다. -베이컨",
    "돈은 진정 중요한 것이다. 따라서 모든 건전하고 성공적인 개인과 국가의 도덕은 이 사실에 기초를 두어야 한다. -조지 버나드 쇼",
    "돈은 쫓을 때는 도망가고, 필요 없다고 생각하면 따라와 자연히 모인다.",
    "돈은 최선의 종이요, 최악의 주인이다. -프랜시스 베이컨",
    "돈은 타인이 보기에는 행복하게 보이는 모든 것을 부여한다. -레니에",
    "돈은 필요악이다. 부유한 채로 죽는 것은 인간의 치욕이다.",
    "돈은 하느님으로부터의 선물을 살 기회를 준다. -탈무드",
    "돈은 현악기와 같다.그것을 적절히 사용할 줄 모르는사람은 불협화음을 듣게 된다. 돈은 사랑과 같다.이것을 잘 베풀려 하지 않는 이들을 천천히 그리고 고통스럽게 죽인다. 반면에,타인에게 이것을 베푸는 이들에게는 생명을 준다. -칼릴 지브란",
    "돈을 끌어오고 받아들이는 능력이 당신이 얼마나 돈을 만지며 살 수 있는가를 결정한다. -앤드류 매튜스",
    "돈을 낭비하거나 저금을 하는 사람은 가장 행복한 사람들이다. 그것은 양쪽 다 같이 그 일을 즐기고 있기 때문이다. – S. 존슨",
    "돈을 낭비하거나 저금을 하는 사람은 가장 행복한 사람이다. 그것은 양쪽 다 같이 그 일을 즐기고 있기 때문이다. – S. 존슨",
    "돈을 너무 많이 가지고 있다는 건, 너무 적게 가지고 있는 것보다 괴로운 일이다. -하이네",
    "돈을 모으려면 반드시 지켜야 할 원칙이 있고 정도(正道)가 있다. -대학",
    "돈을 버는 데 그릇된 방법을 썼다면, 그 만큼 그 마음속에는 상처가 나 있을 것이다. -빌리 그레엄",
    "돈을 빌려 준 사람에 대해서는 화를 참아야만 한다. -탈무드",
    "돈을 빌려주면 종종 돈은 물론이고 친구까지 잃는다. 돈을 빌리면 흔히 검약의 마음이 둔해진다. -셰익스피어",
    "돈을 빌려준 사람은 돈을 빌린 사람보다 훨씬 기억력이 좋다. -프랭클린",
    "돈을 빌리는 것은 노예가 되는 것이다. -에머슨",
    "돈을 빌리러 가는 것은 자유를 팔러 가는 것이다. -프랭클린",
    "돈을 사랑함은 모든 악의 뿌리이다.",
    "돈을 사랑함이 일만 악의 뿌리가 되나니 이것을 사모하는 자들이 미혹을 받아 믿음에서 떠나 많은 근심으로써 자기를 찔렀도다. -신약성서",
    "돈의 가치를 알아보고 싶거든 나가서 남에게 돈을 꾸어 달라고 요청해 보라. 적에게 돈을 꿔주면 그를 이기게 되고, 친구에게 꿔주면 그를 잃게 된다. -벤자민 프랭클린",
    "돈의 가치를 알아보고 싶거든 남에게 돈을 꾸어 달라고 요청해 보아라. -스마일즈",
    "돈이 없는 것은 슬픈 일이다. 하지만 남아도는 것은 그 두 배나 슬픈 일이다. -톨스토이",
    "돈이 있어도 이상이 없는 사람은 몰락의 길을 밟는다. -도스도예프스키",
    "돈이 있으면 이 세상에서는 많은 일을 할 수 있다. 그러나 청춘을 돈으로 살 수는 없다. -다이문트",
    "돈이나 물건은 그냥 주는 것보다도 빌려주는 쪽이 낫다. 그냥 얻으면, 얻은 쪽은 준 사람보다 밑에 있지 않으면 안 되지만, 빌려주고 빌린다면 대등하게 대할 수 있다. -탈무드",
    "돈이란 남에게 행복하게 보이는 온갖 것을 준다. – H. 레니에",
    "돈이란 남의 손에 넘기는 경우를 제외하면, 아무리 가지고 있어도 아무 이익도 가져 다 주지 않는 훌륭한 물건이다. -비어스",
    "돈이란 마치 인간의 제6감과 같아서 그것이 없으면 우리들의 다른 5감도 제대로 활용할 수 없게 된다. -서머셋 모옴",
    "돈이란 바닷물과도 같다. 그것은 마시면 마실수록 목이 말라진다. -쇼펜하우어",
    "돈이란 힘이고 자유이며 쿠션이자 모든 악의 근원이기도 한 동시에 한편으로는 최대의 행복이 되기도 한다. -칼 샌드버그",
    "두툼한 지갑이 좋다고는 말할 수 없다. 그러나 텅 빈 지갑은 더 나쁘다. -유태격언",
    "때 묻은 돈도 돈이다. 돈은 어떻게 쓰느냐가 돈의 가치를 결정하는 것이다.",
    "마음대로 좋은 나뭇잎을 골라 뜯어먹는 목이 긴기린의 행복을 생각할 때,목이 짧아 굶어죽은기린의 고통을 잊어서는 안된다.  -존 M.케인스",
    "만일 사회가 많은 가난한 사람을 도울 수 없 다면 부유한 소수의 사람도 구해 줄 수 없다. -존 F.케네디-",
    "만족할 줄 아는 사람은 진정한 부자이고,탐욕스러운 사람은 진실로 가난한 사람이다. -솔론",
    "모든 것은 필요한 만큼 신이 주고 신이 거두어 간다. 특히 돈은 인간보다 신이 더 유용하게 사용하는 신의 도구일지도 모른다. 신의 뜻을 조금이라도 헤아릴 수 있다면 돈이 있다고 교만하지 않을 것이며 돈이 없다고 결코 불평하지 않을 것이다. -이드리스 샤흐",
    "무조건 돈을 아낀다고 모이는 것이 아니다. 때로는 기회가 왔을 때, 과감하게 써야 한다.",
    "방랑자이지만 돈이 있으면 관광객이라 불린다. -폴 리처",
    "부자가 되는 한 가지 방법이 있다. 내일 할 일을 오늘 하고 오늘 먹을 것을 내일 먹어라. -유대 속담",
    "부정하게 번 돈은 오래 가지 못한다. 그것은 쉽게 와서 쉽게 떠난다. -플라우투스",
    "불로소득은 외상과 같아서, 언젠가는 청구서가 날아오기 마련이다.",
    "빚을 지고 내일 일어나기보다 오늘밤 먹지 않고 잠자라. -프랭클린",
    "빚을 지는 것은 노예가 되는 것이다. -에머슨",
    "사람들은 돈을 벌기는 어려워도 쓰기는 쉽다고 말한다. 그러나 돈을 잘 쓰는 방법이 훨씬 더 어려운 것이다. 돈을 잘 쓰는 사람은 인생의 승리자가 되고, 그렇지 못할 경우에는 패배자가 된다. 그렇기 때문에 집안이 번영하고 못하고는 주부에게 그 절반의 초점이 있다는 것을 알아야 한다. -그락쿠스",
    "사람을 상처 입히는 것이 세 개 있다. 번민, 말다툼, 텅빈 지갑, 그 중에서 텅빈 지갑이 가장 크게 사람을 상처 입힌다. -탈무드",
    "성공해서 만족하는 것은 아니다. 만족하고 있었기 때문에 성공한 것이다. -알랭",
    "세계에는 단지 두 가족의 집밖에 없다. 가진 자의집과 가지지 못한자의 집. -세르반테스",
    "수입을 생각하고 나서 지출 계획을 세우라. -예기",
    "시민으로서의 가장 중요한 미덕은 멋지게 돈을 긁어모으는 재능이다. 다시 말해서 어떠한 일이 있더라도 남에게 폐를 끼치지 말라는 것이다. -도스토예프스키",
    "신은 인간을 만들고,옷은 인간의 외양을 꾸민다. 그러나 인간을 마지막으로 완성하는 것은 돈이다. -존 레이",
    "아, 돈, 돈, 돈, 나는 반드시 그대를 신성한 것으로 생각하는 사람은 아니다. 하지만, 이따금 가던 길을 멈추고 의아해 한다. 그대는 나갈 때는 그렇게 빠르면서, 들어올 때는 왜 그리 더딘가 라고. -오그덴 나슈",
    "아, 돈이여! 돈 때문에 얼마나 많은 슬픈 일이 이 세상에서 일어나고 있는가. -톨스토이",
    "악의 근원을 이루는 것은 돈 자체가 아니라, 돈에 대한 애착인 것이다. -스마일즈",
    "올바르게 금전을 얻기까지는 이것을 쓰지 말라. -제퍼슨",
    "이유없이 생긴 큰 돈, 이것처럼 위험한 것도 없다.",
    "인류는 두 인종으로 이루어져 있다. 빌리는 자외 빌려 주는 사람들로. – C.램",
    "일은 일의 기쁨을 얻기 위해서 일하는 것이며, 어떤 일을 발전시키고 창조하는 가운데 만족감을 얻기 위해서 일하는 것입니다. 일을 사랑해서가 아니라, 돈 때문에 일하는 사람은 돈을 벌지도 못할 뿐만 아니라 즐거움도 얻지 못하는 법입니다. -슈왑",
    "자기 주머니에서 나오는 돈의 가치를 다른 사람이 알아줄 것이라는 기대는 하지 마라. -김용삼",
    "작은 비용을 삼가라. 작은 구멍이 큰 배를 가라앉힌다. -프랭클린",
    "재물과 보배는 불과도 같은 것. 매우 유용한 하인 노릇을 하는가 하면 가장 무서운 주인 노릇도 한다. -칼라일",
    "재물은 생활을 위한 방편일 뿐 그 자체가 목적이 될 수는 없다. -칸트",
    "재산은 가지고 있는 자의 것이 아니고, 그것을 즐기는 자의 것이다. -하우얼",
    "재산이 많은 사람이 그 재산을 자랑하는 있더라도 그 돈을 어떻게 쓰는지 알 수 있을 때까지는 그를 칭찬하지 말라. -소크라테스",
    "정당한 소유는 인간을 자유롭게 하지만 지나친 소유는 소유자체가 주인이되어 소유자를 노예로 만든다. -니체",
    "지갑이 가벼우면 마음이 무겁다. -괴테",
    "참으로 두려운 일은 배금주의 사상이다. 그것은 눈을 멀게 하고 귀를 막게 하고 야수보다 포악하게 하고 양심도 우정도 자기 영혼의 구원을 생각하지 않게 하고 결국 인간을 물욕의 노예로 만들어 버린다. 무엇보다 이 괴로운 노예 상태의 가장 나쁜 점은 사람들이 그 노예 상태를 즐거워하도록 만드는 것이다. 이 때문에 사람들이 황금에 몸을 맡기면 맡길수록 그들의 만족감은 불어난다. 그래서 이 병은 고치기가 힘들며 이러한 야수들은 길들이기 힘든 것이다. -즐라토우스트",
    "채권자도 채무자도 되지 말라. 빚돈은 종종 그 자체를 잃지마는 친구를 잃는 수가 있느니라. -세익스피어",
    "한 사람의 부자가 있기 위해서는 5백 명의가난 뱅이가 있지 않으면 안된다. -애덤 스미스",
    "화폐는 인간의 노동과 생존의 양도된 본질이다. 이 본질은 인간을 지배하며, 인간은 이것을 숭배한다. -마르크스"
];

function generateRandomQuote() {
    const randomIndex = Math.floor(Math.random() * quotes.length);
    const selectedQuote = quotes[randomIndex];
    const quoteElement = document.getElementById('quoteElement');
    quoteElement.textContent = selectedQuote;
}

window.addEventListener('load', generateRandomQuote);

// 취소 버튼 클릭 이벤트 핸들러 추가
document.getElementById('cancel-ledger').addEventListener('click', function() {
    const formContainer = document.getElementById('ledger-form-container');
    const ledgerTitle = document.getElementById('ledger-title');
    ledgerTitle.value = ''; // 가계부 이름 입력 필드 초기화
    formContainer.style.display = 'none'; // 가계부 생성 폼 숨기기
});
