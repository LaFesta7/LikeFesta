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

        // 토큰 만료시간 확인
        checkTokenExpiration(tokenPayload);

        $.ajax({
            url: '/api/users/my-profile',
            type: 'GET',
            success: function (data) {
                console.log(data);
                let html = '';
                const representativeBadges = data.representativeBadges;
                for (let i = 0; i <representativeBadges.length; i++) { // Loop through each festival
                    html += `
                        <div class="image-container">
                                    <img src="${representativeBadges[i].fileUrl}" alt="뱃지 이미지">
                        </div>
                        <p class="tiny opacity-6">${representativeBadges[i].title}</p>
                        `;
                };
                $('#representative-badges').html(html);
            },
            error: function (err) {
                console.log('Error:', err);
            }
        });

        let lastFesFollowId = 0;
        let lastFollowId = 0

        loadFestivalFollow(lastFesFollowId);
        loadFollower(lastFollowId);

        function loadFestivalFollow(lastFesFollowId) {
            $.ajax({
                url: `/api/users/followed-festivals?lt=${lastFesFollowId}`,
                type: 'GET',
                success: function (data) {
                    console.log(data);
                    let html = '';
                    for (let i = 0; i < data.length; i++) { // Loop through each festival
                        html += `
                        <li>
                        <td>${data[i].title}</td>
                        </li>`;
                    }
                    ;
                    if(lastFesFollowId == 0){
                        $('#my-follow-festival').html(html);
                    }else{
                        $('#my-follow-festival').append(html);
                    }

                    lastFesFollowId = data[data.length - 1].id;

                    const loadBtn = document.querySelector('#load-festival-follow');
                    loadBtn.onclick = function () {
                        loadFestivalFollow(lastFesFollowId);
                    }
                },
                error: function (err) {
                    console.log('Error:', err);
                }
            });
        }

        function loadFollower(lastFollowId){
            $.ajax({
                url: `/api/users/follows/followers?lt=${lastFollowId}`,
                type: 'GET',
                success: function (data) {
                    console.log(data);
                    let html = '';
                    for (let i = 0; i < data.length; i++) { // Loop through each festival
                        html += `
                        <li>
                        <td>${data[i].username}</td>
                        </li>`;
                    }
                    ;
                    if(lastFollowId == 0){
                        $('#my-follow-list').html(html);
                    }else{
                        $('#my-follow-list').append(html);
                    }

                    lastFollowId = data[data.length - 1].id;

                    const loadBtn = document.querySelector('#load-follow');
                    loadBtn.onclick = function () {
                        loadFollower(lastFollowId);
                    }
                },
                error: function (err) {
                    console.log('Error:', err);
                }
            });
        }
    }

    let pageNum = 0;
    loadFestivals(pageNum);
});

function loadFestivals(pageNum) {
    $.ajax({
        url: `/api/festivals?page=${pageNum}`,
        type: 'GET',
        dataType: "json",
        success: function (data) {
            console.log(data);

            let html = '';
            data.content.forEach(function (festival){
                html += `<tr>
                        <td>${festival.id}</td>
                        <td><a href="/api/festivals/${festival.id}/page" target="_blank">${festival.title}</a></td>
                        <td>${festival.place}</td>
                        <td>${festival.content}</td>
                        <td>${formatDate(
                    new Date(festival.openDate))} ~ ${formatDate(
                    new Date(festival.endDate))}</td>
                        <td><a href="${festival.officialLink}" target="_blank">Official Link</a></td>
                    </tr>`;
            });
            $('#festival-table-body').html(html);

            makePagination(data);

        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function makePagination(page) {
    let pagination = $("#pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<a onclick='loadFestivals(${cur - 1})'><button>이전</button></a>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<a onclick="loadFestivals(${i - 1});"><button>${i}</button></a>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<a onclick='loadFestivals(${cur + 1})'><button>다음</button></a>`);
    }
}

function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

// 토큰 만료 시간을 확인하는 함수
function checkTokenExpiration(tokenPayload) {
    if (tokenPayload) {
        // 토큰의 만료 시간 (UTC 밀리초 단위) 가져오기
        const expirationTime = tokenPayload.exp * 1000; // 초를 밀리초로 변환

        // 현재 시간 (UTC 밀리초 단위) 가져오기
        const currentTime = Date.now();

        // 만료 시간까지 남은 시간 계산
        const timeRemaining = expirationTime - currentTime;

        if (timeRemaining <= 0) {
            // 토큰이 만료되었으므로 새로고침 또는 로그아웃 등의 작업 수행
            refreshPage(); // 페이지 새로고침 함수 호출
        } else {
            // 만료 시간까지 남은 시간 (밀리초)이 timeRemaining에 저장됩니다.
            // 필요에 따라 이 값을 활용할 수 있습니다.
            console.log(`토큰 만료까지 남은 시간: ${timeRemaining} 밀리초`);

            // 만료 시간까지 남은 시간 후에 새로고침 수행
            setTimeout(refreshPage, timeRemaining);
        }
    }
}

// 페이지 새로고침 함수
function refreshPage() {
    // 페이지 새로고침 코드
    window.location.reload();
}

// 관리자 페이지 이동하기
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

function formatDate(serverDate) {
    return `${serverDate.getFullYear()}. ${serverDate.getMonth() + 1}. ${serverDate.getDate()}. T ${serverDate.getHours()} : ${serverDate.getMinutes()}`;
}