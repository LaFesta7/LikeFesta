$(document).ready(function () {
    $.ajax({
        url: `/api/festivals/1`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
            <div class="festival-header">
                <h1>${data.title}</h1>
                    <br>
                    <p> 시작일 </p>
                    <p>${data.openDate}</p>
                    <p> 종료일 </p>
                    <p>${data.endDate}</p>
                    <br>
                    <p> 위치 </p>
                    p>${data.location}</p>
            </div>
                    <div class="festival-content">
                    <img src="/images/best1.jpg" alt="축제 이미지" class="festival-image">
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