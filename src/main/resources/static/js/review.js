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
            var imageSrc = data.fileUrl;
            let html = `
                <div  class="post-header">
                    <div class="post-title">${data.title}</div>
                    <div class="post-meta"><a href="#" style="text-decoration: none" onclick="moveFestival(${data.festivalId})">${data.festivalTitle}</a></div>
                    <div class="post-meta">${data.createdAtTimeAgo}</div>
                </div>
                <div class="scrollable-content">
                    <img src="${imageSrc}" alt="Review Image" class="post-image">
                    <div class="post-content" style="display: flex">
                        <a href="#" style="text-decoration: none"><strong onclick="moveProfile(${data.userId})" style="font-size: larger; float: right; margin-right: 20px">${data.userNickname}</strong></a>
                        ${data.content}
                    </div>
                    <div class="actions">
                        <div id="heart-group" style="display: flex">
                            <a href="" id="heart-btn" class="heart-btn" style="text-decoration: none; font-size: 25px;" onclick="cancelReviewLike()">❤️</a>
                            <a href="" id="not-heart-btn" class="heart-btn" style="text-decoration: none; font-size: 25px; display: none" onclick="addReviewLike()">🤍</a>
                            <span style="font-size: 20px; margin-left: 5px; margin-top: 5px">${data.likeCnt}</span>
                        </div>
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
                    <div id="review-comment-pagination" style="text-align: center;"><a>1</a></div>
                </div>
                `;
            $('#review-page').html(html);
            showReviewLikeBtn();
            showReviewUDContainer(role, userName, data.username);
            getComments(0);
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function showReviewLikeBtn() {
    var heartBtn = document.getElementById('heart-btn');
    var notHeartBtn = document.getElementById('not-heart-btn');

    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}/user-like`
    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function (data) {
            console.log(data);
            if (data) {
                heartBtn.style.display = 'inline-block';
                notHeartBtn.style.display = 'none';
            } else {
                heartBtn.style.display = 'none';
                notHeartBtn.style.display = 'inline-block';
            }
        },
        error: function (err) {
            console.log('Error:', err);
        }
    });
}

function addReviewLike() {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}/likes`
    $.ajax({
        url: apiUrl,
        type: 'POST',
        success: function (data) {
            console.log(data);
            alert(data.statusMessage);
            getReview();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

function cancelReviewLike() {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}/likes-cancel`
    $.ajax({
        url: apiUrl,
        type: 'DELETE',
        success: function (data) {
            console.log(data);
            alert(data.statusMessage);
            getReview();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
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

async function getComments(pageNum) {
    try {
        const commentData = await $.ajax({
            url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments?page=${pageNum}`,
            type: 'GET'
        });

        let html = '';
        for (let i = 0; i < commentData.content.length; i++) {
            let commentDt = commentData.content[i];
            // 현재 사용자의 이름과 댓글 작성자의 이름을 비교하여 수정 및 삭제 버튼의 가시성 설정
            const isCurrentUser = userName === commentDt.username;
            const isAdmin = role === 'ADMIN';

            // 비동기로 데이터를 가져오고 처리
            const isCommentLike = await getCommentLike(commentDt.id);

            html += `
                    <div class="comment">
                        <div class="comment-author" style="color: #5F5F5F; font-size: 12px"><a href="#" style="text-decoration: none" onclick="moveProfile(${commentDt.userId})">${commentDt.userNickname}</a></div>
                        <p id="comment${commentDt.id}" class="comment-content" style="font-size: 16px">${commentDt.content}</p>
                        <div class="comment-content-container" id="comment-container${commentDt.id}" style="display: flex; justify-content: space-between;">
                            <div style="display: flex">${isCommentLike ? `
                                    <a href="" id="comment-heart-btn" class="heart-btn" style="text-decoration: none; font-size: 15px;" onclick="cancelCommentLike(${commentDt.id})">❤️</a>
                                ` : `
                                <a href="" id="comment-not-heart-btn" class="heart-btn" style="text-decoration: none; font-size: 15px;" onclick="addCommentLike(${commentDt.id})">🤍</a>`}
                            <p id="commentLikeCnt${commentDt.id}" class="comment-content" style="font-size: 14px; margin-left: 5px; margin-top: 5px">${commentDt.likeCnt}</p>
                            </div>
                            <div id="commentUDContainer" style="float: right; margin-top: 10px">
                                ${isCurrentUser || isAdmin ? `
                                    <a href="#" style="color: #5F5F5F; font-size: 14px" onclick="showCommentInput(${commentDt.id}, '${commentDt.content}')">수정</a>
                                    <a href="#" style="color: #5F5F5F; font-size: 14px; margin-left: 10px" onclick="alertDeleteComment(${commentDt.id})">삭제</a>
                                ` : ''}
                            </div>
                        </div>
                    </div>
                `;
        }
        $('#review-comment').html(html);

        makeCommentPagination(commentData);
    } catch (err) {
        console.log('Error:', err);
    }
}

function makeCommentPagination(page){
    let pagination = $("#review-comment-pagination");
    pagination.empty();

    let cur = page.number; // 0부터 센다.
    let endPage = Math.ceil((cur + 1) / 10.0) * 10; // 1~10
    let startPage = endPage - 9; // 1~10
    if (endPage > page.totalPages - 1) // totalPage는 1부터 센다 그래서 1을 빼줌
    {
        endPage = page.totalPages;
    }

    if (cur > 0) // 이전 버튼
    {
        pagination.append(
            `<a onclick='getComments(${cur - 1})'><button>이전</button></a>`);
    }

    for (let i = startPage; i <= endPage; i++) { // 페이지네이션
        pagination.append(
            `<a onclick="getComments(${i - 1});"><button>${i}</button></a>`);
    }
    if (cur + 1 < page.totalPages) // 다음 버튼
    {
        pagination.append(
            `<a onclick='getComments(${cur + 1})'><button>다음</button></a>`);
    }
}

async function getCommentLike(commentId) {
    try {
        const boolean = await $.ajax({
            url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments/${commentId}/user-like`,
            type: 'GET'
        });
        return !!boolean;
    } catch (err) {
        console.log('Error:', err);
        return false;
    }
}

function addCommentLike(commentId) {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}/comments/${commentId}/likes`
    $.ajax({
        url: apiUrl,
        type: 'POST',
        success: function (data) {
            console.log(data);
            alert(data.statusMessage);
            getComments();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

function cancelCommentLike(commentId) {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}/comments/${commentId}/likes-cancel`
    $.ajax({
        url: apiUrl,
        type: 'DELETE',
        success: function (data) {
            console.log(data);
            alert(data.statusMessage);
            getComments();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
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
            alert(err.responseJSON.statusMessage);
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
            alert(err.responseJSON.statusMessage);
        }
    });
}


function showCommentInput(commentId, currentContent) {
    // 댓글 내용을 포함하는 컨테이너 div를 찾습니다.
    const commentContainer = $(`#comment-container${commentId}`);

    // 새로운 입력 요소를 생성합니다.
    const input = $('<textarea>', {
        rows: 2,
        id: `commentInput${commentId}`,
        style: 'width: 85%; font-size: 16px; margin-top: 10px',
    });
    input.val(currentContent);

    // 수정을 확인하는 버튼을 만듭니다.
    const saveButton = $('<a>', {
        href: '#',
        style: 'color: #5F5F5F; font-size: 14px; margin-left: 10px; margin-top:20px',
        click: function () {
            alertCommentModify(commentId);
        }
    }).text('확인');

    // 수정을 취소하는 버튼을 만듭니다.
    const cancelButton = $('<a>', {
        href: '#',
        style: 'color: #5F5F5F; font-size: 14px; margin-left: 10px; margin-top:20px',
        click: function () {
            getComments();
        }
    }).text('취소');

    // 내용을 입력 요소와 버튼으로 대체합니다.
    commentContainer.empty().append(input).append(saveButton).append(cancelButton);
}

function alertCommentModify(commentId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("댓글을 수정하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        modifyComment(commentId);
    } else {
        getComments();
    }
}

function modifyComment(commentId) {
    const commentInput = document.getElementById(`commentInput${commentId}`);
    const requestDto = {
        content: commentInput.value
    };

    $.ajax({
        url: `/api/festivals/${festivalId}/reviews/${reviewId}/comments/${commentId}`,
        type: 'PUT',
        data: JSON.stringify(requestDto), // 데이터 객체를 JSON 문자열로 변환하여 전송
        contentType: 'application/json',
        success: function (data) {
            alert('댓글 수정 완료!');
            getComments();
        },
        error: function (err) {
            alert(err.responseJSON.statusMessage);
            console.log('Error:', err);
            getComments();
        }
    });
}

function alertDeleteComment(commentId) {
    // 경고창을 띄웁니다.
    const confirmation = confirm("댓글을 삭제하시겠습니까?");

    // 사용자가 확인을 누르면 메소드를 실행합니다.
    if (confirmation) {
        deleteComment(commentId);
    }
}

function deleteComment(commentId) {
    const apiUrl = `/api/festivals/${festivalId}/reviews/${reviewId}`
    $.ajax({
        url: apiUrl + `/comments/${commentId}`,
        type: 'DELETE',
        success: function (data) {
            alert(data.statusMessage);
            getReview();
        },
        error: function (err) {
            console.log('Error:', err);
            alert(err.responseJSON.statusMessage);
        }
    });
}

// 페스티벌로 이동하기
function moveFestival(festivalId) {
    window.location.href = `/api/festivals/${festivalId}/page`;
}

// 프로필로 이동하기
function moveProfile(userId) {
    window.location.href = `/api/users/${userId}/profile-page`;
}