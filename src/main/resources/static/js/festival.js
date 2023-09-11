$(document).ready(function () {
    // 현재 URL을 가져옵니다.
    const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
    const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
    const festivalId = pathSegments[pathSegments.length - 2];

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
                    <img src="${data.files[0] ? data.files[0].uploadFileUrl : '/images/best1.jpg'}" alt="축제 이미지" class="festival-image">
                    <p class="festival-description">${data.content}</p>
                    <div id="post-review"></div>
                    </div>
                `;
            $('#festival-post').html(html);
            $.ajax({
                url: `/api/festivals/1/reviews`,
                type: 'GET',
                success: function (data) {
                    console.log(data);
                    let html = '';
                    for (let i = 0; i <data.length; i++) {
                        html += `
                        <div class="reviews">
                            <div class="review-item">
                                <p><a href="/api/festivals/1/reviews/${data[i].id}">${data[i].title}</a><strong>${data[i].userNickname}</strong></p>
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
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});

function formatDate(serverDate) {
    return `${serverDate.getFullYear()}년 ${serverDate.getMonth() + 1}월 ${serverDate.getDate()}일 ${serverDate.getHours()}시 ${serverDate.getMinutes()}분`;
}