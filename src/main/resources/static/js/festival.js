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
                    <p>${data.tags.map(tag => `<span>#${tag.title}</span>`).join(' ')}</p>
            </div>
                    <div class="festival-content">
                    <div>
                        <a href="/api/users/festivals-map">Map</a>
                        <a href="/#features">List</a>
                    </div>
                    <div id="festivalUDContainer" style="float: right; display: none; margin-bottom: 15px;">
                        <input type="submit" value="삭제" style="background-color: crimson" onclick="alertDeleteFestival('${data.title}')">
                        <input type="submit" value="수정" style="margin-left: 10px" onclick="alertEditFestival('${data.title}')">
                    </div>
                    <img src="${data.files[0] ? data.files[0].uploadFileUrl : '/images/best1.jpg'}" alt="축제 이미지" class="festival-image">
                    <p class="festival-description">${data.content}</p>
                    <div id="post-review"></div>
                    </div>
                `;
            $('#festival-post').html(html);
            showUDContainer(role, userName, data.editorName);

            // $.ajax({
            //     url: `/api/festivals/1/reviews`,
            //     type: 'GET',
            //     success: function (data) {
            //         console.log(data);
            //         let html = '';
            //         for (let i = 0; i <data.length; i++) {
            //             html += `
            //             <div class="reviews">
            //                 <div class="review-item">
            //                     <p><a href="/api/festivals/1/reviews/${data[i].id}">${data[i].title}</a><strong>${data[i].userNickname}</strong></p>
            //                 </div>
            //             </div>
            //             `;
            //         }
            //         $('#post-review').html(html);
            //     },
            //     error: function (err) {
            //         console.log('Error:', err);
            //     }
            // });
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});

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

function showUDContainer(role, userName, editorName) {
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
            window.location.href = '/api/users/festivals-map';
        },
        error: function (err) {
            alert(err.statusMessage);
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