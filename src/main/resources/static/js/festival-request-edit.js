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
    getFestivalRequestEditPage();
});

function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

function getFestivalRequestEditPage() {
    $.ajax({
        url: `/api/festival-requests/${festivalRequestId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
            <div style="margin-left: 30px">
        <label for="titleInput">페스티벌 이름</label>
        <input type="text" id="titleInput" name="titleInput" required="" value="${data.title}"><br><br>

        <div style="display: grid; grid-template-columns: repeat(2, 1fr); width: 100%">
            <div style="width: 90%">
                <label for="openDateInput">시작 일시</label>
                <input type="datetime-local" id="openDateInput" name="openDateInput" required="" value="${data.openDate}"><br><br>
            </div>

            <div style="width: 90%">
                <label for="endDateInput">종료 일시</label>
                <input type="datetime-local" id="endDateInput" name="endDateInput" required="" value="${data.endDate}"><br><br>
            </div>
        </div>

        <label for="placeInput">페스티벌 장소</label>
        <input type="text" id="placeInput" name="placeInput" required="" value="${data.place}"><br><br>

        <label for="contentInput">페스티벌 설명</label>
        <textarea id="contentInput" name="contentInput" required="" ></textarea><br><br>

        <div style="display: grid; grid-template-columns: repeat(2, 1fr); width: 100%">
            <div style="width: 90%">
                <label for="reservationOpenDateInput">예매 오픈 일시</label>
                <input type="datetime-local" id="reservationOpenDateInput" name="reservationOpenDateInput"
                       value="${data.reservationOpenDate}"><br><br>
            </div>

            <div style="width: 90%">
                <label for="reservationPlaceInput">예매처</label>
                <input type="text" id="reservationPlaceInput" name="reservationPlaceInput" value="${data.reservationPlace}"><br><br>
            </div>
        </div>
        <label for="officialLinkInput">페스티벌 웹사이트/링크</label>
        <input type="url" id="officialLinkInput" name="officialLinkInput" required="" value="${data.officialLink}"><br><br>
        <br><br>
        <div style="float: right;">
        <input type="submit" onclick="redirectFestivalMap()" value="페스티벌 맵 돌아가기" style="background-color: crimson">
        <input type="submit" onclick="redirectFestivalRequestList()" value="페스티벌 요청 목록 돌아가기" style="background-color: crimson">
        <input type="submit" id="festival-edit-submit" name="festival-edit-submit" onclick="editFestivalRequestPost(${data.id})"
               value="페스티벌 정보 수정">
    </div>
    </div>
                `;
            $('#festival-request-edit-form').html(html);
            const contentTextarea = document.querySelector('#contentInput');
            contentTextarea.textContent = data.content;
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function redirectFestivalMap() {
    window.location.href = `/api/festivals-map`;
}

function redirectFestivalRequestList() {
    window.location.href = `/api/admin-page#festival-request`;
}

// 페스티벌 수정
function editFestivalRequestPost(festivalRequestId) {
    // <form> 요소에서 데이터 가져오기
    const title = $('#titleInput').val();
    const place = $('#placeInput').val();
    const content = $('#contentInput').val();
    const openDate = $('#openDateInput').val();
    const endDate = $('#endDateInput').val();
    const reservationOpenDate = $('#reservationOpenDateInput').val();
    const reservationPlace = $('#reservationPlaceInput').val();
    const officialLink = $('#officialLinkInput').val();

    // DTO 객체 생성
    const requestDto = {
        title: title,
        place: place,
        content: content,
        openDate: openDate,
        endDate: endDate,
        reservationOpenDate: reservationOpenDate,
        reservationPlace: reservationPlace,
        officialLink: officialLink,
    };

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/festival-requests/${festivalRequestId}`,
        type: 'PUT',
        data: JSON.stringify(requestDto),
        contentType: 'application/json',
        success: function (data) {
            alert('페스티벌 요청 수정 완료!');
            if (role === 'ADMIN') {
                window.location.href = '/api/admin-page#festival-request';
            } else {
                window.location.href = '/api/festivals-map';
            }
        },
        error: function (err) {
            alert(err.responseJSON.statusMessage);
            console.log('Error:', err);
        }
    });
}