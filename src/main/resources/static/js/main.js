$(document).ready(function () {
    // 쿠키에서 'Authorization' 토큰을 가져옵니다.
    const token = Cookies.get('Authorization');
    // 토큰이 존재하면 서버 API를 호출하여 유효성을 검사합니다.
    if (token === undefined) {
        // 토큰이 존재하지 않으면, 사용자는 로그인 상태가 아닙니다.
        $('#logoutForm').hide();  // 로그아웃 버튼을 숨깁니다
        $('#loginButton').show();  // 로그인 버튼을 표시합니다
        $('#notification-icon').hide();  // 알림 미표시
    } else {
        $('#logoutForm').show();  // 로그아웃 버튼을 표시합니다
        $('#loginButton').hide();  // 로그인 버튼을 숨깁니다

        // JWT 토큰의 권한 확인
        const tokenPayload = parseJwtPayload(token); // JWT 토큰을 해석하여 payload를 가져오는 함수로 직접 구현해야 합니다.
        const role = tokenPayload.auth;

        if (role === 'ADMIN') {
            $('#adminPageLink').show();
        }

        $.ajax({
            url: '/api/users/followed-festivals',
            type: 'GET',
            success: function (data) {
                console.log(data);
                let html = '';
                for (let i = 0; i <data.length; i++) { // Loop through each festival
                    html += `
                        <td>${data[i].title}</td>`;
                };
                $('#my-follow-festival').html(html);
            },
            error: function (err) {
                console.log('Error:', err);
            }
        });

        $.ajax({
            url: '/api/users/follows/followers',
            type: 'GET',
            success: function (data) {
                console.log(data);
                let html = '';
                for (let i = 0; i <data.length; i++) { // Loop through each festival
                    html += `
                        <td>${data[i].username}</td>`;
                };
                $('#my-follow-list').html(html);
            },
            error: function (err) {
                console.log('Error:', err);
            }
        });
    }

    $.ajax({
        url: '/api/festivals',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) { // Loop through each festival
                html += `<tr>
                        <td>${data[i].id}</td>
                        <td><a href="/api/festivals/${data[i].id}" target="_blank">${data[i].title}</a></td>
                        <td>${data[i].place}</td>
                        <td>${data[i].content}</td>
                        <td>${data[i].openDate} ~ ${data[i].endDate}</td>
                        <td><a href="${data[i].officialLink}" target="_blank">Official Link</a></td>
                    </tr>`;
            };
            $('#festival-table-body').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});

function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

async function moveAdminPage() {
    try {
        const response = await fetch('/api/admin-page');

        if (!response.ok) {
            const errorMessage = await response.json();
            const statusMessage = errorMessage.statusMessage;
            console.error(statusMessage);
            alert(statusMessage);

            window.location.href = '/';
        } else {
            window.location.href = '/api/admin-page';
        }
    } catch (error) {
        console.error(error);
    }
}