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
        url: '/api/festivals',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) { // Loop through each festival
                html += `<tr>
                        <td>${data[i].id}</td>
                        <td>${data[i].title}</td>
                        <td>${data[i].location}</td>
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

    $.ajax({
        url: '/api/festivals',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) { // Loop through each festival
                html += `<tr>
                        <td>${data[i].id}</td>
                        <td>${data[i].title}</td>
                        <td>${data[i].location}</td>
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
});