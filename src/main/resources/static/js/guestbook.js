const guestbookData = [
    { name: "홍길동", message: "안녕하세요!" },
    { name: "홍길동", message: "방명록을 남겨봅니다." },
    { name: "홍길동", message: "안녕하세요 ㅎㅎ" },
    { name: "홍길동", message: "ㅎㅇ" },
    { name: "홍길동", message: "방명록을 남겨봅니다." },
    { name: "홍길동", message: "방명록을 남겨봅니다." },
    { name: "홍길동", message: "방명록을 남겨봅니다." },
    { name: "홍길동", message: "방명록을 남겨봅니다." },
];

const itemsPerPage = 6;
let currentPage = 1;

function displayGuestbookItems(pageNumber) {
    const guestbookContainer = document.getElementById("guestbook-container");
    guestbookContainer.innerHTML = "";

    const startIndex = (pageNumber - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;

    for (let i = startIndex; i < endIndex && i < guestbookData.length; i++) {
        const item = guestbookData[i];
        const guestbookItem = `
                <div class="guestbook-item">
                    <h3>${item.name}</h3>
                    <p>${item.message}</p>
                </div>
            `;
        guestbookContainer.innerHTML += guestbookItem;
    }
}

function displayPagination() {
    const totalPages = Math.ceil(guestbookData.length / itemsPerPage);
    const paginationContainer = document.getElementById("pagination-container");
    paginationContainer.innerHTML = "";

    for (let i = 1; i <= totalPages; i++) {
        const pageLink = document.createElement("a");
        pageLink.href = "#";
        pageLink.textContent = i;
        if (i === currentPage) {
            pageLink.classList.add("active");
        }
        pageLink.addEventListener("click", () => {
            currentPage = i;
            displayGuestbookItems(currentPage);
            displayPagination();
        });
        paginationContainer.appendChild(pageLink);
    }
}

//첫 번째 페이지의 방명록 아이템과 페이징을 표시
displayGuestbookItems(currentPage);
displayPagination();