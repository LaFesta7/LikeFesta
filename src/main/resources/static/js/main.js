// 쿠키에서 'Authorization' 토큰을 가져옵니다.
const token = Cookies.get('Authorization');

$(document).ready(function () {
    // 토큰이 존재하면 서버 API를 호출하여 유효성을 검사합니다.
    if (token === undefined) {
        // 토큰이 존재하지 않으면, 사용자는 로그인 상태가 아닙니다.
        $('#myPageSection').hide();
        $('#myPageHiddenSection').show();
        $('#myPageTab').hide();
        $('#logoutForm').hide();  // 로그아웃 버튼을 숨깁니다
        $('#loginButton').show();  // 로그인 버튼을 표시합니다
        $('#notification-icon').hide();  // 알림 미표시
    } else {
        $('#myPageSection').show();
        $('#myPageHiddenSection').hide();
        $('#myPageTab').show();
        $('#logoutForm').show();  // 로그아웃 버튼을 표시합니다
        $('#loginButton').hide();  // 로그인 버튼을 숨깁니다
        $('#notification-icon').show();

        // JWT 토큰의 권한 확인
        const tokenPayload = parseJwtPayload(token); // JWT 토큰을 해석하여 payload를 가져오는 함수로 직접 구현해야 합니다.
        const role = tokenPayload.auth;
        if (role === 'ADMIN') {
            $('#adminPageLink').show();
        }

        // 토큰 만료시간 확인
        checkTokenExpiration(tokenPayload);

        let lastFesFollowId = 0;
        let lastFollowingId = 0;
        let lastFollowerId = 0;

        getMyProfile();
        loadFestivalFollow(lastFesFollowId);
        loadFollowing(lastFollowingId);
        loadFollower(lastFollowerId);
    }

    let pageNum = 0;
    loadFestivals(pageNum);

    getRank();
});

// JWT 읽기
function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

// 내 프로필 불러오기
function getMyProfile() {
    $.ajax({
        url: '/api/users/my-profile',
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            const representativeBadges = data.representativeBadges;
            for (let i = 0; i < representativeBadges.length; i++) { // Loop through each festival
                html += `
                        <div class="image-container">
                                    <img src="${representativeBadges[i].fileUrl}" alt="뱃지 이미지">
                        </div>
                        <p class="tiny opacity-6">${representativeBadges[i].title}</p>
                        `;
            }
            ;
            $('#representative-badges').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

// 페스티벌 불러오기
function loadFestivals(pageNum) {
    $.ajax({
        url: `/api/festivals?page=${pageNum}`,
        type: 'GET',
        dataType: "json",
        success: function (data) {
            console.log(data);

            let html = '';
            data.content.forEach(function (festival) {
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

// 페스티벌 팔로우 불러오기
function loadFestivalFollow(lastFesFollowId) {
    $.ajax({
        url: `/api/users/followed-festivals?lt=${lastFesFollowId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) { // Loop through each festival
                html += `
                    <li style="display: flex;">
                    <td style="display: flex;">
                        <strong><a onclick="moveFestival(${data[i].id})" style="text-decoration: none">${data[i].title}</a></strong>
                        <button style="float: right; margin-right: 20px" onclick="unfollowFestival(${data[i].id})">삭제</button>
                    </td>
                    </li>`;
            }
            ;
            if (lastFesFollowId == 0) {
                $('#my-follow-festival').html(html);
            } else {
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

// 페스티벌 언팔로우
function unfollowFestival(festivalId) {
    const confirmation = confirm("해당 페스티벌을 언팔로우하시겠습니까? 언팔로우를 진행할 경우 해당 페스티벌에 관한 알림이 발송되지 않습니다.");

    if (confirmation) {
        const apiUrl = `/api/users/followed-festivals/${festivalId}`
        $.ajax({
            url: apiUrl,
            type: 'DELETE',
            success: function (data) {
                console.log(data);
                alert(data.statusMessage + ' 해당 페스티벌의 알림이 발송되지 않습니다!');
                loadFestivalFollow();
            },
            error: function (err) {
                console.log('Error:', err);
                alert(err.responseJSON.statusMessage);
            }
        });
    }
}

// 팔로잉 불러오기
function loadFollowing(lastFollowingId) {
    $.ajax({
        url: `/api/users/follows/followings?lt=${lastFollowingId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += `
                    <li>
                   <a onclick="moveProfile(${data[i].id})" style="text-decoration: none"> <strong>${data[i].username}</strong> (${data[i].nickname})</a>
                        <button style="float: right; margin-right: 20px" onclick="unfollowUSer(${data[i].id})">삭제</button>
                    </li>`;
            }
            ;
            if (lastFollowingId == 0) {
                $('#my-following-list').html(html);
            } else {
                $('#my-following-list').append(html);
            }

            lastFollowingId = data[data.length - 1].id;

            const loadBtn = document.querySelector('#load-following');
            loadBtn.onclick = function () {
                loadFollowing(lastFollowingId);
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

// 유저 언팔로우
function unfollowUSer(followingUserId) {
    const confirmation = confirm("해당 유저 언팔로우하시겠습니까? 언팔로우를 진행할 경우 해당 유저에 관한 알림이 발송되지 않습니다.");

    if (confirmation) {
        const apiUrl = `/api/users/follows/${followingUserId}`
        $.ajax({
            url: apiUrl,
            type: 'DELETE',
            success: function (data) {
                console.log(data);
                alert(data.statusMessage + ' 해당 유저에 관한 알림이 발송되지 않습니다!');
                loadFollowing();
            },
            error: function (err) {
                console.log('Error:', err);
                alert(err.responseJSON.statusMessage);
            }
        });
    }
}

// 팔로워 불러오기
function loadFollower(lastFollowerId) {
    $.ajax({
        url: `/api/users/follows/followers?lt=${lastFollowerId}`,
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
            if (lastFollowerId == 0) {
                $('#my-follower-list').html(html);
            } else {
                $('#my-follower-list').append(html);
            }

            lastFollowerId = data[data.length - 1].id;

            const loadBtn = document.querySelector('#load-follower');
            loadBtn.onclick = function () {
                loadFollower(lastFollowerId);
            }
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
            `<button><a onclick='loadFestivals(${cur - 1})'>이전</a></button>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<button><a onclick="loadFestivals(${i - 1});">${i}</a></button>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<button><a onclick='loadFestivals(${cur + 1})'>다음</a></button>`);
    }
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

// 랭킹 가져오기
function getRank() {
    $.ajax({
        url: `/api/festivals/rank`,
        type: 'GET',
        success: function (data) {
            console.log(data);

            // 이미지 슬라이더를 초기화
            $('#festivalRank').slick({
                infinite: true, // 무한 루프
                slidesToShow: 1, // 한 번에 보여질 슬라이드 개수 (이미지 하나만 보이도록 설정)
                autoplay: true, // 자동 재생
                autoplaySpeed: 3000 // 자동 재생 속도 (3초마다 전환)
            });

            // 데이터를 슬라이더에 동적으로 추가
            for (let i = 0; i < data.length; i++) {
                const slide = `
                <div class="rank" onclick="moveFestival(${data[i].id})">
                    <img class="wide" src="${data[i].files[0].uploadFileUrl}" alt="">
                    <strong id="rank${data[i].id}" style="font-size: 30px; color: white; background-color: #5F5F5F; margin-left: 10px">${i + 1}. ${data[i].title}</strong>
                </div>
            `;

                // 슬라이더에 슬라이드 추가
                $('#festivalRank').slick('slickAdd', slide);
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });

    $.ajax({
        url: `/api/festivals/reviews/rank`,
        type: 'GET',
        success: function (data) {
            console.log(data);

            // 이미지 슬라이더를 초기화
            $('#reviewRank').slick({
                infinite: true, // 무한 루프
                slidesToShow: 1, // 한 번에 보여질 슬라이드 개수 (이미지 하나만 보이도록 설정)
                autoplay: true, // 자동 재생
                autoplaySpeed: 3000 // 자동 재생 속도 (3초마다 전환)
            });

            // 데이터를 슬라이더에 동적으로 추가
            for (let i = 0; i < data.length; i++) {
                const slide = `
                <div class="rank" onclick="moveReview(${data[i].festivalId}, ${data[i].id})">
                    <img class="wide" src="${data[i].files[0].uploadFileUrl}" alt=""/>
                    <strong id="rank${data[i].id}" style="font-size: 30px; color: white; background-color: #5F5F5F; margin-left: 10px">${i + 1}. ${data[i].festivalTitle} : ${data[i].title}</strong>
                </div>
            `;

                // 슬라이더에 슬라이드 추가
                $('#reviewRank').slick('slickAdd', slide);
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });

    $.ajax({
        url: `/api/users/rank`,
        type: 'GET',
        success: function (data) {
            console.log(data);

            // 이미지 슬라이더를 초기화
            $('#userRank').slick({
                infinite: true, // 무한 루프
                slidesToShow: 1, // 한 번에 보여질 슬라이드 개수 (이미지 하나만 보이도록 설정)
                autoplay: true, // 자동 재생
                autoplaySpeed: 3000 // 자동 재생 속도 (3초마다 전환)
            });

            // 데이터를 슬라이더에 동적으로 추가
            for (let i = 0; i < data.length; i++) {
                const slide = `
                <div class="rank" onclick="moveProfile(${data[i].id})">
                    <img class="wide" src="${data[i].files[0].uploadFileUrl}" alt=""/>
                    <strong id="rank${data[i].id}" style="font-size: 30px; color: white; background-color: #5F5F5F; margin-left: 10px">${i + 1}. ${data[i].nickname}</strong>
                </div>
            `;

                // 슬라이더에 슬라이드 추가
                $('#userRank').slick('slickAdd', slide);
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });

}

// 페스티벌 태그 검색
function searchFestivalTag() {
    const tagSearchInput = document.querySelector('#tagSearchInput').value; // 입력 필드의 값 가져오기
    console.log(tagSearchInput);
    $.ajax({
        url: `/api/festivals/tags?tag=${tagSearchInput}`,
        type: 'GET',
        dataType: "json",
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += `
                <div style="margin-top:30px; margin-bottom: 15px; border-bottom: 1px solid #cccccc">
                    <strong class="left" style="font-size: 20px"><p onclick="moveFestival(${data[i].id})">${data[i].title}</p></strong>
                    <div style="display: flex">
                    <p>${data[i].tags.map(tag => `<span>#${tag.title}</span>`).join(' ')}</p>
                    </div>
                </div>
                `;
            }
            ;
            $('#tag-search-festival-list').html(html);

            makePagination(data);
        },
        error: function (err) {
            if (token === undefined) {
                alert('로그인을 진행해주세요!')
            }
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

// 페스티벌로 이동하기
function moveFestival(festivalId) {
    window.location.href = `/api/festivals/${festivalId}/page`;
}

// 리뷰로 이동하기
function moveReview(festivalId, reviewId) {
    window.location.href = `/api/festivals/${festivalId}/reviews/${reviewId}/page`;
}

// 프로필로 이동하기
function moveProfile(userId) {
    window.location.href = `/api/users/${userId}/profile-page`;
}

function formatDate(serverDate) {
    return `${serverDate.getFullYear()}. ${serverDate.getMonth() + 1}. ${serverDate.getDate()}. T ${serverDate.getHours()} : ${serverDate.getMinutes()}`;
}