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

    $.ajax({
        url: '/api/users/notifications',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) { // Loop through each festival
                html += `<tr>
                        <td>${data[i].title}</td>
                        <td>${data[i].detail}</td>
                        <td>${data[i].timeSinceCreated}</td>
                        <td>${data[i].rd}</td>
                    </tr>`;
            };
            $('#notification-table-body').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});