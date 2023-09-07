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
        getOrganizerRequests();
    }
});

function getOrganizerRequests() {
    $.ajax({
        url: '/api/admin/users/organizer-requests',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) { // Loop through each festival
                html += `<tr>
                    <td>${data[i].id}</td>
                    <td>${data[i].username}</td>
                    <td>${data[i].nickname}</td>
                    <td>${data[i].email}</td>
                    <td><a id="checkbox${i}" onclick="alertOrganizerRequest('${data[i].username}', '${data[i].id}')">승인</a></td>
                </tr>`;
            }
            ;
            $('#organizer-request-table-body').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function alertOrganizerRequest(username, userId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + username + "'의 주최자 권한을 승인하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        approveOrganizerRequest(userId);
    }
}

function approveOrganizerRequest(userId) {
    $.ajax({
        url: `/api/admin/users/organizer-requests/${userId}`,
        type: 'PATCH',
        success: function (data) {
            alert(data.username + '의 주최자 승인이 완료되었습니다!');
            getOrganizerRequests();
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}