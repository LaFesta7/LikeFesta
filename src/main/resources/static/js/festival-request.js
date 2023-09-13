// 쿠키에서 'Authorization' 토큰을 가져옵니다.
const token = Cookies.get('Authorization');

// JWT 토큰의 권한 확인
const tokenPayload = parseJwtPayload(token); // JWT 토큰을 해석하여 payload를 가져오는 함수로 직접 구현해야 합니다.
const role = tokenPayload.auth;

// 현재 URL을 가져옵니다.
const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
const festivalRequestId = pathSegments[pathSegments.length - 2];

$(document).ready(function () {
    getFestivalRequest();
});

function getFestivalRequest() {
    $.ajax({
        url: `/api/festival-requests/${festivalRequestId}`,
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
                    <br>
                    <strong> 위치 </strong>
                    <p>${data.place}</p>
                    <br>
                    <br>
                    <strong> 예매일 </strong>
                    <p>${formatDate(new Date(data.reservationOpenDate))}</p>
                    <strong> 예매처 </strong>
                    <p>${data.reservationPlace}</p>
                    <br>
                    <br>
                    <strong> 공식사이트 </strong>
                    <br>
                    <a href="${data.officialLink}">${data.officialLink}</a>
                    <br>
                    <br>
                    <br>
            </div>
                    <div class="festival-content">
                    <div>
                        <a href="/api/festivals-map" style="margin-right: 10px">Map</a>
                        <a href="/api/admin-page#festival-request">Festival Request List</a>
                    </div>
                    <div id="festivalUDContainer" style="float: right; display: none; margin-bottom: 10px;">
                        <input type="submit" value="삭제" style="background-color: crimson" onclick="alertDeleteFestivalRequest('${data.title}')">
                        <input type="submit" value="수정" style="margin-left: 10px" onclick="alertEditFestivalRequest('${data.title}')">
                    </div>
                    <p class="festival-description" style="margin-top: 20px">${data.content}</p>
                    </div>
                `;
            $('#festival-post').html(html);
            showFestivalUDContainer(role);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
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

function showFestivalUDContainer(role) {
    if (role === 'ADMIN') {
        $('#festivalUDContainer').show();
    } else {
        $('#festivalUDContainer').hide();
    }
}

function alertDeleteFestivalRequest(festivalRequestTitle) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + festivalRequestTitle + "'을 삭제하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
            deleteFestivalRequest();
    }
}

function deleteFestivalRequest() {
    $.ajax({
        url: `/api/festival-requests/${festivalRequestId}`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            if (role === 'ADMIN') {
                window.location.href = '/api/admin-page#festival-request';
            } else {
                window.location.href = '/api/festivals-map';
            }
        },
        error: function (err) {
            alert(err.statusMessage);
            console.log('Error:', err);
        }
    });
}

function alertEditFestivalRequest(festivalRequestTitle) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("'" + festivalRequestTitle + "'을 수정하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        window.location.href = `/api/festival-requests/${festivalRequestId}/edit-page`;
    }
}