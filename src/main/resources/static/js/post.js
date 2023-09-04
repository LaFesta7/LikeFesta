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
                     <p>${data.location}</p>
            </div>
                       <div class="festival-content">
                       <img src="/images/best1.jpg" alt="축제 이미지" class="festival-image">
                       <p class="festival-description">${data.content}</p>
                `;
            $('#festival-post').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
});