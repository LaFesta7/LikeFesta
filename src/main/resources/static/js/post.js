$(document).ready(function () {
    const url = window.location.href;
    const festivalId = url.substr(url.lastIndexOf('/') + 1);
    $.ajax({
        url: `/api/festivals/page/${festivalId}`,
        type: 'GET',
        success: function (festivals) {
            console.log(festivals);
            for (let i = 1; i <= festivals.length; i++) {
                let html = `
            <div class="festival-header">
                <h1>${festivals.title}</h1>
                    <br>
                    <p> 시작일 </p>
                    <p>${festivals.openDate}</p>
                    <p> 종료일 </p>
                    <p>${festivals.endDate}</p>
                    <br>
                    <p> 위치 </p>
                    <p>${festivals.place}</p>
            </div>
                    <div class="festival-content">
                    <img src="/images/best1.jpg" alt="축제 이미지" class="festival-image">
                    <p class="festival-description">${festivals.content}</p>
                    <div id="post-review"></div>
                    </div>
                `;
                $('#festival-post').append(html);
                $.ajax({
                    url: `/api/festivals/${festivals.id}/reviews`,
                    type: 'GET',
                    success: function (reviews) {
                        console.log(reviews);
                        let reviewHtml  = '';
                        for (let j = 0; j < reviews.length; j++) {
                            reviewHtml += `
                        <div class="reviews">
                            <div class="review-item">
                                <p><a href="/api/festivals/${festivals[i].id}/reviews/${reviews[j].id}">${reviews[j].title}</a><strong>${reviews[j].userNickname}</strong></p>
                            </div>      
                        </div>       
                        `;
                        }
                        $('.post-review').eq(i).html(reviewHtml);
                    },
                    error: function (err) {
                        console.log('Error:', err);
                    }
                });
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});