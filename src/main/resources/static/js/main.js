document.addEventListener("DOMContentLoaded", function() {
    // 토큰을 확인합니다.
    var token = localStorage.getItem("userToken");

    // 로그인 및 로그아웃 버튼을 찾습니다.
    var loginButton = document.querySelector(".button[href='login.html']");
    var logoutButton = document.querySelector(".button[href='']");

    // 토큰이 있을 경우 로그인 버튼을 숨기고 로그아웃 버튼을 표시합니다.
    if (token) {
        loginButton.style.display = "none";
        logoutButton.style.display = "block";
    }
    // 토큰이 없을 경우 로그아웃 버튼을 숨기고 로그인 버튼을 표시합니다.
    else {
        loginButton.style.display = "block";
        logoutButton.style.display = "none";
    }
});

// 초기에 두 버튼을 숨기기 위한 스타일을 추가합니다.
var styleElement = document.createElement("style");
styleElement.innerHTML = `
    .button[href='login.html'],
    .button[href=''] {
        display: none;
    }
`;
document.head.appendChild(styleElement);
