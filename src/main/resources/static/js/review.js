// 현재 URL을 가져옵니다.
const currentURL = window.location.href;

// URL에서 경로 파라미터 부분을 추출합니다.
const pathSegments = currentURL.split('/');

// 경로 파라미터 중에서 필요한 값을 추출합니다.
const festivalId = pathSegments[pathSegments.length - 4];
const reviewId = pathSegments[pathSegments.length - 2];

$(document).ready(function () {
    getReview();
});

function getReview() {
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = `
                <div  class="post-header">
                    <div class="post-title">${data.title}</div>
                    <div class="post-meta">${data.userNickname}</div>
                    <div class="post-meta">${data.createdAtTimeAgo}</div>
                </div>
                <img src="${data.files[0].uploadFileUrl}" alt="Review Image" class="post-image">
                <div class="post-content">
                    ${data.content}
                </div>
                <div class="actions">
                    <div class="like-button">${data.likeCnt}</div>
                    <div class="edit-delete">
                        <a href="review-edit.html" class="edit">Edit</a>
                        <button class="delete" onclick="deletePost()">Delete</button>
                    </div>
                </div>
                <div id="review-comment"></div>
                `;
            $('#review-page').html(html);
            getComments();
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function getComments() {
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) {
                html += `
                        <div class="comment">
                          <div class="comment-author" style="color: #5F5F5F; font-size: 12px">${data[i].userNickname}</div>
                          <div class="comment-content" style="font-size: 16px">${data[i].content}</div>
                        </div>
                        `;
            }
            $('#review-comment').html(html);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function deletePost() {
    var r = confirm("포스트를 삭제하시겠습니까?");
    if (r == true) {
        window.location.href = "index.html";
    }
}