// 쿠키에서 'Authorization' 토큰을 가져옵니다.
const token = Cookies.get('Authorization');

// JWT 토큰의 권한 확인
const tokenPayload = parseJwtPayload(token); // JWT 토큰을 해석하여 payload를 가져오는 함수로 직접 구현해야 합니다.
const role = tokenPayload.auth;
const userName = tokenPayload.sub;

// 현재 URL을 가져옵니다.
const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
const userId = pathSegments[pathSegments.length - 2];

let lastReviewId = 0;
let lastFestivalId = 0;

$(document).ready(function () {
    let lastFollowingId = 0;
    let lastFollowerId = 0;

    getProfile();
    loadFollowing(lastFollowingId);
    loadFollower(lastFollowerId);
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

let profileRole;

// 프로필 불러오기
function getProfile() {
    $.ajax({
        url: `/api/users/${userId}/profile`,
        type: 'GET',
        success: function (data) {
            profileRole = data.role;
            loadPost(lastReviewId, lastFestivalId);
            console.log(data);
            let html1 = '';
            html1 += `
                    <p class="margin-bottom-2 ae-1"><span class="opacity-6">${data.role}</span></p>
                        <h1 class="ae-2 huge fromAbove">${data.nickname}</h1>
                        <div style="display: flex; justify-content: center; align-items: center;">
                            <div>
                                <div class="circular-image">
                                    <img src="https://vignette.wikia.nocookie.net/the-sun-vanished/images/5/5d/Twitter-avi-gender-balanced-figure.png/revision/latest?cb=20180713020754"
                                         alt="Image Description" style="width: 100%; height: 100%; object-fit: cover;">
                                </div>
                                <p>${data.username}</p>
                            </div>
                            <div id="heart-group" style="margin-left: 50px">
                                <input type="submit" id="follow-btn" class="heart-btn"
                                       style="font-size: 25px; background-color: darkgray; color: white" value="팔로잉"
                                    onclick="unfollowUser()"></input>
                                <input type="submit" id="unfollow-btn" class="heart-btn"
                                       style="font-size: 25px; background-color: forestgreen; color: white; display: none"
                                    value="팔로우" onclick="followUser()"></a>
                            </div>
                        </div>
                        <div>
                            <p>${data.introduce !== null ? data.introduce : ''}</p>
                        </div>`;
            $('#profile-header').html(html1);
            let html2 = '';
            const representativeBadges = data.representativeBadges;
            for (let i = 0; i < representativeBadges.length; i++) { // Loop through each festival
                html2 += `
                        <div class="image-container">
                                    <img src="${representativeBadges[i].fileUrl}" alt="뱃지 이미지">
                        </div>
                        <p class="tiny opacity-6">${representativeBadges[i].title}</p>
                        `;
            }
            ;
            $('#representative-badges').html(html2);
            showUserFollowBtn();
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function showUserFollowBtn() {
    var followBtn = document.getElementById('follow-btn');
    var unfollowBtn = document.getElementById('unfollow-btn');

    const apiUrl = `/api/users/${userId}/follows/check`
    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function (data) {
            console.log(data);
            if (data) {
                followBtn.style.display = 'inline-block';
                unfollowBtn.style.display = 'none';
            } else {
                followBtn.style.display = 'none';
                unfollowBtn.style.display = 'inline-block';
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function followUser() {
    const confirmation = confirm("해당 유저를 팔로우하시겠습니까? 팔로우를 진행할 경우 해당 유저에 관한 알림이 발송됩니다.");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        const apiUrl = `/api/users/follows/${userId}`
        $.ajax({
            url: apiUrl,
            type: 'POST',
            success: function (data) {
                console.log(data);
                alert(data.statusMessage + ' 해당 유저에 관한 알림이 발송됩니다!');
                getProfile();
            },
            error: function (err) {
                console.log('Error:', err);
                alert(err.responseJSON.statusMessage);
            }
        });
    }
}

function unfollowUser() {
    const confirmation = confirm("해당 유저를 언팔로우하시겠습니까? 언팔로우를 진행할 경우 해당 유저에 관한 알림이 발송되지 않습니다.");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        const apiUrl = `/api/users/follows/${userId}`
        $.ajax({
            url: apiUrl,
            type: 'DELETE',
            success: function (data) {
                console.log(data);
                alert(data.statusMessage + ' 해당 유저에 관한 알림이 발송되지 않습니다!');
                getProfile();
            },
            error: function (err) {
                console.log('Error:', err);
                alert(err.responseJSON.statusMessage);
            }
        });
    }
}

// 게시글 불러오기
function loadPost(lastReviewId, lastFestivalId) {
    if (profileRole === 'USER') {
        console.log(profileRole);
        loadReview(lastReviewId);
    } else {
        console.log(profileRole);
        loadFestival(lastFestivalId);
    }
}

// 리뷰 불러오기
function loadReview(lastReviewId) {
    $.ajax({
        url: `/api/users/${userId}/festivals/reviews?lt=${lastReviewId}`,
        type: 'GET',
        success: function (result) {
            const data = result.content;
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += `
                    <li>
                    <a onclick="moveReview(${data[i].id})" style="text-decoration: none"> <strong>${data[i].title}</strong> (${data[i].festivalTitle})</a>
                    </li>`;
            }
            ;

            if (lastReviewId == 0) {
                $('#my-post-list').html(html);
            } else {
                $('#my-post-list').append(html);
            }

            lastReviewId = data[data.length - 1].id;

            const loadBtn = document.querySelector('#load-post');
            loadBtn.onclick = function () {
                loadReview(lastReviewId);
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

// 페스티벌 불러오기
function loadFestival(lastFestivalId) {
    $.ajax({
        url: `/api/users/${userId}/festivals?lt=${lastFestivalId}`,
        type: 'GET',
        success: function (result) {
            const data = result.content;
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += `
                    <li>
                    <a onclick="moveFestival(${data[i].id})" style="text-decoration: none"> <strong>${data[i].title}</strong></a>
                    </li>`;
            }
            ;

            if (lastFestivalId == 0) {
                $('#my-post-list').html(html);
            } else {
                $('#my-post-list').append(html);
            }

            lastFestivalId = data[data.length - 1].id;

            const loadBtn = document.querySelector('#load-post');
            loadBtn.onclick = function () {
                loadFestival(lastFestivalId);
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

// 팔로워 불러오기
function loadFollower(lastFollowerId) {
    $.ajax({
        url: `/api/users/${userId}/follows/followers?lt=${lastFollowerId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) { // Loop through each festival
                html += `
                    <li>
                    <a onclick="moveProfile(${data[i].id})" style="text-decoration: none"> <strong>${data[i].username}</strong> (${data[i].nickname})</a>
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

// 팔로잉 불러오기
function loadFollowing(lastFollowingId) {
    $.ajax({
        url: `/api/users/${userId}/follows/followings?lt=${lastFollowingId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += `
                    <li>
                   <a onclick="moveProfile(${data[i].id})" style="text-decoration: none"> <strong>${data[i].username}</strong> (${data[i].nickname})</a>
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

// 페이지 새로고침 함수
function refreshPage() {
    // 페이지 새로고침 코드
    window.location.reload();
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