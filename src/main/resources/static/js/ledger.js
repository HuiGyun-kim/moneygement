document.addEventListener('DOMContentLoaded', function () {
    const editButtons = document.querySelectorAll('.edit-button');
    const ledgerQuote = document.getElementById('ledgerQuote');
    const ledgerDetails = document.getElementById('ledgerDetails');

    editButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            ledgerQuote.style.opacity = '0';
            setTimeout(function () {
                ledgerQuote.style.display = 'none';
                ledgerDetails.style.display = 'block';
                setTimeout(function () {
                    ledgerDetails.style.opacity = '1';
                }, 50);
            }, 500);
        });
    });
});