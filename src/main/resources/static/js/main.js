$(document).ready(function () {
    // 쿠키에서 'Authorization' 토큰을 가져옵니다.
    const token = Cookies.get('Authorization');
    // 토큰이 존재하면 서버 API를 호출하여 유효성을 검사합니다.
    if (token === undefined) {
        // 토큰이 존재하지 않으면, 사용자는 로그인 상태가 아닙니다.
        $('#logoutForm').hide();  // 로그아웃 버튼을 숨깁니다
        $('#loginButton').show();  // 로그인 버튼을 표시합니다
    } else {
        $('#logoutForm').show();  // 로그아웃 버튼을 표시합니다
        $('#loginButton').hide();  // 로그인 버튼을 숨깁니다
    }

    $("#getTagsBtn").click(function() {
        $.ajax({
            url: '/api/tags',
            type: 'GET',
            dataType: 'json',
            headers: {

            },
            success: function(data) {

                console.log(data);
            },
            error: function(error) {
                console.error("There was an error!", error);
            }
        });
    });
});
document.addEventListener("DOMContentLoaded", function() {
    const FESTIVAL_TABLE_BODY = document.getElementById('festival-table-body');
    const PAGINATION = document.getElementById('pagination');
    const TOTAL_PAGES = 6;
    let paginationLinks = '';
    for (let page = 1; page <= TOTAL_PAGES; page++) {
        paginationLinks += `<a href="?page=${page}">${page}</a>`;
    }
    PAGINATION.innerHTML = paginationLinks;
});

document.addEventListener("DOMContentLoaded", function() {
    const token = "YOUR_JWT_TOKEN_HERE"; // Replace this with the actual token value
    const myButton = document.getElementById("myButton");

    myButton.addEventListener("click", function() {
        fetch("/your-api-endpoint", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            },

        })
            .then(response => response.json())
            .then(data => console.log(data))
            .catch(error => console.error("Error:", error));
    });
});

const button = document.getElementById('getTagsBtn');

button.addEventListener('click', function() {
    if (this.id === 'getTagsBtn') {
        // Do something for getTagsBtn
    }
    if (this.classList.contains('myButton')) {
        // Do something for myButton
    }
});