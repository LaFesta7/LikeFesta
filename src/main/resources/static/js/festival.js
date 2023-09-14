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
const festivalId = pathSegments[pathSegments.length - 2];

$(document).ready(function () {
    getFestival();
});

function getFestival() {
    $.ajax({
        url: `/api/festivals/${festivalId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
            <div class="festival-header">
                <h1 style="margin-left: 10px; margin-right: 10px">${data.title}</h1>
                    <br>
                    <strong> 시작일 </strong>
                    <p>${formatDate(new Date(data.openDate))}</p>
                    <strong> 종료일 </strong>
                    <p>${formatDate(new Date(data.endDate))}</p>
                    <strong> 위치 </strong>
                    <p>${data.place}</p>
                    <br>
                    <strong> 예매일 </strong>
                    <p>${data.reservationOpenDate ? formatDate(new Date(data.reservationOpenDate)) : ''}</p>
                    <strong> 예매처 </strong>
                    <p>${data.reservationPlace}</p>
                    <br>
                    <strong> 공식사이트 </strong>
                    <br>
                    <a href="${data.officialLink}">→ 바로가기</a>
                    <br>
                    <p>${data.tags.map(tag => `<span>#${tag.title}</span>`).join(' ')}</p>
            </div>
                    <div class="festival-content">
                    <div>
                        <a href="/api/festivals-map">Map</a>
                        <a href="/#festival" style="margin-left: 10px">List</a>
                    </div>
                    <div class="scrollable-content">
                        <div id="festivalUDContainer" style="float: right; display: none; margin-bottom: 15px;">
                            <input type="submit" value="삭제" style="background-color: crimson" onclick="alertDeleteFestival('${data.title}')">
                            <input type="submit" value="수정" style="margin-left: 10px" onclick="alertEditFestival('${data.title}')">
                        </div>
                        <img src="${data.files[0] ? data.files[0].uploadFileUrl : '/images/background/img-21.jpg'}" alt="축제 이미지" class="festival-image">
                        <div style="display: flex">
                            <div class="festival-description">
                                <a href="#" style="text-decoration: none"><strong onclick="moveProfile(${data.editorId})" style="font-size: larger; margin-right: 15px">${data.editorName}</strong></a></div>
                                <p class="festival-description">${data.content}</p>
                        </div>
                        <div id="heart-group" style="display: flex">
                            <input type="submit" id="follow-btn" class="heart-btn" style="font-size: 14px; background-color: darkgray; color: white" value="팔로잉" onclick="unfollowFestival()"></input>
                            <input type="submit" id="unfollow-btn" class="heart-btn" style="font-size: 14px; background-color: darkgreen; color: white; display: none" value="팔로우" onclick="followFestival()"></a>
                            <a href="" id="heart-btn" class="heart-btn" style="margin-left: 10px; text-decoration: none; font-size: 25px;" onclick="cancelFestivalLike()">❤️</a>
                            <a href="" id="not-heart-btn" class="heart-btn" style="margin-left: 10px; text-decoration: none; font-size: 25px; display: none" onclick="addFestivalLike()">🤍</a>
                            <span style="font-size: 20px; margin-left: 5px; margin-top: 5px">${data.likeCnt}</span>
                        </div>
                        <div id="moveReviewPostBtn" style="float: right; display: none;">
                            <input type="submit" value="리뷰 작성" style="margin-left: 10px" onclick="moveReviewPost()">
                        </div>
                        <div id="post-review" style="margin-top: 80px"></div>
                        </div>
                    </div>
                `;
            $('#festival-post').html(html);
            showFestivalUDContainer(role, userName, data.editorName);
            showFestivalFollowBtn();
            showFestivalLikeBtn();
            showReviewPostBtn();
            getReviews();
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function showFestivalFollowBtn() {
    var followBtn = document.getElementById('follow-btn');
    var unfollowBtn = document.getElementById('unfollow-btn');

    const apiUrl = `/api/users/followed-festivals/${festivalId}`
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

function followFestival() {
    const confirmation = confirm("해당 페스티벌을 팔로우하시겠습니까? 팔로우를 진행할 경우 해당 페스티벌에 관한 알림이 발송됩니다.");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        const apiUrl = `/api/users/followed-festivals/${festivalId}`
        $.ajax({
            url: apiUrl,
            type: 'POST',
            success: function (data) {
                console.log(data);
                alert(data.statusMessage + ' 해당 페스티벌의 알림이 발송됩니다!');
                getFestival();
            },
            error: function (err) {
                console.log('Error:', err);
                alert(err.responseJSON.statusMessage);
            }
        });
    }
}

function unfollowFestival() {
    const confirmation = confirm("해당 페스티벌을 언팔로우하시겠습니까? 언팔로우를 진행할 경우 해당 페스티벌에 관한 알림이 발송되지 않습니다.");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        const apiUrl = `/api/users/followed-festivals/${festivalId}`
        $.ajax({
            url: apiUrl,
            type: 'DELETE',
            success: function (data) {
                console.log(data);
                alert(data.statusMessage + ' 해당 페스티벌의 알림이 발송되지 않습니다!');
                getFestival();
            },
            error: function (err) {
                console.log('Error:', err);
                alert(err.responseJSON.statusMessage);
            }
        });
    }
}

function showFestivalLikeBtn() {
    var heartBtn = document.getElementById('heart-btn');
    var notHeartBtn = document.getElementById('not-heart-btn');

    const apiUrl = `/api/festivals/${festivalId}/user-like`
    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function (data) {
            console.log(data);
            if (data) {
                heartBtn.style.display = 'inline-block';
                notHeartBtn.style.display = 'none';
            } else {
                heartBtn.style.display = 'none';
                notHeartBtn.style.display = 'inline-block';
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function addFestivalLike() {
    const apiUrl = `/api/festivals/${festivalId}/likes`
    $.ajax({
        url: apiUrl,
        type: 'POST',
        success: function (data) {
            console.log(data);
            alert(data.statusMessage);
            getFestival();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

function cancelFestivalLike() {
    const apiUrl = `/api/festivals/${festivalId}/likes-cancel`
    $.ajax({
        url: apiUrl,
        type: 'DELETE',
        success: function (data) {
            console.log(data);
            alert(data.statusMessage);
            getFestival();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

function getReviews() {
    const apiUrl = `/api/festivals/${festivalId}/reviews`
    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i < data.length; i++) {
                html += `
                <div class="reviews">
                    <div class="review-item">
                        <p><a href="${apiUrl}/${data[i].id}/page" style="text-decoration: none; margin-left: 20px">${data[i].title}</a>
                            <a href="#" style="text-decoration: none"><strong onclick="moveProfile(${data[i].userId})" style="font-size: larger; float: right; margin-right: 20px">${data[i].userNickname}</strong></a>
                        </p>
                    </div>
                </div>
                `;
            }
            $('#post-review').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function moveReviewPost() {
    window.location.href = `/api/festivals/${festivalId}/reviews/post-page`;
}

function showReviewPostBtn(role) {
    if (role === 'ADMIN') {
        $('#moveReviewPostBtn').hide();
    } else {
        $('#moveReviewPostBtn').show();
    }
}

function formatDate(serverDate) {
    return `${serverDate.getFullYear()}년 ${serverDate.getMonth() + 1}월 ${serverDate.getDate()}일 ${serverDate.getHours()}시 ${serverDate.getMinutes()}분`;
}

function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

function showFestivalUDContainer(role, userName, editorName) {
    if (role === 'ADMIN' || userName === editorName) {
        $('#festivalUDContainer').show();
    } else {
        $('#festivalUDContainer').hide();
    }
}

function alertDeleteFestival(festivalTitle) {
    // 경고창을 띄웁니다.
    const confirmation1 = confirm("'" + festivalTitle + "'을 삭제하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation1) {
        const confirmation2 = confirm("삭제를 진행할 경우 연관된 리뷰와 댓글 모두가 함께 삭제됩니다. 그래도 삭제하시겠습니까?");
        if (confirmation2) {
            deleteFestival();
        }
    }
}

function deleteFestival() {
    $.ajax({
        url: `/api/festivals/${festivalId}`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            window.location.href = '/api/festivals-map';
        },
        error: function (err) {
            alert(err.responseJSON.statusMessage);
            console.log('Error:', err);
        }
    });
}

function alertEditFestival(festivalTitle) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + festivalTitle + "'을 수정하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        window.location.href = `/api/festivals/${festivalId}/edit-page`;
    }
}

// 프로필로 이동하기
function moveProfile(userId) {
    window.location.href = `/api/users/${userId}/profile-page`;
}