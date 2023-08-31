let userId = localStorage.getItem('userId');

// 팔로우 목록을 불러오는 함수
function loadFollowers() {
    $.ajax({
        url: `/api/users/'+${userId}+'/follows/followings`,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            let followerListHTML = '';
            data.forEach(follower => {
                followerListHTML += `
          <div class="follower-item">
            <span>${follower.followedUser.name}</span> <!-- 팔로워 이름 -->
            <button onclick="unfollow(${follower.id})">팔로우 취소</button> <!-- 팔로우 취소 버튼 -->
          </div>`;
            });
            $('#followerList').html(followerListHTML);
        },
        error: function(error) {
            console.log("에러가 발생했습니다:", error);
        }
    });
}

// 팔로우를 취소하는 함수
function unfollow(followerId) {
    $.ajax({
        url: `/api/users/${userId}/follows/${followerId}`,
        type: 'DELETE',
        success: function() {
            loadFollowers(); // 팔로우 취소 후 목록을 다시 불러옴
        }
    });
}

// 페이지가 로드되면 팔로우 목록을 불러옵니다.
$(document).ready(function() {
    loadFollowers();
});