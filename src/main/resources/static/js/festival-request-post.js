// 페스티벌 요청 작성
function submitFestivalRequestPost() {
    // <form> 요소에서 데이터 가져오기
    const title = document.querySelector('#titleInput').value;
    const place = document.querySelector('#placeInput').value;
    const content = document.querySelector('#contentInput').value;
    const openDate = document.querySelector('#openDateInput').value;
    const endDate = document.querySelector('#endDateInput').value;
    const reservationOpenDate = document.querySelector('#reservationOpenDateInput').value;
    const reservationPlace = document.querySelector('#reservationPlaceInput').value;
    const officialLink = document.querySelector('#officialLinkInput').value;

// 위도, 경도 정보
// URL에서 쿼리 문자열을 가져옵니다.
    var queryString = window.location.search;

// URLSearchParams를 사용하여 쿼리 문자열을 파싱합니다.
    var queryParams = new URLSearchParams(queryString);
    const latitude = queryParams.get('lat');
    const longitude = queryParams.get('lng');

    // DTO 객체 생성
    const requestDto = {
        title: title,
        place: place,
        content: content,
        latitude: latitude,
        longitude: longitude,
        openDate: openDate,
        endDate: endDate,
        reservationOpenDate: reservationOpenDate,
        reservationPlace: reservationPlace,
        officialLink: officialLink,
    };

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/festival-requests`,
        type: 'POST',
        data: JSON.stringify(requestDto),
        contentType: 'application/json',
        success: function (data) {
            alert(data.statusMessage);
            // AJAX 요청이 완료되면 리다이렉트 수행
            window.location.href = '/api/festivals-map';
        },
        error: function (err) {
            alert(err.responseJSON.statusMessage);
            console.log('Error:', err);
        }
    });
}

function redirectFestivalMap() {
    window.location.href = `/api/festivals-map`;
}