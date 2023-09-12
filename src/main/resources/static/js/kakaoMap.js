$(document).ready(function () {
    // 쿠키에서 'Authorization' 토큰을 가져옵니다.
    const token = Cookies.get('Authorization');

    // JWT 토큰의 권한 확인
    const tokenPayload = parseJwtPayload(token); // JWT 토큰을 해석하여 payload를 가져오는 함수로 직접 구현해야 합니다.
    const role = tokenPayload.auth;
    if (role === 'USER') {
        $('#festival-post-button').hide();
        $('#festival-request-post-button').show();
    } else {
        $('#festival-post-button').show();
        $('#festival-request-post-button').hide();
    }
});

function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

document.addEventListener('DOMContentLoaded', function () {
    // Your previous code
    const FESTIVAL_TABLE_BODY = document.getElementById('festival-table-body');
    const PAGINATION = document.getElementById('pagination');
    const TOTAL_PAGES = 6;
    let paginationLinks = '';

    for (let page = 1; page <= TOTAL_PAGES; page++) {
        paginationLinks += `<a href="?page=${page}">${page}</a>`;
    }

    PAGINATION.innerHTML = paginationLinks;

    const token = "7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLg=="; // Replace this with the actual token value
    const myButton = document.getElementById("myButton");

    if (myButton) {  // Check if myButton is not null
        myButton.addEventListener("click", function () {
            fetch("/api/users/profile/followings", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                },
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error("Error:", error));
        });
    }

    const button = document.getElementById('getTagsBtn');

    if (button) {
        button.addEventListener('click', function () {
            if (this.classList.contains('myButton')) {
            }
        });
    }
});