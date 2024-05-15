document.addEventListener('DOMContentLoaded', function() {
    const slides = document.querySelectorAll('.slide');
    let currentSlide = 0;

    // 첫 번째 슬라이드 활성화
    slides[currentSlide].classList.add('active');

    function showSlide(n) {
        slides[currentSlide].classList.remove('active');
        currentSlide = (n + slides.length) % slides.length;
        slides[currentSlide].classList.add('active');
    }

    function autoSlide() {
        showSlide(currentSlide + 1);
    }

    // 3초 후 인터벌 시작
    setTimeout(function() {
        setInterval(autoSlide, 3000);
    }, 500);
});