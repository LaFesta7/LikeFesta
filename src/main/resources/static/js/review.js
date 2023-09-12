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
const festivalId = pathSegments[pathSegments.length - 4];
const reviewId = pathSegments[pathSegments.length - 2];

$(document).ready(function () {
    getReview();
});

function parseJwtPayload(token) {
    const base64Url = token.split('.')[1]; // JWT의 두 번째 부분이 페이로드입니다.
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // URL 안전한 Base64 문자열로 변환
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join('')); // Base64 디코딩 및 URL 디코딩

    return JSON.parse(jsonPayload); // JSON 문자열을 객체로 파싱
}

function getReview() {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}`;
    $.ajax({
        url: apiUrl,
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
                    <div id="reviewUDContainer" class="edit-delete">
                        <a href="${apiUrl}/edit-page" class="edit">Edit</a>
                        <button class="delete" onclick="alertDeleteReview()">Delete</button>
                    </div>
                </div>
                <div class="comment-post-container-before"></div>
                <div style="margin-top: 20px">
                    <strong>Comments</strong>
                </div>
                <div id="comment-post" class="comment-post-container" style="margin-top: 20px">
                    <textarea id="commentInput" name="commentInput" rows="2" required=""></textarea>
                    <button type="submit" class="comment-post" onclick="postComment()">작성</button>
                </div>
                <div id="review-comment"></div>
                `;
            $('#review-page').html(html);
            showReviewUDContainer(role, userName, data.username);
            getComments();
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function showReviewUDContainer(role, userName, editorName) {
    if (role === 'ADMIN' || userName === editorName) {
        $('#reviewUDContainer').show();
    } else {
        $('#reviewUDContainer').hide();
    }
}

function getComments() {
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments`,
        type: 'GET',
        success: function (data) {
            console.log(data);
            let html = '';
            for (let i = 0; i <data.length; i++) {
                // 현재 사용자의 이름과 댓글 작성자의 이름을 비교하여 수정 및 삭제 버튼의 가시성 설정
                const isCurrentUser = userName === data[i].username;
                html += `
                    <div class="comment">
                        <div class="comment-author" style="color: #5F5F5F; font-size: 12px">${data[i].userNickname}</div>
                        <div style="display: flex; justify-content: space-between;">
                            <div class="comment-content" style="font-size: 16px">${data[i].content}</div>
                            <div id="commentUDContainer" style="float: right; margin-top: 10px">
                                ${isCurrentUser ? `
                                    <a href="#" style="color: #5F5F5F; font-size: 14px" onclick="editComment(${data[i].id})">수정</a>
                                    <a href="#" style="color: #5F5F5F; font-size: 14px; margin-left: 10px" onclick="alertDeleteComment(${data[i].id})">삭제</a>
                                ` : ''}
                            </div>
                        </div>
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

function alertDeleteReview() {
    // 경고창을 띄웁니다.
    const confirmation1 = confirm("리뷰를 삭제하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation1) {
        const confirmation2 = confirm("삭제를 진행할 경우 연관된 댓글 모두가 함께 삭제됩니다. 그래도 삭제하시겠습니까?");
        if (confirmation2) {
            deleteReview();
        }
    }
}

function deleteReview() {
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            window.location.href = `/api/festivals/${festivalId}/page`;
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.statusMessage);
        }
    });
}

function postComment() {
    const content = document.querySelector('#commentInput').value;

    // DTO 객체 생성
    const requestDto = {
        content: content,
    };

    // 서버로 데이터를 전송
    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments`,
        type: 'POST',
        data: JSON.stringify(requestDto),
        contentType: 'application/json',
        success: function (data) {
            alert(data.statusMessage);
            getReview();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseText.statusMessage);
        }
    });
}