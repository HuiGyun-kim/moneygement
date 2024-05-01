document.querySelector('.more').addEventListener('click', function() {
    var dropdownMenu = document.getElementById('myDropdown');
    if (dropdownMenu.style.display === 'block') {
    dropdownMenu.style.display = 'none';
} else {
    dropdownMenu.style.display = 'block';
}
});
    // 드롭다운 메뉴말고 다른 공간 클릭하면 닫히도록 처리
    window.addEventListener('click', function(event) {
    var dropdownMenu = document.getElementById('myDropdown');
    if (!event.target.matches('.more')) {
    dropdownMenu.style.display = 'none';
}
});
